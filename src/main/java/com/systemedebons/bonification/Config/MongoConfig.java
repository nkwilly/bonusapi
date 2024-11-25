package com.systemedebons.bonification.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
/**
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    protected String getDatabaseName() {
        return "bonusapi";
    }
}
 */
