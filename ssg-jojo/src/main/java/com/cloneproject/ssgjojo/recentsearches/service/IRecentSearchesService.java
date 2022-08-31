package com.cloneproject.ssgjojo.recentsearches.service;

import com.cloneproject.ssgjojo.recentsearches.dto.RecentSearchesAddDto;
import com.cloneproject.ssgjojo.recentsearches.dto.RecentSearchesDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IRecentSearchesService {

    RecentSearchesAddDto addRecentSearches(RecentSearchesAddDto recentSearchesAddDto);
//    List<RecentSearchesDto> getRecentSearchesByUserId(HttpServletRequest request);
    List<String> getRecentSearchesByUserId(HttpServletRequest request);
    void deleteRecentSearches(Long id, HttpServletRequest request);
    void deleteAllByUser(HttpServletRequest request);

}
