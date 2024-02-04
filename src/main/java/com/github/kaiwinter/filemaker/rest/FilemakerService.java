package com.github.kaiwinter.filemaker.rest;

import java.io.IOException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaiwinter.filemaker.excel.model.Project;
import com.github.kaiwinter.filemaker.rest.model.CreateRecordRequest;
import com.github.kaiwinter.filemaker.rest.model.CreateRecordResponse;
import com.github.kaiwinter.filemaker.rest.model.GetLayoutsResponse;
import com.github.kaiwinter.filemaker.rest.model.TokenExpiredException;

import retrofit2.Response;

public class FilemakerService {
   private static final Logger LOGGER = LoggerFactory.getLogger(FilemakerService.class);

   private final String database;
   private final FilemakerApi apiClient;

   public FilemakerService(String database, String baseUrl, String token) {
      this.database = database;
      this.apiClient = ApiClientFactory.getClient(baseUrl, token).create(FilemakerApi.class);
   }

   public String createProject(Project project) throws IOException, TokenExpiredException {
      var data = CreateRecordRequest.fromProject(project);

      Response<CreateRecordResponse> response = apiClient.createProject(database, data).execute();
      if (response.isSuccessful()) {
         LOGGER.info("Created project with ID {}", data.fieldData.id);
         return data.fieldData.id;
      }
      LOGGER.error("Failed to create project '{}' (excel row {}), Response ({}): {}", project.acronym,
            project.excelRowNumber, response.code(), response.errorBody().string());
      if (response.code() == 401) {
         throw new TokenExpiredException();
      }
      return null;
   }

   public String getLayoutNames() throws IOException, TokenExpiredException {
      Response<GetLayoutsResponse> response = apiClient.getLayoutNames(database).execute();
      if (response.isSuccessful()) {
         String layoutNames = response.body().response.layouts.stream() //
               .map(layout -> layout.name) //
               .collect(Collectors.joining(","));
         LOGGER.info("Received layout names: {}", layoutNames);
         return layoutNames;
      }

      LOGGER.error("Failed to get layout names, Response ({}): {}", response.code(), response.errorBody().string());
      if (response.code() == 401) {
         throw new TokenExpiredException();
      }
      return null;
   }
}
