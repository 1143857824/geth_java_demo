package com.wanliu.admin.interceptor;

import com.wanliu.admin.config.JwtProperties;
import com.wanliu.admin.entity.UserInfo;
import com.wanliu.admin.utils.jwt.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MyInterceptor implements HandlerInterceptor {


    JwtProperties jwtProperties;

    //这里用构造注入
    //@Autowired在拦截器里没有用
    public MyInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登陆用户
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (jwtProperties.getCookieName().equals(cookies[i].getName())) {
                    token = cookies[i].getValue();
                    break;
                }
            }
        }
        System.out.println(new Date() + " token:******************* " + token);
        try {
            //从token中解析token信息

            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            System.out.println(new Date() + " login_user:---------------- " + user);
            if (user.getUsername() == null || user.getId() == null) {
                //说明没有登陆过
                if (isHtml(request)) {
                    response.sendRedirect("extra-login.html");
                }
                //返回没有权限信息信息
                returnJson(response, "未登陆");
                return false;
            }

            //刷新时间
            token = JwtUtils.generateToken(user, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire() * 60);
            Cookie cookie = new Cookie(jwtProperties.getCookieName(), token);
//            cookie.setMaxAge(jwtProperties.getExpire()*60);
            response.addCookie(cookie);

            String[] split = user.getUsername().split(",&,");
            String s = split.length >= 2 ? split[split.length - 1] : "0";
            if(isSys(request) &&  !"1".equals(s)){
                //后台管理没有权限不能访问
                //返回没有权限信息信息
                returnJson(response, "不是管理员");
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("此错误是未登录报错，无需注意");
            e.printStackTrace();
        }
        if (token == null || "".equals(token)) {
            //说明没有登陆过
            if (isHtml(request)) {
                response.sendRedirect("extra-login.html");
            }
            //返回没有权限信息信息
            returnJson(response, "未登陆");
            return false;
        }

        //返回没有权限信息信息
        returnJson(response, "未登陆");
        //返回false时则postHandle不执行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 如果判断是否是后台页面
     *
     * @param request
     * @return
     */
    public boolean isHtml(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.toString().endsWith(".html") || requestURL.toString().endsWith("/");
    }

    /**
     * 如果判断是否是管理接口
     *
     * @param request
     * @return
     */
    public boolean isSys(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String url = requestURL.toString();
        String substring = url.substring(url.lastIndexOf("/") + 1);
        return substring.startsWith("sys");
    }

    /**
     * 返回json数据
     * @param response
     */
    private void returnJson(HttpServletResponse response,String str){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print("{'status' : '400', 'message' : " + str + "}");
        } catch (IOException e){
            System.out.println(e);
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
