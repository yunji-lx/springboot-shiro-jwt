package com.howie.shirojwt.controller;

import com.howie.shirojwt.mapper.UserMapper;
import com.howie.shirojwt.model.ResultMap;
import com.howie.shirojwt.util.JWTUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description
 * @Date 2018-04-29
 * @Time 13:20
 */
@RestController
public class LoginController {
    private final UserMapper userMapper;
    private final ResultMap resultMap;

    @Autowired
    public LoginController(UserMapper userMapper, ResultMap resultMap) {
        this.userMapper = userMapper;
        this.resultMap = resultMap;
    }

    @PostMapping("/login")
    public ResultMap login(@RequestBody Map map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String realPassword = userMapper.getPassword(username);
        System.out.println("realPassword" + realPassword);
        Map map1 = new HashMap();
        if (realPassword == null) {
            return resultMap.fail().code(401).message("用户名错误");
        } else if (!realPassword.equals(password)) {
            return resultMap.fail().code(401).message("密码错误");
        } else {
            map1.put("token",JWTUtil.createToken(username));
            return resultMap.success().code(200).data(map1).message("ok");
        }
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }


    @GetMapping("/getInfo")
    public ResultMap getInfo(@RequestParam("token") String token){

        Map map1 = new HashMap();
        List roles = new ArrayList();

        String name = JWTUtil.getUsername(token);
        String role = userMapper.getRole(name);

        roles.add(role);

        map1.put("name",name);
        map1.put("roles",roles);

        return resultMap.success().code(200).data(map1).message("ok");


    }

}
