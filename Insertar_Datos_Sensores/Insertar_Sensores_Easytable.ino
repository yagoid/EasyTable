/*********************************************************************************************************************************
  Basic_Insert_ESP.ino
      
  Library for communicating with a MySQL or MariaDB Server
  
  Based on and modified from Dr. Charles A. Bell's MySQL_Connector_Arduino Library https://github.com/ChuckBell/MySQL_Connector_Arduino
  to support nRF52, SAMD21/SAMD51, SAM DUE, STM32F/L/H/G/WB/MP1, ESP8266, ESP32, etc. boards using W5x00, ENC28J60, LAM8742A Ethernet,
  WiFiNINA, ESP-AT, built-in ESP8266/ESP32 WiFi.

  The library provides simple and easy Client interface to MySQL or MariaDB Server.
  
  Built by Khoi Hoang https://github.com/khoih-prog/MySQL_MariaDB_Generic
  Licensed under MIT license
 **********************************************************************************************************************************/
/*
  MySQL Connector/Arduino Example : connect by wifi

  This example demonstrates how to connect to a MySQL server from an
  Arduino using an Arduino-compatible Wifi shield. Note that "compatible"
  means it must conform to the Ethernet class library or be a derivative
  with the same classes and methods.
  
  For more information and documentation, visit the wiki:
  https://github.com/ChuckBell/MySQL_Connector_Arduino/wiki.

  INSTRUCTIONS FOR USE

  1) Change the address of the server to the IP address of the MySQL server
  2) Change the user and password to a valid MySQL user and password
  3) Change the SSID and pass to match your WiFi network
  4) Connect a USB cable to your Arduino
  5) Select the correct board and port
  6) Compile and upload the sketch to your Arduino
  7) Once uploaded, open Serial Monitor (use 115200 speed) and observe

  If you do not see messages indicating you have a connection, refer to the
  manual for troubleshooting tips. The most common issues are the server is
  not accessible from the network or the user name and password is incorrect.

  Created by: Dr. Charles A. Bell
*/

#if ! (ESP8266 || ESP32 )
  #error This code is intended to run on the ESP8266/ESP32 platform! Please check your Tools->Board setting
#endif

#include "Credentials.h"
// Librerias sensor temperatura y humedad
#include <DHT.h>
#include <DHT_U.h>
#include <ESP32Time.h>

#define MYSQL_DEBUG_PORT      Serial

// Debug Level from 0 to 4
#define _MYSQL_LOGLEVEL_      1

#include <MySQL_Generic.h>

#define USING_HOST_NAME     false
// Pines que se van a usar para los sensores
#define DRH_pin 13
#define LDR_pin 14

#if USING_HOST_NAME
  // Optional using hostname, and Ethernet built-in DNS lookup
  char server[] = "your_account.ddns.net"; // change to your server's hostname/URL
#else
IPAddress server(195, 235, 211, 197);    //(192, 168, 2, 112);
#endif

uint16_t server_port = 3306;    //5698;

char default_database[] = "prieasytable";           //"test_arduino";
char default_table[]    = "sensor";                 //"test_arduino";

String default_value    = "Humes"; 

// Sample query
String INSERT_SQL;
String INSERT_SQL2;
String INSERT_SQL3;

// Se inicializa el sensor DTH (temperatura y humedad)
DHT dht(DRH_pin, DHT11);

ESP32Time rtc;  // crear estructura tipo ESP32Time

// Variables de los sensores
float temp, hume, luminosidad;
String fecha;

MySQL_Connection conn((Client *)&client);

MySQL_Query *query_mem;

