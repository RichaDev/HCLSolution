package solution.two.system.design;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ProcessDownloadedCSVCallable implements Callable<String> {
    String filename;

    public ProcessDownloadedCSVCallable(String filename) {
        this.filename = filename;
    }

    @Override
    public String call() throws Exception {
        Connection connection = null;
        try {

            connection = new DatabaseImpl().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(SFTPCsvUtil.getSQLQuery());
            BufferedReader lineReader = new BufferedReader(new FileReader(Constants.destDir + filename));
            String lineText = null;

            int count = 0;

            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String resultTime = data[0];
                String granularityPeriod = data[1];
                String ObjectName = data[2];
                String cellId = data[3];
                String callAttempts = data[4];

                statement.setString(1, resultTime);
                statement.setString(2, granularityPeriod);
                statement.setString(3, ObjectName);
                statement.setString(4, cellId);
                statement.setString(5, callAttempts);

                statement.addBatch();

                if (count % Constants.BATCH_SIZE_FOR_DB_INSERT == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            return Constants.SUCCESS;
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null)
                connection.close();
        }

        return filename;  //returning filename so that it can be retried
    }
}
