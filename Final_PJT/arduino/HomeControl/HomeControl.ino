#include <Servo.h>
#include "DHT.h"
#include"Timer.h" 

#define L_light A0      //거실 조도 센서 A2핀
#define L_LED 3         //거실 LED
#define K_light A1      //부엌 조도 센서 A1핀
#define K_LED 5         //부엌 LED 

#define AIR 6           //온습도 센서 
#define A_type DHT11
DHT dht(AIR, A_type);
Timer timer;
Timer dhtTimer;

#define FAN 1           //FAN모터

#define MOTION_PIN A2    //모션감지
#define GAS_PIN 10      //가스감지  
#define FLAME_PIN 11    //불꽃감지0005276273

#define F_LED 4
#define SERVO 2         //가스벨브모터
Servo SERVO_MOTOR;  

int rfid = 0;
int L_mode = 0;   //자동 수동 변수
int K_mode = 0;   //자동 수동 변수
int L_bri = 0;    //밝기 입력 변수
int L_value = 0;  //조도센서값 입력 변수
int K_bri = 0;    //밝기 입력 변수
int K_value = 0;  //조도센서값 입력 변수
int num = 100;    //온도 초기값
int t = 0;        //온도
int h = 0;        //습도
int A_mode = 0;   //fan 변수
int pirState = LOW;   // 센서 초기상태는 움직임이 없음을 가정
int val = 0;          // 센서 신호의 판별을 위한 변수
int on = 1;
int flame = 0;
int gas = 0;
int angle = 0;

void setup() {
  dht.begin();
  dhtTimer.every(3000, send_A);
  pinMode(L_light, INPUT);
  pinMode(L_LED, OUTPUT);
  pinMode(K_light, INPUT);
  pinMode(K_LED, OUTPUT);
  pinMode(FAN, OUTPUT);
  pinMode(MOTION_PIN, INPUT);    
 
  Serial.begin(9600);             
  SERVO_MOTOR.attach(SERVO);
  SERVO_MOTOR.write(0);
}

void loop() {
  val = digitalRead(MOTION_PIN);         // 센서 신호값을 읽어와서 val에 저장
  if (Serial.available() > 0) {
    rfid = Serial.read();
    livingRoom();
    kitchen();
    fan();
       
    if (rfid == '9') {
      on = 0;
    } else if (rfid == '8') {
      on = 1;
    }
  }
  if (on == 0) {
    pirState = LOW;
  } else if (on == 1) {
    if (val == HIGH) {                   // 센서 신호값이 HIGH면(인체 감지가 되면)
      if (pirState == LOW) {
        Serial.println("pirLed/M_on");    // 시리얼 모니터 출력
        pirState = HIGH;
      }
    }
    else {                             // 센서 신호값이 LOW면(인체감지가 없으면)
        if (pirState == HIGH) {
        Serial.println("pirLed/M_off");   // 시리얼 모니터 출력
        pirState = LOW;
      }
    }
  }

  flame = digitalRead(FLAME_PIN);
  delay(10);
  if(flame==HIGH){
    Serial.println("flame/F_on");
    delay(50);
    int i=0;
    while(i<20){
      digitalWrite(F_LED,HIGH);
      delay(100);
      digitalWrite(F_LED,LOW);
      delay(100);
      i=i+1;
    }    
  } else {
    //Serial.println("flame/F_off");
    delay(50);
    digitalWrite(F_LED,LOW);
  }


  gas = analogRead(GAS_PIN);
  delay(50);
  if(gas>600){
    Serial.println("gas/G_on");
    angle =180;
    delay(50);    
  } else {
    //Serial.println("gas/G_off");
    angle = 0;
    delay(50); 
  }
  SERVO_MOTOR.write(angle);


  
  if (L_mode == 1) {
    delay(50);
    L_value = analogRead(L_light);
    int L_ledLight = map(L_value, 0, 1023, 255, 0);
    analogWrite(L_LED, L_ledLight);
  } else if (L_mode == 2) {
    delay(50);
    analogWrite(L_LED, (L_bri * 255) / 10);
  }
  if (K_mode == 3) {
    delay(50);
    K_value = analogRead(K_light);
    int K_ledLight = map(K_value, 0, 1023, 255, 0);
    analogWrite(K_LED, K_ledLight);
  } else if (K_mode == 4) {
    delay(50);
    analogWrite(K_LED, (K_bri * 255) / 10);
  }
  if (A_mode == 5) {
    if (t > num) {
      digitalWrite(FAN, LOW);
    } else {
      digitalWrite(FAN, HIGH);
    }
  } else if (A_mode == 6) {
    digitalWrite(FAN, LOW);
  } else if (A_mode == 7) {
    digitalWrite(FAN, HIGH);
  }
  dhtTimer.update();
}

void livingRoom(void) {
  if (rfid == '0') {
    L_mode = 1;
  } else if (rfid == '1') {
    L_mode = 2;
    L_bri = Serial.read() - 48;
  }
}
void kitchen(void) {
  if (rfid == '2') {
    K_mode = 3;
  } else if (rfid == '3') {
    K_mode = 4;
    K_bri = Serial.read() - 48;
  }
}

void fan(void) {
  if (rfid == '4') {
    A_mode = 5;
    num = Serial.parseInt();
  } else if (rfid == '5') {
    A_mode = 6;
  } else if (rfid == '6') {
    A_mode = 7;
  }
}

void send_A() {
  h = dht.readHumidity();
  t = dht.readTemperature();
  String st = String(t);
  String sh = String(h);
  Serial.println("fan/temperature-"+st+"-humidity-"+sh);
}    
