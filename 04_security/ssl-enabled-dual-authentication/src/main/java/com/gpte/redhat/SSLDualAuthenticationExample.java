package com.gpte.redhat;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

/**
 * Lab exercise using a JMS Queue configured with a dual broker authentication mechanisms for SSL and non-SSL connections.
 */
public class SSLDualAuthenticationExample {

   public static void main(final String[] args) throws Exception {
      Connection producerConnection = null;
      Connection consumerConnection = null;
      InitialContext initialContext = null;
      try {
         // Step 1. Create an initial context to perform the JNDI lookup.
         initialContext = new InitialContext();

         // Step 2. Perfom a lookup on the queue
	 //EXCLUDE-BEGIN
         Queue queue = (Queue) initialContext.lookup("queue/demoQueue");
	 //EXCLUDE-END

         // Step 3. Perform a lookup on the producer's SSL Connection Factory
	 //EXCLUDE-BEGIN
         ConnectionFactory producerConnectionFactory = (ConnectionFactory) initialContext.lookup("SslConnectionFactory");
	 //EXCLUDE-END

         // Step 4. Perform a lookup on the consumer's Connection Factory
	 //EXCLUDE-BEGIN
         ConnectionFactory consumerConnectionFactory = (ConnectionFactory) initialContext.lookup("SslConnectionFactory");
	 //EXCLUDE-END

         // Step 5.Create a JMS Connection for the producer
	 //EXCLUDE-BEGIN
         producerConnection = producerConnectionFactory.createConnection();
	 //EXCLUDE-END

         // Step 6.Create a JMS Connection for the consumer
	 //EXCLUDE-BEGIN
         consumerConnection = consumerConnectionFactory.createConnection("consumer", "activemq");
	 //EXCLUDE-END

         // Step 7. Create a JMS Session for the producer
	 //EXCLUDE-BEGIN
         Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	 //EXCLUDE-END

         // Step 8. Create a JMS Session for the consumer
	 //EXCLUDE-BEGIN
         Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	 //EXCLUDE-END

         // Step 9. Create a JMS Message Producer
	 //EXCLUDE-BEGIN
         MessageProducer producer = producerSession.createProducer(queue);
	 //EXCLUDE-END

         // Step 10. Create a Text Message
         TextMessage message = null;
	 //EXCLUDE-BEGIN
         message = producerSession.createTextMessage("This is a text message");
	 //EXCLUDE-END

         System.out.println("Sent message: " + message.getText());

         // Step 11. Send the Message
	 //EXCLUDE-BEGIN
         producer.send(message);
	 //EXCLUDE-END

         // Step 12. Create a JMS Message Consumer
	 //EXCLUDE-BEGIN
         MessageConsumer messageConsumer = consumerSession.createConsumer(queue);
	 //EXCLUDE-END

         // Step 13. Start the Connection
	 //EXCLUDE-BEGIN
         consumerConnection.start();
	 //EXCLUDE-END

         // Step 14. Receive the message
         TextMessage messageReceived = null;
	 //EXCLUDE-BEGIN
         messageReceived = (TextMessage) messageConsumer.receive(5000);
	 //EXCLUDE-END

         System.out.println("Received message: " + messageReceived.getText());

         initialContext.close();
      } finally {
         // Step 15. Be sure to close our JMS resources!
         if (initialContext != null) {
            initialContext.close();
         }
         if (producerConnection != null) {
            producerConnection.close();
         }
         if (consumerConnection != null) {
            consumerConnection.close();
         }
      }
   }
}
