package com.education.project.utils;

public class HtmlUtils {

    public static String removeTagsTd(String contentHtml){
        if(contentHtml == null || contentHtml.isEmpty()){
            return "";
        }
        int start = contentHtml.indexOf("<div>");
        int end = contentHtml.lastIndexOf("</div>");
        return contentHtml.substring(start, end + "</div>".length());
    }// removeTagsTd()

}// HtmlUtils
