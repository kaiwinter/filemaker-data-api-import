package com.github.kaiwinter.filemaker.rest.model;

import java.util.List;

public class GetTokenResponse {

   public Response response;
   public List<Message> messages;

   public class Response {
      public String token;
   }

   public class Message {
      public String code;
      public String message;
   }
}
