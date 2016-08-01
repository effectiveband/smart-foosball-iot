package is.handsome.labs.iotfoosball;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Locale;

public class FirebaseListGames extends FirebaseList<Game> {
    private FirebaseListPlayers mPlayers;
    private ArrayList<IncludePlayer> mIncludePlayers;
    private CurentGame mCurentGame;

    public FirebaseListGames(DatabaseReference Ref,
            ArrayList<IncludePlayer> includePlayers) {
        super(Ref, Game.class);
        this.mIncludePlayers = includePlayers;
    }

    @Override
    protected void afterAdded(int index) {
        Log.d("score", "game add " + index + " size of data " + mDataList.size());
        if (mPlayers != null) {
            Log.d("score", "player = " + mPlayers.toString());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerA1());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerA2());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerB1());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerB2());
            reCalcIncludes();
       }
    }

    @Override
    protected void afterChanged(int index) {
        Log.d("score", "game change " + index + " size of data " + mDataList.size());
        if (mPlayers != null) {
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerA1());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerA2());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerB1());
            mPlayers.recalcScore(mDataList.get(index).getIdPlayerB2());
            reCalcIncludes();
        }
    }

    @Override
    protected void afterRemoved(int index) {

    }

    public void setPlayer(FirebaseListPlayers players) {
        this.mPlayers = players;
    }

    public void setCurentGame(CurentGame curentGame) {
        this.mCurentGame = curentGame;
    }

    protected void reCalcIncludes() {
        if (mCurentGame != null) {
            for (int i = 0; i < 4; i++) {
                if (mCurentGame.getPlayerId(i) != "") {
                    int index = mPlayers.getKeyList().indexOf(mCurentGame.getPlayerId(i));
                    String score = String.format(Locale.US, "%d:%d",
                            mPlayers.getDataList().get(index).getWins(),
                            mPlayers.getDataList().get(index).getLoses());
                    mIncludePlayers.get(i).score.setText(score);
                }
            }
        }
    }
}