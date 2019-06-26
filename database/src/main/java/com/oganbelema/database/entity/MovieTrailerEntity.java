package com.oganbelema.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "movie_trailer", foreignKeys = @ForeignKey(entity = FavoriteMovieEntity.class,
parentColumns = "id", childColumns = "movie_id", onDelete = CASCADE),
        indices = @Index(value = "movie_id"))
public class MovieTrailerEntity {

    @ColumnInfo(name = "movie_id")
    public int movieId;
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "iso_639_1")
    private String iso6391;
    @ColumnInfo(name = "iso_3166_1")
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public MovieTrailerEntity(int movieId, String id, String iso6391, String iso31661, String key,
                              String name, String site, int size, String type) {
        this.movieId = movieId;
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
