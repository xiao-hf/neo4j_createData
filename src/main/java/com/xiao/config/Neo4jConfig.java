package com.xiao.config;

import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {
    @Value("${neo4j.username}")
    String username;

    @Value("${neo4j.password}")
    String password;

    @Value("${neo4j.uri}")
    String uri;

    @Bean
    public Session getSession() {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        SessionConfig sessionConfig = SessionConfig.defaultConfig();
        return driver.session(sessionConfig);
    }
}