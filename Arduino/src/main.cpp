#include <Arduino.h>
#include <GateListener.h>
#include <ESP8266WiFi.h>

#define TABLET_IP "10.0.1.239"
#define TABLET_PORT 4444

WiFiClient client;
GateListener gateA(13, A0, 'a', 600, TABLET_IP, TABLET_PORT);
GateListener gateB(2, A0, 'b', 90, TABLET_IP, TABLET_PORT);

uint32_t last_send_time_millis = 0;
constexpr uint32_t INTERVAL_MILLIS = 5000;
uint32_t counter = 0;

void setup() {
  Serial.begin(115200);
  WiFi.begin("HANDSOME", "handsomeisawesome");

  Serial.print("WIFI: Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());

  pinMode(15, OUTPUT);
 	digitalWrite(15, HIGH);
  
  pinMode(13, OUTPUT);
}

void loop() {
  gateA.check(&client);
  gateB.check(&client);
}
