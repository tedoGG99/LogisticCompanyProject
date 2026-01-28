# <center> LogisticCompanyProject

## <center> Setup Instructions

In order to run the application properly, please follow the steps below.

### 1. Database Configuration
Create a MySQL database using the following SQL command:
```
CREATE DATABASE <your_database_name>;
```

### 2. Configure Properties
1.  **Copy the template file:**
    `application.template.properties` -> `application.properties`

2.  **Update the Database URL:**
    Open `application.properties` and update the `spring.datasource.url` line with your specific database name:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/<your_database_name>
    ```

3.  **Add Credentials:**
    Add your database username and password to the `application.properties` file:
    ```properties
    spring.datasource.username=root
    spring.datasource.password=your_password
    ```

### 3. System Requirements
* **Java Version:** Check your version. Currently, the application works with **Java 17**.
* **Database Driver:** Ensure the **MySQL JDBC Driver** is correctly loaded in your project dependencies.


## <center> Commands

1. build
    - mvn clean package (Without wrapper)
    - ./mvnw clean package (With wrapper)
2. run:
    - Linux : ./mvnw spring-boot:run 
    - Windows: mvnw spring-boot:run
    