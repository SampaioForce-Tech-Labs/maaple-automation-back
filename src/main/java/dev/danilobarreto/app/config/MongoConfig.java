package dev.danilobarreto.app.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient) {
        return GridFSBuckets.create(mongoClient.getDatabase("automatizador"));
    }

    @Bean
    public dev.danilobarreto.app.model.mongoDB.Contrato contrato() {
        return new dev.danilobarreto.app.model.mongoDB.Contrato();
    }

}
