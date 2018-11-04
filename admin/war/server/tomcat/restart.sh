#!/bin/sh
PID=`cat /usr/local/output/qksms/admin/pid`
projectup=$1
echo "==========="$projectup
kill $PID
sleep 2
kill -9 $PID
sleep 2

mkdir -p /usr/local/app/qksms/admin/tomcat/

rm -rf /usr/local/app/qksms/admin/tomcat/*

cp -r $projectup/qksms/admin/war/target/tomcat/* /usr/local/app/qkclearing/admin/tomcat/

cd /usr/local/app/qksms/admin/tomcat/
rm -rf webapps
mkdir webapps
rm -rf temp
mkdir temp

rm -rf /usr/local/output/qksms/admin/pid

echo 'start tomcat'
CATALINA_HOME=/usr/local/tomcat
CATALINA_BASE=/usr/local/app/qksms/admin/tomcat
CATALINA_OUT=/usr/local/output/qksms/admin/catalina.out
CATALINA_PID=/usr/local/output/qksms/admin/pid
JAVA_OPTS="-Xms512m  -Xmx1024m   -XX:PermSize=128M -XX:MaxNewSize=256m -XX:MaxPermSize=256m   -Xdebug -Xrunjdwp:transport=dt_socket,address=17002,server=y,suspend=n"
unzip -o $projectup/qksms/admin/war/target/admin.war-1.0.war -d /usr/local/app/qksms/admin/tomcat/webapps/ROOT
export CATALINA_HOME CATALINA_BASE CATALINA_PID CATALINA_OUT JAVA_OPTS
echo "unzip complete"
echo "start ...."
$CATALINA_HOME/bin/catalina.sh start
