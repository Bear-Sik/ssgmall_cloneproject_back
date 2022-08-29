package com.cloneproject.ssgjojo.categorylv3.controller;

import com.cloneproject.ssgjojo.categorylv3.domain.CategoryLv3;
import com.cloneproject.ssgjojo.categorylv3.dto.CategoryLv3Dto;
import com.cloneproject.ssgjojo.categorylv3.service.ICategoryLv3Service;
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

    @PutMapping("/category/Lv3/edit")
    public CategoryLv3 editCategory(@RequestBody CategoryLv3 categoryLv3) {
        return iCategoryLv3Service.editCategory(categoryLv3);
    }

    @DeleteMapping("/category/Lv3/{id}")
    public void deleteCategory(@PathVariable Long id) {
        iCategoryLv3Service.deleteCategory(id);
    }


}
