package util.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtil {
    
    // ======================= 단순 태그 추출 =======================
    
    /**
     * 파일에서 특정 태그의 값 추출
     */
    public static List<String> extractTagFromFile(String filePath, String tagName) throws IOException {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.DOTALL);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    results.add(matcher.group(1).trim());
                }
            }
        }
        return results;
    }
    
    /**
     * 문자열에서 특정 태그의 값 추출
     */
    public static List<String> extractTag(String xmlContent, String tagName) {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xmlContent);
        
        while (matcher.find()) {
            results.add(matcher.group(1).trim());
        }
        return results;
    }
    
    /**
     * 특정 태그의 첫 번째 값만 추출
     */
    public static String extractFirstTag(String xmlContent, String tagName) {
        List<String> results = extractTag(xmlContent, tagName);
        return results.isEmpty() ? null : results.get(0);
    }
    
    // ======================= 속성 추출 =======================
    
    /**
     * 태그의 속성값 추출
     * 예: <user id="123" name="john">content</user> → {id=123, name=john}
     */
    public static Map<String, String> extractAttributes(String xmlContent, String tagName) {
        Map<String, String> attributes = new HashMap<>();
        Pattern tagPattern = Pattern.compile("<" + tagName + "\\s+([^>]+)>");
        Matcher tagMatcher = tagPattern.matcher(xmlContent);
        
        if (tagMatcher.find()) {
            String attributeString = tagMatcher.group(1);
            Pattern attrPattern = Pattern.compile("(\\w+)=\"([^\"]+)\"");
            Matcher attrMatcher = attrPattern.matcher(attributeString);
            
            while (attrMatcher.find()) {
                attributes.put(attrMatcher.group(1), attrMatcher.group(2));
            }
        }
        return attributes;
    }
    
    /**
     * 특정 속성값만 추출
     */
    public static String extractAttribute(String xmlContent, String tagName, String attributeName) {
        Map<String, String> attributes = extractAttributes(xmlContent, tagName);
        return attributes.get(attributeName);
    }
    
    // ======================= 태그와 내용 동시 추출 =======================
    
    /**
     * 태그의 속성과 내용을 함께 추출
     */
    public static Map<String, Object> extractTagWithAttributes(String xmlContent, String tagName) {
        Map<String, Object> result = new HashMap<>();
        Pattern pattern = Pattern.compile("<" + tagName + "\\s*([^>]*)>(.*?)</" + tagName + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xmlContent);
        
        if (matcher.find()) {
            String attributeString = matcher.group(1);
            String content = matcher.group(2).trim();
            
            result.put("content", content);
            
            // 속성 파싱
            if (!attributeString.trim().isEmpty()) {
                Pattern attrPattern = Pattern.compile("(\\w+)=\"([^\"]+)\"");
                Matcher attrMatcher = attrPattern.matcher(attributeString);
                Map<String, String> attributes = new HashMap<>();
                
                while (attrMatcher.find()) {
                    attributes.put(attrMatcher.group(1), attrMatcher.group(2));
                }
                result.put("attributes", attributes);
            }
        }
        return result;
    }
    
    // ======================= 다중 태그 처리 =======================
    
    /**
     * 여러 태그의 값을 한 번에 추출
     */
    public static Map<String, List<String>> extractMultipleTags(String xmlContent, List<String> tagNames) {
        Map<String, List<String>> results = new HashMap<>();
        
        for (String tagName : tagNames) {
            results.put(tagName, extractTag(xmlContent, tagName));
        }
        return results;
    }
    
    /**
     * 중첩된 태그 구조 처리
     * 예: <parent><child>값</child></parent>
     */
    public static List<String> extractNestedTag(String xmlContent, String parentTag, String childTag) {
        List<String> results = new ArrayList<>();
        List<String> parentContents = extractTag(xmlContent, parentTag);
        
        for (String parentContent : parentContents) {
            List<String> childContents = extractTag(parentContent, childTag);
            results.addAll(childContents);
        }
        return results;
    }
    
    // ======================= XML 검증 및 유틸리티 =======================
    
    /**
     * 특정 태그가 존재하는지 확인
     */
    public static boolean hasTag(String xmlContent, String tagName) {
        Pattern pattern = Pattern.compile("<" + tagName + "(?:\\s[^>]*)?>.*?</" + tagName + ">", Pattern.DOTALL);
        return pattern.matcher(xmlContent).find();
    }
    
    /**
     * 자체 닫힘 태그 추출
     * 예: <img src="test.jpg" />
     */
    public static List<Map<String, String>> extractSelfClosingTags(String xmlContent, String tagName) {
        List<Map<String, String>> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("<" + tagName + "\\s+([^/>]+)\\s*/>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xmlContent);
        
        while (matcher.find()) {
            String attributeString = matcher.group(1);
            Pattern attrPattern = Pattern.compile("(\\w+)=\"([^\"]+)\"");
            Matcher attrMatcher = attrPattern.matcher(attributeString);
            Map<String, String> attributes = new HashMap<>();
            
            while (attrMatcher.find()) {
                attributes.put(attrMatcher.group(1), attrMatcher.group(2));
            }
            results.add(attributes);
        }
        return results;
    }
    
    /**
     * XML 태그 제거 (내용만 추출)
     */
    public static String removeAllTags(String xmlContent) {
        return xmlContent.replaceAll("<[^>]+>", "").trim();
    }
    
    /**
     * 특정 태그만 제거
     */
    public static String removeSpecificTags(String xmlContent, String tagName) {
        String pattern = "<" + tagName + "(?:\\s[^>]*)?>(.*?)</" + tagName + ">";
        return xmlContent.replaceAll(pattern, "$1");
    }
    
    // ======================= CDATA 처리 =======================
    
    /**
     * CDATA 섹션 내용 추출
     */
    public static List<String> extractCDATA(String xmlContent) {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("<!\\[CDATA\\[(.*?)\\]\\]>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xmlContent);
        
        while (matcher.find()) {
            results.add(matcher.group(1));
        }
        return results;
    }
    
    // ======================= 간단한 XML 생성 =======================
    
    /**
     * 간단한 XML 태그 생성
     */
    public static String createTag(String tagName, String content) {
        return "<" + tagName + ">" + content + "</" + tagName + ">";
    }
    
    /**
     * 속성이 있는 XML 태그 생성
     */
    public static String createTagWithAttributes(String tagName, String content, Map<String, String> attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            sb.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
        }
        
        sb.append(">").append(content).append("</").append(tagName).append(">");
        return sb.toString();
    }
    
    /**
     * 자체 닫힘 태그 생성
     */
    public static String createSelfClosingTag(String tagName, Map<String, String> attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            sb.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
        }
        
        sb.append(" />");
        return sb.toString();
    }
}