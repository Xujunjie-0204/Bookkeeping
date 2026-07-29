package com.example.bookkeeping.system.user.mapper;

import com.example.bookkeeping.system.user.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface SysUserMapper {

    SysUser selectById(@Param("id") Long id);

    SysUser selectByUsername(@Param("username") String username);

    int updateLastLoginAt(@Param("id") Long id, @Param("lastLoginAt") LocalDateTime lastLoginAt);

    int updateProfile(@Param("id") Long id,
                      @Param("nickname") String nickname,
                      @Param("phone") String phone);

    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
