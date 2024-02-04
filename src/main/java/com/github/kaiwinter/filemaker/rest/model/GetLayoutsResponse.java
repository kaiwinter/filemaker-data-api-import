package com.github.kaiwinter.filemaker.rest.model;

import java.util.List;

public class GetLayoutsResponse {
   public Response response;
   public List<Message> messages;

   public static class Response {
      public List<Layout> layouts;
   }

   public static class Message {
      public String code;
      public String message;
   }

   public class Layout {
      public String name;
      public String table;
   }
}
