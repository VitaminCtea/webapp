package com.tomcat.jspTags;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Properties;

public class MessageTag extends TagSupport {
    private String key;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    @Override public int doEndTag() {
        try {
            Properties ps = (Properties) pageContext.getAttribute("ps", PageContext.APPLICATION_SCOPE);
            Properties ps_ch = (Properties) pageContext.getAttribute("ps_ch", PageContext.APPLICATION_SCOPE);
            HttpSession session = pageContext.getSession();
            String language = (String) session.getAttribute("language");
            String message;
            if (language != null && language.equals("Chinese")) {
                message = ps_ch.getProperty(key);
            } else message = ps.getProperty(key);
            pageContext.getOut().print(message);
        } catch (IOException e) { throw new RuntimeException(e); }

        return EVAL_PAGE;
    }
}
