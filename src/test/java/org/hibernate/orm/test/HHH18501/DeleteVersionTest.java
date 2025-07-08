package org.hibernate.orm.test.HHH18501;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionImpl;
import org.hibernate.orm.test.HHH18501.pojo.AbstractVersion;
import org.hibernate.orm.test.HHH18501.pojo.ContainerVersion;
import org.hibernate.orm.test.HHH18501.pojo.Version;
import org.hibernate.orm.test.HHH18501.pojo.VersionAttribute;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

@DomainModel(annotatedClasses = {
        AbstractVersion.class,
        Version.class,
        ContainerVersion.class,
        VersionAttribute.class
})

@SessionFactory(generateStatistics = true)
@ServiceRegistry(settings = {
        @Setting(name = AvailableSettings.USE_QUERY_CACHE, value = "true"),
        @Setting(name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "true")
})

public class DeleteVersionTest {

    @BeforeEach
    public void setUp(SessionFactoryScope scope) {
        scope.inTransaction(session -> {
            ContainerVersion container = new ContainerVersion();
            container.setId("1");
            container.setVersionName("ContainerVersion");
            container.setStatus("O");
            container.setRepraesentation("G");
            session.persist(container);

            VersionAttribute attr1 = new VersionAttribute();
            attr1.setId(1);
            attr1.setVersion(container);
            attr1.setAttributeName("attr1");
            session.persist(attr1);

            VersionAttribute attr2 = new VersionAttribute();
            attr2.setId(2);
            attr2.setVersion(container);
            attr2.setAttributeName("attr2");
            session.persist(attr2);
        });
    }

    @Test
    public void testWorkingBehavior(SessionFactoryScope scope) {
        testQueryWorking(scope, session -> {
        });
    }

    @Test
    public void testNonWorkingBehavior(SessionFactoryScope scope) {
        testQueryNotWorking(scope, session -> {
        });
    }

    private void testQueryWorking(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.inTransaction(session -> {
            List<VersionAttribute> attributes = loadAllVersionAttributes(session);
            assertEquals(2, attributes.size());
            List<Version> versions = findVersionByIds(session, List.of("1"));
            assertEquals(1, versions.size());

            deleteAll(session, versions);
//            refreshPersistentContext(session);

            versions = findVersionByIds(session, List.of("1"));
            assertEquals(0, versions.size());
            attributes = loadAllVersionAttributes(session);
            assertEquals(0, attributes.size());
        });
    }

    private void testQueryNotWorking(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.inTransaction(session -> {
            List<Version> versions = findVersionByIds(session, List.of("1"));
            assertEquals(1, versions.size());
            List<VersionAttribute> attributes = loadAllVersionAttributes(session);
            assertEquals(2, attributes.size());

            deleteAll(session, versions);
            //refreshPersistentContext(session);

            versions = findVersionByIds(session, List.of("1"));
            assertEquals(1, versions.size());
            attributes = loadAllVersionAttributes(session);
            assertEquals(2, attributes.size());
        });
    }

    private List<ContainerVersion> findContainerVersionsByIds(SessionImplementor session, List<String> ids) {
        return session.createQuery("from ContainerVersion v where v.id in :ids", ContainerVersion.class)
                .setParameter("ids", ids)
                .list();
    }

    private List<AbstractVersion> findAbstractVersionsByIds(SessionImplementor session, List<String> ids) {
        return session.createQuery("from AbstractVersion v where v.id in :ids", AbstractVersion.class)
                .setParameter("ids", ids)
                .list();
    }

    private List<Version> findVersionByIds(SessionImplementor session, List<String> ids) {
        return session.createQuery("from Version v where v.id in :ids", Version.class)
                .setParameter("ids", ids)
                .list();
    }

    private void deleteAll(SessionImplementor session, List<Version> versions) {
        for (Version version : versions) {
            session.remove(version);
        }
    }

    private void deleteAllContainerVersions(SessionImplementor session, List<ContainerVersion> versions) {
        for (Version version : versions) {
            session.remove(version);
        }
    }

    private void deleteAllAbstractVersions(SessionImplementor session, List<AbstractVersion> versions) {
        for (AbstractVersion version : versions) {
            session.remove(version);
        }
    }

    private List<VersionAttribute> loadAllVersionAttributes(SessionImplementor session) {
        return session.createQuery("from VersionAttribute v", VersionAttribute.class).list();

    }

    public static void refreshPersistentContext(SessionImplementor session) {

        Map.Entry<Object, EntityEntry>[] entities = ((SessionImpl) session).getPersistenceContext()
                .reentrantSafeEntityEntries();
        for (Map.Entry<Object, EntityEntry> entry : entities) {
            Object obj = entry.getKey();
            try {
                session.refresh(obj);
            } catch (Exception ex) {
                //session.detach(obj);
            }
        }
        session.flush();
        session.clear();
    }

}
