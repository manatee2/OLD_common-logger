package manatee2.prototype.common.logger.service;

import java.sql.DriverManager;
import java.sql.Statement;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import manatee2.prototype.common.logger.shared.LogConstants;
import manatee2.prototype.common.logger.shared.LogMessage;


/**
 * Stand-alone application which:
 * 
 * 1) Listens to the JMS Queue for new Log Messages.
 * 
 * 2) Stores the Log Messages in the Database.
 * 
 * 3) Re-sends the Log Messages on the JMS Topic.
 * 
 * Note: This is currently a single-threaded application; it will most-likely be upgraded to a
 * multi-threaded/multi-instance application to increase its throughput rate.
 */
public class Logger
{
    public static void main(String[] args)
    {
        try
        {
            //
            // Create a JMS Connection and start it.
            //
            ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory(LogConstants.JMS_URL);
            Connection jmsConnection = jmsConnectionFactory.createConnection();
            jmsConnection.start();

            //
            // Create a JMS Session.
            //
            Session jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //
            // Create the Incoming JMS Queue.
            //
            Destination jmsIncomingQueue = jmsSession.createQueue(LogConstants.JMS_QUEUE);

            //
            // Create a JMS Message Consumer from the Session to the Queue.
            //
            MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsIncomingQueue);

            //
            // Create the Outgoing JMS Topic.
            //
            Destination jmsOutgoingTopic = jmsSession.createTopic(LogConstants.JMS_TOPIC);

            //
            // Create a JMS Message Producer from the Session to the Topic.
            //
            MessageProducer jmsProducer = jmsSession.createProducer(jmsOutgoingTopic);

            //
            // Open a JDBC Connection to the Database.
            //
            java.sql.Connection jdbcConnection =
                    DriverManager.getConnection(LogConstants.DATABASE_URL, LogConstants.DATABASE_USER,
                            LogConstants.DATABASE_PASSWORD);

            //
            // Create a JDBC Statement.
            //
            Statement jdbcStatement = jdbcConnection.createStatement();

            //
            // Loop forever.
            //
            while (true)
            {
                //
                // Wait for a Log Message to show-up on the JMS Queue.
                //
                Message message = jmsConsumer.receive(1000);

                //
                // Handle a Timeout.
                //
                if (message == null)
                {
                    continue;
                }

                //
                // Verify it is a valid JMS Message.
                //
                if (!(message instanceof ObjectMessage))
                {
                    System.err.println("Ignoring Non-ObjectMessage: " + message);
                    continue;
                }

                //
                // Extract the raw message.
                //
                ObjectMessage objectMessage = (ObjectMessage) message;

                //
                // Verify it is actually a Log Message.
                //
                if (!(objectMessage.getObject() instanceof LogMessage))
                {
                    System.err.println("Ignoring Non-LogMessage: " + message);
                    continue;
                }

                //
                // Extract the Log Message from the JMS Message.
                //
                LogMessage logMessage = (LogMessage) objectMessage.getObject();

                System.out.println("Received: " + logMessage.toString());

                //
                // Insert the Log Message into the Database.
                //
                String query =
                        "INSERT INTO log_messages (timestamp, reporter, severity, text) VALUES "
                                + "("
                                + "'" + logMessage.getTimestamp() + "'"
                                + ","
                                + "'" + logMessage.getReporter() + "'"
                                + ","
                                + "'" + logMessage.getSeverity() + "'"
                                + ","
                                + "'" + logMessage.getText() + "'"
                                + ")";
                jdbcStatement.executeUpdate(query);

                //
                // Re-send the Log Message to the JMS Topic.
                //
                jmsProducer.send(objectMessage);
            }
        }
        catch (Exception exception)
        {
            //
            // TODO - Handle the Exception in a better manner.
            //
            System.err.println("Unable to Log Message: " + exception);
            exception.printStackTrace();
        }

    }


    /**
     * Prevent instantiation of Application Class.
     */
    private Logger()
    {
        // Nothing to do.
    }

}
