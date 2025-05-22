package com.questionpro.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    private String name;
    private String email;
    private Map<Long, String> answers; // questionId -> userAnswer

    
}