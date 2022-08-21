package com.mymoneykarma.phonebook.directory.controller;

import com.mymoneykarma.phonebook.directory.domain.MessageRequest;
import com.mymoneykarma.phonebook.directory.domain.MessageResponse;
import com.mymoneykarma.phonebook.directory.service.BlockSenderForReceiver;
import com.mymoneykarma.phonebook.directory.service.RateLimiterService;
import com.mymoneykarma.phonebook.directory.service.accountService;
import com.mymoneykarma.phonebook.directory.service.phoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class messageController {


    @Value("${spring.api.hit.count}")
    private int apiCountLimit;
    @Autowired
    phoneNumberService phoneNumberService;

    @Autowired
    accountService accountService ;

    @Autowired
    RateLimiterService rateLimiterService;

    @Autowired
    BlockSenderForReceiver blockSenderForReceiver ;

    @PostMapping("/inbound/sms")
    public ResponseEntity<MessageResponse> receiveMessage(@RequestBody MessageRequest messageRequest){
        System.out.println( messageRequest.toString());
        /* Basic Input Validation */
        MessageResponse messageResponse = isInboundMessageValid(messageRequest);

        if(messageResponse.getError().equals("")) {
            Optional<Integer> id = accountService.findIDbyName(messageRequest.getSender());
            int SenderID ;
            if( id.isPresent() ){

                /* Rate Limit check for Sender */

                SenderID = id.get() ;
                if (Integer.parseInt( rateLimiterService.getApiHitCount(SenderID)) > apiCountLimit ){
                    messageResponse.setMessage("");
                    messageResponse.setError("Limit reached for Sender "+ messageRequest.getSender());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
                }
                // incrementing the count
                rateLimiterService.incrementApiHitCount(SenderID);
            } else {
                messageResponse.setMessage("");
                messageResponse.setError("Sender Does not exist");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
            }

            boolean connection = phoneNumberService.findConnection(SenderID,messageRequest.getReceiver());

            /* Is sender connected to Receiver */
            if(!connection) {
                messageResponse.setMessage("");
                messageResponse.setError("receiver parameter not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
            }

            if(messageRequest.getText().trim().equals("STOP")){
                blockSenderForReceiver.setBlockUser(messageRequest.getSender(),messageRequest.getReceiver());
            }

            return ResponseEntity.ok(messageResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
        }
    }



    @PostMapping("/outbound/sms")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest messageRequest){
        System.out.println( messageRequest.toString());
        MessageResponse messageResponse = isInboundMessageValid(messageRequest);

        if(messageResponse.getError().equals("")) {

            Optional<Integer> id = accountService.findIDbyName(messageRequest.getSender());
            int SenderID ;
            if( id.isPresent() ){
                 SenderID = id.get() ;
                if (Integer.parseInt( rateLimiterService.getApiHitCount(SenderID)) > apiCountLimit ){
                    messageResponse.setMessage("");
                    messageResponse.setError("Limit reached for Sender "+ messageRequest.getSender());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
                }
                // incrementing the count
                rateLimiterService.incrementApiHitCount(SenderID);
            } else {
                messageResponse.setMessage("");
                messageResponse.setError("Sender Does not exist");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
            }

            if( blockSenderForReceiver.getBlockedUser(messageRequest.getSender()).equals( messageRequest.getReceiver()) ){

                messageResponse.setMessage("");
                messageResponse.setError("sms from "+ messageRequest.getSender() + " to "+ messageRequest.getReceiver() + " blocked by STOP request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
            }

            boolean connection = phoneNumberService.findConnection(SenderID,messageRequest.getReceiver());

            if(!connection) {
                messageResponse.setMessage("");
                messageResponse.setError("receiver parameter not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
            }
            return ResponseEntity.ok(messageResponse);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
        }
    }

    private MessageResponse isInboundMessageValid( MessageRequest messageRequest) {
        MessageResponse messageResponse = new MessageResponse();
        boolean error = false;
        if (messageRequest.getSender() == null || messageRequest.getSender().length() == 0 || messageRequest.getSender().trim().equals("")){
            messageResponse.setError("'Sender' is missing");
            error= true ;
        }else if (messageRequest.getReceiver() == null || messageRequest.getReceiver().length() == 0 || messageRequest.getReceiver().trim().equals("")) {
            messageResponse.setError( "'receiver is missing'" );
            error = true;
        } else if (messageRequest.getText() == null || messageRequest.getText().length() == 0 || messageRequest.getText().trim().equals("")) {
            messageResponse.setError( "'text' is missing");
            error= true;
        }

        if( !error ){

            if( messageRequest.getSender().length() < 6 || messageRequest.getSender().length()  > 16){
                error=true;
                messageResponse.setError("'sender' is invalid");
            }else if( messageRequest.getReceiver().length() < 6 || messageRequest.getReceiver().length()  > 16){
                messageResponse.setError("'receiver' is invalid");
                error=true;
            }else if( messageRequest.getText().length() < 1 || messageRequest.getText().length()  > 120){
                messageResponse.setError("'text' is invalid");
                error=true;
            }

        }

        if(!error){
            messageResponse.setMessage("inbound sms ok");
            messageResponse.setError("");
        }else {
            messageResponse.setMessage("");
        }
        return messageResponse ;
    }

}
