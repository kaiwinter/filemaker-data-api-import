package com.github.kaiwinter.filemaker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads config.properties and provides getters for its values.
 */
public class ImportProperties {

   private static final Logger LOGGER = LoggerFactory.getLogger(ImportProperties.class);

   private final String poolId;
   private final String clientAppId;
   private final String region;
   private final String username;
   private final String password;
   private final String hostname;
   private final String database;

   public ImportProperties(String configFilename) {
      LOGGER.info("Reading properties file {}", configFilename);
      try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilename)) {
         Properties properties = new Properties();

         // load properties file
         properties.load(input);

         // Read the property values
         poolId = properties.getProperty("POOL_ID");
         clientAppId = properties.getProperty("CLIENTAPP_ID");
         region = properties.getProperty("REGION");
         username = properties.getProperty("USERNAME");
         password = properties.getProperty("PASSWORD");
         hostname = properties.getProperty("HOSTNAME");
         database = properties.getProperty("DATABASE");

         requireNonEmpty(poolId);
         requireNonEmpty(clientAppId);
         requireNonEmpty(region);
         requireNonEmpty(username);
         requireNonEmpty(password);
         requireNonEmpty(hostname);
         requireNonEmpty(database);

      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void requireNonEmpty(String string) {
      if (string == null || string.isBlank()) {
         throw new IllegalArgumentException("Property must not be empty");
      }
   }

   public String getPoolId() {
      return poolId;
   }

   public String getClientAppId() {
      return clientAppId;
   }

   public String getRegion() {
      return region;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public String getHostname() {
      return hostname;
   }

   public String getDatabase() {
      return database;
   }

}
