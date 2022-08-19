package com.cloneproject.ssgjojo.categorylv1.service;

import com.cloneproject.ssgjojo.categorylv1.domain.CategoryLv1;
import com.cloneproject.ssgjojo.categorylv1.dto.CategoryLv1Dto;
import com.cloneproject.ssgjojo.categorylv1.repository.ICategoryLv1Repository;
import com.cloneproject.ssgjojo.categorylv2.domain.CategoryLv2;
import com.cloneproject.ssgjojo.categorylv2.dto.CategoryLv2Dto;
import com.cloneproject.ssgjojo.categorylv2.repository.ICategoryLv2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryLv1ServiceImple implements ICategoryLv1Service {

    private final ICategoryLv1Repository iCategoryLv1Repository;
    private final ICategoryLv2Repository iCategoryLv2Repository;

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
    public List<CategoryLv1Dto> findAllCategory() {

        List<CategoryLv1> categoryLv1 = iCategoryLv1Repository.findAll();
        List<CategoryLv1Dto> returnDto = new ArrayList<>();


        if(!categoryLv1.isEmpty()) {
            for(CategoryLv1 lv1 : categoryLv1) {
                List<CategoryLv2> categoryLv2List = iCategoryLv2Repository.findAllByCategoryLv1(lv1);
                List<CategoryLv2Dto> categoryLv2DtoList = new ArrayList<>();

                for(CategoryLv2 categoryLv2 : categoryLv2List) {
                    categoryLv2DtoList.add(CategoryLv2Dto.builder()
                            .lv2name(categoryLv2.getLv2name())
                            .id(categoryLv2.getId())
                            .categoryLv1(categoryLv2.getCategoryLv1().getId())
                            .build());
                }

                returnDto.add(CategoryLv1Dto.builder()
                        .lv1name(lv1.getLv1name())
                        .lv1imgpath(lv1.getLv1imgpath())
                        .categoryLv2List(categoryLv2DtoList)
                        .build());
            }
        }

        return returnDto;

    }

    @Override
    public void deleteCategory(Long id) {

        Optional<CategoryLv1> temp = iCategoryLv1Repository.findById(id);
        if(temp.isPresent()) {
            iCategoryLv1Repository.deleteById(id);
        }
    }
}