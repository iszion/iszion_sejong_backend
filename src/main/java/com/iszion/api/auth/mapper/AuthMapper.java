package com.iszion.api.auth.mapper;

import com.iszion.api.auth.dto.CustomUserDetails;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AuthMapper {

    CustomUserDetails getUserId(String userId);

    int existsUserId(String userId);

    void insert(CustomUserDetails user);

    HashMap<String, Object> test();
}
