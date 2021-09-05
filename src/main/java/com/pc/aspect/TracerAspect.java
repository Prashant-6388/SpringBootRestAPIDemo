package com.pc.aspect;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracerAspect {

    @Around("@annotation(com.pc.annotation.Trace)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Trace annotation invoked...");

        Object[] args = joinPoint.getArgs();
        System.out.println("Args = ");
        Arrays.stream(args).forEach(System.out::println);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String nameGivenWithAnnotation = signature.getMethod().getName();
        System.out.println("Name = "+nameGivenWithAnnotation);

        Object result = joinPoint.proceed();
        System.out.println("Trace annotation completed....");
        return result;
    }

}
