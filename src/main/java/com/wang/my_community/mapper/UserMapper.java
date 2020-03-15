package com.wang.my_community.mapper;

import com.wang.my_community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into community (name,account_id,token,gmt_create,gmt_modified,node_id) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{nodeId})")
    void create(User user);

    @Select("select * from community where token=#{token}")
    User findByToken(@Param("token") String token);
}
