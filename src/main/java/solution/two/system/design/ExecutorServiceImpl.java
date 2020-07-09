package solution.two.system.design;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceImpl {
    private ExecutorService executor;

    public ExecutorServiceImpl() {
        executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
    }

    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }


    public ExecutorService getExecutor() {
        return executor;
    }
}
