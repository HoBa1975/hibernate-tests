package org.hibernate.orm.test.HHH18413;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.orm.test.HHH18085.pojo.Account;
import org.hibernate.orm.test.HHH18085.pojo.DatabaseAccount;
import org.hibernate.orm.test.HHH18085.pojo.DomainAccount;
import org.hibernate.orm.test.HHH18085.pojo.Grantee;
import org.hibernate.orm.test.HHH18085.pojo.InternalAccount;
import org.hibernate.orm.test.HHH18085.pojo.TestEntity;
import org.hibernate.orm.test.HHH18085.pojo.User;
import org.hibernate.orm.test.HHH18413.pojo.AbstractVersion;
import org.hibernate.orm.test.HHH18413.pojo.Element;
import org.hibernate.orm.test.HHH18413.pojo.ElementClass;
import org.hibernate.orm.test.HHH18413.pojo.Version;
import org.hibernate.stat.spi.StatisticsImplementor;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@DomainModel(annotatedClasses = {
        AbstractVersion.class,
        Version.class,
        Element.class,
        ElementClass.class
})

@SessionFactory(generateStatistics = true)
@ServiceRegistry(settings = {
        @Setting(name = AvailableSettings.USE_QUERY_CACHE, value = "true"),
        @Setting(name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "true")
})
public class FieldListIsAmbiguousTest {
    private static final String VERSIONBYLOCATIONANDCLASSNAMEQUERY = "from Version v where v.element.elementClass.location = :loc and v.element.elementClass.name = :name and v.isLatest = true";

    @BeforeAll
    public void setUp(SessionFactoryScope scope) {
        scope.inTransaction(session -> {
            ElementClass elementClass = new ElementClass();
            elementClass.setId("1");
            elementClass.setElementClassName("ElementClassName");
            elementClass.setLocation("GERMANY");
            elementClass.setValid(true);
            session.persist(elementClass);

            Element element = new Element();
            element.setId("1");
            element.setElementName("ElementName");
            element.setElementClass(elementClass);
            session.persist(element);

            Version version = new Version();
            version.setId("1");
            version.setElement(element);
            version.setStatus("O");
            version.setRepraesentation("N");
            version.setIsLatest(true);
            version.setVersionData1("VersionData1");
            version.setVersionData2("VersionData2");
            session.persist(version);
        });
    }

    @Test
    public void testNormalBehavior(SessionFactoryScope scope) {
        testQuery(scope, session -> {
        });
    }

    private void testQuery(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.getSessionFactory().getCache().evictQueryRegions();
        final StatisticsImplementor statistics = scope.getSessionFactory().getStatistics();
        statistics.clear();

        scope.inTransaction(session -> {
            executeQueryByName(session);
        });
    }

    private static void executeQueryByName(SessionImplementor session) {
        final Version v = session.createQuery(VERSIONBYLOCATIONANDCLASSNAMEQUERY, Version.class)
                .setParameter("loc", "GERMANY")
                .setParameter("name", "ElementClassName")
                .getSingleResult();

        assertEquals("VersionData1", v.getVersionData1());
    }
}

