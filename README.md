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
<!--      <picture>
        <img alt="" title="" src="readme/jwt.png" align="center" width="2%" height="2%">
    </picture> -->
    <picture>
        <img alt="" title="" src="readme/java.png" align="center" width="2%" height="2%">
    </picture> 
    <picture>
        <img alt="" title="" src="readme/aws.png" align="center" width="2%" height="2%">
     </picture> 
     <picture>
        <img alt="" title="" src="readme/spring.png" align="center" width="2%" height="2%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/postgres.png" align="center" width="2%" height="2%">
    </picture>
<!--      <picture>
        <img alt="" title="" src="readme/junit.png" align="center" width="2%" height="2%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/thymeleaf.png" align="center" width="2%" height="2%">
    </picture> -->
     <picture>
        <img alt="" title="" src="readme/javascript.png" align="center" width="2%" height="2%">
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
<br>NB: Currently, cookies must be enabled on the browser for JWT authentication
<a name="description"></a>

## Description

The Admin page contains most of the functionality.

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
    - Authentication, authorization, forwarding and exception handling
        - `authentication`
        - `user details` stored in a Postgres database.
        - `passwords` hashed using *BCryptPasswordEncoder*.
        - `CustomUserDetailsService` given a username, gets user data from the database
        - `invalid login` forwards the user to '/invalid'.
        - `valid login/authorization` admin users are forwarded '/admin'.
          <br></br>
- **Admin Page**
    - JWT Cookies, JWT authentication, HTTP request caching and functionality to view/delete submitted 'Contact Us' forms from the database.
    - To prevent the page refreshing, vanilla JavaScript is used to submit HTTP requests to the Servlet and update only the HTML elements
      that have changed.
    - NB: Cross-site tracking cookies must be enabled on the web browser (Firefox, Safari) to include JWT cookies
        - `JwtAuthenticationFilter` extracts a JWT cookie from the request and creates a *UserNamePasswordAuthenticationToken*.
        - `PrintRequestFilter` can log all url-encoded/JSON HTTP requests.
        - `InputStreamCachingFilter` caches content type *application/json* requests to prevent *IllegalStateException: “getInputStream()
          has already been called for this request*.
        - `CustomAuthenticationProvider` verifies the JWT token and authenticates the request.
        - `JWTUtility` creates JWT cookies and extracts a username from a token.
        - `view forms` admin users can iterate through forms - retrieved from the database.
          <br><picture><img alt="" title="" src="readme/view-next-button.png" align="center" width="30%" height="30%" alt=""></picture>
        - `delete forms` admin users can delete forms - deleting them from the database.
          <br><picture><img alt="" title="" src="readme/delete-button.png" align="center" width="30%" height="30%" alt=""></picture>
        - `logout` redirects the user to the /index page.

<a name="dependencies"></a>

## Dependencies, Libraries etc <a id="dependencies"></a>

- **`Spring Boot`**
    - various e.g. spring-boot-starter-web
- **`Security`**
    - io.jsonwebtoken
    - spring-boot-starter-security
    - org.jsoup
- **`JavaScript`**
    - submits client side HTTP requests and processes their response
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
- **`CSS`**
    - bootstrap5

<a name="interesting"></a>

## Learning Interesting Stuff <a name="interesting"></a>

**Viewing HTTP Requests**

Logged using *PrintRequestFilter*<br/>
Helpful when debugging

~~~
JWT cookie

HEADER:
    POST http://localhost:8080/admin/view-next
    content-type:application/json
    accept:*/*
    sec-fetch-site:same-origin
    cookie:Bearer=eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY5MTk4OTgyOSwiZXhwIjoxNjkyNTk0NjI5fQ.heWyctaIKy1EJOHVKAcN_0XfDg9F_yzmeU1EjUl3h3AcHhfR5SxP2Ctz9_wCLl15;
BODY:
    id: 1
~~~

~~~
Form data

HEADER:
    POST http://localhost:8080/form
    content-type: application/x-www-form-urlencoded
    ...
BODY:
    first_name=[Billy],
    last_name=[Brown],
    email=[billy@gmail.com],
    phone=[022 546 8888],
    address_line1=[16 Pinero Place],
    address_line2=[Bucklands Beach],
    message=[Lawnmowing quote]
~~~

~~~    
Username/password

HEADER:
    POST http://localhost:8080/login
    content-type: application/x-www-form-urlencoded
    ...
BODY:
    username=[user1],
    password=[password]
~~~

<a name="screenshots"></a>

## Screenshots

### `Login`

<picture>
    [<img src="readme/login.png" width="100%"/>](src/main/resources/readme/login.png)
</picture>picture>
<br/><br/>

### `Invalid login`

[<img src="readme/invalid-login.png" width="100%"/>](src/main/resources/readme/invalid-login.png)
<br/><br/>

### `Admin`

[<img src="readme/admin.png" width="100%"/>](src/main/resources/readme/admin.png)
<br/><br/>

### `View next contact form`

[<img src="readme/admin-view-next.png" width="100%"/>](src/main/resources/readme/admin-view-next.png)
<br/><br/>

### `Delete contact form`

[<img src="readme/admin-delete.png" width="100%"/>](src/main/resources/readme/admin-delete.png)
<br/><br/>

### `All forms deleted`

[<img src="readme/all-deleted.png" width="100%"/>](src/main/resources/readme/admin-delete.png)
<br/><br/>

<a name="backlog"></a>

## Backlog
- [ ] Separate front-end app using Anguar ![](https://geps.dev/progress/20)
- [ ] Form submission confirmation
- [ ] Brute force attack mitigation
- [ ] Form captcha
- [ ] Domain name
- [ ] SSH certificate
- [ ] https
  <br/>

<a name="version"></a>

## Version

<ul>
    <li>2.0 Java web app(no CI/CD)</li>
    <ul>
        <li>2.1 admin page</li>
        <li>2.2 security</li>
    </ul>
    <li>1.0 PHP web app</li>
    <ul>
        <li>1.1 contact form</li>
        <li>1.1 Postgres db</li>
        <li>1.2 CI/CD</li>
    </ul>
</ul>
