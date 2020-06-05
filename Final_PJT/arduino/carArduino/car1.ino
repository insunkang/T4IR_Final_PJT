#include"DHT.h"
#include"Timer.h"
#define DHTPIN 10
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
Timer timer;
Timer dhtTimer;
Timer emergencyTimer;
Timer llTimer;
Timer rlTimer;

int method = 0;

#define EML 9 // 비상등
#define TH 10 // 온습도센서
#define RL A1 // 왼쪽 라이트
#define LL A2 // 오른쪽 라이트
void setup() {
  Serial.begin(9600);
  
  pinMode(EML, OUTPUT);
  pinMode(LL, OUTPUT);
  pinMode(RL, OUTPUT);

  dht.begin();
  dhtTimer.every(3000, dht11);
  emergencyTimer.oscillate(EML, 600, lightOff(),5);
  llTimer.oscillate(LL, 600, lightOff(),5);
  rlTimer.oscillate(RL, 600, lightOff(),5);
}

void loop() {
  if (Serial.available() > 0) {
    method = Serial.read();
  }
    switch(method){
      case 'e':
        digitalWrite(LL, LOW);
        digitalWrite(RL, LOW);
        emergencyTimer.update();
        break;
      case 'R':
        digitalWrite(EML, LOW);
        digitalWrite(LL, LOW);
        rlTimer.update();
        break;
      case 'L':
        digitalWrite(EML, LOW);
        digitalWrite(RL, LOW);
        llTimer.update();
        break;
    }
    dhtTimer.update();
  }

void dht11() {
  int t = dht.readHumidity();
  int h = dht.readTemperature();
  Serial.print("temporature/");
  Serial.print(t);
  Serial.print("/humidity/");
  Serial.print(h);
}
int lightOff() {
  digitalWrite(EML, LOW);
  digitalWrite(LL, LOW);
  digitalWrite(RL, LOW);
  return LOW;
}
