package com.github.kaiwinter.filemaker.rest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kaiwinter.filemaker.excel.model.Project;
import com.github.kaiwinter.filemaker.rest.model.TokenExpiredException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class ApiTest {

   private MockWebServer server;

   @BeforeEach
   void before() {
      server = new MockWebServer();
   }

   @Test
   void code200() throws IOException, TokenExpiredException {
      server.enqueue(new MockResponse().setBody("{}").setResponseCode(200));
      Project project = new Project();
      project.acronym = "MOT";
      project.excelRowNumber = 1;
      String result = new FilemakerService("test", "http://localhost:" + server.getPort(), "abc")
            .createProject(project);
      assertNotNull(result);
   }

   @Test
   void code401() throws IOException, TokenExpiredException {
      server.enqueue(new MockResponse().setBody("{status: \"failed\"}").setResponseCode(401));
      Project project = new Project();
      project.acronym = "MOT";
      project.excelRowNumber = 1;
      assertThatThrownBy(() -> {
         new FilemakerService("test", "http://localhost:" + server.getPort(), "abc").createProject(project);
      }).isInstanceOf(TokenExpiredException.class);
   }

   @Test
   void code500() throws IOException, TokenExpiredException {
      server.enqueue(new MockResponse().setBody("{status: \"failed\"}").setResponseCode(500));
      Project project = new Project();
      project.acronym = "MOT";
      project.excelRowNumber = 1;
      String result = new FilemakerService("test", "http://localhost:" + server.getPort(), "abc")
            .createProject(project);
      assertNull(result);
   }

   @AfterEach
   void after() throws IOException {
      server.shutdown();
   }

}
