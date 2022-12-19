package com.antnest.bch.service;


import com.antnest.bch.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HASH_KEY = "AddressCoin";

    public String checkKey(String key){
        Object value =redisTemplate.opsForHash().get(HASH_KEY,key);
        if(value != null){
            return value.toString();
        }
        return null;
    }



}
