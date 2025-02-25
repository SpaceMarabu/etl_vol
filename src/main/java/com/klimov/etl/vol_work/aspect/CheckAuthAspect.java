package com.klimov.etl.vol_work.aspect;

import com.klimov.etl.vol_work.entity.MainScreenStateService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

//@Component
//@Aspect
public class CheckAuthAspect {

    @Pointcut("execution(* com.klimov.etl.vol_work.controller.MainController.controlPanel(..))")
    public void controlPanel() {
    }

    @Pointcut("execution(* com.klimov.etl.vol_work.controller.MainController.add*(..))")
    public void allAddMethods() {
    }

    @Pointcut("execution(* com.klimov.etl.vol_work.controller.MainController.set*(..))")
    public void allSetMethods() {
    }

    @Pointcut("controlPanel() || allAddMethods() || allSetMethods()")
    public void needAuthMethods() {
    }

    @Around("needAuthMethods()")
    public Object aroundAllNeedAuthMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof MainScreenStateService) {
                MainScreenStateService screenState = (MainScreenStateService) arg;
                if (screenState.isSignedIn()) {
                    return joinPoint.proceed();
                } else {
                    System.out.println(joinPoint.getSignature().getName());
                    return "redirect:/";
                }
            }
        }

        throw new RuntimeException("Аспект вызван не на целевом методе. Целевой имеет на входе MainScreenState");
    }
}
