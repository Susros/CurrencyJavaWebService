# Currency Java Web Service

This is Java Web Service assignment from SENG3400 class of University of Newcastle, 2018. The goal is to create client applications that communicate servier via SOAP.

## Java Web Services

In this project, there are two Java web services.

### Identity Service

The identity service provides user authorisation. It exposes two endpoints. **Login** endpoint is used for checking username and password of users, and generate session keys. **Authorisation** endpoint is used for checking if session key passed from client application is valid, i.e. the user has logged in.

Please use one of the following username and password to login.
1. username: **hayden**; password: **1234**;
2. username: **josh**; password: **4321**;

### Currency Service

The currency service manage all aspects of currency listing and conversion. It exposes two endpoints. **Conversion** endpoint is used for getting information of currency exchange rate. **Admin** endpoint is used for adding and deleting currencies in the database. All actions in **Admin** endpoint will need a valid session key, which means the user must be authorised to access it.

## Client Applications

In this project, there are two client applications.

### Admin Client

The Admin Client is a command line application which allows an administrator to manage the Conversion Service. It will consume services offered by Admin endpoint. This application will request for the **Login** endpoint to validate the username and password. If a valid session key is returned, the application will record it. If the username and password is incorrect, the application will request the user for their username and password two more times, before exiting on the third invalid attempt.

On successful login, the application will present the user with a prompt. This is a free-form text prompt. The user may type one of 10 commands at the prompt, supplying all required parameters. The commands (with parameters) consist of:

1. addCurrency <currencyCode>
2. removeCurrency <currencyCode>
3. listCurrencies
4. conversionsFor <currencyCode>
5. addRate <fromCurrency> <toCurrency> <rate>
6. updateRate <fromCurrency> <toCurrency> <rate> 7. removeRate <fromCurrency> <toCurrency>
8. listRates
9. help
10. logout

### Conversion Client

The application does not require a user to login, all functionality is unrestricted. The application will present the user with a prompt. The user may type one of 4 commands at the prompt, supplying all required parameters:

1. convert <fromCurrency> <toCurrency> <amount> 
2. rateOf <fromCurrency> <toCurrency>
3. listRates
4. help
5. exit

## Conceptual Overview

![alt text] (https://github.com/Susros/CurrencyJavaWebService/blob/master/conceptual_overview.png "Concept overview image")

## Requirement

1. Java 8
2. Tomcat 8

## Setup

1. Copy currency and identity folder into tomcat webapps
2. AdminClient and ConversionClient folders contains the client applications. Thus, those two folders are outside of server.
3. Start tomcat. Make sure the port is listening at 8080 on localhost.
4. For Admin Client, got to AdminClient folder and run ```java AdminClient <usernaame> <password>```
5. For Conversion Client, got to ConversionClient folder and run ```java CurrencyClient```

If it fails, please recompiled it as followed:

1. Make sure to set classpath environment variables:

```
export /Users/path/to//axis/lib/axis.jar;/Users/path/to//axis/lib/commons-discovery- 0.2.jar;/Users/path/to//axis/lib/commons-logging- 1.0.4.jar;/Users/path/to//axis/lib/jaxrpc.jar;/Users/path/to//axis/lib/log4j- 1.2.8.jar;/Users/path/to//axis/lib/saaj.jar;/Users/path/to//axis/lib/wsdl4j- 1.5.1.jar
```

2. Compile all Java files in AdminClient and ConversionClient folders: ```javac *```
3. Compile all java files in currency/WEB-INF/classes/seng3400a2 and identity/WEB-INF/classes/seng3400a2: ```javac *```
4. If WSDL need to be regenerated: ```localhost:8080/identity/SERVICE_NAME.jws```, then convert WSDL into Java with java org.apache.axis.wsdl.WSDL2Java your_wsdl.xml```
