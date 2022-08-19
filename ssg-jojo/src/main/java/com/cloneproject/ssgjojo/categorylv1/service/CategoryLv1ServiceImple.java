package com.cloneproject.ssgjojo.categorylv1.service;

import com.cloneproject.ssgjojo.categorylv1.domain.CategoryLv1;
import com.cloneproject.ssgjojo.categorylv1.repository.ICategoryLv1Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryLv1ServiceImple implements ICategoryLv1Service {

    private final ICategoryLv1Repository iCategoryLv1Repository;

    @Override
    public CategoryLv1 addCategory(CategoryLv1 categoryLv1) {
        return iCategoryLv1Repository.save(categoryLv1);
    }

    @Override
    public CategoryLv1 getCategoryById(Long id) {
        Optional<CategoryLv1> categoryLv1 = iCategoryLv1Repository.findById(id);

        if(categoryLv1.isPresent())
            return categoryLv1.get();

        return null;
    }

    @Override
    public CategoryLv1 editCategory(CategoryLv1 categoryLv1) {

        Optional<CategoryLv1> temp = iCategoryLv1Repository.findById(categoryLv1.getId());
        if(temp.isPresent()) {
            return iCategoryLv1Repository.save(CategoryLv1.builder()
                    .id(temp.get().getId())
                    .lv1name(categoryLv1.getLv1name())
                    .lv1imgpath(categoryLv1.getLv1imgpath())
                    .build());
        }

        return null;

    }

    @Override
    public List<CategoryLv1> getAllCategory() {
        return iCategoryLv1Repository.findAll();
    }

    @Override
    public void deleteCategory(Long id) {

        Optional<CategoryLv1> temp = iCategoryLv1Repository.findById(id);
        if(temp.isPresent()) {
            iCategoryLv1Repository.deleteById(id);
        }
    }
}
