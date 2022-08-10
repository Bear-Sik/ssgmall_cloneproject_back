package com.cloneproject.ssgjojo.coupon.domain;

import com.cloneproject.ssgjojo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponName; // 쿠폰 이름

    @Column(nullable = false)
    private Timestamp couponStartDate; // 쿠폰 시작날짜

    @Column(nullable = false)
    private Timestamp couponEndDate; // 쿠폰 마감날짜

    @Column(nullable = false)
    private String couponContent; // 쿠폰 내용

    @Column(nullable = false)
    private boolean isUseStatus; // 쿠폰 사용여부

    @ManyToOne
    private User user;

}
