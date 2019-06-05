README

The cache-app is a demo cache service application which has the following functionality
   Store data in cache with persistence backup from postgresql DB
   Put data in to cache service using REST API
   Read data from cache service using REST API 
   Reload data from DB with a notification from message broker like Kafka 


Build requirements on RHEL
----------------------------
1. Maven sould be installed
2. Docker should be installed
3. Apache-tomcat bundle should be available(download apache tomcat, extract it and rename to apache-tomcat)


Build steps
-----------
1. clone the project from following git repo
2. cd vishapp
3. copy apache-tomcat dir to vishapp dir
4. Run mvn clean install
5. run mvn package docker:build to build docker image 
6. Push the docker image to repository by command "docker push cache-service" 

Note: Make sure that JAVA_HOME set properly before maven build. The project has been complied succesfully on java8.


Postgresql changes/setup:
-------------------------

1. create test DB 
   su - postgres
   createdb test
2. Create user test user and set passwd
   su - postgres
   createuser --interactive --pwprompt
  
3. create Employee table    
   psql -d test
  
   CREATE TABLE Employee(
   id SERIAL PRIMARY KEY,     
   NAME           TEXT    NOT NULL,
   SALARY         REAL,
   DESIGNATION    CHAR(50)
);

Note: make sure postgresql listens on 5432 and md5 password authentication is used

kafka setup
-----------
Download apache-kafka if not available or if you do not have container 
1. Run kafka if not running.
   go to bin dir	
   sh zookeeper-server-start.sh config/zookeeper.properties &
   sh kafka-server-start.sh config/server.properties &

2. Create a topic cache-service
   kafka-topics.sh --create --bootstrap-server 135.250.28.62:9092 --replication-factor 1 --partitions 1 --topic cache-service

Note: Make sure kafka server/broker runs on 9092 port and 9092 port is open for access

   
configuration setup on minikube VM
----------------------------------
1. Add the Ipaddress and host name of postgres and kafka host  in to minikube /ets/host files
   
   <postgresql server IP address> pghost
   <kafka IP address> kafkahost
   
   minikube ssh
   sudo /etc/hosts


cache-service container deployment on minikube
------------------------------------------------
Steps to deploy container if you are able to push docker image to your registry and able to pull docker image on minikue VM

1. deploy cache-service container
   kubectl create deployment vishapp --image=cache-service
2. Expose the app 
   kubectl expose deployment vishapp --type=LoadBalancer --port=8080
3. check the external port to access the cache-service app
   kubectl get services
   
   kubernetes   ClusterIP      10.96.0.1      <none>        443/TCP          129m
   vishapp      LoadBalancer   10.98.12.220   <pending>     8070:32285/TCP   75s

You can access the cache-service app on http://<minikube IP>:32285/vishapp/


Steps to deploy if you are not able to push and pull container image

1. After maven package docker command download the docker image locally
   docler save cache-servive > cache-service.tar
2. load the downloaded cache-service image to minikube docker registry
   minikube ssh docker load < cache-service.tar 
3. deploy/start the cache-service container
   kubectl run vishapp --image=cache-service --image-pull-policy=Never
4. Expose the app
   kubectl expose deployment vishapp --type=LoadBalancer --port=8080
5. check the external port to access the cache-service app
   kubectl get services
   
   kubernetes   ClusterIP      10.96.0.1      <none>        443/TCP          129m
   vishapp      LoadBalancer   10.98.12.220   <pending>     8070:32285/TCP   75s

   You can access the cache-service app on http://<minikube IP>:32285/vishapp/ 
  

Swagger API access from cache-service application
----------------------------------------------
1. Open the following url in browser to access the cache-service application REST API info in json format
   http://<minikube IP>:<port>/vishapp/v2/api-docs
2. Open the following url in browser to access the cache-service application REST API info in swagger UI
   http://<minikube IP>:<port>/vishapp/swagger-ui.html
 



