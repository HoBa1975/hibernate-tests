package org.hibernate.orm.test;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.orm.test.pojo.Account;
import org.hibernate.orm.test.pojo.DatabaseAccount;
import org.hibernate.orm.test.pojo.DomainAccount;
import org.hibernate.orm.test.pojo.Grantee;
import org.hibernate.orm.test.pojo.InternalAccount;
import org.hibernate.orm.test.pojo.TestEntity;
import org.hibernate.orm.test.pojo.User;
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
        Grantee.class,
        Account.class,
        InternalAccount.class,
        DatabaseAccount.class,
        TestEntity.class,
        User.class,
        DomainAccount.class
})

@SessionFactory(generateStatistics = true)
@ServiceRegistry(settings = {
        @Setting(name = AvailableSettings.USE_QUERY_CACHE, value = "true"),
        @Setting(name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "true")
})

public class SharedCollectionReferencesTest {
    private static final String ENTITYQUERY = "from TestEntity";
    private static final String ACCOUNTQUERY = "from Account";
    private static final String ACCOUNTBYNAMEQUERY = "from Account where name = :name";
    private static final String NAME = "INT_D";

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
        assertTrue(entity.hasDomainAccounts());
    }

    @BeforeAll
    public void setUp(SessionFactoryScope scope) {
        scope.inTransaction(session -> {
            DomainAccount domainAccount1 = new DomainAccount("DOM_ACC_A");
            session.persist(domainAccount1);
            DomainAccount domainAccount4 = new DomainAccount("DOM_ACC_D");
            session.persist(domainAccount4);

            User user1 = new User("USER_ID_A", "Doe", "John");
            session.persist(user1);
            User user2 = new User("USER_ID_B", "Doe", "Jane");
            session.persist(user2);
            User user3 = new User("USER_ID_C", "Doe", "Helga");
            session.persist(user3);
            User user4 = new User("USER_ID_D", "Doe", "Hugo");
            session.persist(user4);

            DatabaseAccount acc1 = new DatabaseAccount("A", "DB_A", user1);
            session.persist(acc1);
            DatabaseAccount acc2 = new DatabaseAccount("B", "DB_B", user2);
            session.persist(acc2);
            DatabaseAccount acc3 = new DatabaseAccount("C", "DB_C", user3);
            session.persist(acc3);
            InternalAccount acc4 = new InternalAccount("D", "INT_D", user4);
            acc4.getDomainAccounts().add(domainAccount4);
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


//        scope.inTransaction(QueryCacheExistingEntityInstanceTest::executeQueryByGivenName);
//        scope.inTransaction(QueryCacheExistingEntityInstanceTest::executeQueryByGivenName);
        scope.inTransaction(session -> {
            executeQueryByGivenName(session);
            executeQueryByGivenName(session);
        });
    }

    private void executeQueryElementFindAll(SessionImplementor session) {
        session.createQuery(ENTITYQUERY, TestEntity.class).list();
    }
}

