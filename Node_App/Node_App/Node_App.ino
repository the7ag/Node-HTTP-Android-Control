#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
String  i;
ESP8266WebServer server(80);
const char* ssid ="GALAXY";
const char* password="man01289057240";
bool LEDstatus = LOW;
void setup()
{
  Serial.begin(9600);
  delay(100);
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.println("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
  delay(300);
  Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected..!");
  Serial.print("Got IP: ");  Serial.println(WiFi.localIP());
  server.on("/", handle_OnConnect);
  server.on("/ON", handle_ledon);
  server.on("/OFF", handle_ledoff);
  server.onNotFound(handle_NotFound);
  server.begin();
  Serial.println("HTTP server started");
}
void loop() {
  server.handleClient();
  if(LEDstatus)
  digitalWrite(LED_BUILTIN, HIGH);
  else
  digitalWrite(LED_BUILTIN, LOW);
}
void handle_OnConnect() {
  LEDstatus = LOW;
  server.send(200, "text/html", SendHTML(false)); 
}

void handle_ledon() {
  LEDstatus = HIGH;
  server.send(200, "text/html", SendHTML(true)); 
}

void handle_ledoff() {
  LEDstatus = LOW;
  server.send(200, "text/html", SendHTML(false)); 
}

void handle_NotFound(){
  server.send(404, "text/plain", "Not found");
}

String SendHTML(uint8_t led){
  String ptr = "<!DOCTYPE html>\n";
  ptr +="<html>\n";
  ptr +="<head>\n";
  ptr +="<title>LED Control</title>\n";
  ptr +="</head>\n";
  ptr +="<body>\n";
  ptr +="<h1>LED</h1>\n";
  ptr +="<p>Click to switch LED on and off.</p>\n";
  ptr +="<form method=\"get\">\n";
  if(led)
  ptr +="<input type=\"button\" value=\"LED OFF\" onclick=\"window.location.href='/ON'\">\n";
  else
  ptr +="<input type=\"button\" value=\"LED ON\" onclick=\"window.location.href='/OFF'\">\n";
  ptr +="</form>\n";
  ptr +="</body>\n";
  ptr +="</html>\n";
  return ptr;
}
