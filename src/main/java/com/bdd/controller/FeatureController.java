package com.bdd.controller;

import com.bdd.entity.FeatureFile;
import com.bdd.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    // Endpoint to upload Excel and save the feature file as JSON in MongoDB
    @PostMapping("/upload-excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            featureService.processExcelToFeatureFile(file);
            return ResponseEntity.ok("Excel processed and saved as JSON.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing Excel file: " + e.getMessage());
        }
    }

    // Endpoint to execute feature file from MongoDB
    @PostMapping("/execute-feature/{featureId}")
    public ResponseEntity<String> executeFeature(@PathVariable String featureId) {
        FeatureFile featureFile = null;
        try {
            featureFile = featureService.executeFeatureById(featureId);
            return ResponseEntity.ok(String.valueOf(featureFile));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
