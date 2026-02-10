package com.example.aop;

import com.example.exception.TransferException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {

    @Pointcut("execution(* com.example.service..*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info(">>> Entering: {} | Args: {}", methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("<<< Exiting:  {} | Result: SUCCESS", methodName);
            return result;

        } catch (Throwable ex) {
            if (ex instanceof TransferException te) {
                log.error("<<< Exiting:  {} | Business Error: {}", methodName, te.getMessage());
            } else {
                log.error("<<< Exiting:  {} | System Error: {}", methodName, ex.getMessage());
            }
            throw ex;
        }
    }
}
