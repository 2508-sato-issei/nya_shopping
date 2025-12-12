package com.example.nya_shopping.controller.form;

import com.example.nya_shopping.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Getter
@Setter
public class ProductForm {

    private Integer id;

    @NotBlank(message = E0024)
    @Size(max = 100, message = E0030)
    private String name;

    @NotNull(message = E0025)
    private Integer price;

    @NotNull(message = E0026)
    private Category category;

    @NotNull(message = E0027)
    private Integer stock;

    @NotNull(message = E0028)
    private String imageUrl;
    private MultipartFile imageFile;

    @Size(max = 1000, message = E0032)
    private String description;

    @NotNull(message = E0029)
    private Boolean isActive;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
