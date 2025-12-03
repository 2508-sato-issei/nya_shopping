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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
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

    public List<ProductDto> search(ProductSearchCondition cond) {
        return productRepository.search(cond);
    }

    public int count(ProductSearchCondition cond) {
        return productRepository.count(cond);
    }

    /**
     * CSV出力
     */
    public void exportCsv(ProductSearchCondition cond, Writer writer) throws IOException {

        List<ProductDto> list = productRepository.searchAll(cond);

        writer.write("id,name,price,category,stock,is_active,created_at\n");

        for (ProductDto p : list) {
            writer.write(String.format(
                    "%d,%s,%d,%s,%d,%b,%s\n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getCategory(),
                    p.getStock(),
                    p.getIsActive(),
                    p.getCreatedAt()
            ));
        }
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

    //在庫を減らす処理
    @Transactional
    public void decreaseStock(List<CartItem> cart){

        for(CartItem ci : cart){
            Product product = productRepository.findProductById(ci.getProductId());
            if(cart == null){
                throw new RuntimeException("商品が存在しません");
            }
            int newStock = product.getStock() - ci.getQuantity();
            if(newStock < 0){
                throw new RuntimeException(E0019);
            }
            product.setStock(newStock);
            productRepository.updateStock(product);
        }
    }

}
