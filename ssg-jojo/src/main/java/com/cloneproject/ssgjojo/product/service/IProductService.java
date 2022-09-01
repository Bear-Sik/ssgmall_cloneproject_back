package com.cloneproject.ssgjojo.product.service;

import com.cloneproject.ssgjojo.product.domain.Product;
import com.cloneproject.ssgjojo.product.dto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IProductService {
    Product addProduct(ProductAddDto ProductAddDto);
    Product addProductWithPhoto(ProductAddDto productAddDto, MultipartFile thumbnail, List<MultipartFile> productPhoto, List<MultipartFile> productDetail);
    ProductInfoDto getProductById(Long id);
    List<ProductInfoDto> getAllProduct();
    void deleteProduct(Long id);
    Product editProduct(ProductUpdateDto productUpdateDto);
    List<ProductListDto> getAllProductList();
    List<ProductListDto> findProductByCategoryLv(Long lv, Long id, int page);
    ProductDetailDto getProductDetail(Long productId, HttpServletRequest request);
    List<ProductListDto> productSearch(String keyword, HttpServletRequest request);
}
