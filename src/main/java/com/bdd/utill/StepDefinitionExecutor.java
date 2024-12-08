package com.bdd.utill;

import com.bdd.stepdef.StepDefinitions;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class StepDefinitionExecutor {

    private final StepDefinitions stepDefinitions;

    public StepDefinitionExecutor() {
        this.stepDefinitions = new StepDefinitions();
    }

    public void executeStep(String stepDescription) {
        try {
            // Find the appropriate method in StepDefinitions
            Method[] methods = stepDefinitions.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(io.cucumber.java.en.Given.class) ||
                    method.isAnnotationPresent(io.cucumber.java.en.When.class) ||
                    method.isAnnotationPresent(io.cucumber.java.en.Then.class)) {

                    String annotationValue = getAnnotationValue(method);
                    if (annotationValue != null && stepDescription.contains(annotationValue)) {
                        method.invoke(stepDefinitions);
                        System.out.println("Executed: " + stepDescription);
                        return;
                    }
                }
            }
            throw new RuntimeException("No matching step definition found for: " + stepDescription);
        } catch (Exception e) {
            throw new RuntimeException("Error executing step: " + stepDescription, e);
        }
    }

    private String getAnnotationValue(Method method) {
        if (method.isAnnotationPresent(io.cucumber.java.en.Given.class)) {
            return method.getAnnotation(io.cucumber.java.en.Given.class).value();
        } else if (method.isAnnotationPresent(io.cucumber.java.en.When.class)) {
            return method.getAnnotation(io.cucumber.java.en.When.class).value();
        } else if (method.isAnnotationPresent(io.cucumber.java.en.Then.class)) {
            return method.getAnnotation(io.cucumber.java.en.Then.class).value();
        }
        return null;
    }
}
