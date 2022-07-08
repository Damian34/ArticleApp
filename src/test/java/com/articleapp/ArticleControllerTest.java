package com.articleapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Article;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ArticleControllerTest {

    private ArticleDaoExtension ex = new ArticleDaoExtension();

    @Test
    public void getArticlesTest() {
        String urlString = "http://localhost:7070/article/";
        String json = ex.getResponseByUrl(urlString);
        assertEquals(json != null, true);
        json = json.replace("{\"articles\": ", "").replace("}]}", "}]");
        ObjectMapper mapper = new ObjectMapper();
        int size = -1;
        try {
            List<Article> list = mapper.readValue(json, new TypeReference<ArrayList<Article>>() {
            });
            size = list.size();
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        assertEquals(size > 0, true);
    }

    @Test
    public void getArticleById() {
        String urlString = "http://localhost:7070/article/1";
        String json = ex.getResponseByUrl(urlString);
        assertEquals(json != null, true);
    }

    @Test
    public void getArticleByIdFailed() {
        int maxId = ex.getMaxId() + 1;
        String urlString = "http://localhost:7070/article/" + maxId;
        int code = ex.getDeleteResponseCode(urlString, "GET");
        assertEquals(code, 404);
    }

    @Test
    public void getArticlesByKeyWord() {//it will work if there will be same records that i showed in README
        String urlString = "http://localhost:7070/articles/Lord";
        String json = ex.getResponseByUrl(urlString);
        assertEquals(json != null, true);
        json = json.replace("{\"articles\": ", "").replace("}]}", "}]");
        ObjectMapper mapper = new ObjectMapper();
        int size = -1;
        try {
            List<Article> list = mapper.readValue(json, new TypeReference<ArrayList<Article>>() {
            });
            size = list.size();
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        assertEquals(size > 0, true);
    }

    @Test
    public void addArticleTest() {
        String urlString = "http://localhost:7070/add";
        String jsonDataToSend = "{\"contents\" : \"Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard ...\",\"publicationDate\" : \"1997-06-26\",\"name\" : \"Harry Potter\",\"author\" : \"J.K. Rowling\"}";
        int maxId = ex.getMaxId();
        ex.putPostResponseByUrl(urlString, jsonDataToSend, "PUT");
        int maxId2 = ex.getMaxId();
        assertEquals(maxId != maxId2, true);
    }

    public void modifyArticleTest() {
        String urlAdd = "http://localhost:7070/add";
        String jsonDataToSend = "{\"contents\" : \"Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard ...\",\"publicationDate\" : \"1997-06-26\",\"name\" : \"Harry Potter\",\"author\" : \"J.K. Rowling\"}";
        ex.putPostResponseByUrl(urlAdd, jsonDataToSend, "PUT");

        int maxId = ex.getMaxId();
        String url = "http://localhost:7070/article/" + maxId;
        String json = ex.getResponseByUrl(url);
        Article art = ex.convertJson(json);

        String urlString = "http://localhost:7070/modify";
        String jsonDataToSend2 = "{\"id\" : " + maxId + ",\"name\" : \"Harry Potter3!\"}";
        ex.putPostResponseByUrl(urlString, jsonDataToSend2, "POST");

        String url2 = "http://localhost:7070/article/" + maxId;
        String json2 = ex.getResponseByUrl(url2);
        Article art2 = ex.convertJson(json2);

        assertEquals(art.getName() != art2.getName(), true);
    }

    @Test
    public void deleteArticleTest() {
        int maxId = ex.getMaxId();
        String urlString = "http://localhost:7070/delete/" + maxId;
        ex.getDeleteResponseCode(urlString, "DELETE");
        int maxId2 = ex.getMaxId();
        assertEquals(maxId > maxId2, true);
    }

}
