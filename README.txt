To run the project on branch manual_persitent:

0) create a schema called 'jackrabbit' in your mysql installation
1) download jackrabbit 2.5.1 standalone from:

http://jackrabbit.apache.org/downloads.html

2) copy the downloaded jar in your <anywhere>/jackrabbit_home, let's call this dir JACKRABBIT_HOME
3) run jackrabbit (http://jackrabbit.apache.org/jackrabbit-standalone.html):

java -jar jackrabbit-standalone-2.5.1.jar

4) stop the server (ctrl+d)
5) copy repository/repository.xml to JACKRABBIT_HOME/jackrabbit
7) create the directory JACKRABBIT_HOME/lib
8) download/get from your local m2repo copy mysql-connector-java-5.1.21.jar in JACKRABBIT_HOME/lib
9) restart jackrabbit as:

java -cp "jackrabbit-standalone-2.5.1.jar:lib/mysql-connector-java-5.1.21.jar" org.apache.jackrabbit.standalone.Main

10) access http://localhost:8080/ you should see the initial page and no error message
11) add this dependency to your pom:
	<dependency>
	   <groupId>org.apache.jackrabbit</groupId>
	   <artifactId>jackrabbit-jcr-rmi</artifactId>
	   <version>2.5.1</version>
	</dependency>
10) change the code in order to use this method for logging in the repo:

JcrUtils.getRepository("http://localhost:8080/rmi");


Reference:
http://wiki.apache.org/jackrabbit/RemoteAccess