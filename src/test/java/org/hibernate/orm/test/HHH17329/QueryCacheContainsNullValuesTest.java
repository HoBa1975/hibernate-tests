package org.hibernate.orm.test.HHH17329;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.orm.test.HHH17329.pojo.Account;
import org.hibernate.orm.test.HHH17329.pojo.DatabaseAccount;
import org.hibernate.orm.test.HHH17329.pojo.Grantee;
import org.hibernate.orm.test.HHH17329.pojo.InternalAccount;
import org.hibernate.orm.test.HHH17329.pojo.TestEntity;
import org.hibernate.orm.test.HHH17329.pojo.MyUser;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DomainModel(annotatedClasses = {
        Grantee.class,
        Account.class,
        InternalAccount.class,
        DatabaseAccount.class,
        TestEntity.class,
        MyUser.class
})

@SessionFactory(generateStatistics = true)
@ServiceRegistry(settings = {
        @Setting(name = AvailableSettings.USE_QUERY_CACHE, value = "true"),
        @Setting(name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "true")
})

public class QueryCacheContainsNullValuesTest {
    private static final String ENTITYQUERY = "from TestEntity";
    private static final String ACCOUNTQUERY = "from Account";
    private static final String ACCOUNTBYNAMEQUERY = "from Account where name = :name";
    private static final String NAME = "INT_D";

    @BeforeAll
    public void setUp(SessionFactoryScope scope) {
        scope.inTransaction(session -> {
            MyUser myUser1 = new MyUser("USER_ID_A", "Doe", "John");
            session.persist(myUser1);
            MyUser myUser2 = new MyUser("USER_ID_B", "Doe", "Jane");
            session.persist(myUser2);
            MyUser myUser3 = new MyUser("USER_ID_C", "Doe", "Helga");
            session.persist(myUser3);
            MyUser myUser4 = new MyUser("USER_ID_D", "Doe", "Hugo");
            session.persist(myUser4);

            DatabaseAccount acc1 = new DatabaseAccount("A", "DB_A", myUser1);
            session.persist(acc1);
            DatabaseAccount acc2 = new DatabaseAccount("B", "DB_B", myUser2);
            session.persist(acc2);
            DatabaseAccount acc3 = new DatabaseAccount("C", "DB_C", myUser3);
            session.persist(acc3);
            InternalAccount acc4 = new InternalAccount("D", "INT_D", myUser4);
            session.persist(acc4);

            TestEntity e1 = new TestEntity("A", "Entity_A", acc2);
            session.persist(e1);
            TestEntity e2 = new TestEntity("B", "Entity_B", acc4);
            session.persist(e2);
        });
    }

    @Test
    public void testNormalBehavior(SessionFactoryScope scope) {
        testQueryCache(scope, session -> {
        });
    }

    private void testQueryCache(SessionFactoryScope scope, Consumer<SessionImplementor> beforeQuery) {
        scope.getSessionFactory().getCache().evictQueryRegions();
        final StatisticsImplementor statistics = scope.getSessionFactory().getStatistics();
        statistics.clear();

        scope.inTransaction(session -> {
            executeQueryElementFindAll(session);
            executeQueryAccountFindAll(session);
        });


        scope.inTransaction(QueryCacheContainsNullValuesTest::executeQueryByGivenName);
    }

    private void executeQueryElementFindAll(SessionImplementor session) {
        session.createQuery(ENTITYQUERY, TestEntity.class).list();
    }

    private static void executeQueryAccountFindAll(SessionImplementor session) {
        final List<Account> entities = session.createQuery(ACCOUNTQUERY, Account.class)
                .setCacheable(true)
                .setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
                .list();
        for (Account a : entities) {
            executeQueryByName(session, a.getLoginName());
        }
    }

    private static void executeQueryByName(SessionImplementor session, String loginName) {
        final Account entity = session.createQuery(ACCOUNTBYNAMEQUERY, Account.class)
                .setParameter("name", loginName)
                .setCacheable(true)
                .setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
                .getSingleResult();
    }

    private static void executeQueryByGivenName(SessionImplementor session) {
        final Account entity = session.createQuery(ACCOUNTBYNAMEQUERY, Account.class)
                .setParameter("name", NAME)
                .setCacheable(true)
                .setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
                .getSingleResult();
        assertEquals(NAME, entity.getLoginName());
        assertNotNull(entity.getUser());
    }
}

