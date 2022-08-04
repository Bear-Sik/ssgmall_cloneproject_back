package com.cloneproject.ssgjojo.categoryLv3.controller;

import com.cloneproject.ssgjojo.categoryLv3.domain.CategoryLv3;
import com.cloneproject.ssgjojo.categoryLv3.dto.CategoryLv3Dto;
import com.cloneproject.ssgjojo.categoryLv3.service.ICategoryLv3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryLv3Controller {

    private final ICategoryLv3Service iCategoryLv3Service;

    @PostMapping("/category/Lv3/add")
    public CategoryLv3 addCategory(@RequestBody CategoryLv3Dto categoryLv3Dto) {
        return iCategoryLv3Service.addCategory(categoryLv3Dto);
    }

    @GetMapping("/category/Lv3/getAll")
    public List<CategoryLv3> getAllCategory() {
        return iCategoryLv3Service.getAllCategory();
    }

    @PutMapping("/category/Lv3/edit")
    public CategoryLv3 editCategory(@RequestBody CategoryLv3Dto categoryLv3Dto) {
        return iCategoryLv3Service.editCategory(categoryLv3Dto);
    }
}
