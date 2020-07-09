package solution.two.system.design;

//Crating interface for public static final constants
interface Constants {
    int THREAD_POOL_SIZE = 50;
    String destDir = "csv/";
    String SUCCESS = "success";
    String FAIL = "fail";

    String TABLE_NAME = "";
    int BATCH_SIZE_FOR_DB_INSERT = 100;
    int TIME_INTERVAL_FOR_DOWNLOADING = 900000; // represents 15min in milliseconds

}
