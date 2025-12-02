package com.example.nya_shopping.validation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginError {

    //IDチェック共通化
    public Integer validateId(String id, List<String> errors) {
        try {
            Integer result = Integer.parseInt(id);
            if (result <= 0) {
                errors.add("E0018");
                return null;
            }
            return result;
        } catch (NumberFormatException e) {
            errors.add("E0018");
            return null;
        }
    }
}
