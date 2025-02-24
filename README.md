# communicationplatform
A Java project written during undergraduate period (**2019**), simple home-school communication platform, support job management, exam management and message and other functions

# Features
- User login and registration
- User information management for administrators
- Announcement management for administrators
- Create a class, invite students to join the class, and manage the class for teachers
- Release homework, mark homework, and view homework for teachers
- Release exam, mark exam, and view exam for teachers
- Chat room for class members
- ...

# Installation
1. Clone the project
```shell
    git clone https://github.com/moumchen/communicationplatform.git
```
2. Import the project into IDEA
3. Import the database file `schoolandfamily.sql` into mysql
4. Set your mysql username and password in `src/main/resources/config/db.properties`
```properties
    jdbc.username=
    jdbc.password=
```
5. Set your redis host and port in `src/main/resources/config/redis.properties`
```properties
    redis.host=
    redis.port=
```
6. Run the project
7. Default data
```
    the default administrator account is `admin`, password is `123456` 
    the default teacher account is `teacher`, password is `123456`
    the default student account is `student`, password is `123456`
```

# Preview
![img.png](readme/img.png)
![img_1.png](readme/img_1.png)
![img_2.png](readme/img_2.png)
![img_3.png](readme/img_3.png)
![img_4.png](readme/img_4.png)
>>>>>>> 5e81ad7 (fix: adjust the version of mysql)
