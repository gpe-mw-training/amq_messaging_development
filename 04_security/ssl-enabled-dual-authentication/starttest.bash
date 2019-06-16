# ssl-enabled-dual-authentication example
# build
mvn clean install

# create the broker
$AMQ_HOME/bin/artemis create --user guest --password guest --role guest --allow-anonymous --java-options -Djava.net.preferIPv4Stack=true ./target/server0

# copy the security configuration files to the broker dir
cp src/main/resources/activemq/server0/* target/server0/etc/

# start the broker
"./target/server0/bin/artemis-service" start

# run the Java example
mvn exec:java -Dexec.mainClass="com.gpte.redhat.SSLDualAuthenticationExample" -Dexec.cleanupDaemonThreads=false

