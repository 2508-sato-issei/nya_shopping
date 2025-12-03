package com.example.nya_shopping.service;

import com.example.nya_shopping.repository.ProductBulkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductBulkService {

    private final ProductBulkRepository productBulkRepository;

    public List<String> processCsv(MultipartFile file) {

        List<String> errors = new ArrayList<>();

        // ===============================
        // 1. ファイルチェック
        // ===============================
        if (file == null || file.isEmpty()) {
            errors.add("E0020: CSVファイルが選択されていません。");
            return errors;
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            errors.add("E0021: CSVファイルの拡張子が不正です。");
            return errors;
        }

        // ===============================
        // 2. CSVパース
        // ===============================
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1)); // 空文字も保持
            }

        } catch (IOException e) {
            errors.add("CSV読み込み中にエラーが発生しました。");
            return errors;
        }

        // ===============================
        // 3. ヘッダー項目チェック
        // ===============================
        String[] header = {"id", "name", "price", "category", "stock", "image_url", "description", "is_active"};

        if (rows.isEmpty() || rows.get(0).length != header.length) {
            errors.add("E0022: ヘッダーが正しくありません。");
            return errors;
        }

        for (int i = 0; i < header.length; i++) {
            if (!rows.get(0)[i].equals(header[i])) {
                errors.add("E0022: ヘッダー項目が一致しません。");
                return errors;
            }
        }

        // ===============================
        // 4. データ行パース ＋ 入力チェック
        // ===============================
        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            if (cols.length != header.length) {
                errors.add("E0023: " + i + "行目のカラム数が不正です。");
                continue;
            }

            try {
                // 変換
                Long id = cols[0].isEmpty() ? null : Long.valueOf(cols[0]);
                String name = cols[1];
                Integer price = Integer.valueOf(cols[2]);
                String category = cols[3];
                Integer stock = Integer.valueOf(cols[4]);
                String imageUrl = cols[5];
                String description = cols[6];
                Boolean isActive = Boolean.valueOf(cols[7]);

                // 必須チェック
                if (name.isEmpty()) errors.add(i + "行目：商品名が空です。");
                if (price == null) errors.add(i + "行目：価格が空です。");
                if (category.isEmpty()) errors.add(i + "行目：カテゴリが空です。");
                if (stock == null) errors.add(i + "行目：在庫数が空です。");
                if (isActive == null) errors.add(i + "行目：公開/非公開が空です。");

                // 文字数チェック
                if (name.length() > 100) errors.add(i + "行目：商品名は100文字以内です。");
                if (description.length() > 1000) errors.add(i + "行目：商品説明は1000文字以内です。");

                // エラーがある行はスキップ
                if (!errors.isEmpty()) continue;

                // ===============================
                // 5. DB登録（新規 or 更新）
                // ===============================
                if (id == null || productBulkRepository.exists(id) == 0) {
                    productBulkRepository.insertProduct(name, price, category, stock, imageUrl, description, isActive);
                } else {
                    productBulkRepository.updateProduct(id, name, price, category, stock, imageUrl, description, isActive);
                }

            } catch (Exception e) {
                errors.add(i + "行目：形式エラー（値が不正です）");
            }
        }

        return errors;
    }
}
