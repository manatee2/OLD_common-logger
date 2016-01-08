package manatee2.prototype.common.logger.tailer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import manatee2.prototype.common.logger.shared.LogConstants;
import manatee2.prototype.common.logger.shared.LogMessage;


/**
 * Stand-alone application which listens for new Log Messages to be posted to the JMS Topic.
 */
public class TailerJMS
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
            // Create the Incoming JMS Topic.
            //
            Destination jmsIncomingTopic = jmsSession.createTopic(LogConstants.JMS_TOPIC);

            //
            // Create a JMS Message Consumer from the Session to the Topic.
            //
            MessageConsumer jmsConsumer = jmsSession.createConsumer(jmsIncomingTopic);

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

                //
                // Show our stuff.
                //
                System.out.println("Received: " + logMessage.toString());
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
    private TailerJMS()
    {
        // Nothing to do.
    }

}
