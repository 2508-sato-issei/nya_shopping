package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.UserNarrowForm;
import com.example.nya_shopping.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<User> findByEmail(@Param("email") String email);

    void save(User user);
    
    List<User> findAllUser(@Param("form")UserNarrowForm form,
                            @Param("offset") int offset,
                            @Param("limit") int limit);

    int countUser(@Param("form")UserNarrowForm form,
                  @Param("offset") int offset,
                  @Param("limit") int limit);
    void updateUserStatus(@Param("id") Integer id,
                          @Param("isStopped") boolean isStopped);
}
