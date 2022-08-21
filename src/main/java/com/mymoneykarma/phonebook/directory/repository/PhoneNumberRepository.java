package com.mymoneykarma.phonebook.directory.repository;

import com.mymoneykarma.phonebook.directory.entity.phone_number;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<phone_number,Integer> {

    @Query("SELECT ph FROM phone_number ph WHERE ph.account_id = ?1 AND ph.number = ?2 ")
    public List<phone_number> isConnected(int senderID ,String receiver);
}
