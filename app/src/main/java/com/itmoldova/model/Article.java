package com.itmoldova.model;

//import org.simpleframework.xml.Element;
//import org.simpleframework.xml.Root;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Author vgrec, on 09.07.16.
 */
@Root(name = "item", strict=false)
public class Article {

    @Element(name = "title")
    public String title;

    public String getTitle() {
        return title;
    }

    public Article(){}
}
