package com.github.kaiwinter.filemaker.excel.model;

public class Project {

   public String acronym;
   public String fullName;

   // Actual Excel row number
   public int excelRowNumber = -1;

   @Override
   public String toString() {
      return "Project [acronym=" + acronym + ", fullName=" + fullName + ", excelRowNumber=" + excelRowNumber + "]";
   }

}