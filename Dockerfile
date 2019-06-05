#FROM openjdk:1.8.0_144
FROM openjdk:8
ENV CATALINA_HOME /usr/local/tomcat
RUN rm -rf  $CATALINA_HOME/*
RUN mkdir -p $CATALINA_HOME
RUN mkdir -p /usr/local/vishapp/logs/
RUN ls $CATALINA_HOME
ENV PATH $CATALINA_HOME/bin:$PATH
ADD apache-tomcat  $CATALINA_HOME/
RUN ls  $CATALINA_HOME/
ADD target/vishapp.war  $CATALINA_HOME/webapps/

RUN ls  $CATALINA_HOME/webapps/
RUN echo $PATH
CMD ["catalina.sh", "run"]
EXPOSE 8080


