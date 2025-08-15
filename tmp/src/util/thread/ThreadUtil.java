package util.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadUtil {
    
    private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    
    /**
     * 여러 작업을 병렬로 실행하고 결과를 Map으로 반환
     * @param tasks 작업명과 Callable 맵
     * @return 작업명과 결과값 맵
     */
    public static <T> Map<String, T> executeParallel(Map<String, Callable<T>> tasks) {
        return executeParallel(tasks, DEFAULT_THREAD_POOL_SIZE);
    }
    
    /**
     * 여러 작업을 병렬로 실행하고 결과를 Map으로 반환 (스레드 풀 크기 지정)
     */
    public static <T> Map<String, T> executeParallel(Map<String, Callable<T>> tasks, int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        Map<String, Future<T>> futures = new HashMap<>();
        Map<String, T> results = new HashMap<>();
        
        try {
            // 작업 제출
            for (Map.Entry<String, Callable<T>> entry : tasks.entrySet()) {
                futures.put(entry.getKey(), executor.submit(entry.getValue()));
            }
            
            // 결과 수집
            for (Map.Entry<String, Future<T>> entry : futures.entrySet()) {
                try {
                    results.put(entry.getKey(), entry.getValue().get());
                } catch (Exception e) {
                    System.err.println("작업 " + entry.getKey() + " 실행 중 오류: " + e.getMessage());
                    results.put(entry.getKey(), null);
                }
            }
        } finally {
            executor.shutdown();
        }
        
        return results;
    }
    
    /**
     * CompletableFuture를 사용한 비동기 작업 실행
     * @param tasks 작업명과 Supplier 맵
     * @return 작업명과 CompletableFuture 맵
     */
    public static <T> Map<String, CompletableFuture<T>> executeAsync(Map<String, Supplier<T>> tasks) {
        Map<String, CompletableFuture<T>> futures = new HashMap<>();
        
        for (Map.Entry<String, Supplier<T>> entry : tasks.entrySet()) {
            futures.put(entry.getKey(), CompletableFuture.supplyAsync(entry.getValue()));
        }
        
        return futures;
    }
    
    /**
     * CompletableFuture 결과 수집 (모든 작업 완료 대기)
     */
    public static <T> Map<String, T> collectResults(Map<String, CompletableFuture<T>> futures) {
        Map<String, T> results = new HashMap<>();
        
        // 모든 작업 완료 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            futures.values().toArray(new CompletableFuture[0])
        );
        allOf.join();
        
        // 결과 수집
        for (Map.Entry<String, CompletableFuture<T>> entry : futures.entrySet()) {
            try {
                results.put(entry.getKey(), entry.getValue().get());
            } catch (Exception e) {
                System.err.println("작업 " + entry.getKey() + " 결과 수집 중 오류: " + e.getMessage());
                results.put(entry.getKey(), null);
            }
        }
        
        return results;
    }
    
    /**
     * 여러 작업을 병렬로 실행하고 첫 번째 완료된 결과만 반환
     */
    public static <T> T executeFirstCompleted(List<Callable<T>> tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        
        try {
            return executor.invokeAny(tasks);
        } catch (Exception e) {
            System.err.println("작업 실행 중 오류: " + e.getMessage());
            return null;
        } finally {
            executor.shutdown();
        }
    }
    
    /**
     * 리스트의 각 요소에 함수를 병렬로 적용
     */
    public static <T, R> List<R> parallelMap(List<T> list, Function<T, R> mapper) {
        return parallelMap(list, mapper, DEFAULT_THREAD_POOL_SIZE);
    }
    
    /**
     * 리스트의 각 요소에 함수를 병렬로 적용 (스레드 풀 크기 지정)
     */
    public static <T, R> List<R> parallelMap(List<T> list, Function<T, R> mapper, int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<R>> futures = new ArrayList<>();
        List<R> results = new ArrayList<>();
        
        try {
            // 작업 제출
            for (T item : list) {
                futures.add(executor.submit(() -> mapper.apply(item)));
            }
            
            // 결과 수집
            for (Future<R> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    System.err.println("병렬 처리 중 오류: " + e.getMessage());
                    results.add(null);
                }
            }
        } finally {
            executor.shutdown();
        }
        
        return results;
    }
    
    /**
     * 지정된 시간 후에 작업 실행 (스케줄링)
     */
    public static <T> CompletableFuture<T> executeAfter(Supplier<T> task, long delay, TimeUnit unit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(unit.toMillis(delay));
                return task.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * 타임아웃이 있는 작업 실행
     */
    public static <T> T executeWithTimeout(Callable<T> task, long timeout, TimeUnit unit) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        try {
            Future<T> future = executor.submit(task);
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            System.err.println("작업 타임아웃: " + timeout + " " + unit);
            return null;
        } catch (Exception e) {
            System.err.println("작업 실행 중 오류: " + e.getMessage());
            return null;
        } finally {
            executor.shutdown();
        }
    }
    
    /**
     * 스레드 안전하게 대기
     */
    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
    
    /**
     * 여러 스레드가 모두 완료될 때까지 대기
     */
    public static void joinAll(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread join interrupted", e);
            }
        }
    }
}