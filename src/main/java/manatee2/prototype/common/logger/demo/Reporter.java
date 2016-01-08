package manatee2.prototype.common.logger.demo;

import org.apache.commons.lang3.StringUtils;

import manatee2.prototype.common.logger.shared.LogMessageSeverity;
import manatee2.prototype.common.logger.shared.LogReporter;


/**
 * A stand-alone Application which infinitely loops and produces Log Messages.
 * 
 * The application takes a single command-line argument specifying the Sample Application's runtime name.
 */
public class Reporter implements LogReporter
{
    /**
     * Clump of message texts to be sent periodically.
     */
    private static final String[] MESSAGE_TEXT_LIST =
            {
                    "Now is the time for all good men to come to the aid of their country.",
                    "The quick brown fox jumped over the lazy dogs back.",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            };

    /**
     * Application's runtime name.
     */
    private String reporter;


    /**
     * Identifies the Process which is sending the Log Messages. Required by LogReporter Interface.
     * 
     * @return Unique Process Name (i.e. "Larry", "Curly", "Moe", etc).
     */
    @Override
    public String logReporter()
    {
        return reporter;
    }


    /**
     * A stand-alone Application which infinitely loops and produces Log Messages.
     * 
     * @param args Application's runtime name.
     */
    public static void main(String[] args)
    {
        //
        // Failsafe.
        //
        if (args.length != 1 || !StringUtils.isNotBlank(args[0]))
        {
            System.err.println("Must specify Runtime Name.");
            System.exit(1);
        }

        //
        // Start sending messages.
        //
        new Reporter(args[0].trim());
    }


    /**
     * Constructor. Private to avoid external instantiation.
     * 
     * @param reporter Application's runtime name.
     */
    private Reporter(String reporter)
    {
        //
        // Determine the Application's runtime name.
        //
        this.reporter = reporter;

        //
        // Loop forever.
        //
        while (true)
        {
            //
            // Send one message of each severity.
            //
            for (LogMessageSeverity severity : LogMessageSeverity.values())
            {
                //
                // Send one message with each textual body.
                //
                for (String text : MESSAGE_TEXT_LIST)
                {
                    //
                    // Send the Log Message via JMS.
                    //
                    logMessage(severity, text);

                    //
                    // Rest awhile.
                    //
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException exception)
                    {
                        // Ignore it.
                    }
                }

            }

        }
    }
}
