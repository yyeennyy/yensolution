# XmlUtil 사용 가이드

## 📖 기본 태그 추출

```java
// 파일에서 title 태그 추출
List<String> titles = XmlUtil.extractTagFromFile("sample.xml", "title");

// 문자열에서 name 태그 첫 번째 값만
String name = XmlUtil.extractFirstTag(xmlString, "name");
```

## 🏷️ 속성 처리

```java
// <user id="123" name="john">content</user>
Map<String, String> attrs = XmlUtil.extractAttributes(xml, "user");
String userId = XmlUtil.extractAttribute(xml, "user", "id");
```

## 🔍 고급 추출

```java
// 태그와 속성 동시 추출
Map<String, Object> result = XmlUtil.extractTagWithAttributes(xml, "product");

// 중첩 태그: <books><book>제목</book></books>
List<String> bookTitles = XmlUtil.extractNestedTag(xml, "books", "book");

// 자체 닫힘: <img src="test.jpg" />
List<Map<String, String>> images = XmlUtil.extractSelfClosingTags(xml, "img");
```

## 🛠️ 유틸리티

```java
// 태그 존재 확인
boolean hasTitle = XmlUtil.hasTag(xml, "title");

// 모든 태그 제거 (텍스트만)
String plainText = XmlUtil.removeAllTags(xml);

// CDATA 추출
List<String> cdataContents = XmlUtil.extractCDATA(xml);
```

## ✏️ XML 생성

```java
// 단순 태그
String tag = XmlUtil.createTag("title", "Hello World");

// 속성 포함
Map<String, String> attrs = Map.of("id", "123", "class", "main");
String tagWithAttrs = XmlUtil.createTagWithAttributes("div", "Content", attrs);

// 자체 닫힘 태그
String selfClosing = XmlUtil.createSelfClosingTag("img", 
    Map.of("src", "image.jpg", "alt", "설명"));
```

## 📋 실제 사용 예시

### XML 파일 파싱
```java
String xmlContent = """
    <users>
        <user id="1" name="Alice">
            <email>alice@test.com</email>
            <age>25</age>
        </user>
        <user id="2" name="Bob">
            <email>bob@test.com</email>
            <age>30</age>
        </user>
    </users>
    """;

// 모든 이메일 추출
List<String> emails = XmlUtil.extractTag(xmlContent, "email");
System.out.println(emails); // [alice@test.com, bob@test.com]

// 첫 번째 사용자 이름 속성
String firstName = XmlUtil.extractAttribute(xmlContent, "user", "name");
System.out.println(firstName); // Alice
```

### RSS 피드 파싱
```java
// RSS 제목들 추출
List<String> titles = XmlUtil.extractTagFromFile("rss.xml", "title");

// 링크 추출
List<String> links = XmlUtil.extractTagFromFile("rss.xml", "link");
```

### HTML에서 이미지 정보 추출
```java
String html = """
    <img src="photo1.jpg" alt="사진1" width="100" />
    <img src="photo2.jpg" alt="사진2" width="200" />
    """;

List<Map<String, String>> images = XmlUtil.extractSelfClosingTags(html, "img");
for (Map<String, String> img : images) {
    System.out.println("이미지: " + img.get("src") + ", 설명: " + img.get("alt"));
}
```