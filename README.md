# Medicine-Delivery-App
Medicine Delivery Project Application to assist deliveries of medicine.

Details:
Language: Java
Framework: Spring Boot for back-end
Database: MySQL

# Setting Up!

Requirements:
1. Java 11.0 (https://www.oracle.com/sg/java/technologies/javase/jdk11-archive-downloads.html)
   - To run the application
2. Maven (https://maven.apache.org/)
   - To start the website with only a simple command
3. Java Plugin Package in VS Code
   - To start the website in VS Code
4. MySQL 8.0
   - v8.0 or above since old version is not applicable


# Important Reminder

1. Make a Schema
   Name it with your taste!
2. Configure Database Connection
Under src -> main -> resources, there's an application.properties to modify the database connection.
```
spring.datasource.url=jdbc:mysql://localhost:3308/YOUR_SCHEMA_NAME
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```
Change the schema name to your schema name (and port if not 3308) in your database along with your username and password.

Have fun and enjoy!
