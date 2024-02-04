package com.github.kaiwinter.filemaker.rest;

import com.github.kaiwinter.filemaker.rest.model.CreateRecordRequest;
import com.github.kaiwinter.filemaker.rest.model.CreateRecordRequest.ProjectFieldData;
import com.github.kaiwinter.filemaker.rest.model.CreateRecordResponse;
import com.github.kaiwinter.filemaker.rest.model.GetLayoutsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FilemakerApi {

   @Headers("Content-Type: application/json")
   @POST("/fmi/data/v2/databases/{database}/layouts/projects/records/")
   Call<CreateRecordResponse> createProject(@Path("database") String database,
         @Body CreateRecordRequest<ProjectFieldData> data);

   @GET("/fmi/data/v2/databases/{database}/layouts")
   Call<GetLayoutsResponse> getLayoutNames(@Path("database") String database);
}
