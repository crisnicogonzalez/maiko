package com.lemon.maiko;

import com.lemon.maiko.core.services.impl.MessageServiceImpl;
import com.lemon.maiko.health.HealthCheckImpl;
import com.lemon.maiko.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        environment.jersey().register(new MessageResource(new MessageServiceImpl()));
        environment.healthChecks().register("template", new HealthCheckImpl());
    }

}