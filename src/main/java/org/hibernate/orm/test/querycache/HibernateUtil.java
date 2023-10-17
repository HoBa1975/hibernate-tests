package org.hibernate.orm.test.querycache;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.orm.test.querycache.pojo.Account;
import org.hibernate.orm.test.querycache.pojo.DatabaseAccount;
import org.hibernate.orm.test.querycache.pojo.Grantee;
import org.hibernate.orm.test.querycache.pojo.InternalAccount;
import org.hibernate.orm.test.querycache.pojo.TestEntity;
import org.hibernate.service.ServiceRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class HibernateUtil {
    private static String PROPERTY_FILE_NAME;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() throws IOException {
        return getSessionFactory("");
    }

    public static SessionFactory getSessionFactory(String propertyFileName) throws IOException {
        if (propertyFileName.equals("")) propertyFileName = null;
        PROPERTY_FILE_NAME = propertyFileName;
        ServiceRegistry serviceRegistry = configureServiceRegistry();
        return makeSessionFactory(serviceRegistry);
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

//        metadataSources.addPackage("com.baeldung.hibernate.pojo");
        metadataSources.addAnnotatedClass(Grantee.class);
        metadataSources.addAnnotatedClass(Account.class);
        metadataSources.addAnnotatedClass(InternalAccount.class);
        metadataSources.addAnnotatedClass(DatabaseAccount.class);
        metadataSources.addAnnotatedClass(TestEntity.class);

        Metadata metadata = metadataSources.getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();

    }


    private static ServiceRegistry configureServiceRegistry() throws IOException {
        Properties properties = getProperties();
        return new StandardServiceRegistryBuilder().applySettings(properties)
                .build();
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        URL propertiesURL = Thread.currentThread()
                .getContextClassLoader()
                .getResource(StringUtils.defaultString(PROPERTY_FILE_NAME, "hibernate.properties"));
        try (FileInputStream inputStream = new FileInputStream(propertiesURL.getFile())) {
            properties.load(inputStream);
        }
        return properties;
    }
}