package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.error.RecordNotFoundException;
import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.repository.ProductRepository;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.nya_shopping.validation.ErrorMessage.E0018;
import static com.example.nya_shopping.validation.ErrorMessage.E0028;

@Service
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

    /* 商品管理一覧画面表示 */
    public List<Product> searchProducts(ProductSearchCondition condition) {
        return productRepository.search(condition);
    }

    /* 商品登録処理 */
    public void create(ProductForm form) throws IOException {

        // 画像チェック（サイズ・拡張子）
        MultipartFile file = form.getImageFile();
        if (file.isEmpty()) {
            throw new RuntimeException(E0028);
        }

        // UUIDファイル名生成
        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;

        // 保存先
        Path uploadPath = Paths.get("uploads/products/" + fileName);
        Files.copy(file.getInputStream(), uploadPath);

        // Entityへ変換して保存
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setStock(form.getStock());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());
        product.setImageUrl("/images/products/" + fileName);

        productRepository.insert(product);
    }

    /* 商品編集画面表示 */
    public Product findById(Long id) {
        return productRepository.FindById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0018));
    }

    /* 商品編集処理 */
    public void update(Long id, ProductForm form) throws IOException {

        Product product = productRepository.FindById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0018));

        // 必要な項目だけチェックして上書き
        if (form.getName() != null) {
            product.setName(form.getName());
        }
        if (form.getName() != null) {
            product.setName(form.getName());
        }
        if (form.getName() != null) {
            product.setName(form.getName());
        }
        if (form.getName() != null) {
            product.setName(form.getName());
        }
        if (form.getName() != null) {
            product.setName(form.getName());
        }
        if (form.getName() != null) {
            product.setName(form.getName());
        }

        // --- 画像変更がある場合のみ更新 ---
        MultipartFile file = form.getImageFile();
        String imageUrl = product.getImageUrl();

        if (!file.isEmpty()) {
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + ext;

            Path uploadPath = Paths.get("uploads/products/" + fileName);
            Files.copy(file.getInputStream(), uploadPath);

            imageUrl = "/images/products/" + fileName;
        }
        product.setImageUrl(imageUrl);

        productRepository.update(product);
    }

}
