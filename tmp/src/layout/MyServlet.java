package layout;
import dto.*;
import dto.ClientReq;
import dto.ClientRes;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyServlet extends HttpServlet {
    private HttpClient httpClient;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // [요청 body 구성하기]
    	// 필요하다면 getPath(request)를 통해 uri 얻고 분기합시다
    	ClientReq clientReq = getReqBody(request, ClientReq.class);;
        // for (반복 요청이 필요하다면 for을 구성하세요!) {

    	    // 아까 구현한 메서드를 그대로 사용하게 될 것이다
    		// .. here ..
        
            // [uri 얻기]
        	// FileService fileService = new FileService();
            // String uri = fileService.readLines("EXAMPLE.TXT").get(0);
        	String uri = "";
        	
        	// [API Data 구성하기] 
        	String outData = ""; 

            try {
            	// [요청을 보내세요]
                OutRes outRes1 = GsonUtils.postToDto(uri, outData, OutRes.class);
                OutRes outRes2 = GsonUtils.getToDto(uri, OutRes.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        // }

        
        // [응답 구성하기]
        // 1. 간단한 경우 result Map 만들자
        // 2. 복잡하면 ClientRes 구성해서 쓰자
        Map<String, String> resultMap = new HashMap<>();
        ClientRes clientRes = new ClientRes();
        // write here
        // ...
        response.setContentType("application/json");
        response.getWriter().print(GsonUtils.toJson(clientRes));  // clientRes or resultMap
    }

    
    // 요청 받기 관련
    private String getPath(HttpServletRequest req) {
        return req.getRequestURI();  // e.g. /json, /api/json
    }
    
    private String getReqBody(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestData.append(line);
        }
        reader.close();
        return requestData.toString();
    }
    
    private <T> T getReqBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestData.append(line);
        }
        reader.close();
        String jsonData = requestData.toString();
        return GsonUtils.fromJson(jsonData, clazz);
    }

    
    // init & destroy
    @Override
    public void init() throws ServletException {
        try {
            httpClient = new HttpClient();
            httpClient.start();    
        } catch (Exception e) {
            throw new ServletException("HttpClient 시작 실패", e);
        }
    }
    
    @Override
    public void destroy() {
        try {
            if (httpClient != null) {
                httpClient.stop();        
            }
        } catch (Exception e) { }
    }
}