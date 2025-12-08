package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.UserEditForm;
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

    //メールアドレス重複確認メソッド
    public boolean isEmailDuplicateExcludingUser(String email, Integer userId){
        //既存のユーザーをメールアドレスで検索
        Optional<User> existUser= userRepository.findByEmail(email);
        //存在して、かつそのIDが更新対象のIDと異なれば、重複と判定
        //Optional取得➡ID取得➡userIdと一致するか確認
        return existUser.isPresent() && !existUser.get().getId().equals(userId);
    }


    public User updateUser(UserEditForm form){
        //既存のユーザー情報をIDで取得
        Optional<User> optionalUser = userRepository.findById(form.getUserId());
        User user = optionalUser.get();

        //Entityにフォームの値を詰める
        user.setEmail(form.getEmail());
        user.setName(form.getName());
        user.setPostalCode(form.getPostalCode());
        user.setAddress(form.getAddress());
        user.setPhone(form.getPhone());
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        //パスワードが入力されていれば更新（ハッシュ化してセット）
        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            String encodePassword = passwordEncoder.encode(form.getPassword());
            user.setPassword(encodePassword);
        }

        //データベースに更新を反映。
        userRepository.updateUser(user);

        return user;
    }

}
