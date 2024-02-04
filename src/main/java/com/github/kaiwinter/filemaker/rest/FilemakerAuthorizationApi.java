package com.github.kaiwinter.filemaker.rest;

import com.github.kaiwinter.filemaker.rest.model.GetTokenResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FilemakerAuthorizationApi {
   @Headers("Content-Type: application/json")
   @POST("/fmi/data/v2/databases/{database}/sessions")
   Call<GetTokenResponse> getToken(@Path("database") String database, @Header("Authorization") String fmid);
}
