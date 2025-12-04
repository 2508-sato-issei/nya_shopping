package com.example.nya_shopping.dto;

import com.example.nya_shopping.repository.entity.User;
import com.example.nya_shopping.repository.entity.Order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyPageDto {

    //会員情報（表示用）
    //ログインユーザーの User エンティティをそのまま表示データとして利用
    private User user;

    //注文履歴のリスト
    private List<OrderHistoryItemDto> orderHistoryList;

    //絞り込み条件の選択肢
    private List<Integer> orderYears;

}