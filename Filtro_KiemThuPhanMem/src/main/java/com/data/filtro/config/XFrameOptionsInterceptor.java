package com.data.filtro.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class XFrameOptionsInterceptor implements HandlerInterceptor {
    private static final String STRICT_TRANSPORT_SECURITY_HEADER_NAME = "Strict-Transport-Security";
    private static final String STRICT_TRANSPORT_SECURITY_HEADER_VALUE = "max-age=31536000; includeSubDomains";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setHttpOnlyCookie(response, "ajs_user_id", getCookieValue(request,"ajs_user_id" ), 3600);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(STRICT_TRANSPORT_SECURITY_HEADER_NAME, STRICT_TRANSPORT_SECURITY_HEADER_VALUE);
        if (!isCookieHttpOnly(request, "ajs_user_id")){
//            System.out.println("ajs_user_id" + " la httponly");
        }
        else{
            System.out.println("ajs_user_id" + " khong phai la httponly");
        }
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("Content-Security-Policy", "script-src 'self' https://stackpath.bootstrapcdn.com " +
                "https://code.jquery.com " +
                "https://cdn.jsdelivr.net " +
                "https://cdnjs.cloudflare.com 'unsafe-inline' 'unsafe-eval'; ");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4040/*");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public void setHttpOnlyCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        Cookie oldCookie = new Cookie(cookieName, cookieValue);
        Cookie newCookie = new Cookie(cookieName, cookieValue);
        oldCookie.setMaxAge(0);
        oldCookie.setPath("/");
        // dòng này có nghĩa việc ghi đè old cookie có thoi gian song = 0 sẽ áp dụng cho mọi đường dẫn
        response.addCookie(oldCookie);

        newCookie.setMaxAge(maxAge);
        newCookie.setHttpOnly(true);
        response.addCookie(newCookie);
    }
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public boolean isCookieHttpOnly(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.isHttpOnly();
                }
            }
        }
        return false; // Không tìm thấy cookie
    }
}