void setup()
{
  Serial.begin(115200);
  pinMode(LDR_pin, INPUT);  // El sensor LDR como input
  rtc.setTime(11, 06, 17, 48, 6, 2022);   // Se configura la hora
  
  Serial.println("Datos del Sensor!");
  dht.begin();
  
  while (!Serial);

  MYSQL_DISPLAY1("\nStarting Basic_Insert_ESP on", ARDUINO_BOARD);
  MYSQL_DISPLAY(MYSQL_MARIADB_GENERIC_VERSION);

  // Begin WiFi section
  MYSQL_DISPLAY1("Connecting to", ssid);
  
  WiFi.begin(ssid, pass);
  
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    MYSQL_DISPLAY0(".");
  }

  // print out info about the connection:
  MYSQL_DISPLAY1("Connected to network. My IP address is:", WiFi.localIP());

  MYSQL_DISPLAY3("Connecting to SQL Server @", server, ", Port =", server_port);
  MYSQL_DISPLAY5("User =", user, ", PW =", password, ", DB =", default_database);
}

void runInsert()
{
  // Initiate the query class instance
  MySQL_Query query_mem = MySQL_Query(&conn);

  if (conn.connected())
  {
    MYSQL_DISPLAY(INSERT_SQL);
    
    // Execute the query
    // KH, check if valid before fetching
    if ( !query_mem.execute(INSERT_SQL.c_str()) )
    {
      MYSQL_DISPLAY("Insert error");
    }
    else
    {
      MYSQL_DISPLAY("Data Inserted.");
    }
  }
  else
  {
    MYSQL_DISPLAY("Disconnected from Server. Can't insert.");
  }
}

void loop()
{
  // Se leen los datos de los sensores
  hume = dht.readHumidity();
  temp = dht.readTemperature();
  luminosidad = digitalRead(LDR_pin);
  int id_sensor = 1;

  // Se recoge el valor de la fecha actual
  fecha = String(rtc.getDay()) + "/" + String(rtc.getMonth()) + "/" + String(rtc.getYear()) + " " + rtc.getTime();

  if (isnan(hume) || isnan(temp)) {
    Serial.println("LECTURA ERRRONEA");
  }

  // Se muestran por pantalla los datos de los sensores
  Serial.println("\nTemperatura: " + String(temp) + " Humedad: " + String(hume) + " Luminosidad: " + String(luminosidad));
  Serial.println("Fecha: " + fecha + "\n");

  // Se meten los datos de los sensores en la base de datos
  INSERT_SQL = String("INSERT INTO ") + default_database + "." + default_table + " (id_sensor, temp, hum, lumi, `timestamp`) VALUES (" + id_sensor + ",'"+String(temp)+"','"+String(hume)+"','"+String(luminosidad)+"',now());";
  
  /*INSERT_SQL = String("INSERT INTO ") + default_database + "." + default_table 
                   + " (dato, fecha_medicion) VALUES ('" + String(hume) + "', '" + fecha + "');" +
                   ("INSERT INTO ") + default_database + "." + default_table 
                   + " (dato, fecha_medicion) VALUES ('" + String(temp) + "', '" + fecha + "');" +
                   ("INSERT INTO ") + default_database + "." + default_table 
                   + " (dato, fecha_medicion) VALUES ('" + String(luminosidad) + "', '" + fecha + "');";

                   
  INSERT_SQL2 = String("INSERT INTO ") + default_database + "." + default_table 
                   + " (dato, fecha_medicion) VALUES ('" + String(temp) + "', '" + fecha + "');";
  INSERT_SQL3 = String("INSERT INTO ") + default_database + "." + default_table 
                   + " (dato, fecha_medicion) VALUES ('" + String(luminosidad) + "', '" + fecha + "');";
                 
  INSERT_SQL = String("INSERT INTO ") + default_database + "." + default_table 
                   + " (tipo) VALUES ('" + default_value + "');";
  INSERT_SQL = String("INSERT INTO ") + default_database + "." + default_table 
                   + " (message) VALUES ('" + default_value + "')";*/

  MYSQL_DISPLAY("Connecting...");
  
  //if (conn.connect(server, server_port, user, password))
  if (conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL)
  {
    delay(500);
    runInsert();
    conn.close();                     // close the connection
  } 
  else 
  {
    MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
  }

  Serial.println(INSERT_SQL);

  MYSQL_DISPLAY("\nSleeping...");
  MYSQL_DISPLAY("================================================");
  
  delay(120000);
}
