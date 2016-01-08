package manatee2.prototype.common.logger.shared;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A single Log Message.
 */
public class LogMessage implements Serializable
{
    /**
     * Supports serialization. This number will be incremented as non-backward-compatible releases are available.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Date-Time at-which the Message was logged. This will be auto-populated when the Log Message is instantiated.
     */
    private LocalDateTime timestamp;

    /**
     * Identifies the process which reported the message.
     */
    private String reporter;

    /**
     * Log Level (Debug, Info, Warning, Error, Fatal).
     */
    private LogMessageSeverity severity;

    /**
     * Verbose message text.
     */
    private String text;


    public LogMessage(String reporter, LogMessageSeverity severity, String text)
    {
        super();
        this.timestamp = LocalDateTime.now();
        this.reporter = reporter;
        this.severity = severity;
        this.text = text;
    }


    @Override
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-hh:mm:ss");
        return "LogMessage [timestamp=" + formatter.format(timestamp) + ", reporter=" + reporter + ", severity="
                + severity + ", text=" + text + "]";
    }


    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }


    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }


    public String getReporter()
    {
        return reporter;
    }


    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }


    public LogMessageSeverity getSeverity()
    {
        return severity;
    }


    public void setSeverity(LogMessageSeverity severity)
    {
        this.severity = severity;
    }


    public String getText()
    {
        return text;
    }


    public void setText(String text)
    {
        this.text = text;
    }

}
