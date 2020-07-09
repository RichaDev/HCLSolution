package solution.two.system.design;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

class SFTPCsvToDatabaseUpload {

    ExecutorServiceImpl executorService = new ExecutorServiceImpl();

    Set<String> fileNamesDownloadedCache = new HashSet<>();

    private void downloadFilesAndSaveInDatabase(String sourceDir, int timeSecInSec) {


        List<String> filenames = SFTPCsvUtil.getLatestFileList(sourceDir, timeSecInSec);
        List<ProcessDownloadedCSVCallable> tasks = new ArrayList<>();
        List<Future<String>> callableResponses = new ArrayList<>();

        //for loop to create multiple callable for running in parellel
        for (String filename : filenames) {
            if (fileNamesDownloadedCache.contains(filename))
                continue;

            SFTPCsvUtil.downloadFile(sourceDir + filename, Constants.destDir + filename);
            tasks.add(new ProcessDownloadedCSVCallable(filename));
        }

        try {
            callableResponses = executorService.getExecutor().invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //can be retried.
        }
        fileNamesDownloadedCache.addAll(filenames);


        for (Future<String> eachResponse : callableResponses) {
            SFTPCsvUtil.updateFileCacheFromResponse(eachResponse, fileNamesDownloadedCache);
        }

    }

    public static void main(String... args) throws InterruptedException {
        int currTimeInSec = 0;
        SFTPCsvToDatabaseUpload service = new SFTPCsvToDatabaseUpload();
        //infinite loop to keep code running and updating the csv to database in 15 mins.

        while (true) {
            service.downloadFilesAndSaveInDatabase("SourceDir/csv/", currTimeInSec);
            currTimeInSec = (int) Calendar.getInstance().getTimeInMillis() / 1000;
            //making thread sleep for 15 min before next iteration
            Thread.sleep(Constants.TIME_INTERVAL_FOR_DOWNLOADING);
        }
    }

}