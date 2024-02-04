package com.github.kaiwinter.filemaker.cognito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.github.kaiwinter.filemaker.ImportProperties;
import com.github.kaiwinter.filemaker.cognito.model.FilemakerToken;
import com.github.kaiwinter.filemaker.cognito.model.RefreshTokenExpiredException;

/**
 * The CognitoHelper class abstracts the functionality of connecting to the
 * Cognito user pool and Federated Identities.
 */
public class CognitoHelper {
   private static final Logger LOGGER = LoggerFactory.getLogger(CognitoHelper.class);

   private static final String TOKEN_FILENAME = "token.properties";
   private static final String ID_TOKEN_PROPERTY = "ID_TOKEN";
   private static final String REFRESH_TOKEN_PROPERTY = "REFRESH_TOKEN";
   private static final String BEARER_TOKEN_PROPERTY = "BEARER_TOKEN";

   private final ImportProperties importProperties;

   public CognitoHelper(ImportProperties importProperties) {
      this.importProperties = importProperties;
   }

   public static FilemakerToken loadToken() {
      File file = new File(TOKEN_FILENAME);
      LOGGER.info("Token filename: {}", file.getAbsolutePath());
      try (InputStream input = new FileInputStream(file)) {
         Properties prop = new Properties();

         prop.load(input);

         FilemakerToken token = new FilemakerToken(prop.getProperty(ID_TOKEN_PROPERTY),
               prop.getProperty(REFRESH_TOKEN_PROPERTY), prop.getProperty(BEARER_TOKEN_PROPERTY));
         return token;
      } catch (FileNotFoundException e) {
         return null;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void saveToken(FilemakerToken filemakerToken) {
      try (FileOutputStream fileOutputStream = new FileOutputStream(TOKEN_FILENAME)) {
         Properties prop = new Properties();
         if (filemakerToken.getIdToken() != null) {
            prop.setProperty(ID_TOKEN_PROPERTY, filemakerToken.getIdToken());
         }

         if (filemakerToken.getRefreshToken() != null) {
            prop.setProperty(REFRESH_TOKEN_PROPERTY, filemakerToken.getRefreshToken());
         }

         if (filemakerToken.getBearerToken() != null) {
            prop.setProperty(BEARER_TOKEN_PROPERTY, filemakerToken.getBearerToken());
         }
         prop.store(fileOutputStream, "");
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Helper method to validate the user
    *
    * @return returns the JWT token after the validation
    */
   public AuthenticationResultType loginUser() {
      AuthenticationHelper helper = new AuthenticationHelper(importProperties.getPoolId(),
            importProperties.getClientAppId());
      return helper.performSRPAuthentication(importProperties.getUsername(), importProperties.getPassword(),
            importProperties.getRegion());
   }

   /**
    * Uses the Cognito refresh token to get a new id token.
    * 
    * <strong>Attention: The refresh token in the answer will be null!</strong>
    * 
    * @param refreshToken
    * @return
    * @throws RefreshTokenExpiredException
    */
   public AuthenticationResultType refreshToken(String refreshToken) throws RefreshTokenExpiredException {
      AuthenticationHelper helper = new AuthenticationHelper(importProperties.getPoolId(),
            importProperties.getClientAppId());
      return helper.performTokenRequest(importProperties.getUsername(), refreshToken, importProperties.getRegion());
   }
}
