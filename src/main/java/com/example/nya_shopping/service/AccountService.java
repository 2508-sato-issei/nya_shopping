package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.UserRegisterForm;
import com.example.nya_shopping.repository.UserRepository;
import com.example.nya_shopping.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean isEmailDuplicate(String email){
        Optional<User> existUser= userRepository.findByEmail(email);
        return existUser.isPresent();
    }

    public void registerNewUser(UserRegisterForm form){

        User user = new User();
        //パスワードをハッシュ化する
        String encodePassword = passwordEncoder.encode(form.getPassword());

        //Entityに詰めなおす
        user.setEmail(form.getEmail());
        user.setPassword(encodePassword);
        user.setName(form.getName());
        user.setPostalCode(form.getPostalCode());
        user.setAddress(form.getAddress());
        user.setPhone(form.getPhone());
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setRole("USER");
        user.setIsStopped(false);

        userRepository.save(user);
    }

}
