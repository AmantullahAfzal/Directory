package com.mymoneykarma.phonebook.directory.repository;

import com.mymoneykarma.phonebook.directory.entity.account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<account, Integer> {


    @Query("SELECT ac FROM account ac WHERE ac.username= ?1 ")
    public account findByName(String sender);
}
