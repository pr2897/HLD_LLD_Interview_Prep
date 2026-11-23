import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

enum HttpRequestMethod { GET, POST, PATCH, PUT, DELETE }

class HttpRequest {
    private String url;
    private HttpRequestMethod method;
    private Map<String,String> headers;
    private String body;
    private int timeout;

    public void serve() {
        try {   
            System.out.printf("serving [%s](%s|[%d]) {%s}: %s\n\n", method, url, timeout, headers, body);
            System.out.println();
        } catch (Exception e) {
        }
    }

    private HttpRequest(Builder b) {
        this.url = b.url;
        this.method = b.method;
        this.headers = Map.copyOf(b.headers);
        this.body = b.body;
        this.timeout = b.timeout;
    }

    public static class Builder {
        private String url;
        private HttpRequestMethod method;
        private Map<String,String> headers;
        private String body;
        private int timeout;

        public Builder() {
            this.headers = new HashMap<>();
            this.timeout = 5;
        }

        public Builder url(String url) {this.url = url; return this; }
        public Builder method(HttpRequestMethod method) {this.method = method; return this; }
        public Builder header(String k, String v) {headers.put(k, v); return this; }
        public Builder body(String content) {this.body = content; return this; }
        public Builder timeout(int duration) {this.timeout = duration; return this; }

        public HttpRequest build() {
            if (Objects.isNull(url) || url.isBlank()) throw new IllegalArgumentException("missing url");
            if(method == HttpRequestMethod.GET && Objects.nonNull(body)) throw new IllegalArgumentException("GET method does support body");
            if(timeout <= 0) throw new IllegalArgumentException("timeout must be >0.");

            return new HttpRequest(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}

class HttpClient {
    
    private final BlockingQueue<String> connectionPool = new ArrayBlockingQueue<>(5);
    
    private HttpClient() {
        for(int i=1;i<=5;i++) {
            connectionPool.add("Conn-" + i);
        }
    }

    public void send(HttpRequest request) {
        String conn = null;
        try {
            conn = connectionPool.take(); // borrows 1 of 5. (wait if all 5 are busy)
            System.out.printf("[%s] Borrowed %s%n", Thread.currentThread().getName(), conn);
            request.serve();
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            if(conn != null) {
                System.out.printf("[%s] returning %s%n", Thread.currentThread().getName(), conn);
                connectionPool.offer(conn);
            }
        }
    }

    public static HttpClient getInstance() {
        return HOLDER.INSTANCE;
    }

    private static class HOLDER {
        private static HttpClient INSTANCE = new HttpClient();
    }

}

public class Code_M2 {
    public static void main(String[] args) throws InterruptedException {
        HttpClient client = HttpClient.getInstance();
        HttpRequest request = HttpRequest.newBuilder().url("test.com")
        .method(HttpRequestMethod.GET).build();
        

        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<20;i++) {
            threads.add(new Thread(() -> client.send(request)));
        }
        
        for(Thread t: threads) t.start();
        for(Thread t: threads) t.join(); 

    }

}


/*
    - JDK's HttpClient not singleton.
    - multiple client with isolated configurations (different ssl, proxies, timeouts, etc).
 */