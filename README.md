<!-- Heading start-->
<h1 align="center"> mgm-server </h1> <br>

<div align="center">
    <picture>
        <img src="readme/man.png" align="center" width="10%" height="10%" alt="">
    </picture>
</div>

<p align="center">
  Spring Boot Web App
</p>

<div align="center">
<!--      <picture>
        <img alt="" title="" src="readme/jwt.png" align="center" width="5%" height="5%">
    </picture> -->
    <picture>
        <img alt="" title="" src="readme/java.png" align="center" width="5%" height="5%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/spring.png" align="center" width="5%" height="5%">
    </picture>
     <picture>
        <img alt="" title="" src="readme/postgres.png" align="center" width="5%" height="5%">
    </picture>
    <picture>
        <img alt="" title="" src="readme/junit.png" align="center" width="5%" height="5%">
    </picture>
    <picture>
        <img alt="" title="" src="readme/jwt.png" align="center" width="5%" height="5%">
    </picture>
</div>

<div align="center">

[https://mrgrassmaster.com](https://mrgrassmaster.com)

</div>
<!-- Heading end-->

<!-- Contents start-->

## Contents
- [Backlog](#backlog)
- [Description](#desc)
- [Spring Boot](#spring_boot)
- [Hosting](#hosting)
- [Logging](#screenshots)
- [Testing](#testing)

<!-- Contents end-->

<a name="desc"></a>
## Backlog
- [x] Add new endpoints
- [x] Remove redundant endpoints
- [x] Unit testing
- [ ] Deploy

<a name="desc"></a>
## Description
First attempt at client server architecture - been developing it on/off since 2022.<br>
<ul>
	<li>Client uses Angular</li>
		<ul>
			<li>mgm-client</li>
		</ul>
	<li>Server uses Spring Boot</li>
		<ul>
			<li>mgm-server</li>
            <li>local postgres database</li>
		</ul>
 	<li>Previous hosted on AWS</li>
</ul>

<a name="spring_boot"></a>
## Spring Boot details

Application is stateless - user details stored on a database

- Custom Authentication
    - `CustomUserDetailsFilter` extracts username/password from HTTP request body
    - `CustomUserDetailsServer` gets user details from the database
    - `CustomAuthenticationProvider` authenticates user and adds user authorities/priviledges
    <br></br>

- **Login Authorization**
  - Handled by Spring Security
      - Requires user authority role ADMIN
      - HTTP response contains a JWT cookie
      <br></br>
    
- **Custom Authorization**
    - Verifies user details extracted from JWT cookie 
      - `CustomJwtUtility` validates the cookie and extracts user details
      - Proceeds with custom authentication
      <br></br>

- **Custom JWT Cookies**
    - Creates a JWT cookie using the username and appends it to the HTTP response
        - `CustomJwtUtility` creates response cookie
        <br></br>

- **Custom Logging**
    - Logs all HTTP requests to file
    - Logs successful authentication attempts
    - Logs errors sent by the Angular client to file angular.log
        - `Logback`
          <br></br>

- **Injection attack mitigation**
    - `HTML/JavaScript injection` form input is sanitised to remove HTML/JavaScript
    - `SQL Injection` input is inserted into the database as String parameters
      <br></br>

- **Database**
    - `postgresl` use to store data - contact forms, user details
      <br></br>

- **Custom HTTP request caching**
    - Caches content type application/json requests to prevent IllegalStateException: “getInputStream() has already been called for this request.
      <br></br>

- **Custom Header Filter**
    - Adds custom HTTP response headers to the http-response - required by Angular front-end
      <br></br>



<a name="hosting"></a>
## Hosting

Previously hosted on AWS ec2 - no longer<br/>
AWS Route53 routes requests for server.mrgrassmaster.com to the instance

- **ec2 details**
    - instance type t3a.micro
        - `ubuntu`
          <br><br>
- **Install packages**
    - `openjdk-17-jdk`, `tomcat`, certbot
      <br><br>
- **Add files**
    - add Spring war file and SSL certificate files
      <br><br>
- **tomcat configuration**
    - enable ssl
    - ssl certificate and key
    - java war file
      <br><br>
- **Configure firewall**
    - enable ssh, http, https, database port



<a name="logging"></a>
## Logging

- **Uses `logback`<br/>**
    - Spring Boot (mgm-server) to all.log  
      - log exceptions, HTTP-requests, login attempts
    - Angular (mgm-client) to angular.log sent via HTTP request
<br><br>
<div align="center">
    <picture>
        <img src="readme/logging.png" align="center" width="100%" height="100%" alt="">
    </picture>
</div>
HTTP request from mgm-client


<a name="testing"></a>
## Testing
- **Unit Tests<br/>**
<div align="center">
    <picture>
        <img src="readme/unit_tests.png" align="center" width="100%" height="100%" alt="">
    </picture>
</div>
