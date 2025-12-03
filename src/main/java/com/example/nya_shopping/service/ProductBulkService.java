package com.example.nya_shopping.service;

import com.example.nya_shopping.dto.CsvParseResult;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.ProductBulkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ProductBulkService {

    private final ProductBulkRepository productBulkRepository;

    public List<String> processCsv(MultipartFile file) {

        CsvParseResult result = parseAndValidateCsv(file);

        // エラーが1件でもあれば、そのまま画面へ返す（DBは触らない）
        if (!result.getErrors().isEmpty()) {
            return result.getErrors();
        }

        registerValidRows(result.getRows());

        return List.of();
    }

    private CsvParseResult parseAndValidateCsv(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        List<ProductDto> validRows = new ArrayList<>();

        // ----- ファイルチェック -----
        if (file == null || file.isEmpty()) {
            errors.add(E0020);
            return new CsvParseResult(errors, validRows);
        }
        if (!file.getOriginalFilename().endsWith(".csv")) {
            errors.add(E0021);
            return new CsvParseResult(errors, validRows);
        }

        // ----- CSV読み込み -----
        List<String[]> rows = readCsvRows(file, errors);
        if (!errors.isEmpty()) return new CsvParseResult(errors, validRows);

        // ----- ヘッダーチェック -----
        validateHeader(rows, errors);
        if (!errors.isEmpty()) return new CsvParseResult(errors, validRows);

        // ----- データ行バリデーション -----
        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            List<String> rowErrors = validateRow(cols, i);
            if (!rowErrors.isEmpty()) {
                errors.addAll(rowErrors);
                continue; // 有効行には追加しない
            }

            validRows.add(convertToDto(cols));
        }

        return new CsvParseResult(errors, validRows);
    }

    private List<String[]> readCsvRows(MultipartFile file, List<String> errors) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1)); // 空欄を保持
            }

        } catch (IOException e) {
            errors.add("CSV読み込み中にエラーが発生しました。");
        }
        return rows;
    }

    private void validateHeader(List<String[]> rows, List<String> errors) {

        String[] header = {"商品ID","商品名","価格","カテゴリー","在庫数","画像ファイル名","商品説明","公開/非公開","登録日"};

        if (rows.isEmpty() || rows.get(0).length != header.length) {
            errors.add(E0022);
            return;
        }

        for (int i = 0; i < header.length; i++) {
            if (!rows.get(0)[i].equals(header[i])) {
                errors.add(E0022);
                return;
            }
        }
    }

    private List<String> validateRow(String[] cols, int rowNum) {

        List<String> errors = new ArrayList<>();

        if (cols.length != 9) {
            errors.add(rowNum + "行目：カラム数が不正です。");
            return errors;
        }

        // パース（tryで囲む）
        Long id = null;
        Integer price = null;
        Integer stock = null;
        Boolean isActive = null;

        try {
            id = cols[0].isEmpty() ? null : Long.valueOf(cols[0]);
            price = Integer.valueOf(cols[2]);
            stock = Integer.valueOf(cols[4]);
            isActive = Boolean.valueOf(cols[7]);
        } catch (Exception e) {
            errors.add(rowNum + "行目：形式エラー（値が不正です）");
            return errors;
        }

        // 必須チェック
        if (cols[1].isEmpty()) errors.add(rowNum + "行目：商品名が空です。");
        if (cols[2].isEmpty()) errors.add(rowNum + "行目：価格が空です。");
        if (cols[3].isEmpty()) errors.add(rowNum + "行目：カテゴリーが空です。");
        if (cols[4].isEmpty()) errors.add(rowNum + "行目：在庫数が空です。");
        if (cols[5].isEmpty()) errors.add(rowNum + "行目：画像ファイル名が空です。");
        if (cols[7].isEmpty()) errors.add(rowNum + "行目：公開/非公開が空です。");

        // 文字数チェック
        if (cols[1].length() > 100) errors.add(rowNum + "行目：商品名は100文字以内です。");
        if (cols[6].length() > 1000) errors.add(rowNum + "行目：商品説明は1000文字以内です。");

        return errors;
    }

    private ProductDto convertToDto(String[] cols) {
        ProductDto row = new ProductDto();

        row.setId(cols[0].isEmpty() ? null : Integer.valueOf((cols[0])));
        row.setName(cols[1]);
        row.setPrice(Integer.valueOf(cols[2]));
        row.setCategory(Category.valueOf(cols[3]));
        row.setStock(Integer.valueOf(cols[4]));
        row.setImageUrl(cols[5]);
        row.setDescription(cols[6]);
        row.setIsActive(Boolean.valueOf(cols[7]));

        return row;
    }

    @Transactional
    public void registerValidRows(List<ProductDto> rows) {

        for (ProductDto row : rows) {

            if (productBulkRepository.exists(row.getId()) == 0) {

                productBulkRepository.insertProduct(
                        row.getName(),
                        row.getPrice(),
                        row.getCategory(),
                        row.getStock(),
                        row.getImageUrl(),
                        row.getDescription(),
                        row.getIsActive()
                );

            } else {

                productBulkRepository.updateProduct(
                        row.getId(),
                        row.getName(),
                        row.getPrice(),
                        row.getCategory(),
                        row.getStock(),
                        row.getImageUrl(),
                        row.getDescription(),
                        row.getIsActive()
                );
            }
        }
    }
}
