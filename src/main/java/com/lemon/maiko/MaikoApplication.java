package com.lemon.maiko;

import com.lemon.maiko.client.impl.FoaasClientImpl;
import com.lemon.maiko.client.impl.JerseyRestClientImpl;
import com.lemon.maiko.filter.service.impl.ApiRateLimitServiceImpl;
import com.lemon.maiko.core.services.impl.MessageServiceImpl;
import com.lemon.maiko.filter.service.impl.RedisLockServiceImpl;
import com.lemon.maiko.filter.service.impl.UserAccessLogRedisServiceImpl;
import com.lemon.maiko.filter.RequestsRateLimiterFilter;
import com.lemon.maiko.health.HealthCheckImpl;
import com.lemon.maiko.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MaikoApplication extends Application<MaikoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MaikoApplication().run(args);
    }

    @Override
    public String getName() {
        return "maiko";
    }

    @Override
    public void initialize(final Bootstrap<MaikoConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                ));
    }

    @Override
    public void run(final MaikoConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        environment.jersey().register(new MessageResource(new MessageServiceImpl(new FoaasClientImpl("some host", new JerseyRestClientImpl("http://foaas.com")))));
        environment.healthChecks().register("template", new HealthCheckImpl());

        RedissonClient redisConn = this.createRedisConn();
        environment.servlets().addFilter("RequestsRateLimiterFilter", new RequestsRateLimiterFilter(new ApiRateLimitServiceImpl(new UserAccessLogRedisServiceImpl(redisConn), 5, new RedisLockServiceImpl(redisConn))))
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }


    private RedissonClient createRedisConn() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

}
