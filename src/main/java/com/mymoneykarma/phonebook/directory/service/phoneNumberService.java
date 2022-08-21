package com.mymoneykarma.phonebook.directory.service;

import com.mymoneykarma.phonebook.directory.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class phoneNumberService {

    @Autowired
    PhoneNumberRepository phoneNumberRepository ;
    public boolean findConnection(int senderID, String receiver) {

        if( phoneNumberRepository.isConnected(senderID , receiver).size() > 0){
            return true;
        }else{
            return false;
        }
    }
}
