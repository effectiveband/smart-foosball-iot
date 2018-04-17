package is.handsome.labs.iotfoosball.interactor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.ListenCallback;

import timber.log.Timber;

import static is.handsome.labs.iotfoosball.view.MainActivity.A;
import static is.handsome.labs.iotfoosball.view.MainActivity.B;

public class TcpIpGoalListenerServer {
    private int port;
    private static volatile TcpIpGoalListenerServer instance;

    private CurrentGame currentGame;

    public static TcpIpGoalListenerServer getInstance(CurrentGame currentGame, int port) {
        TcpIpGoalListenerServer localInstance = instance;
        if (localInstance == null) {
            synchronized (TcpIpGoalListenerServer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TcpIpGoalListenerServer(currentGame, port);
                }
            }
        }
        return localInstance;
    }

    public void start(final Context context) {
        AsyncServer.getDefault().listen(null, port, new ListenCallback() {

            @Override
            public void onAccepted(final AsyncSocket socket) {
                init(context, socket);
                handleAccept(socket);
            }

            @Override
            public void onListening(AsyncServerSocket socket) {
                System.out.println("[TcpIpGoalListenerServer] TcpIpGoalListenerServer started listening for connections");
            }

            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[TcpIpGoalListenerServer] Successfully shutdown server");
            }
        });
    }

    public void stop() {
        AsyncServer.getDefault().stop();
    }

    private TcpIpGoalListenerServer(CurrentGame currentGame, int port) {
        this.currentGame = currentGame;
        this.port = port;
    }

    private void init(final Context context, final AsyncSocket socket) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                System.out.println("[TcpIpGoalListenerServer] New Connection " + socket.toString());
            }
        });
    }

    private void handleAccept(final AsyncSocket socket) {

        socket.setDataCallback(new DataCallback() {
                                   @Override
                                   public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {

                                       String msg = new String(bb.getAllByteArray());
                                       System.out.println("TcpIpGoalListenerServer message: " + msg);
                                       if (msg.contains("a")) {
                                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                                               @Override
                                               public void run() {
                                                   currentGame.notifyScored(A);
                                                   Timber.d("Serial port message = GOAL in A");
                                               }
                                           });

                                       }
                                       if (msg.contains("b")) {
                                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                                               @Override
                                               public void run() {
                                                   currentGame.notifyScored(B);
                                                   Timber.d("Serial port message = GOAL in B");
                                               }
                                           });
                                       }

                                       socket.setClosedCallback(new CompletedCallback() {
                                           @Override
                                           public void onCompleted(Exception ex) {
                                               if (ex != null) throw new RuntimeException(ex);
                                               System.out.println("[TcpIpGoalListenerServer] Successfully closed connection");
                                           }
                                       });

                                       socket.setEndCallback(new CompletedCallback() {
                                           @Override
                                           public void onCompleted(Exception ex) {
                                               if (ex != null) throw new RuntimeException(ex);
                                               System.out.println("[TcpIpGoalListenerServer] Successfully end connection");
                                           }
                                       });

                                   }
                               }
        );
    }
}
