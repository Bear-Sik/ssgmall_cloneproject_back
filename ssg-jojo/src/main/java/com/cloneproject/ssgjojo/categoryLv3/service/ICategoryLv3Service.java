package com.cloneproject.ssgjojo.categorylv3.service;

import com.cloneproject.ssgjojo.categorylv3.domain.CategoryLv3;
import com.cloneproject.ssgjojo.categorylv3.dto.CategoryLv3Dto;

import java.util.List;

public interface ICategoryLv3Service {

    CategoryLv3 addCategory(CategoryLv3Dto categoryLv3Dto);
    CategoryLv3 getCategoryById(Long id);
    CategoryLv3 editCategory(CategoryLv3Dto categoryLv3Dto);
    List<CategoryLv3> getAllCategory();

    void deleteCategory(Long id);
}
