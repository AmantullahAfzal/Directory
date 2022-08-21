package com.mymoneykarma.phonebook.directory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlockSenderForReceiver {

    @Autowired
    RedisTemplate<String, Object> template;
    @Cacheable(cacheNames = "stopMessage", key ="{#Sender}" )
    public String setBlockUser(String Sender , String receiver) {
        return  receiver;
    }

    public String getBlockedUser(String Sender ) {
         return template.opsForValue().get(Sender).toString() ;
    }


}
