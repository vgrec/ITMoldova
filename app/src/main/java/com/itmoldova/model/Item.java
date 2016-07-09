package com.itmoldova.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Author vgrec, on 09.07.16.
 */
@Root(name = "item", strict = false)
public class Item {

    @Element(name = "title", required = true)
    private String title;


    @Element(name = "link", required = true)
    private String link;

    @Element(name = "description", required = true)
    private String description;


    @Element(name = "guid", required = false)
    private String guid; // A string that uniquely identifies the item.

    @Element(name = "pubDate", required = false)
    private String pubDate;

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

    public String getGuid() {
        return guid;
    }

}
