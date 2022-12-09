package com.howie.shirojwt.mapper;

import com.howie.shirojwt.pojo.EndUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndUserMapper {

    /**
     * 获得存在的用户
     */
    List<EndUser> getUser();

    /**
     * 获取密码
     * @param username
     * @return
     */
    String getPassword(String username);

}
