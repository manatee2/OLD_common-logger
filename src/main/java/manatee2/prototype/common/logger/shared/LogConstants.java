package manatee2.prototype.common.logger.shared;

/**
 * Constants used throughout the Logger.
 *
 * TODO - Replace the hard-coded values with Spring/JNLP/Properties/etc.
 */
public interface LogConstants
{
    /**
     * URL of the JMS Message Broker (i.e. ActiveMQ).
     */
    static final String JMS_URL = "tcp://localhost:61616";

    /**
     * JMS Queue to-which the Log Message is sent.
     */
    static final String JMS_QUEUE = "LogDemoQueue";

    /**
     * JMS Topic to-which the Log Message is re-sent.
     */
    static final String JMS_TOPIC = "LogDemoTopic";

    /**
     * URL of the Database in-which the Log Messages are retained.
     */
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/manateedb";

    /**
     * Username of the Database in-which the Log Messages are retained.
     */
    static final String DATABASE_USER = "manateeuser";

    /**
     * Password of the Database in-which the Log Messages are retained.
     */
    static final String DATABASE_PASSWORD = "manateepassword";
}
