package com.howie.shirojwt.filter;

import com.howie.shirojwt.util.JWTUtil;
import com.howie.shirojwt.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 终端用户登录过滤
 */

@WebFilter(urlPatterns = "/app/*")
public class AppFilter implements Filter {

    private static final String WHITE = "/app/end_login";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        try {
            // 如果路径在白名单中则直接放行
            String path = request.getServletPath();
            if(path.equals(WHITE)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 如果token为空，过滤
            String token = request.getHeader("Authorization");

            if (StringUtils.isEmpty(token) || "null".equals(token)) {
                response.sendError(0, "token为空！");
                return ;
            }

            String name = JWTUtil.getUsername(token);
            System.out.println(name);
            // 如果token不在redis缓存中，过滤
            if(redisUtils.get(name) == null) {
                response.sendError(1, "无有效登陆信息");
                return ;
            }

            // 若token解析失败，过滤
            boolean id = JWTUtil.verify(token,name);
            if (!id) {
                response.sendError(2, "token解析失败！");
                return ;
            }

            // 如果鉴权成功，则更新token
            // 生成新的token
            String newToken = JWTUtil.createToken(name);
            // 删除旧的token
            if(!redisUtils.remove(name)) {
                response.sendError(3, "token更新失败！");
                return ;
            }
            // 加入新的token
            if(!redisUtils.set(name,newToken,10L,TimeUnit.MINUTES)) {
                response.sendError(4, "token更新失败！");
                return ;
            }
            response.addHeader("Authorization", newToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.info("[AuthFilter.doFilter] exception: {}", e);
        }


    }

    @Override
    public void destroy() {

    }
}
