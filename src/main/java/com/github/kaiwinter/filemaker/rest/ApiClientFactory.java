package com.github.kaiwinter.filemaker.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientFactory {
   private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientFactory.class);

   private ApiClientFactory() {
   }

   /**
    * 
    * @param baseUrl
    * @param token   bearer token, if null it is not added to the header
    * @return
    */
   public static Retrofit getClient(String baseUrl, String token) {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOGGER::debug);

      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      Builder clientBuilder = new OkHttpClient.Builder();

      if (token != null) {
         clientBuilder.addInterceptor(new Interceptor() {

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
               Request authorisedRequest = chain.request().newBuilder() //
                     .addHeader("Authorization", "Bearer " + token) //
                     .build();
               return chain.proceed(authorisedRequest);
            }

         });
      }
      OkHttpClient client = clientBuilder.addInterceptor(interceptor).build();

      Retrofit retrofit = new Retrofit.Builder() //
            .baseUrl(baseUrl) //
            .client(client) //
            .addConverterFactory(GsonConverterFactory.create()) //
            .build();

      return retrofit;
   }

}
