# ms-state-handler-template
A sample application demonstrating how to do state management of microservices (circuit breaker / state design pattern).  This is a plain old java application (no spring or springboot dependencies) that connects to a kafka servcie as a consumer and submits consumed data to a REST endpoint.  The objective of this project is:
- To demonstrate auto-recovery from connection failures to kafka and/or http end point.
- The program maintains various internal states such as
  - connectedState
  - notConnectedToHttp
  - notConnectedToKafka

Based on the runtime connection conditions, the application switches its state.  The states are implemented as Java classes, which implement a state interface having an execute method.  The advantage of this approach is that it is scalable.  It avoids lot of complex coding patterns that may otherwise arise due to usage of if...else... conditions.


![image](https://github.com/rv-nath/ms-state-handler-template-java/assets/87602915/20c8567d-9e9b-4e4a-b40f-d8fb0a36dcd2)


# Pre-requisites
In order to successfully run this project, you will need a kafka servcie and any locally hosted http service.  
You also need maven and Java SDK latest version.

# How to setup 
- Install apache-kafka on your system either natively or through docker.  This will act as a data source for our service.
- Setup any http web server.  You can use json-serve to get started quickly.
- Clone the respository to your local folder.
- build the jar file using `mvn clean install`.
- **TODO**: Add instructions on how to configure kafka and http connections to the service.
