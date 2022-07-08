package com.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            Connection c = ConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from article order by publication_date desc;");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                articles.add(toArticle(result));
            }
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return articles;
    }

    public Article getArticleById(int id) {
        try {
            Connection c = ConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from article where id=" + id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                return toArticle(result);
            }
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return null;
    }

    public List<Article> getAllArticles(String keyWord) {
        List<Article> articles = new ArrayList<>();
        try {
            Connection c = ConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from article where LOWER(contents) like LOWER('%" + keyWord + "%') order by publication_date desc;");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                articles.add(toArticle(result));
            }
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return articles;
    }

    public boolean addArticle(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Article articleBody = mapper.readValue(requestBody, new TypeReference<Article>(){});
            Connection c = ConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO article(contents,publication_date,name,author) VALUES(?,?,?,?);");

            Date date = articleBody.getPublicationDateSQL();
            if (date == null || articleBody.getContents() == null || articleBody.getName() == null || articleBody.getAuthor() == null) {
                return false;
            }
            ps.setString(1, articleBody.getContents());
            ps.setDate(2, date);
            ps.setString(3, articleBody.getName());
            ps.setString(4, articleBody.getAuthor());
            ps.executeUpdate();
            return true;
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException-error: " + e);
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return false;
    }

    public boolean modifyArticle(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Article articleBody = mapper.readValue(requestBody, new TypeReference<Article>() {
            });
            Connection c = ConnectionManager.getConnection();
            if (articleBody.getId() < 0) {
                return false;
            }
            if (articleBody.getContents() == null && articleBody.getPublicationDate() == null && articleBody.getName() == null && articleBody.getAuthor() == null) {
                return true;
            }
            List<String> fields = new ArrayList<>();
            if (articleBody.getContents() != null) {
                fields.add("contents");
            }
            if (articleBody.getPublicationDate() != null) {
                fields.add("publication_date");
            }
            if (articleBody.getName() != null) {
                fields.add("name");
            }
            if (articleBody.getAuthor() != null) {
                fields.add("author");
            }
            String query = "update article set ";
            for (int i = 0; i < fields.size(); i++) {
                query += fields.get(i) + "=?";
                query += i < fields.size() - 1 ? ", " : "";
            }
            query += ", recording_date=? where id= ?;";
            PreparedStatement ps = c.prepareStatement(query);
            for (int i = 0; i < fields.size(); i++) {
                switch (fields.get(i)) {
                    case "contents":
                        ps.setString(i + 1, articleBody.getContents());
                        break;
                    case "publication_date":
                        ps.setDate(i + 1, articleBody.getPublicationDateSQL());
                        break;
                    case "name":
                        ps.setString(i + 1, articleBody.getName());
                        break;
                    case "author":
                        ps.setString(i + 1, articleBody.getAuthor());
                        break;
                }
            }
            ps.setTimestamp(fields.size() + 1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(fields.size() + 2, articleBody.getId());
            ps.executeUpdate();
            return true;
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException-error: " + e);
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return false;
    }

    public boolean deleteArticle(int id) {
        try {
            Connection c = ConnectionManager.getConnection();
            String query = "delete from article where id= ? ;";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
        }
        return false;
    }

    private Article toArticle(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        String contents = result.getString("contents");
        String publication_date = result.getString("publication_date");
        String name = result.getString("name");
        String author = result.getString("author");
        String recording_date = result.getString("recording_date");
        return new Article(id, contents, publication_date, name, author, recording_date);
    }

}
