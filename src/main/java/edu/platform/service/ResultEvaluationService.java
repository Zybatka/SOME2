package edu.platform.service;

import edu.platform.entity.enums.AttemptStatus;

import java.math.BigDecimal;

public interface ResultEvaluationService {
    AttemptStatus evaluate(BigDecimal score, BigDecimal maxScore);
}

