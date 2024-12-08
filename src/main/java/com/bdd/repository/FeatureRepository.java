package com.bdd.repository;

import com.bdd.entity.FeatureFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeatureRepository extends MongoRepository<FeatureFile, String> {
}
