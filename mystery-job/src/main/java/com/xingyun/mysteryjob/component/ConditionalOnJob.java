package com.xingyun.mysteryjob.component;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJobCondition.class)
public @interface ConditionalOnJob {
}
