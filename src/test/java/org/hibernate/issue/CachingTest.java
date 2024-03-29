package org.hibernate.issue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CachingTest {

  SessionFactory sessionFactory;

  @Before
  public void configureAndPopulateHibernate() {

    Configuration config = new Configuration().
        addResource("Pets.hbm.xml").
        setProperty("hibernate.hbm2ddl.auto", "create-drop").
        setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect").
        setProperty("hibernate.connection.url", "jdbc:derby:target/petstore;create=true").
        setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver").
        setProperty("hibernate.cache.region.factory_class", "net.sf.ehcache.hibernate.EhCacheRegionFactory").
        setProperty("hibernate.generate_statistics", "true").
        setProperty("hibernate.cache.use_structured_entries", "true").
        setProperty("hibernate.cache.use_query_cache", "true").
        setProperty("hibernate.connection.autocommit", "true");

    sessionFactory = config.buildSessionFactory();

    Session populationSession = sessionFactory.openSession();

    Owner owner = new Owner();
    owner.setName("bill");
    Dog fido = new Dog();
    fido.setAge(2);
    fido.setFeet(4);
    owner.getPets().put("fido", fido);

    populationSession.save(owner);

    populationSession.flush();
    populationSession.close();

    Session verificationSession = sessionFactory.openSession();

    Owner retrievedOwner = listAllOwners(verificationSession).get(0);
    Dog retrievedDog = (Dog) retrievedOwner.getPets().get("fido");

    assertEquals(4, retrievedDog.getFeet());

    verificationSession.close();

    long queries = sessionFactory.getStatistics().getQueryExecutionCount();
    assertEquals(1, queries);

  }

  @Test
  public void collectionsShouldBeCached() {
    Session session = sessionFactory.openSession();

    // Although this is supposed to be a functional test and hence would fail for 1 repetition,
    // interestingly enough, on my machine, the caching seems to kick in at about 2350000-2400000 repetitions,
    // as the stats printed to the console seem to level off at a certain value. This value appears to be
    // non-deterministic, but is in a certain range on a particular machine.

    int repetitions = 2500000;
    for(int i = 0; i < repetitions; i++) {
      listAllOwners(session);
      if (i % 25000 == 0) {
        System.err.println(sessionFactory.getStatistics().getQueryExecutionCount());
      }
    }

    session.close();

    long queries = sessionFactory.getStatistics().getQueryExecutionCount();
    assertEquals(1, queries);
  }

  @SuppressWarnings("unchecked")
  private List<Owner> listAllOwners(Session session) {
    return session.createQuery("select o from Owner o").setCacheable(true).list();
  }
}
