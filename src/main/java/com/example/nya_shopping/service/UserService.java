package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.UserNarrowForm;
import com.example.nya_shopping.controller.form.UserStatusForm;
import com.example.nya_shopping.repository.UserRepository;
import com.example.nya_shopping.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public Page<User> findAllUser(UserNarrowForm form, PageRequest pageRequest) {
        int offset = (int) pageRequest.getOffset();
        int limit = pageRequest.getPageSize();

        //開始時刻をtimestampに変換
        if (form.getStartDate() != null) {
            Timestamp startTs = Timestamp.valueOf(form.getStartDate().atStartOfDay());
            form.setStartTimeStamp(startTs);
        }
        //終了時刻をtimestampに変換
        if (form.getEndDate() != null) {
            Timestamp endTs = Timestamp.valueOf(form.getEndDate().atTime(23, 59, 59));
            form.setEndTimeStamp(endTs);
        }

        List<User> userList = userRepository.findAllUser(form, offset, limit);
        int total = userRepository.countUser(form, offset, limit);

        return new PageImpl<>(userList, pageRequest, total);
    }

   public void updateUserStatus(UserStatusForm form) {
       userRepository.updateUserStatus(form.getId(), form.getIsStopped());
    }
}
