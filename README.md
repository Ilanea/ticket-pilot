## Ticket Pilot MVP

# DO NOT EDIT THE SENDMAIL API KEY IN application.properties!


- copy application.properties file in src/main/resources/ and rename it to ticketpilot.properties before working on the project
- by default the application will be using H2 in memory database, follow the instructions in ticketpilot.properties to use postgresql

Test Installation in Ubuntu:

- Get your Vaadin License, follow their instructions:
  ```
  https://vaadin.com/docs/latest/configuration/licenses
  ```
- Install JDK17
  ```
  sudo apt install openjdk-17-jdk openjdk-17-jre
  ```
- Install Apache Maven 3.9
  ```
  wget https://dlcdn.apache.org/maven/maven-3/3.9.3/binaries/apache-maven-3.9.3-bin.tar.gz
  sudo tar -xvf apache-maven-3.9.3-bin.tar.gz -C /opt
  ```
  Now add the Maven binary to your PATH variable, for example in ~/.bashrc
  ```
  export PATH="$PATH:/opt/apache-maven-3.9.3/bin"
  ```
- Clone the repository and run maven in it
  ```
  mvn package install
  ```
- Run the created Java file
  ```
  java -jar target/NAME_OF_CREATED_JAR.jar
  ```

