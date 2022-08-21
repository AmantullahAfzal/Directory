package com.mymoneykarma.phonebook.directory.service;

import com.mymoneykarma.phonebook.directory.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class accountService {
    @Autowired
    AccountRepository accountRepository ;

    public Optional<Integer> findIDbyName(String sender) {

        return Optional.of(accountRepository.findByName(sender).getId() );

    }
}
