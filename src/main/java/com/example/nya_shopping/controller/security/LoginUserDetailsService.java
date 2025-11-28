package com.example.nya_shopping.controller.security;

import com.example.nya_shopping.repository.UserRepository;
import com.example.nya_shopping.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        return user
                .map(LoginUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("not found email : " + email));
    }

}
