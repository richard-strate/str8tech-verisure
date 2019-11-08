# Verisure API Client

A Java library used to access verisure installations over their HTTP/JSON. Inspired by https://github.com/persandstrom/python-verisure and https://github.com/wahlly/Verisure_Yale_Doorman_PHP_HTTP_Connection.

# Legal Disclaimer
This software is not affiliated with Verisure Holding AB and the developers take no legal responsibility for the functionality or security of your Verisure Alarms and devices.

# Source

Source is available on github: https://github.com/richard-strate/str8tech-verisure

# Maven

Include the dependency found in the Str8tech repository (add to `pom.xml`):

````xml
<dependency>
  <groupId>com.str8tech</groupId>
  <artifactId>str8tech-verisure</artifactId>
  <version>1</version>
</dependency>  
````

The str8tech repository (add to `settings.xml`):

````xml
<repository>
  <id>str8tech.repo</id>
  <url>https://www.str8tech.com/m2</url>
  <releases>
    <enabled>true</enabled>
  </releases>
</repository>
````

# Usage

## JSON HTTP Client

Use (this)[https://github.com/richard-strate/str8tech-verisure/blob/master/src/main/java/com/str8tech/verisure/JsonHttpClient.java] class to execute requests towards the Versiure API and get access to the raw JSON responses (except for error messages, which are implicitly parsed and put into a (RemoteException)[https://github.com/richard-strate/str8tech-verisure/blob/master/src/main/java/com/str8tech/verisure/RemoteException.java]). This may allow the application to get access to data which is not exposed via the Java Object Client (below). 

HINT: All parsing from JSON into java objects done in the Java Object Client is available in (JsonParserImpl)[https://github.com/richard-strate/str8tech-verisure/blob/master/src/main/java/com/str8tech/verisure/JsonParserImpl.java].

## Java Object Client 

Use (this)[https://github.com/richard-strate/str8tech-verisure/blob/master/src/main/java/com/str8tech/verisure/ClientImpl.java] class to execute requests towards the Versiure API and get resposnes parsed into java objets (instead of JSON as seen in the JSON HTTP Client). This is fast and eaiser to integrate but the responses might not contain all the information you're looking for. 

Review [this](https://github.com/richard-strate/str8tech-verisure/blob/master/src/test/java/com/str8tech/verisure/ClientImplIT.java) integration test for a simple example on how to use the client.

# Integration test setup

The integration tests require that account information is added to `settings.xml`:

Key | Meaning
-|-
`verisure.it.unlock` | Set to 'TRUE' to enable the unlock/lock tests*
`verisure.it.userName` | Verisure user name
`verisure.it.password` | Verisure password
`verisure.it.doorCode` | Lock PIN code

*) The unlock/lock integraton tests are disabled by default because of the fact that if a test is successful in unlocking a door but then fails to lock the door will remain open for anyone (eg a thief) to enter. Only enable `verisure.it.unlock` **IF** you have physical access to the door and can lock in manually (or perhaps if you  have automatic locking enabled). HINT: It should always be possible to lock again via the application or verisure web page.

# 429 Too Many Requests

The verisure API will detect an excessive amount of requests and block them. The threshold values are not clear but it has happened with integration tests during development (meaning: it's not a high number). Forget about using the API for any kind of polling and focus on connecting it to manual/few invocations.
