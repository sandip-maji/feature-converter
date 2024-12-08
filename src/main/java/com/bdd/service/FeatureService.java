package com.bdd.service;

import com.bdd.entity.FeatureFile;
import com.bdd.repository.FeatureRepository;
import com.bdd.utill.StepDefinitionExecutor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private StepDefinitionExecutor stepDefinitionExecutor;


    public FeatureFile executeFeatureById(String featureId) {
        // Fetch the feature file from MongoDB
        FeatureFile featureFile = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature file not found for id: " + featureId));

        // Execute each scenario in the feature file
        for (Map<String, Object> scenario : featureFile.getFeatureData()) {
            String feature = (String) scenario.get("feature");
            String scenarioOutline = (String) scenario.get("scenarioOutline");
            String given = (String) scenario.get("when"); // Represents the "Given" step
            String then = (String) scenario.get("then"); // Represents the "Then" step
            String examples = (String) scenario.get("examples");

            System.out.println("Executing Feature: " + feature);
            System.out.println("Scenario: " + scenarioOutline);

            // Execute the "Given" and "Then" steps
            stepDefinitionExecutor.executeStep(given);
            stepDefinitionExecutor.executeStep(then);

            if (examples != null && !examples.isEmpty()) {
                System.out.println("Examples: " + examples);
            }
        }

        return featureFile;
    }

    // Method to process Excel file and convert it to JSON format
    public void processExcelToFeatureFile(MultipartFile file) throws IOException {
        // Convert the Excel file into a Map format (representing the feature file)
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Map<String, Object>> featureData = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            Map<String, Object> scenarioData = new HashMap<>();

            // Safely get each cell, and handle null or empty cells
            scenarioData.put("feature", getCellValue(row.getCell(0)));
            scenarioData.put("scenarioOutline", getCellValue(row.getCell(1)));
            scenarioData.put("when", getCellValue(row.getCell(2)));
            scenarioData.put("then", getCellValue(row.getCell(3)));
            scenarioData.put("examples", getCellValue(row.getCell(4)));

            featureData.add(scenarioData);
        }

        // Convert the data into FeatureFile object (using JSON format)
        FeatureFile featureFile = new FeatureFile();
        featureFile.setFeatureData(featureData);

        // Save the feature file into MongoDB
        featureRepository.save(featureFile);
    }

    // Utility method to get a cell's value as a string, handling nulls and different types
    private String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";  // Return empty string if cell is null
        }

        // Check the cell type and extract the value accordingly
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";  // Return empty string for other types or unsupported cells
        };
    }


    // Method to execute a feature file by its ID
    public FeatureFile getFeatureById(String featureId) {
        FeatureFile featureFile = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        // Logic to execute the step definitions based on the stored feature file (JSON)
        // This can be enhanced by using Cucumber's Java API to trigger specific steps.
        System.out.println("Executing feature: " + featureFile);

        return featureFile;
    }
}
