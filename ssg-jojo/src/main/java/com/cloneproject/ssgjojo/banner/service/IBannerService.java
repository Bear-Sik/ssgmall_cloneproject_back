package com.cloneproject.ssgjojo.banner.service;

import com.cloneproject.ssgjojo.banner.domain.Banner;
import com.cloneproject.ssgjojo.banner.dto.BannerAddDto;

import java.util.List;

public interface IBannerService {
    Banner addBanner(BannerAddDto bannerAddDto);
    Banner getBannerById(Long id);
    List<Banner> getAllBanner();
    void deleteBanner(Long id);
    Banner editBanner(Banner banner);
}