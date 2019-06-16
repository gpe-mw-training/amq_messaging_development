
# stop both brokers
"./target/server0/bin/artemis-service" stop

# clear the target directory for subsequent runs of this test
mvn clean
