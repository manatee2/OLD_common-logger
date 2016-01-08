package manatee2.prototype.common.logger.shared;

/**
 * Defines the Log Levels, Lowest (Debug) to Highest (Fatal).
 */
public enum LogMessageSeverity
{
    /**
     * Fine-grain events which are most-useful to the Developer attempting to diagnose the application.
     * 
     * Example: "File abc.jpg was copied in 1500 milliseconds to disk block 3000."
     */
    Debug,

    /**
     * Informational messages which highlight the progress of the application. These are typically viewable by the
     * End-User.
     * 
     * Example: "File abc.jpg were copied to the Backup Drive."
     */
    Info,

    /**
     * Non-standard circumstances which do not interfere with the current processing. These events are often
     * auto-corrected.
     * 
     * Example: "File abc.jpg already existed on the Backup Drive and it was overwritten with the newer version."
     */
    Warning,

    /**
     * Problems which inhibit the current processing.
     * 
     * Example: "File abc.jpg was not copied to the Backup Drive because the original file no-longer exists."
     */
    Error,

    /**
     * Terminal errors which abort all processing.
     * 
     * Example: "The Archive was aborted because the Backup Drive is not accessible."
     */
    Fatal
}
