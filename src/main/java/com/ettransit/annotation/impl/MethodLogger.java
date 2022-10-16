package com.ettransit.annotation.impl;

import com.ettransit.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Ensures that method calls can be logged with entry-exit logs in console or log file.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class MethodLogger {

  /**
   * - visibility modifier is * (public, protected or private) - name is * (any name); - arguments
   * are .. (any arguments); and - is annotated with @Loggable.
   *
   * @param joinPoint the joinPoint
   * @return the log object
   * @throws Throwable if an error occurs
   */
  @Around("execution(* *(..)) && @annotation(loggable)")
  public Object log(final ProceedingJoinPoint joinPoint, final Loggable loggable) throws Throwable {

    var method = joinPoint.toShortString();
    var start = System.currentTimeMillis();

    switchStartingLogger(loggable.level(), method, joinPoint.getArgs());
    Object response = joinPoint.proceed();
    switchFinishingLogger(loggable.level(), method, response, start);

    return response;
  }

  private void switchStartingLogger(final String level, final String method, final Object args) {
    final String format = "=> Starting -  {} args: {}";

    switch (level) {
      case "warn" -> LOG.warn(format, method, args);
      case "error" -> LOG.error(format, method, args);
      case "debug" -> LOG.debug(format, method, args);
      case "trace" -> LOG.trace(format, method, args);
      default -> LOG.info(format, method, args);
    }
  }

  private void switchFinishingLogger(String level, String method, Object response, long start) {
    final String format = "<= {} : {} - Finished, duration: {} ms";
    final long duration = System.currentTimeMillis() - start;

    switch (level) {
      case "warn" -> LOG.warn(format, method, response, duration);
      case "error" -> LOG.error(format, method, response, duration);
      case "debug" -> LOG.debug(format, method, response, duration);
      case "trace" -> LOG.trace(format, method, response, duration);
      default -> LOG.info(format, method, response, duration);
    }
  }
}
