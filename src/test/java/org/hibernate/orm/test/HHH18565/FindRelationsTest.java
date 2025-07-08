package org.hibernate.orm.test.HHH18565;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.orm.test.HHH18565.pojo.AbstractVersion;
import org.hibernate.orm.test.HHH18565.pojo.ContainerVersion;
import org.hibernate.orm.test.HHH18565.pojo.ContentRelation;
import org.hibernate.orm.test.HHH18565.pojo.SourceAndTarget;
import org.hibernate.orm.test.HHH18565.pojo.SourceTargetAndType;
import org.hibernate.orm.test.HHH18565.pojo.TypedRelationDef;
import org.hibernate.orm.test.HHH18565.pojo.TypedVersionRelation;
import org.hibernate.orm.test.HHH18565.pojo.Version;
import org.hibernate.query.Query;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

@DomainModel(annotatedClasses = {
        AbstractVersion.class,
        Version.class,
        ContainerVersion.class,
        ContentRelation.class,
//        SourceAndTarget.class,
//        SourceTargetAndType.class,
        TypedRelationDef.class,
        TypedVersionRelation.class
})

@SessionFactory(generateStatistics = true)
@ServiceRegistry(settings = {
        @Setting(name = AvailableSettings.USE_QUERY_CACHE, value = "true"),
        @Setting(name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "true")
})

public class FindRelationsTest {

    @BeforeEach
    public void setUp(SessionFactoryScope scope) {
        scope.inTransaction(session -> {
            ContainerVersion container = new ContainerVersion();
            container.setId("1");
            container.setVersionName("ContainerVersion");
            container.setStatus("O");
            container.setRepraesentation("G");
            session.persist(container);

            ContainerVersion content = new ContainerVersion();
            content.setId("2");
            content.setVersionName("ContentVersion");
            content.setStatus("O");
            content.setRepraesentation("G");
            session.persist(content);

            SourceAndTarget sourceAndTarget = new SourceAndTarget();
            sourceAndTarget.setSource(container);
            sourceAndTarget.setTarget(content);
//            session.persist(sourceAndTarget);

            ContentRelation relation = new ContentRelation();
            relation.setUseId("1");
            relation.setSourceObject(container);
            relation.setTargetObject(content);
            relation.setSourceAndTarget(sourceAndTarget);
            session.persist(relation);

            TypedRelationDef relationDef = new TypedRelationDef();
            relationDef.setId("1");
            session.persist(relationDef);

            SourceTargetAndType sourceTargetAndType = new SourceTargetAndType();
            sourceTargetAndType.setSource(container);
            sourceTargetAndType.setTarget(content);
            sourceTargetAndType.setRelationType(relationDef);


            TypedVersionRelation versionRelation = new TypedVersionRelation();
            versionRelation.setId("1");
            versionRelation.setSourceObject(container);
            versionRelation.setTargetObject(content);
            versionRelation.setRelationDefinition(relationDef);
            session.persist(versionRelation);
        });
    }

    @Test
    public void testWorkingBehavior(SessionFactoryScope scope) {
        testQueryWorking(scope, session -> {
        });
    }

    private void testQueryWorking(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.inTransaction(session -> {
            ContentRelation relation = findBySourceIdAndTargetId(session,"1", "2");
            assertEquals("1", relation.getUseId());
            ContentRelation relation1 = findBySourceIdAndTargetId(session,"1", "2");
            assertEquals("1", relation1.getUseId());
        });
    }

    private void testQueryNotWorking(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.inTransaction(session -> {
        });
    }

    private ContentRelation findBySourceIdAndTargetId(SessionImplementor session, String containerId,
                                                      String contentId) {
        Query<ContentRelation> query = session.createQuery("select v from ContentRelation v inner join fetch v" +
                ".sourceObject inner join fetch v.targetObject where v.sourceObject.id = :p1 and v.targetObject.id = " +
                ":p2");
        query.setParameter("p1", containerId);
        query.setParameter("p2", contentId);

        List<ContentRelation> ret = query.list();
        return ret.isEmpty() ? null : ret.get(0);
    }
}
