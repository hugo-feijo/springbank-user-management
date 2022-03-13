package com.springbank.user.core.configuration;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoSettingsFactory;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.RevisionResolver;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class AxonConfig {
    @Value("${spring.data.mongodb.host:127.0.0.1}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:user}")
    private String mongoDatabase;

    @Bean
    public MongoClient mongo() {
        var mongoFactory = new MongoFactory();
        var mongoSettingFactory = new MongoSettingsFactory();
        mongoSettingFactory.setMongoAddresses(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)));
        mongoFactory.setMongoClientSettings(mongoSettingFactory.createMongoClientSettings());

        return mongoFactory.createMongo();
    }

    @Bean
    public MongoTemplate axonMongoTemplate() {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongo(), mongoDatabase)
                .build();
    }
    @Bean
    XStream xstream(){
        XStream xstream = new XStream();
        xstream.setClassLoader(ClassUtils.getDefaultClassLoader());
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.allowTypeHierarchy(Collection.class);
        xstream.allowTypesByWildcard(new String[] {
                "com.springbank.user.**",
                "org.axonframework.**",
                "com.thoughtworks.xstream.**"
        });

        return xstream;
    }
    @Bean
    @Primary
    public Serializer serializer() {
        return XStreamSerializer.builder().xStream(xstream()).build();
    }

    @Bean
    public TokenStore tokenStore(RevisionResolver revisionResolver, MongoTemplate mongoTemplate){

        var serializer = XStreamSerializer.builder()
                .revisionResolver(revisionResolver)
                .xStream(xstream())
                .build();

        return MongoTokenStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(serializer)
                .build();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoClient mongoClient) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(DefaultMongoTemplate.builder()
                                .mongoDatabase(mongoClient).build())
                .build();
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
    }
}
