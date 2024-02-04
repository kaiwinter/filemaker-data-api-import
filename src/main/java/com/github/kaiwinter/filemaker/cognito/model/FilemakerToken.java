package com.github.kaiwinter.filemaker.cognito.model;

import org.apache.poi.util.StringUtil;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;

public class FilemakerToken {

   private String idToken;
   private String refreshToken;
   private String bearerToken;

   public FilemakerToken(String idToken, String refreshToken, String bearerToken) {
      this.idToken = idToken;
      this.refreshToken = refreshToken;
      this.bearerToken = bearerToken;
   }

   public String getIdToken() {
      return idToken;
   }

   public void setIdToken(String idToken) {
      this.idToken = idToken;
   }

   public String getRefreshToken() {
      return refreshToken;
   }

   public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
   }

   public String getBearerToken() {
      return bearerToken;
   }

   public void setBearerToken(String bearerToken) {
      this.bearerToken = bearerToken;
   }

   public static FilemakerToken from(AuthenticationResultType result) {
      return new FilemakerToken(result.getIdToken(), result.getRefreshToken(), null);
   }

   @Override
   public String toString() {
      return "FilemakerToken [\nidToken=" + idToken + ", \nrefreshToken=" + refreshToken + ", \nbearerToken="
            + bearerToken + "\n]";
   }

   public boolean isEmpty() {
      return StringUtil.isBlank(idToken) || StringUtil.isBlank(refreshToken) || StringUtil.isBlank(bearerToken);
   }

}
