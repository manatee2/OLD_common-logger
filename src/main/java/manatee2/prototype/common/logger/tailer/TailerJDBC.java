package manatee2.prototype.common.logger.tailer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import manatee2.prototype.common.logger.shared.LogConstants;


/**
 * Stand-alone application which listens for new Log Messages to be inserted into the Database.
 */
public class TailerJDBC
{
    public static void main(String[] args)
    {
        try
        {
            //
            // Open a JDBC Connection.
            //
            Connection jdbcConnection =
                    DriverManager.getConnection(LogConstants.DATABASE_URL, LogConstants.DATABASE_USER,
                            LogConstants.DATABASE_PASSWORD);

            //
            // Create a JDBC Statement.
            //
            Statement jdbcStatement = jdbcConnection.createStatement();

            //
            // Find the last message logged.
            //
            String query = "SELECT MAX(id) FROM log_messages";
            ResultSet resultSet = jdbcStatement.executeQuery("SELECT MAX(id) FROM log_messages");
            int lastId = 0;
            while (resultSet.next())
            {
                lastId = resultSet.getInt("max");
            }

            //
            // Loop forever.
            //
            while (true)
            {
                //
                // Pull the latest rows.
                //
                query = "SELECT id, timestamp, reporter, severity, text FROM log_messages WHERE id > " + lastId
                        + "ORDER BY id ASC";
                resultSet = jdbcStatement.executeQuery(query);

                //
                // Examine each row.
                //
                while (resultSet.next())
                {
                    //
                    // Extract each column by name.
                    //
                    int id = resultSet.getInt("id");
                    Date timestamp = resultSet.getDate("timestamp");
                    String reporter = resultSet.getString("reporter");
                    String severity = resultSet.getString("severity");
                    String messageText = resultSet.getString("text");

                    //
                    // Show our stuff.
                    //
                    System.out.println("" + id + ":" + timestamp + ":" + reporter + ":" + severity + ":" + messageText);

                    //
                    // Pick up where we left off.
                    //
                    lastId = Math.max(lastId, id);
                }

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
        catch (SQLException exception)
        {
            //
            // TODO - Handle the Exception in a better manner.
            //
            System.err.println("Unable to Tail Log Message: " + exception);
            exception.printStackTrace();
        }
    }


    /**
     * Prevent instantiation of Application Class.
     */
    private TailerJDBC()
    {
        // Nothing to do.
    }

}
