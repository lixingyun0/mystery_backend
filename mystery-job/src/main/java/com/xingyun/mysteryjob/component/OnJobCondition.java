package com.xingyun.mysteryjob.component;

import com.xingyun.mysteryjob.common.EnvHolder;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class OnJobCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        RedisClient redisClient = null;
        try {
            String springEnv = EnvHolder.SPRING_ENV;
            System.out.println("====================="+EnvHolder.SPRING_ENV);
            YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
            if (StringUtils.isBlank(springEnv)){
                yamlProFb.setResources(new ClassPathResource("application.yml"));
                springEnv = yamlProFb.getObject().getProperty("spring.profiles.active");
            }
            yamlProFb.setResources(new ClassPathResource("application-"+springEnv+".yml"));
            Properties properties = yamlProFb.getObject();

            RedisURI redisUri = RedisURI.builder()
                    .withHost(properties.getProperty("spring.redis.host"))
                    .withPort(Integer.parseInt(properties.getProperty("spring.redis.port")))
                    .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                    .withPassword(properties.getProperty("spring.redis.password"))
                    .withDatabase(Integer.parseInt(properties.getProperty("spring.redis.database")))
                    .build();

            redisClient = RedisClient.create(redisUri);
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisCommands<String, String> sync = connect.sync();
            String jobProcessing = sync.get("job_processing");
            boolean match = StringUtils.isBlank(jobProcessing);
            ConditionMessage message = ConditionMessage.forCondition(ConditionalOnJava.class, "Process job")
                    .because("Job not on processing");
            return new ConditionOutcome(match, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            redisClient.shutdown();
        }

    }

}
