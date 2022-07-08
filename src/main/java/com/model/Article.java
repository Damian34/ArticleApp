package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.util.GregorianCalendar;

public class Article {

    @JsonProperty(value = "id")
    private int id = -1;
    @JsonProperty(value = "contents")
    private String contents = null;
    @JsonProperty(value = "publicationDate")
    private String publicationDate = null;
    @JsonProperty(value = "name")
    private String name = null;
    @JsonProperty(value = "author")
    private String author = null;
    private String recordingDate;

    public Article() {
        super();
    }

    public Article(int id, String contents, String publication_date, String name, String author, String recording_date) {
        this.id = id;
        this.contents = contents;
        this.publicationDate = publication_date;
        this.name = name;
        this.author = author;
        this.recordingDate = recording_date;
    }

    public int getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getRecordingDate() {
        return recordingDate;
    }

    public Date getPublicationDateSQL() {
        String[] s = publicationDate.split("-");
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.set(Integer.valueOf(s[0]), Integer.valueOf(s[1]), Integer.valueOf(s[2]));
            return new Date(c.getTimeInMillis());
        } catch (NumberFormatException e) {
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\"id\": \"" + id + "\",\"contents\": \"" + contents + "\",\"publicationDate\": \"" + publicationDate + "\",\"name\": \"" + name + "\",\"author\": \"" + author + "\",\"recordingDate\": \"" + recordingDate + "\"}";
    }
}
