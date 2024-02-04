package com.github.kaiwinter.filemaker.rest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.kaiwinter.filemaker.rest.model.CreateRecordRequest.ProjectFieldData;
import com.google.gson.Gson;

class GsonTest {

   @Test
   void projectFieldData() {
      ProjectFieldData projectFieldData = new ProjectFieldData();
      projectFieldData.id = "UUID";
      projectFieldData.projectId = "projectId";
      projectFieldData.name = "name";
      var request = new CreateRecordRequest<>(projectFieldData);
      String json = new Gson().toJson(request);
      assertEquals("""
            {"fieldData":{"id":"UUID","project_id":"projectId","name":"name"}}""", json);
   }

}
