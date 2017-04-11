package com.bonial.mushopl.util;

import javax.servlet.http.HttpServletResponse;

public final class HttpUtils {

    /**
     * Sets HTTP status and headers to response with redirection
     * @param response HTTP servlet response to update
     * @param location URL to redirect to
     */
    public static void redirect(final HttpServletResponse response, final String location) {
        response.setHeader("Location", location);
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
    }

    /**
     * Extracts first resource identificator from path
     * @param path resource path
     * @return first identificator
     */
    public static String extractPathFirstPart(String path) {
        path = path.replaceAll("/+", "/");
        final int slashIndex = path.indexOf('/');
        if(slashIndex == -1) {
            return path;
        } else {
            final String afterSlash = path.substring(slashIndex + 1);
            if(afterSlash.contains("/")) {
                return afterSlash.substring(0, afterSlash.indexOf('/'));
            } else {
                return afterSlash;
            }
        }
    }

}
