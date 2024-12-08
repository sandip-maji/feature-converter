package com.bdd.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "feature_files")
public class FeatureFile {

    @Id
    private String id;
    private List<Map<String, Object>> featureData;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<String, Object>> getFeatureData() {
        return featureData;
    }

    public void setFeatureData(List<Map<String, Object>> featureData) {
        this.featureData = featureData;
    }

    @Override
    public String toString() {
        return "FeatureFile{" + "id='" + id + '\'' +
                ", featureData=" + featureData +
                '}';
    }
}
