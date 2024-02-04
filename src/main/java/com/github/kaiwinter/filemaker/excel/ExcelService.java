package com.github.kaiwinter.filemaker.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaiwinter.filemaker.excel.model.Project;

public class ExcelService {

   private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

   private final String excelFile;

   public ExcelService(String excelFile) {
      this.excelFile = excelFile;
   }

   public List<Project> parse() throws IOException {
      try (InputStream file = getClass().getClassLoader().getResourceAsStream(excelFile);
            XSSFWorkbook workbook = new XSSFWorkbook(file);) {
         XSSFSheet sheet = workbook.getSheetAt(0);

         List<Project> projects = new ArrayList<>();
         // Iterate through each rows one by one
         Iterator<Row> rowIterator = sheet.iterator();

         // Skip first row
         rowIterator.next();
         while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Project project = parseRow(row);
            projects.add(project);
         }
         LOGGER.info("Parsed {} project(s)", projects.size());
         return projects;
      }
   }

   private Project parseRow(Row row) {
      Project project = new Project();
      project.excelRowNumber = row.getRowNum();
      project.acronym = getCellStringValue(row.getCell(0));
      project.fullName = getCellStringValue(row.getCell(1));

      return project;
   }

   private String getCellStringValue(Cell cell) {
      if (cell == null) {
         return "";
      }
      switch (cell.getCellType()) {
      case NUMERIC:
         return cell.getNumericCellValue() + " ";
      case STRING:
         return cell.getStringCellValue() + " ";
      case BLANK:
         return "";
      default:
         return cell.getCellType().name();
      }
   }

}
