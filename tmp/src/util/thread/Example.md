# Thread 시험문제 & ThreadUtil 사용 예시

## 🧪 시험문제 1: 여러 서버에서 데이터 가져오기

**문제**: 3개의 서버(A, B, C)에서 동시에 데이터를 가져와서 각 서버별 응답시간과 데이터를 출력하는 프로그램을 작성하시오.
- 서버 A: 1초 후 "Data from A" 반환
- 서버 B: 2초 후 "Data from B" 반환  
- 서버 C: 0.5초 후 "Data from C" 반환

```java
import java.util.Map;
import java.util.concurrent.Callable;

public class Problem1_MultiServer {
    public static void main(String[] args) {
        // 시작 시간 기록
        long startTime = System.currentTimeMillis();
        
        Map<String, Callable<String>> serverTasks = Map.of(
            "ServerA", () -> {
                Thread.sleep(1000);
                return "Data from A";
            },
            "ServerB", () -> {
                Thread.sleep(2000);
                return "Data from B";
            },
            "ServerC", () -> {
                Thread.sleep(500);
                return "Data from C";
            }
        );
        
        Map<String, String> results = ThreadUtil.executeParallel(serverTasks);
        
        long endTime = System.currentTimeMillis();
        System.out.println("전체 실행시간: " + (endTime - startTime) + "ms");
        System.out.println("결과: " + results);
        // 예상 출력: 약 2초 (가장 오래 걸리는 ServerB 기준)
    }
}
```

## 🧪 시험문제 2: 대용량 리스트 병렬 처리

**문제**: 1부터 1000까지의 숫자 리스트에서 각 숫자의 제곱을 구하되, 병렬 처리로 성능을 최적화하시오.

```java
import java.util.List;
import java.util.stream.IntStream;

public class Problem2_ParallelCalculation {
    public static void main(String[] args) {
        // 1부터 1000까지 리스트 생성
        List<Integer> numbers = IntStream.rangeClosed(1, 1000)
                .boxed().toList();
        
        long startTime = System.currentTimeMillis();
        
        // 병렬로 제곱 계산
        List<Integer> squares = ThreadUtil.parallelMap(numbers, x -> x * x);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("처리된 개수: " + squares.size());
        System.out.println("처리 시간: " + (endTime - startTime) + "ms");
        System.out.println("처음 10개 결과: " + squares.subList(0, 10));
    }
}
```

## 🧪 시험문제 3: 타임아웃 처리

**문제**: 외부 API 호출 시 3초 이내에 응답이 없으면 타임아웃 처리하는 코드를 작성하시오.

```java
import java.util.concurrent.TimeUnit;

public class Problem3_TimeoutHandling {
    
    // 느린 API 시뮬레이션
    private static String slowApiCall() throws InterruptedException {
        Thread.sleep(5000); // 5초 걸리는 작업
        return "API 응답 데이터";
    }
    
    public static void main(String[] args) {
        System.out.println("API 호출 시작...");
        
        String result = ThreadUtil.executeWithTimeout(
            () -> slowApiCall(), 
            3, 
            TimeUnit.SECONDS
        );
        
        if (result != null) {
            System.out.println("API 응답: " + result);
        } else {
            System.out.println("API 호출 타임아웃 발생!");
        }
    }
}
```

## 🧪 시험문제 4: 가장 빠른 응답 받기

**문제**: 3개의 백업 서버 중에서 가장 빨리 응답하는 서버의 데이터만 사용하는 프로그램을 작성하시오.

```java
import java.util.List;
import java.util.concurrent.Callable;

public class Problem4_FastestResponse {
    public static void main(String[] args) {
        List<Callable<String>> backupServers = List.of(
            () -> {
                Thread.sleep(3000); // 3초
                return "Server1 응답";
            },
            () -> {
                Thread.sleep(1000); // 1초 (가장 빠름)
                return "Server2 응답";
            },
            () -> {
                Thread.sleep(2000); // 2초
                return "Server3 응답";
            }
        );
        
        long startTime = System.currentTimeMillis();
        String fastestResult = ThreadUtil.executeFirstCompleted(backupServers);
        long endTime = System.currentTimeMillis();
        
        System.out.println("가장 빠른 응답: " + fastestResult);
        System.out.println("응답 시간: " + (endTime - startTime) + "ms");
        // 예상: Server2 응답, 약 1초
    }
}
```

