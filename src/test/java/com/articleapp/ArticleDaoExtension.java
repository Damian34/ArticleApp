package com.articleapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Article;
import com.model.ConnectionManager;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ArticleDaoExtension {

    public int getMaxId() {
        try {
            Connection c = ConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement("select max(id) as max from article;");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                return result.getInt("max");
            }
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return -1;
    }

    public String getResponseByUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                while (scan.hasNext()) {
                    return scan.nextLine();
                }
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e2) {
            System.out.println(e2);
        }
        return null;
    }

    public int getDeleteResponseCode(String urlString, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.connect();
            return conn.getResponseCode();
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e2) {
            System.out.println(e2);
        }
        return -1;
    }

    public void putPostResponseByUrl(String urlString, String jsonData, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonData);
            out.close();
            conn.getInputStream();
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e2) {
            System.out.println(e2);
        }
    }

    public Article convertJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Article>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return null;
    }
}
