package com.drebo.blog.backend.services.utils;

public class ServiceUtils {

    private static final int WORDS_PER_MINUTE = 200;

    public static Integer calculateReadingTime(String content) {
        if(content == null || content.trim().isEmpty()) {
            return 0;
        }

        //trim leading/trailing space
        //split to array of substrings using any one or more space as delimiter
        String[] words = content.trim().split("\\s+");
        int wordCount = words.length;

        return (int)(Math.ceil((double) wordCount / WORDS_PER_MINUTE));
    }
}
