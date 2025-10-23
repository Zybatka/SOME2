package edu.platform.media.storage;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.*;

import org.springframework.context.annotation.Primary;

@Component
@Primary
@RequiredArgsConstructor
public class ResilientFileStorage implements FileStorage {

    private final LocalFileStorage delegate;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final CircuitBreaker cb = CircuitBreaker.ofDefaults("fileStorage");
    private final Retry retry = Retry.ofDefaults("fileStorage");
    private final TimeLimiter timeLimiter = TimeLimiter.of(java.time.Duration.ofSeconds(3));

    @Override
    public String save(String originalFileName, String contentType, long size, InputStream contentStream) {
        Callable<String> base = () -> delegate.save(originalFileName, contentType, size, contentStream);
        Callable<String> withCb = CircuitBreaker.decorateCallable(cb, base);
        Callable<String> withRetry = Retry.decorateCallable(retry, withCb);
        try {
            return timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
                try {
                    return withRetry.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor));
        } catch (Exception e) {
            throw new RuntimeException("Resilient save failed", e);
        }
    }

    @Override
    public InputStream load(String storagePath) {
        Callable<InputStream> base = () -> delegate.load(storagePath);
        Callable<InputStream> withCb = CircuitBreaker.decorateCallable(cb, base);
        Callable<InputStream> withRetry = Retry.decorateCallable(retry, withCb);
        try {
            return timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
                try {
                    return withRetry.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor));
        } catch (Exception e) {
            throw new RuntimeException("Resilient load failed", e);
        }
    }

    @Override
    public void delete(String storagePath) {
        Callable<Void> base = () -> { delegate.delete(storagePath); return null; };
        Callable<Void> withCb = CircuitBreaker.decorateCallable(cb, base);
        Callable<Void> withRetry = Retry.decorateCallable(retry, withCb);
        try {
            timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
                try {
                    return withRetry.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor));
        } catch (Exception e) {
            throw new RuntimeException("Resilient delete failed", e);
        }
    }
}
