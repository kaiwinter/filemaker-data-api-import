package com.github.kaiwinter.filemaker.rest.model;

import java.util.UUID;

import com.github.kaiwinter.filemaker.excel.model.Project;
import com.github.kaiwinter.filemaker.rest.model.CreateRecordRequest.IFieldData;
import com.google.gson.annotations.SerializedName;

public class CreateRecordRequest<T extends IFieldData> {

   @SerializedName("fieldData")
   public final T fieldData;

   public CreateRecordRequest(T fieldData) {
      this.fieldData = fieldData;
   }

   public static CreateRecordRequest<ProjectFieldData> fromProject(Project project) {
      return new CreateRecordRequest<>(ProjectFieldData.fromProject(project));
   }

   interface IFieldData {
   }

   public static class ProjectFieldData implements IFieldData {

      @SerializedName("id")
      public String id;

      @SerializedName("project_id")
      public String projectId;

      @SerializedName("name")
      public String name;

      private static ProjectFieldData fromProject(Project project) {

         ProjectFieldData projectFieldData = new ProjectFieldData();
         projectFieldData.id = UUID.randomUUID().toString();
         projectFieldData.projectId = project.acronym;
         projectFieldData.name = project.fullName;

         return projectFieldData;
      }
   }

}
