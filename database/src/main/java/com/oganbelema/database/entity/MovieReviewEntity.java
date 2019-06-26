package com.oganbelema.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "movie_review", foreignKeys = @ForeignKey(entity = FavoriteMovieEntity.class,
parentColumns = "id", childColumns = "movie_id", onDelete = CASCADE),
        indices = @Index(value = "movie_id"))
public class MovieReviewEntity {

    @ColumnInfo(name = "movie_id")
    public int movieId;
    private String author;
    private String content;
    @PrimaryKey
    @NonNull
    private String id;
    private String url;

    public MovieReviewEntity(int movieId, String author, String content, String id, String url) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
