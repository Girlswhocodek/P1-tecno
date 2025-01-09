/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*package coneccion.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    private final static String GMAIL = "d=gmail";
    private final static String TECNOWEB = "d=tecnoweb";
    private final static String HOTMAIL = "d=hotmail";
    private final static String YAHOO = "d=yahoo";
    private final static String UAGRM = "d=uagrm";

    public static Email getEmail(String plain_text) {
        return new Email(getFrom(plain_text), getSubject(plain_text));
    }

    private static String getFrom(String plain_text) {
        return extractField(plain_text, "Return-Path: <", ">");
    }

    private static String getTo(String plain_text) {
        return extractField(plain_text, "To: ", "[\\s<]");
    }

    private static String getSubject(String plain_text) {
        return extractField(plain_text, "Subject: ", "(\\r\\n|\\n)");
    }

    private static String extractField(String text, String startPattern, String endPattern) {
        String patternString = Pattern.quote(startPattern) + "(.*?)" + endPattern;
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}*/


package coneccion.utils;


public class Extractor {
    private final static String GMAIL = "d=gmail";
    private final static String TECNOWEB = "d=tecnoweb";
    private final static String HOTMAIL = "d=hotmail";
    private final static String YAHOO = "d=yahoo";
    private final static String UAGRM = "d=uagrm";
    
    
    public static Email getEmail(String plain_text){
        return new Email(getFrom(plain_text),getSubject(plain_text));
    }
    
    private static String getFrom(String plain_text){
        String search = "Return-Path: <";
        int index_begin = plain_text.indexOf(search) + search.length();
        int index_end = plain_text.indexOf(">");
        return plain_text.substring(index_begin, index_end);
    }
    
    private static String getTo(String plain_text){
        String to = "";
        if(plain_text.contains(GMAIL)){
            to = getToFromGmail(plain_text);
        } else if(plain_text.contains(HOTMAIL)){
            to = getToFromHotmail(plain_text);
        } else if(plain_text.contains(YAHOO)){
            to = getToFromYahoo(plain_text);
        } else if(plain_text.contains(UAGRM)){
            to = getToFromUagrm(plain_text);
        } else if(plain_text.contains(TECNOWEB)){
            to = getToFromTecnoweb(plain_text);
        }
        return to;
    }
    
    private static String getSubject(String plain_text){
        String search = "Subject: ";
        int i = plain_text.indexOf(search) + search.length();
        String end_string = "";
        if(plain_text.contains(GMAIL)){
            end_string = "To:";
        } else if(plain_text.contains(HOTMAIL)){
            end_string = "Thread-Topic";
        } else if(plain_text.contains(YAHOO)){
            end_string = "MIME-Version:";
        } else if(plain_text.contains(UAGRM)){
            end_string = "To:";
        } else if(plain_text.contains(TECNOWEB)){
            end_string = "To:";
        }
        int e = plain_text.indexOf(end_string, i);
        return plain_text.substring(i, e);
    }

    private static String getToFromUagrm(String plain_text){
        return getToCommon(plain_text);
    }
    
    private static String getToFromTecnoweb(String plain_text){
        return getToCommon(plain_text);
    }
    private static String getToFromGmail(String plain_text){
        return getToCommon(plain_text);
    }
    
    private static String getToFromHotmail(String plain_text){
        String aux = getToCommon(plain_text);
        return aux.substring(1, aux.length() - 1);
    }
    
    private static String getToFromYahoo(String plain_text){
        int index = plain_text.indexOf("To: ");
        int i = plain_text.indexOf("<", index);
        int e = plain_text.indexOf(">", i);
        return plain_text.substring(i + 1, e);
    }
    
    private static String getToCommon(String plain_text){
        String aux = "To: ";
        int index_begin = plain_text.indexOf(aux) + aux.length();
        int index_end = plain_text.indexOf("\n", index_begin);
        return plain_text.substring(index_begin, index_end);
    }
}

