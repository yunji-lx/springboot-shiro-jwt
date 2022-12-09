package com.howie.shirojwt.controller;

import com.howie.shirojwt.mapper.EndUserMapper;
import com.howie.shirojwt.model.ResultMap;
import com.howie.shirojwt.util.JWTUtil;
import com.howie.shirojwt.util.RedisUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 终端用户控制器
 */


@RestController
@RequestMapping("/app")
public class EndUserController {

    private final EndUserMapper endUserMapper;
    private final ResultMap resultMap;


    @Autowired
    private RedisUtils redisUtils;


    public EndUserController(EndUserMapper endUserMapper, ResultMap resultMap) {
        this.endUserMapper = endUserMapper;
        this.resultMap = resultMap;
    }

    @GetMapping("/geteuser")
//    @RequiresRoles("admin")
    public ResultMap getenduser(){
        List list = endUserMapper.getUser();
//        String s = (String) redisUtils.get("endll");
//        System.out.println(s);
//        String str = JWTUtil.getUsername(s);
//        System.out.println(str);
        return resultMap.code(200).success().message(list);
    }


    @PostMapping("/end_login")
    public ResultMap end_login(@RequestBody Map map){

        String username = (String) map.get("username");
        String password = (String) map.get("password");

        String realpassword = endUserMapper.getPassword(username);

        if (realpassword == null) {
            return resultMap.fail().code(401).message("用户名错误");
        } else if (!realpassword.equals(password)) {
            return resultMap.fail().code(401).message("密码错误");
        } else {
            String token = JWTUtil.createToken(username);
            redisUtils.set(username,token);
            return resultMap.success().code(200).message(token);
        }


    }







}
