package com.edu.nju.seckill.service.impl;

import com.edu.nju.seckill.dao.FavoriteMapper;
import com.edu.nju.seckill.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lql
 * @date 2020/1/11 20:20
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
}
