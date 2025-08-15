package layout;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class MyServer {

    public static void main(String[] args) throws Exception {
        Server proxyServer = createProxyServer();
        proxyServer.start();
        proxyServer.join();
    }

    private static Server createProxyServer() {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(MyServlet.class, "/");
        server.setHandler(context);
        return server;
    }
}