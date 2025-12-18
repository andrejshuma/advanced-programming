package labs.lab7;

import java.util.*;
import java.util.concurrent.*;

public class FakeApiPing {

    public static class ApiResult {
        public final int requestId;
        public final boolean success;
        public final String value;

        public ApiResult(int requestId, boolean success, String value) {
            this.requestId = requestId;
            this.success = success;
            this.value = value;
        }

        @Override
        public String toString() {
            return "ApiResult{" +
                    "requestId=" + requestId +
                    ", success=" + success +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    static class Api {
        public static ApiResult get(int requestId, int parameter) throws InterruptedException {
            long delayMillis = parameter * 100L;
            Thread.sleep(delayMillis);

            String response = "VALUE_" + parameter;
            return new ApiResult(requestId, true, response);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        List<Callable<ApiResult>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int parameter = sc.nextInt();

            int requestId = i + 1;

            tasks.add(() -> Api.get(requestId, parameter));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<ApiResult>> futures = new ArrayList<>();

        for (Callable<ApiResult> task : tasks) {
            futures.add(executor.submit(task));
        }

        List<ApiResult> results = new ArrayList<>();

        long timeoutMillis = 200;

        for (int i = 0; i < futures.size(); i++) {
            Future<ApiResult> future = futures.get(i);
            int requestId = i + 1;

            try {
                ApiResult result = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                results.add(result);
            } catch (TimeoutException e) {
                results.add(new ApiResult(requestId, false, "TIMEOUT"));
            }
        }

        executor.shutdown();

        results.sort(Comparator.comparingInt(r -> r.requestId));

        for (ApiResult r : results) {
            System.out.printf(
                    "%d %s %s%n",
                    r.requestId,
                    r.success ? "OK" : "FAILED",
                    r.value
            );
        }
    }
}