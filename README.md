# str8tech-verisure
A Java library used to access verisure installations over their HTTP/JSON. Inspired by https://github.com/persandstrom/python-verisure and https://github.com/wahlly/Verisure_Yale_Doorman_PHP_HTTP_Connection.

# Maven

Include the dependency found in the Str8tech repository (add to `pom.xml`):

````xml
<dependency>
  <groupId>com.str8tech</groupId>
  <artifactId>str8tech-verisure</artifactId>
  <version>1.0</version>
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

Check out [this](https://github.com/richard-strate/str8tech-verisure/blob/master/src/test/java/com/str8tech/verisure/ClientImplIT.java) integration test for the best usage illustration.

# Integration test setup

The integration tests require that account information is added to `settings.xml`:

Key | Meaning
-|-
`verisure.it.unlock` | Set to 'TRUE' to enable the unlock/lock tests*
`verisure.it.userName` | Verisure user name
`verisure.it.password` | Verisure password
`verisure.it.doorCode` | Lock PIN code

*) The unlock/lock integraton tests are disabled by default because of the fact that if a test is successful in unlocking a door but then fails to lock the door will remain open for anyone (eg a thief) to enter. Only enable `verisure.it.unlock` **IF** you have physical access to the door and can lock in manually (or perhaps if you  have automatic locking enabled). HINT: It should always be possible to lock again via the application or verisure web page.
