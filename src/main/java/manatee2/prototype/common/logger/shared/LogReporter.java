package manatee2.prototype.common.logger.shared;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;


/**
 * Utility for Logging Messages throughout the Manatee2 Prototype.
 * 
 * Any class which wishes to send Log Messages will implement this interface.
 * 
 * The only required method is 'logReporter()'.
 * 
 * To send a Log Message, the application would then call the logMessage(...) method.
 */
public interface LogReporter
{
    /**
     * Identifies the Application which is sending the Log Messages.
     * 
     * @return Application's runtime name (i.e. "Larry", "Curly", "Moe", etc).
     */
    public String logReporter();


    /**
     * Logs a single message.
     * 
     * @param severity Log Level (Debug, Info, Warning, Error, Fatal).
     * @param text Verbose message text.
     */
    public default void logMessage(LogMessageSeverity severity, String text)
    {
        //
        // Identify the Log Reporter.
        //
        String reporter = logReporter();

        //
        // Determine the current Log Level.
        //
        LogMessageSeverity logLevel = logLevel();

        //
        // Failsafe.
        //
        if (!StringUtils.isNotBlank(reporter))
        {
            reporter = "unknown";
        }
        if (severity == null)
        {
            severity = LogMessageSeverity.Info;
        }
        if (!StringUtils.isNotBlank(text))
        {
            text = "unknown";
        }
        if (logLevel == null)
        {
            logLevel = LogMessageSeverity.Debug;
        }

        //
        // Cleanup.
        //
        reporter = reporter.trim();
        text = text.trim();

        //
        // Determine whether the Log Message meets the currently-specified Minimum Log Level.
        //
        if (severity.compareTo(logLevel) < 0)
        {
            return;
        }

        //
        // Create a Log Message object.
        //
        LogMessage logMessage = new LogMessage(reporter, severity, text);

        //
        // Send it via JMS.
        //
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
            // Create the Outgoing JMS Destination Queue.
            //
            Destination jmsDestination = jmsSession.createQueue(LogConstants.JMS_QUEUE);

            //
            // Create a JMS Message Producer from the Session to the Queue.
            //
            MessageProducer jmsProducer = jmsSession.createProducer(jmsDestination);

            //
            // Retain the Message should the JMS Broker reboot.
            //
            jmsProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //
            // Bundle the Message and send it.
            //
            ObjectMessage objectMessage = jmsSession.createObjectMessage(logMessage);
            jmsProducer.send(objectMessage);
        }
        catch (JMSException exception)
        {
            //
            // TODO - Handle the Exception in a better manner.
            //
            System.err.println("Unable to Log Message: " + exception);
            exception.printStackTrace();
        }
    }


    /**
     * Allows limitation of Log Level.
     * 
     * @return Minimum Level at-which messages should be shown; anything below this level are discarded.
     */
    public default LogMessageSeverity logLevel()
    {
        return LogMessageSeverity.Debug;
    }


    /**
     * Convenience method to log a Debug message.
     * 
     * @param text Verbose message text.
     */
    public default void logDebug(String text)
    {
        logMessage(LogMessageSeverity.Debug, text);
    }


    /**
     * Convenience method to log an Info message.
     * 
     * @param text Verbose message text.
     */
    public default void logInfo(String text)
    {
        logMessage(LogMessageSeverity.Info, text);
    }


    /**
     * Convenience method to log a Warning message.
     * 
     * @param text Verbose message text.
     */
    public default void logWarning(String text)
    {
        logMessage(LogMessageSeverity.Warning, text);
    }


    /**
     * Convenience method to log an Error message.
     * 
     * @param text Verbose message text.
     */
    public default void logError(String text)
    {
        logMessage(LogMessageSeverity.Error, text);
    }


    /**
     * Convenience method to log a Fatal message.
     * 
     * @param text Verbose message text.
     */
    public default void logFatal(String text)
    {
        logMessage(LogMessageSeverity.Fatal, text);
    }

}
