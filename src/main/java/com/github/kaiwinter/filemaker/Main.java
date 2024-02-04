package com.github.kaiwinter.filemaker;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.github.kaiwinter.filemaker.cognito.CognitoHelper;
import com.github.kaiwinter.filemaker.cognito.model.FilemakerToken;
import com.github.kaiwinter.filemaker.cognito.model.RefreshTokenExpiredException;
import com.github.kaiwinter.filemaker.excel.ExcelService;
import com.github.kaiwinter.filemaker.excel.model.Project;
import com.github.kaiwinter.filemaker.rest.FilemakerAuthorizationService;
import com.github.kaiwinter.filemaker.rest.FilemakerService;
import com.github.kaiwinter.filemaker.rest.model.TokenExpiredException;

public class Main {
   private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

   private static final ImportProperties IMPORT_PROPERTIES = new ImportProperties("config.properties");

   public static void main(String[] args) throws IOException {

      // Load projects from Excel file
      List<Project> projects = new ExcelService("Projects.xlsx").parse();

      // Assure we are logged in
      runLoggedIn(filemakerService -> {
         // Print existing Filemaker layouts
         filemakerService.getLayoutNames();

         // Create projects according to Excel file
         for (Project project : projects) {
            filemakerService.createProject(project);
         }
      });
   }

   /**
    * A runnable to be executed when a valid login exists.
    */
   public interface FilemakerRunnable {
      public void run(FilemakerService filemakerService) throws IOException, TokenExpiredException;
   }

   /**
    * Assures a valid login and runs the {@link FilemakerRunnable}.
    * 
    * @param runnable
    */
   private static void runLoggedIn(FilemakerRunnable runnable) throws IOException {
      FilemakerToken token = CognitoHelper.loadToken();
      if (token == null || token.isEmpty()) {
         LOGGER.info("Token missing");
         token = cognitoLogin();
      }

      if (token == null || token.getBearerToken() == null) {
         LOGGER.error("Token failure: {}", token);
         return;
      }

      FilemakerService filemakerService = new FilemakerService(IMPORT_PROPERTIES.getDatabase(),
            IMPORT_PROPERTIES.getHostname(), token.getBearerToken());
      try {
         runnable.run(filemakerService);
      } catch (TokenExpiredException e) {
         LOGGER.warn("Token expired, trying to get a new one");
         cognitoRefreshLogin(token);

         runLoggedIn(runnable);
      }
   }

   /**
    * Executes an initial Cognito login.
    * 
    * @return a {@link FilemakerToken}
    */
   private static FilemakerToken cognitoLogin() throws IOException {
      LOGGER.info("Starting Cognito authorization");
      AuthenticationResultType loginUser = new CognitoHelper(IMPORT_PROPERTIES).loginUser();
      FilemakerToken token = FilemakerToken.from(loginUser);
      CognitoHelper.saveToken(token);
      return getBearerToken(token);
   }

   /**
    * Refreshes the Filemaker token by using an existing refresh token.
    * 
    * @param token the existing (expired) token
    * @return a {@link FilemakerToken}
    */
   private static FilemakerToken cognitoRefreshLogin(FilemakerToken token) throws IOException {
      LOGGER.info("Refreshing Cognito authorization");
      try {
         AuthenticationResultType loginUser = new CognitoHelper(IMPORT_PROPERTIES)
               .refreshToken(token.getRefreshToken());
         token.setIdToken(loginUser.getIdToken());
         LOGGER.info("Refreshing Cognito authorization, result: {}", token);
         CognitoHelper.saveToken(token);
         return getBearerToken(token);
      } catch (RefreshTokenExpiredException e2) {
         LOGGER.warn("Refresh token expired, trying to relogin");
         return cognitoLogin();
      }
   }

   /**
    * Loads the bearer token for the passed FilemakerToken.
    * 
    * @param token the existing token (with missing bearer token)
    * @return the existing token (with loaded bearer token)
    */
   private static FilemakerToken getBearerToken(FilemakerToken token) throws IOException {
      LOGGER.info("Getting bearer token from FileMaker");
      FilemakerAuthorizationService filemakerAuthorizationService = new FilemakerAuthorizationService(
            IMPORT_PROPERTIES.getDatabase(), IMPORT_PROPERTIES.getHostname());
      String bearerToken = filemakerAuthorizationService.getToken("FMID " + token.getIdToken());
      if (bearerToken == null) {
         LOGGER.error("failure");
      } else {
         LOGGER.info("success");
      }

      token.setBearerToken(bearerToken);
      CognitoHelper.saveToken(token);
      return token;
   }

}
