package com.wang.my_community.mapper;

import com.wang.my_community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into community (name,account_id,token,gmt_create,gmt_modified,node_id,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{nodeId},#{avatarUrl})")
    void create(User user);

    @Select("select * from community where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from community where id=#{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from community where account_id=#{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update({"update community set name=#{name},avatar_url=#{avatarUrl},token=#{token},gmt_modified=#{gmtModified} where account_id=#{accountId}"})
    void Update(User user);
}
