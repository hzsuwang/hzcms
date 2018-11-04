#!/bin/sh
pname="hzcms"
PID=`cat /usr/local/output/${pname}/admin/pid`
kill $PID
sleep 2
kill -9 $PID
sleep 2
mkdir -p /usr/local/output/${pname}/admin/tomcat_logs
echo 'start tomcat'
JAVA_HOME=/usr/local/jdk
CATALINA_HOME=/usr/local/tomcat
CATALINA_BASE=/usr/local/project/${pname}/admin/tomcat
CATALINA_OUT=/usr/local/output/${pname}/admin/tomcat_logs/catalina.out
CATALINA_PID=/usr/local/output/${pname}/admin/pid
JAVA_OPTS="-Xms2048m -Xmx4096m -XX:PermSize=512M -XX:MaxNewSize=1024m -XX:MaxPermSize=1024m"
export JAVA_HOME CATALINA_HOME CATALINA_BASE CATALINA_PID CATALINA_OUT JAVA_OPTS 
$CATALINA_HOME/bin/catalina.sh start
