package com.itmoldova.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Author vgrec, on 09.07.16.
 */
@Entity
@Root(name = "item", strict = false)
public class Article implements Parcelable {

    public Article() {
    }

    @Element(name = "title", required = true)
    private String title;


    @Element(name = "link", required = true)
    private String link;

    @Element(name = "description", required = true)
    private String description;

    @PrimaryKey
    @Element(name = "guid", required = false)
    private String guid; // A string that uniquely identifies the item.

    @Element(name = "pubDate", required = false)
    private String pubDate;

    @Namespace(prefix = "content")
    @Element(name = "encoded", required = false)
    private String content;

    @Namespace(prefix = "creator")
    @Element(name = "creator", required = false)
    private String dc;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public String getContent() {
        return content;
    }

    public String getCreator() {
        return dc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.guid);
        dest.writeString(this.pubDate);
        dest.writeString(this.content);
        dest.writeString(this.dc);
    }

    protected Article(Parcel in) {
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        this.guid = in.readString();
        this.pubDate = in.readString();
        this.content = in.readString();
        this.dc = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Article article = (Article) obj;

        return guid != null ? guid.equals(article.guid) : article.guid == null;

    }

    @Override
    public int hashCode() {
        return guid != null ? guid.hashCode() : 0;
    }
}
