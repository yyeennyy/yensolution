package layout;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;



public class GsonUtils {
	
	private static final Gson gson = new Gson();
	
	
	// ================================================================================= MAKE object
	
	// 1. string -> Map
	public static Map<String, String> fromJson(String json) {
	    return gson.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
	}
	 
	// 2. string -> DTO
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    // 3. file -> Map
 	public static Map<String, String> fromJson(Reader reader) {
 	    return gson.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
 	}
    
    // 4. file -> DTO
    public static <T> T fromJson(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }
    
    
	// ================================================================================= MAKE json-str

    // 1. object -> string
    public static String toJson(Object obj) {
    	return gson.toJson(obj);
    }
    
    // 2. file -> string
	public static String toJson(String file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file)));
	}
    
	// ================================================================================= API REQUEST

    // [API] post
    public static <T> T postToDto(String url, String jsonBody, Class<T> clazz) throws Exception {
	   String jsonResponse = postToJson(url, jsonBody);
	   return gson.fromJson(jsonResponse, clazz);
	}
    public static String postToJson(String url, String jsonBody) throws Exception {
        HttpClient client = new HttpClient();
        try {
            client.start();
            
            ContentResponse response = client.newRequest(url)
                .method(HttpMethod.POST)
                .header("Content-Type", "application/json")
                .content(new StringContentProvider(jsonBody), "application/json")
                .send();
            
            return response.getContentAsString();
        } finally {
            client.stop();
        }
    }
    public static String fromInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        // 마지막 개행문자 제거
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
    
    // [API] get
    public static <T> T getToDto(String url, Class<T> clazz) throws IOException {
	   java.net.HttpURLConnection conn = null;
	   try {
	       java.net.URL urlObj = new java.net.URL(url);
	       conn = (java.net.HttpURLConnection) urlObj.openConnection();
	       
	       conn.setRequestMethod("GET");
	       conn.setRequestProperty("Accept", "application/json");
	       
	       try (InputStream is = conn.getInputStream()) {
	           String jsonResponse = fromInputStream(is);
	           return gson.fromJson(jsonResponse, clazz);
	       }
	   } finally {
	       if (conn != null) {
	           conn.disconnect();
	       }
	   }
	}
}