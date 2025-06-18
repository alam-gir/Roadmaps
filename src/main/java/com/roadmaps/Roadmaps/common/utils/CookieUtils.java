package com.roadmaps.Roadmaps.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static Optional<String> getCookie(HttpServletRequest request, String name) {
        if(request.getCookies() != null && request.getCookies().length > 0){
            Cookie expectedCookie =  Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(name))
                    .toList()
                    .getFirst();
            if(expectedCookie != null){
                return Optional.of(expectedCookie.getValue());
            } else  {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(name))
                    .forEach(cookie -> {
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    });
        }
    }
}
