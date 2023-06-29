package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;


public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static String url = "https://api.nasa.gov/planetary/apod?api_key=GmIlpwdIC7xwjfaxiTNy5A9euQyVrVTr3l0uv98H";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        JsonNASA jsonNASA = mapper.readValue(response.getEntity().getContent(), JsonNASA.class);
        String urlContent = jsonNASA.hdurl();
        String[] splitUrl = urlContent.split("/");
        String filename = splitUrl[splitUrl.length - 1];
        request = new HttpGet(urlContent);
        response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        FileOutputStream fos = new FileOutputStream(filename);
        int inByte;
        while ((inByte = is.read()) != -1)
            fos.write(inByte);
        is.close();
        fos.close();
    }
}