#include <GateListener.h>

GateListener::GateListener (uint8_t _portd, uint8_t _port, char _serialMsg, uint16_t _treshold, String _serverIp, uint16_t _serverPort) {
  isBallIn = false;
  port = _port;
  portd = _portd;
  serialMsg = _serialMsg;
  treshold = _treshold;
  serverIp = _serverIp;
  serverPort = _serverPort;
  count = 0;
}

void GateListener::check (WiFiClient *client) {
  digitalWrite(portd, 1);
  uint16_t current_value = analogRead(port);

  if (current_value < (treshold)) {
    isBallIn = true;
    timer_ms = millis() + 300;
  }

  if ((current_value > treshold) && isBallIn) {
    count++;
    Serial.print(serialMsg);
    Serial.println(count);
    isBallIn = false;

    Serial.println();

    sendGoal(client);
  }

  if (timer_ms < millis() && isBallIn) {
    isBallIn = false;
  }

  digitalWrite(portd, 0);
}

void GateListener::sendGoal(WiFiClient *client) {
    while (!client->connect(serverIp, serverPort)) {
      Serial.print(".");
      delay(100);
    }

    client->println(serialMsg);
    delay(10);
    client->stop();
}
