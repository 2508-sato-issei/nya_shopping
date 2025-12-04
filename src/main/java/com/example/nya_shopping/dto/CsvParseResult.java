package com.example.nya_shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CsvParseResult {
    private List<String> errors;
    private List<ProductDto> rows;
}