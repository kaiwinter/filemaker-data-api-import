package com.github.kaiwinter.filemaker.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaiwinter.filemaker.rest.model.GetTokenResponse;

import retrofit2.Response;

public class FilemakerAuthorizationService {
   private static final Logger LOGGER = LoggerFactory.getLogger(FilemakerAuthorizationService.class);

   private final String database;
   private final FilemakerAuthorizationApi apiClient;

   public FilemakerAuthorizationService(String database, String baseUrl) {
      this.database = database;
      this.apiClient = ApiClientFactory.getClient(baseUrl, null).create(FilemakerAuthorizationApi.class);
   }

   public String getToken(String fmid) throws IOException {
      Response<GetTokenResponse> response = apiClient.getToken(database, fmid).execute();
      if (response.isSuccessful()) {
         return response.body().response.token;
      }

      LOGGER.error("Failed to get token, Response ({}): {}", response.code(), response.errorBody().string());
      return null;
   }
}
