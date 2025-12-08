package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.error.RecordNotFoundException;
import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.repository.ProductRepository;
import com.example.nya_shopping.repository.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Page<Product> searchProduct(SearchForm form, PageRequest pageRequest) {

        int offset = (int) pageRequest.getOffset();
        int limit = pageRequest.getPageSize();

        // 検索結果を取得
        List<Product> products = productRepository.searchProducts(
                form.getCategory(),
                form.getKeyword(),
                form.getMinPrice(),
                form.getMaxPrice(),
                form.getStock(),
                form.getSort(),
                offset,
                limit
        );

        // 件数取得
        int total = productRepository.countProducts(
                form.getCategory(),
                form.getKeyword(),
                form.getMinPrice(),
                form.getMaxPrice(),
                form.getStock()
        );

        return new PageImpl<>(products, pageRequest, total);
    }

    public Product findById(Integer id) {
        Product product = productRepository.findByIdIsActive(id);
        if (product == null) {
            throw new RecordNotFoundException(E0018);
        }
        return product;
    }

    public Page<ProductDto> search(ProductSearchCondition cond, PageRequest pageRequest) {
        int offset = (int) pageRequest.getOffset();
        int limit = pageRequest.getPageSize();

        List<ProductDto> products = productRepository.search(cond, offset, limit);
        int total = productRepository.count(cond, offset, limit);

        return new PageImpl<>(products, pageRequest, total);
    }

//    public int count(ProductSearchCondition cond) {
//        return productRepository.count(cond);
//    }

    /**
     * CSV出力
     */
    public void exportCsv(ProductSearchCondition cond, Writer writer) throws IOException {

        List<ProductDto> list = productRepository.searchAll(cond);

        writer.write("商品ID,商品名,価格,カテゴリー,在庫数,画像ファイル名,商品説明,公開/非公開,登録日\n");

        for (ProductDto p : list) {
            writer.write(String.format(
                    "%d,%s,%d,%s,%d,%s,%s,%b,%s\n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getCategory(),
                    p.getStock(),
                    p.getImageUrl(),
                    nvl(p.getDescription()),
                    p.getIsActive(),
                    p.getCreatedAt()
            ));
        }
    }

    public List<String> validateAndCreate(ProductForm form) {

        List<String> errors = new ArrayList<>();

        // ==========================
        // 必須チェック
        // ==========================
        if (isEmpty(form.getName())) errors.add(E0024);
        if (form.getPrice() == null) errors.add(E0025);
        if (form.getCategory() == null) errors.add(E0026);
        if (form.getStock() == null) errors.add(E0027);
        if (form.getImageFile() == null || form.getImageFile().isEmpty())
            errors.add(E0028);
        if (form.getIsActive() == null)
            errors.add(E0029);

        // ==========================
        // 文字数チェック
        // ==========================
        if (!isEmpty(form.getName()) && form.getName().length() > 100)
            errors.add(E0030);

        if (!isEmpty(form.getDescription()) && form.getDescription().length() > 1000)
            errors.add(E0032);

        // ==========================
        // 画像形式チェック
        // ==========================
        String fileName = null;

        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {

            String original = form.getImageFile().getOriginalFilename().toLowerCase();

            if (!(original.endsWith(".jpg") || original.endsWith(".jpeg") || original.endsWith(".png"))) {
                errors.add(E0031);
            } else {
                // 保存ファイル名を生成
                fileName = UUID.randomUUID() + "_" + original;

                String projectPath = System.getProperty("user.dir");
                File dir = new File(projectPath + "/uploads/products");
                if (!dir.exists()) dir.mkdirs();

                try {
                    form.getImageFile().transferTo(new File(dir, fileName));
                } catch (IOException e) {
                    errors.add("画像の保存に失敗しました。");
                }
            }
        }

        if (!errors.isEmpty()) return errors;

        // ==========================
        // DB INSERT
        // ==========================
        productRepository.insertProduct(
                form.getName(),
                form.getPrice(),
                form.getCategory(),
                form.getStock(),
                fileName,
                form.getDescription(),
                form.getIsActive()
        );

        return errors;
    }

    /**
     * 編集画面表示用
     */
    public ProductForm findById(Long id) {
        ProductDto dto = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0018));
        if (dto == null) return null;

        ProductForm form = new ProductForm();
        form.setId(dto.getId());
        form.setName(dto.getName());
        form.setPrice(dto.getPrice());
        form.setCategory(dto.getCategory());
        form.setStock(dto.getStock());
        form.setImageUrl(dto.getImageUrl());
        form.setDescription(dto.getDescription());
        form.setIsActive(dto.getIsActive());

        return form;
    }

    /**
     * 更新処理
     */
    public List<String> validateAndUpdate(ProductForm form) {

        List<String> errors = new ArrayList<>();

        // 必須チェック
        if (isEmpty(form.getName())) errors.add(E0024);
        if (form.getPrice() == null) errors.add(E0025);
        if (form.getCategory() == null) errors.add(E0026);
        if (form.getStock() == null) errors.add(E0027);
        if (form.getIsActive() == null) errors.add(E0029);

        // 文字数チェック
        if (!isEmpty(form.getName()) && form.getName().length() > 100)
            errors.add(E0030);

        if (!isEmpty(form.getDescription()) && form.getDescription().length() > 1000)
            errors.add(E0032);

        // 画像アップロード（任意）
        String newImageFileName = form.getImageUrl();

        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {

            String original = form.getImageFile().getOriginalFilename().toLowerCase();

            if (!(original.endsWith(".jpg") || original.endsWith(".jpeg") || original.endsWith(".png"))) {
                errors.add(E0031);
            } else {
                newImageFileName = UUID.randomUUID() + "_" + original;

                String projectPath = System.getProperty("user.dir");
                File dir = new File(projectPath + "/uploads/products");
                if (!dir.exists()) dir.mkdirs();

                try {
                    form.getImageFile().transferTo(new File(dir, newImageFileName));
                } catch (IOException e) {
                    errors.add("画像の保存に失敗しました。");
                }
            }
        }

        if (!errors.isEmpty()) return errors;

        // UPDATE実行
        productRepository.update(
                form.getId(),
                form.getName(),
                form.getPrice(),
                form.getCategory(),
                form.getStock(),
                newImageFileName,
                form.getDescription(),
                form.getIsActive()
        );

        return errors;
    }

    public void delete(Long id) {
        productRepository.delete(id);
    }

    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    //在庫を減らす処理
    @Transactional
    public void decreaseStock(List<CartItem> cart) {

        for (CartItem ci : cart) {
            Product product = productRepository.findProductById(ci.getProductId());
            if (cart == null) {
                throw new RuntimeException("商品が存在しません");
            }
            int newStock = product.getStock() - ci.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException(E0019);
            }
            product.setStock(newStock);
            productRepository.updateStock(product);
        }
    }

    public List<Product> findRecentProduct(int limit) {
        return productRepository.findRecentProducts(limit);
    }
}
