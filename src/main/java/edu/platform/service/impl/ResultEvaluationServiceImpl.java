package edu.platform.service.impl;

import edu.platform.entity.enums.AttemptStatus;
import edu.platform.service.ResultEvaluationService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ResultEvaluationServiceImpl implements ResultEvaluationService {

    @Value("${quiz.result.pass-threshold:0.6}")
    private BigDecimal passThreshold; // 60% default

    private final CircuitBreaker cb = CircuitBreaker.ofDefaults("resultEvaluator");
    private final TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public AttemptStatus evaluate(BigDecimal score, BigDecimal maxScore) {
        Callable<AttemptStatus> task = () -> {
            if (score == null || maxScore == null || maxScore.compareTo(BigDecimal.ZERO) <= 0) {
                return AttemptStatus.FAILED;
            }
            BigDecimal ratio = score.divide(maxScore, 4, java.math.RoundingMode.HALF_UP);
            return ratio.compareTo(passThreshold) >= 0 ? AttemptStatus.PASSED : AttemptStatus.FAILED;
        };
        try {
            Callable<AttemptStatus> withCb = CircuitBreaker.decorateCallable(cb, task);
            return timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
                try {
                    return withCb.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor));
        } catch (Exception e) {
            // Fallback to FAILED to avoid blocking course operations
            return AttemptStatus.FAILED;
        }
    }
}
