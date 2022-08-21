# Directory

Context
1. Overview
2. API
3. API Validation and features
4. Configuration


Overview
--------
It is a message system which has inbound and outbound SMS system


API
----

1. POST :: "/inbound/sms"
  - input parameter :   
    {
        "sender" : "azr1",
        "receiver" : "4924195509198",
        "text" : "Hello"
    }


2. POST :: "/outbound/sms"
  - input parameter :   
    {
        "sender" : "azr1",
        "receiver" : "4924195509198",
        "text" : "I am Good"
    }

API Validation and features
---------------------------

1. "/inbound/sms"  and "/outbound/sms/"

  - If required parameter is missing:
  {“message”: “”, “error”: “<parameter_name> is missing”}

  -  If parameter is invalid:
  {“message”: “”, “error”: “<parameter_name> is invalid”}

  - If ‘to’ is not found in the phone_number table for this account: 
  {“message”: “”, “error”: “to parameter not found”}

  - Any unexpected error:
  {“message”: “”, “error”: “unknown failure”}	

  - If all parameters are valid:
  {“message”: “inbound sms ok”, “error”: ””}	


2. STOP feature

  - When "/inbound/sms" When text is STOP or STOP\n or STOP\r or STOP\r\n 
  Then ‘from’ and ‘to’ pair is stored in cache as a unique entry and should expire after 4 hours.
  - Within this 4 hour if "/outbound/sms" is triggered for ame pair then error message should be thrown as
  {“message”: “”, “error”: “sms from <from> to <to> blocked by STOP request”}


3. Day Limit Feature

   A user can initiate 50 API call in a day. this can be configured variable "spring.api.hit.count=50"

Configuration
-------------
1. Redis is used for caching data.
2. Postgresql used as DataBase.
3. SpringBoot used for Application development.


