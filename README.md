Archivix
========

Outlook msg to sqlite archiver

Building dependencies :

maven
MigLayout
org.xerila.sqlite-jdbc
org.apache.poi
swingx

Compile :
git clone https://github.com/APicq/Archivix.git
cd Archivix
mvn clean compile assembly:single
cd target
java -jar [nameOfJar].jar
