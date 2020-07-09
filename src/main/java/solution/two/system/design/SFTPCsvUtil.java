package solution.two.system.design;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class SFTPCsvUtil {

    public static void downloadFile(String source, String destination) {
        SFTPClient sftpClient = new SFTPClient();
        try {
            sftpClient.connect();
            sftpClient.download(source, destination);

        } catch (JSchException e) {
            e.printStackTrace();
            //can also add retry mechanism
        } catch (SftpException e) {
            e.printStackTrace();
            //can also add retry mechanism
        } finally {
            sftpClient.disconnect();
        }
    }

    public static List<String> getLatestFileList(String sourceDir, int timestampInSec) {
        SFTPClient sftpClient = new SFTPClient();
        List<String> filenames = new ArrayList<>();
        try {
            sftpClient.connect();
            Vector<ChannelSftp.LsEntry> list = sftpClient.getFileList(sourceDir, timestampInSec);
            filenames = list.stream().map(entry -> entry.getFilename()).collect(Collectors.toList());

        } catch (JSchException e) {
            e.printStackTrace();
            //can also add retry mechanism
        } catch (SftpException e) {
            e.printStackTrace();
            //can also add retry mechanism
        } finally {
            sftpClient.disconnect();
        }
        return filenames;
    }

    public static String getSQLQuery() {

        return "INSERT INTO +" + Constants.TABLE_NAME +
                " (result_time, granularity_period, Object_name, cell_id, call_attempts) VALUES (?, ?, ?, ?, ?)";
    }

    public static void updateFileCacheFromResponse(Future<String> callableResponse, Set<String> fileNamesDownloaded) {
        try {
            String response = callableResponse.get(30, TimeUnit.SECONDS);
            if (!Constants.SUCCESS.equalsIgnoreCase(response)) {
                fileNamesDownloaded.remove(response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            //add retry logic
        } catch (ExecutionException e) {
            e.printStackTrace();
            //add retry logic.
        } catch (TimeoutException e) {
            e.printStackTrace();
            //operation timed out. Can increase the timeout.
        }
    }


}
