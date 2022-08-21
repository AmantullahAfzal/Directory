package com.mymoneykarma.phonebook.directory.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String sender;
    private String receiver;
    private String text;

}
