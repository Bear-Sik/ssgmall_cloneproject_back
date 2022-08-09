package com.cloneproject.ssgjojo.categoryLv4.service;

import com.cloneproject.ssgjojo.categoryLv2.domain.CategoryLv2;
import com.cloneproject.ssgjojo.categoryLv2.dto.CategoryLv2Dto;
import com.cloneproject.ssgjojo.categoryLv3.domain.CategoryLv3;
import com.cloneproject.ssgjojo.categoryLv4.domain.CategoryLv4;
import com.cloneproject.ssgjojo.categoryLv4.dto.CategoryLv4Dto;
import com.cloneproject.ssgjojo.categoryLv3.repository.ICategoryLv3Repository;
import com.cloneproject.ssgjojo.categoryLv4.repository.ICategoryLv4Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryLv4ServiceImple implements ICategoryLv4Service {

    private final ICategoryLv4Repository iCategoryLv4Repository;
    private final ICategoryLv3Repository iCategoryLv3Repository;

    @Override
    public CategoryLv4 addCategory(CategoryLv4Dto categoryLv4Dto) {

        Optional<CategoryLv3> temp = iCategoryLv3Repository.findById(categoryLv4Dto.getCategoryLv3());
        if(temp.isPresent()) {
            return iCategoryLv4Repository.save(CategoryLv4.builder()
                    .lv4name(categoryLv4Dto.getLv4name())
                    .categoryLv3(iCategoryLv3Repository.findById(categoryLv4Dto.getCategoryLv3()).get())
                    .build());
        }

        return null;
    }

    @Override
    public CategoryLv4 getCategoryById(Long id) {
        Optional<CategoryLv4> categoryLv4 = iCategoryLv4Repository.findById(id);

        if(categoryLv4.isPresent())
            return categoryLv4.get();

        return null;
    }

    @Override
    public CategoryLv4 editCategory(CategoryLv4Dto categoryLv4Dto) {

        Optional<CategoryLv4> categoryLv4 = iCategoryLv4Repository.findById(categoryLv4Dto.getId());
        Optional<CategoryLv3> categoryLv3 = iCategoryLv3Repository.findById(categoryLv4Dto.getCategoryLv3());

        if(categoryLv3.isPresent() && categoryLv4.isPresent()) {
            return iCategoryLv4Repository.save(CategoryLv4.builder()
                    .id(categoryLv4Dto.getId())
                    .lv4name(categoryLv4Dto.getLv4name())
                    .categoryLv3(iCategoryLv3Repository.findById(categoryLv4Dto.getCategoryLv3()).get())
                    .build());
        }

        return null;
    }

    @Override
    public List<CategoryLv4> getAllCategory() {
        return iCategoryLv4Repository.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<CategoryLv4> temp = iCategoryLv4Repository.findById(id);
        if(temp.isPresent()) {
            iCategoryLv4Repository.deleteById(id);
        }
    }
}
