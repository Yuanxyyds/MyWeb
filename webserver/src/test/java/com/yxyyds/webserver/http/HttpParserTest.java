package com.yxyyds.webserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;
    @BeforeAll
    public void beforeClass(){
        httpParser = new HttpParser();
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assert request.getMethod().equals(HttpMethod.GET);
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequestBadMethod1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethod());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethod2());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequestInvNumItem1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseRequestLineInvNumItems());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequestEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequestOnlyCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseOnlyCRnoLF());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private InputStream generateBadTestCaseRequestLineInvNumItems(){
        String rawData = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:52309\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseOnlyCRnoLF(){
        String rawData = "GET / HTTP/1.1\r" +
                "Host: localhost:52309\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseEmptyRequestLine(){
        String rawData = "\r\n" +
                "Host: localhost:52309\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseMethod2(){
        String rawData = "GETTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:52309\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseMethod(){
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:52309\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateValidGETTestCase(){
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:52309\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Microsoft Edge\";v=\"100\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36 Edg/100.0.1185.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
}