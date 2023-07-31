<h1 align="center"> MrGrassMaster </h1> <br>

<div align="center">
    <picture>
        <img src="src/main/resources/static/image/man.png" align="center" width="10%" height="10%" alt="">
    </picture>
</div>

<p align="center">
  Spring Boot Web App
</p>

<div align="center">
    <picture>
        <img alt="" title="" src="readme/java.png" align="center" width="4%" height="4%">
    </picture> 
    <picture>
        <img alt="" title="" src="readme/aws.png" align="center" width="4%" height="4%">
     </picture> 
     <picture>
        <img alt="" title="" src="readme/spring.png" align="center" width="4%" height="4%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/postgres.png" align="center" width="4%" height="4%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/junit.png" align="center" width="4%" height="4%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/thymeleaf.png" align="center" width="4%" height="4%">
    </picture>
</div>

## Contents

- [Demo](#demo)
- [Description](#description)
- [Dependencies](#dependencies)
- [Learning interesting stuff](#interesting)
- [Screenshots](#screenshots)
- [Backlog](#backlog)
- [Version](#version)

<a name="demo"></a>
## Demo 

[http://m-g-m.ap-southeast-2.elasticbeanstalk.com](http://m-g-m.ap-southeast-2.elasticbeanstalk.com)

<a name="description"></a>
## Description 

- **Hosting**
    - Amazon Web Services
      - `AWS Elastic Beanstalk` running a JAR
      - `AWS RDS` running a Postgres database
        <br></br>
- **Contact Us Form**
    - Inject attack mitigation
        - `HTML/JavaScript injection` form input is sanitised to remove HTML/JavaScript
        - `SQL Injection` input is inserted into the database as String parameters
          <br></br>
- **Login Page**
    - Authentication, authorization, redirecting, forwarding and exception handling
        - `authentication`
        - `invalid login` forwards the user to /invalid
        - `password encoding/decoding` user details stored in database
        - `authorization` admin users can view /admin
        - `valid login` users are redirected to /admin by a custom handler
          <br></br>
- **Admin Page**
    - View/delete submitted 'Contact Us' forms
        - `view forms` admin users can iterate through forms which are retrieved from the database
          <br><picture><img alt="" title="" src="readme/view-next-button.png" align="center" width="30%" height="30%" alt=""></picture>
        - `delete forms` admin user can delete forms which deletes them from the database
          <br><picture><img alt="" title="" src="readme/delete-button.png" align="center" width="30%" height="30%" alt=""></picture>
        - `logout` redirects the user to the /index page.

<a name="dependencies"></a>
## Dependencies/Libraries <a id="dependencies"></a>


- **`Spring Boot`**
    - various e.g. spring-boot-starter-web
- **`Security`**
    - spring-boot-starter-security
    - JSoup
- **`Database`**
    - postgresql
    - h2
    - hibernate
- **`Testing`**
    - junit5
    - hamcrest
    - mockito
    - MockMvc
- **`Logging`**
    - logback
- **`Template Engine`**
   - thymeleaf


<a name="interesting"></a>
## Learning Interesting Stuff <a name="interesting"></a>

**Viewing HTTP Requests**

Logged using a custom security filter<br/>
Helpful when debugging
~~~
HEADER:
    POST http://localhost:8080/form
    host: localhost:8080
    origin: http://localhost:8080
    content-type: application/x-www-form-urlencoded
    accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
    referer: http://localhost:8080/form
    cookie: Idea-c1035a9b=3a68ec60-852b-4005-a4ef-e4c243ce1df1; JSESSIONID=E3888E4060FADEF1E201639266E76AFD
BODY:
    _csrf=[yJwud5V0wzH2VT2UMXcrelxuW6FUmzp6kARNAMsAfOesM1bFrKxLQ_BC9lTbYA2iUFofSGVadsMx-QtXpWd1OPk2TYWeV2D3],
    first_name=[Billy],
    last_name=[Brown],
    email=[billy@gmail.com],
    phone=[022 546 8888],
    address_line1=[16 Pinero Place],
    address_line2=[Bucklands Beach],
    message=[Lawnmowing quote]
~~~
~~~    
HEADER:
    POST http://localhost:8080/login
    host: localhost:8080
    origin: http://localhost:8080
    content-type: application/x-www-form-urlencoded
    accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
    referer: http://localhost:8080/login
    cookie: Idea-c1035a9b=3a68ec60-852b-4005-a4ef-e4c243ce1df1; JSESSIONID=B4C703DAF9E54A677FC09CA857CC2441
BODY:
    username=[user1],
    password=[password],
    _csrf=[VAGZzEvVOn--6LIw1srTXmgzWzPTqlnFLIfZ8J-tSzWer5EOYTOs_3_sWUqT0dMB5OfnaQ1Sdgrqz2_oG-G_k_rLclD7yalr]
~~~

<a name="screenshots"></a>
## Screenshots

### `Login`
<picture>
    [<img src="readme/login.png" width="100%"/>](src/main/resources/readme/login.png)
</picture>picture>
<br/><br/>

### `Invalid login`
[<img src="readme/invalid-login.png" width="100%"/>](readme/invalid-login.png)
<br/><br/>

### `Admin`
[<img src="readme/admin.png" width="100%"/>](readme/admin.png)
<br/><br/>

### `View next contact form`
[<img src="readme/admin-view-next.png" width="100%"/>](readme/admin-view-next.png)
<br/><br/>

###  `Delete contact form`
[<img src="readme/admin-delete.png" width="100%"/>](readme/admin-delete.png)
<br/><br/>

<a name="backlog"></a>
## Backlog
- [ ] Form submission confirmation
- [ ] Brute force attack mitigation
<br/>

<a name="version"></a>
## Version
<ul>
    <li>2.0 Java</li>
    <li>1.0 PHP</li>
</ul>