## 🧪 시험문제 5: 비동기 작업 체이닝

**문제**: 사용자 정보 조회 → 권한 확인 → 데이터 가져오기 순서로 비동기 작업을 체이닝하는 프로그램을 작성하시오.

```java
import java.util.concurrent.CompletableFuture;

public class Problem5_AsyncChaining {
    
    private static String getUserInfo(String userId) {
        ThreadUtil.safeSleep(500);
        return "User: " + userId;
    }
    
    private static String checkPermission(String userInfo) {
        ThreadUtil.safeSleep(300);
        return userInfo + " [권한: ADMIN]";
    }
    
    private static String fetchData(String userWithPermission) {
        ThreadUtil.safeSleep(700);
        return userWithPermission + " → 데이터: [중요한 정보들...]";
    }
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        CompletableFuture<String> result = CompletableFuture
            .supplyAsync(() -> getUserInfo("user123"))
            .thenApply(userInfo -> checkPermission(userInfo))
            .thenApply(userWithPermission -> fetchData(userWithPermission));
        
        String finalResult = result.join();
        long endTime = System.currentTimeMillis();
        
        System.out.println("최종 결과: " + finalResult);
        System.out.println("총 실행시간: " + (endTime - startTime) + "ms");
        // 예상: 약 1.5초 (500+300+700ms)
    }
}
```

## 💡 간단한 사용 예시 모음

### 기본 병렬 처리
```java
public static void example1_기본병렬처리() {
    Map<String, Callable<Integer>> tasks = Map.of(
        "작업1", () -> 100 + 200,
        "작업2", () -> 300 + 400
    );
    Map<String, Integer> results = ThreadUtil.executeParallel(tasks);
    System.out.println(results); // {작업1=300, 작업2=700}
}
```

### 리스트 변환
```java
public static void example2_리스트변환() {
    List<String> names = List.of("alice", "bob", "charlie");
    List<String> upperNames = ThreadUtil.parallelMap(names, String::toUpperCase);
    System.out.println(upperNames); // [ALICE, BOB, CHARLIE]
}
```

### 지연 실행
```java
public static void example3_지연실행() {
    CompletableFuture<String> delayed = ThreadUtil.executeAfter(
        () -> "3초 후 실행됨", 3, TimeUnit.SECONDS
    );
    System.out.println(delayed.join()); // 3초 후 출력
}
```

### 안전한 대기
```java
public static void example4_안전한대기() {
    System.out.println("작업 시작");
    ThreadUtil.safeSleep(1000); // 1초 대기
    System.out.println("작업 완료");
}
```

## 📊 성능 비교 요약

| 문제 | 순차 처리 시간 | 병렬 처리 시간 | 성능 향상 |
|------|-------------|-------------|-----------|
| 멀티 서버 | 3.5초 (1+2+0.5) | 2초 (max) | 1.75배 |
| 리스트 제곱 | 상당히 오래 | 빠름 | CPU 코어 수만큼 |
| 타임아웃 | 5초 대기 | 3초 차단 | 응답성 향상 |
| 빠른 응답 | 6초 (3+1+2) | 1초 (min) | 6배 |
| 작업 체이닝 | 1.5초 | 1.5초 | 순차적 특성 |

## 🎯 핵심 포인트

- **병렬 처리**: 독립적인 작업들을 동시 실행
- **타임아웃**: 무한 대기 방지
- **경쟁 실행**: 여러 옵션 중 가장 빠른 것 선택
- **체이닝**: 순차적 의존성이 있는 작업 처리
- **안전성**: 예외 처리와 리소스 관리