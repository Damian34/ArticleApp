package com.articleapp;

import com.model.Article;
import com.model.ArticleDao;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.util.List;

public class ArticleController {

    private Javalin app;
    private ArticleDao articleDao = new ArticleDao();

    public ArticleController(Javalin app) {
        this.app = app;
        app.get("/article", getArticles);
        app.get("/article/{id}", getArticle);
        app.get("/articles/{keyWord}", getArticlesByKeyWord);
        app.put("/add", addArticle);
        app.post("/modify", modifyArticle);
        app.delete("/delete/{id}", deleteArticle);
    }

    public Handler getArticles = ctx -> {
        List<Article> articles = articleDao.getAllArticles();
        ctx.json(convertListToJson(articles));
    };

    public Handler getArticle = ctx -> {
        String nonConvertedId = ctx.pathParam("id");
        try {
            int id = Integer.valueOf(nonConvertedId);
            Article article = articleDao.getArticleById(id);
            if (article == null) {
                ctx.status(404);
                ctx.html("Not Found!");
            } else {
                ctx.json(article.toString());
            }
        } catch (NumberFormatException e) {
            ctx.status(404);
            ctx.html("Not Found!");
        }
    };

    public Handler getArticlesByKeyWord = ctx -> {
        String keyWord = ctx.pathParam("keyWord");
        List<Article> articles = articleDao.getAllArticles(keyWord);
        ctx.json(convertListToJson(articles));
    };

    public Handler addArticle = ctx -> {
        String body = ctx.body();
        if(!articleDao.addArticle(body)){
            ctx.html("Incorrect Format!");
        }
    };

    public Handler modifyArticle = ctx -> {
        String body = ctx.body();
        if(!articleDao.modifyArticle(body)){
            ctx.html("Incorrect Format!");
        }
    };

    public Handler deleteArticle = ctx -> {
        String nonConvertedId = ctx.pathParam("id");
        try {
            int id = Integer.valueOf(nonConvertedId);
            boolean result = articleDao.deleteArticle(id);
            if (result) {
                ctx.html("Deleted!");
            } else {
                ctx.html("Not Found!");
            }
        } catch (NumberFormatException e) {
            ctx.status(404);
            ctx.html("Not Found!");
        }
    };

    private String convertListToJson(List<Article> articles) {
        String json = "{\"articles\": [";
        for (int i = 0; i < articles.size(); i++) {
            json += articles.get(i);
            json += i < articles.size() - 1 ? "," : "]}";
        }
        return json;
    }
}
