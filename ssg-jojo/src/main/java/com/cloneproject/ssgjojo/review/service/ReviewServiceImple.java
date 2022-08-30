package com.cloneproject.ssgjojo.review.service;

import com.cloneproject.ssgjojo.jwt.JwtTokenProvider;
import com.cloneproject.ssgjojo.orders.domain.Orders;
import com.cloneproject.ssgjojo.orders.repository.IOrdersRepository;
import com.cloneproject.ssgjojo.ordersproductlist.domain.OrdersProductList;
import com.cloneproject.ssgjojo.ordersproductlist.repository.IOrdersProductListRepository;
import com.cloneproject.ssgjojo.product.domain.Product;
import com.cloneproject.ssgjojo.product.repository.IProductRepository;
import com.cloneproject.ssgjojo.review.domain.Review;
import com.cloneproject.ssgjojo.review.dto.*;
import com.cloneproject.ssgjojo.review.repository.IReviewRepository;
import com.cloneproject.ssgjojo.reviewphoto.domain.ReviewPhoto;
import com.cloneproject.ssgjojo.reviewphoto.dto.ReviewPhotoDto;
import com.cloneproject.ssgjojo.reviewphoto.repository.IReviewPhotoRepository;
import com.cloneproject.ssgjojo.user.domain.User;
import com.cloneproject.ssgjojo.user.repository.IUserRepository;
import com.cloneproject.ssgjojo.util.MultipartUtil;
import com.cloneproject.ssgjojo.util.s3.AwsS3ResourceStorage;
import com.cloneproject.ssgjojo.util.s3.FileInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImple implements IReviewService {

    private final IReviewRepository iReviewRepository;
    private final IReviewPhotoRepository iReviewPhotoRepository;
    private final IProductRepository iProductRepository;
    private final IUserRepository iUserRepository;
    private final IOrdersRepository iOrdersRepository;
    private final IOrdersProductListRepository iOrdersProductListRepository;
    private final AwsS3ResourceStorage awsS3ResourceStorage;
    private final JwtTokenProvider jwtTokenProvider;

    // 리뷰 작성
    @Override
    public ReviewOutputDto addReview(ReviewDto reviewDto, HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(reviewDto.getUserId());
        Optional<Product> product = iProductRepository.findById(reviewDto.getProductId());
        Optional<Orders> orders = iOrdersRepository.findById(reviewDto.getOrdersId());

        if(user.isPresent() && product.isPresent() && orders.isPresent()) {
            Review review = iReviewRepository.save(Review.builder()
                    .title(reviewDto.getTitle())
                    .mainText(reviewDto.getMainText())
                    .score(reviewDto.getScore())
                    .user(user.get())
                    .product(product.get())
                    .orders(orders.get())
                    .build());

            return ReviewOutputDto.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .mainText(review.getMainText())
                    .score(review.getScore())
                    .userId(review.getUser().getUserId())
                    .productId(review.getProduct().getId())
                    .createdTime(review.getCreatedDate())
                    .build();
        }

        return null;
    }

    // 리뷰 작성 시 이미지 첨부
    @Override
    public boolean addReviewWithImg(ReviewDto reviewDto, List<MultipartFile> reviewPhoto, HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(userId);
        Optional<Product> product = iProductRepository.findById(reviewDto.getProductId());
        Optional<Orders> orders = iOrdersRepository.findById(reviewDto.getOrdersId());

        if(user.isPresent() && product.isPresent()) {
            Review review = iReviewRepository.save(Review.builder()
                    .title(reviewDto.getTitle())
                    .mainText(reviewDto.getMainText())
                    .score(reviewDto.getScore())
                    .user(user.get())
                    .product(product.get())
                    .orders(orders.get())
                    .build());

            for(MultipartFile file : reviewPhoto) {
                FileInfoDto fileInfoDto = FileInfoDto.multipartOf(file, "review", review.getId());
                awsS3ResourceStorage.store(fileInfoDto, file);

                iReviewPhotoRepository.save(ReviewPhoto.builder()
                        .reviewPhotoPath(MultipartUtil.createURL(fileInfoDto.getRemotePath()))
                        .reviewPhotoOriginName(fileInfoDto.getName())
                        .review(review)
                        .build());
            }

            return  true;
        }

        return false;
    }

    // 작성한 리뷰 수정
    @Override
    public ReviewEditDto editReview(ReviewEditDto reviewEditDto, HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(reviewEditDto.getUserId());
        Optional<Product> product = iProductRepository.findById(reviewEditDto.getProductId());
        Optional<Review> review = iReviewRepository.findById(reviewEditDto.getId());

        if (review.isPresent() && user.isPresent() && product.isPresent()) {
            if(review.get().getUser().getId() == reviewEditDto.getUserId()) {
                Review reviewEdit = iReviewRepository.save(Review.builder()
                        .id(reviewEditDto.getId())
                        .title(reviewEditDto.getTitle())
                        .mainText(reviewEditDto.getMainText())
                        .score(reviewEditDto.getScore())
                        .user(user.get())
                        .product(product.get())
                        .build());

                return ReviewEditDto.builder()
                        .id(review.get().getId())
                        .title(reviewEdit.getTitle())
                        .mainText(reviewEdit.getMainText())
                        .score(reviewEdit.getScore())
                        .userId(reviewEdit.getUser().getId())
                        .productId(reviewEdit.getProduct().getId())
                        .build();
            }
        }

        return null;
    }


    // 리뷰 정렬 (별점 높은 순, 별점 낮은 순, 최신순)
    @Override
    public List<ReviewOutputDto> sortedGetReviewByProductId(Long id, int sort) {
        Optional<Product> product = iProductRepository.findById(id);
        List<ReviewOutputDto> returnDto = new ArrayList<>();
        List<Review> temp = new ArrayList<>();

        if(product.isPresent()) {
            if(sort == 1)
                temp = iReviewRepository.findAllByProductOrderByScoreAsc(product.get());
            else if(sort == 2)
                temp = iReviewRepository.findAllByProductOrderByScoreDesc(product.get());
            else if(sort == 3)
                temp = iReviewRepository.findAllByProductOrderByCreatedDateDesc(product.get());
            else
                temp = iReviewRepository.findAllByProduct(product.get());

            for(Review review : temp) {
                returnDto.add(ReviewOutputDto.builder()
                        .id(review.getId())
                        .title(review.getTitle())
                        .mainText(review.getMainText())
                        .score(review.getScore())
                        .userId(review.getUser().getUserId())
                        .productId(review.getProduct().getId())
                        .createdTime(review.getCreatedDate())
                        .build());
            }

            return returnDto;
        }

        return null;
    }

    // 해당 상품의 리뷰 목록 조회
    @Override
    public List<ReviewOutputDto> findAllByProduct(Long productId) {

        Optional<Product> product = iProductRepository.findById(productId);
        List<ReviewOutputDto> reviewOutputDtoList = new ArrayList<>();

        if(product.isPresent()) {
            List<Review> reviewList = iReviewRepository.findAllByProduct(product.get());
            for(Review review : reviewList) {
                List<ReviewPhoto> reviewPhotoList = iReviewPhotoRepository.findAllByReview(review);
                List<ReviewPhotoDto> reviewPhotoDtoList = new ArrayList<>();

                if(!reviewPhotoList.isEmpty()) {
                    for(ReviewPhoto reviewPhoto : reviewPhotoList) {
                        reviewPhotoDtoList.add(ReviewPhotoDto.builder()
                                .id(reviewPhoto.getId())
                                .reviewPhotoPath(reviewPhoto.getReviewPhotoPath())
                                .reviewId(reviewPhoto.getId())
                                .build());
                    }

                }

                reviewOutputDtoList.add(ReviewOutputDto.builder()
                        .id(review.getId())
                        .reviewPhotoDtoList(reviewPhotoDtoList)
                        .title(review.getTitle())
                        .mainText(review.getMainText())
                        .score(review.getScore())
                        .userId(review.getUser().getUserId())
                        .productId(review.getProduct().getId())
                        .createdTime(review.getCreatedDate())
                        .build());
            }

            return reviewOutputDtoList;
        }

        return null;
    }

    // 해당 유저가 작성한 리뷰 조회
    @Override
    public List<ReviewOutputDto> findAllByUser(HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(userId);
        List<ReviewOutputDto> returnDtoList = new ArrayList<>();

        if(user.isPresent()) {
            List<Review> reviewList = iReviewRepository.findAllByUser(user.get());

            for(Review review : reviewList) {

                List<ReviewPhoto> reviewPhotoList = iReviewPhotoRepository.findAllByReview(review);
                List<ReviewPhotoDto> reviewPhotoDtoList = new ArrayList<>();

                if(!reviewPhotoList.isEmpty()) {
                    for(ReviewPhoto reviewPhoto : reviewPhotoList) {

                        reviewPhotoDtoList.add(ReviewPhotoDto.builder()
                                .id(reviewPhoto.getId())
                                .reviewId(review.getId())
                                .reviewPhotoPath(reviewPhoto.getReviewPhotoPath())
                                .build());

                    }

                }

                returnDtoList.add(ReviewOutputDto.builder()
                        .id(review.getId())
                        .userId(review.getUser().getUserId())
                        .productId(review.getProduct().getId())
                        .title(review.getTitle())
                        .mainText(review.getMainText())
                        .score(review.getScore())
                        .createdTime(review.getCreatedDate())
                        .reviewPhotoDtoList(reviewPhotoDtoList)
                        .build());
            }

            return returnDtoList;
        }

        return null;
    }

    // 해당 유저가 작성 가능한 리뷰 조회
    @Override
    public List<ReviewPossibleWriteDto> findPossibleWrite(HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(userId);

        if(user.isPresent()) {
            List<Orders> ordersList = iOrdersRepository.findAllByUser(user.get());
            List<ReviewPossibleWriteDto> returnDtoList = new ArrayList<>();
            if(ordersList.isEmpty())
                return null;

            for(Orders orders : ordersList) {
                List<OrdersProductList> ordersProductLists = iOrdersProductListRepository.findAllByOrders(orders);

                for(OrdersProductList productList : ordersProductLists) {
                    Product product = productList.getProduct();

                    List<Review> reviewList = iReviewRepository.findAllByOrdersAndProduct(orders, product);
                    if(reviewList.isEmpty())
                        returnDtoList.add(ReviewPossibleWriteDto.builder()
                                .productId(product.getId())
                                .ordersId(orders.getId())
                                .productThumbnail(product.getThumbnail())
                                .deliveryDate(orders.getDeliveryDate())
                                .build());
                }
            }

            return returnDtoList;
        }

        return null;
    }

    // 리뷰 삭제
    @Override
    public void deleteReview(ReviewDeleteDto reviewDeleteDto, HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));
        Optional<User> user = iUserRepository.findById(reviewDeleteDto.getUserId());
        Optional<Review> review = iReviewRepository.findById(reviewDeleteDto.getId());

        if (user.isPresent() && review.isPresent()) {
            if(review.get().getUser().getId() == reviewDeleteDto.getUserId()) {
                iReviewRepository.deleteById(reviewDeleteDto.getId());
            }
        }
    }

    // review 페이징
    @Override
    public List<Review> pageList(Pageable pageable, Long productId) {

        Optional<Product> product = iProductRepository.findById(productId);
        if(product.isPresent()) {
            List<Review> reviewList = iReviewRepository.findAllByProductOrderByCreatedDateAsc(product.get(), pageable);
            Long test=1L;

            return reviewList;
        }
        return null;
    }

//    @Override
//    public Page<Review> pageList(Pageable pageable, Long productId) {
//
//        Optional<Product> product = iProductRepository.findById(productId);
//        if(product.isPresent()) {
//            Page<Review> reviewPage = iReviewRepository.findByProductOrderByCreatedDateAsc(product.get(), pageable);
//            Long test=1L;
//
//            return reviewPage;
//        }
//        return null;
//    }
}
