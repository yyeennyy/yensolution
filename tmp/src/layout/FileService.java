package layout;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;

public class FileService {
	
	// ============================================================= FILE -> Object
	// FILE -> List
    public static List<String> readLines(String stringPath) throws IOException {
        Path path = Paths.get(stringPath);
        BufferedReader br = Files.newBufferedReader(path);
        String line = null;

        List<String> lines = new ArrayList<>();
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }
    
    // CSV -> List<Map>
    public static List<Map<String, String>> readCsv(String stringPath) throws IOException {
        Path path = Paths.get(stringPath);
        BufferedReader br = Files.newBufferedReader(path);
        String line = null;
        
        List<Map<String, String>> csvData = new ArrayList<>();
        String[] headers = null;
        boolean isFirstLine = true;
        
        while((line = br.readLine()) != null) {
            String[] values = line.split(",");
            
            if (isFirstLine) {
                headers = values; // 첫 번째 줄을 헤더로 사용
                isFirstLine = false;
            } else {
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }
                csvData.add(row);
            }
        }
        br.close();
        return csvData;
    }

    
    
	// ============================================================= Object -> FILE
    // Map -> Properties
    public static void saveMapToProperties(Map<String, String> map, String filePath, String comment) throws IOException {
        Properties props = new Properties();
        props.putAll(map);
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            props.store(fos, comment);
        }
    }
    
    // Map -> JSON
    public static void saveMapToJson(Map<String, String> map, String filePath) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(map, writer);
        }
    }
}