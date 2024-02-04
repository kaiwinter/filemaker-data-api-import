package com.github.kaiwinter.filemaker.rest.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CreateRecordResponse {

   @SerializedName("response")
   public Response response;

   @SerializedName("messages")
   public List<Message> messages;

   public static class Response {

      @SerializedName("recordId")
      public String recordId;

      @SerializedName("modId")
      public String modId;
   }

   public static class Message {

      @SerializedName("code")
      public String code;

      @SerializedName("message")
      public String message;
   }
}
