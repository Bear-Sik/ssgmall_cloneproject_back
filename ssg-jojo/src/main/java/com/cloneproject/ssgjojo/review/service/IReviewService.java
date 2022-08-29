package com.cloneproject.ssgjojo.review.service;

import com.cloneproject.ssgjojo.review.domain.Review;
import com.cloneproject.ssgjojo.review.dto.*;
import com.cloneproject.ssgjojo.reviewphoto.domain.ReviewPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IReviewService {

    ReviewOutputDto addReview(ReviewDto reviewDto);
    boolean addReviewWithImg(ReviewDto reviewDto, List<MultipartFile> reviewPhoto);
    ReviewEditDto editReview(ReviewEditDto reviewEditDto);
    List<ReviewOutputDto> sortedGetReviewByProductId(Long id, int sort);
    List<ReviewOutputDto> findAllByProduct(Long productId);
    void deleteReview(ReviewDeleteDto reviewDeleteDto);

    List<ReviewOutputDto> findAllByUser(Long userId);

    List<ReviewPossibleWriteDto> findPossibleWrite(Long userId);

    public Page<Review> pageList(Pageable pageable, Long productId);

}
