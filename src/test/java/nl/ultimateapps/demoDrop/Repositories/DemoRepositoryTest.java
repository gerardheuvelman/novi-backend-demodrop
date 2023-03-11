package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.DemoDropApplication;
import nl.ultimateapps.demoDrop.Models.Demo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
//@ContextConfiguration(classes={DemoRepository.class})
public class DemoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DemoRepository demoRepository;

    @Test
    @Disabled
    void testFindByIdReturnsADemo() {

        // given
        Demo demo = new Demo();//1001L, null, "Prime Audio",  123D, 123D , null, null, null , null, null );
//        demo.setDemoId(1001L);
        entityManager.persist(demo);
        entityManager.flush();

        // when
        Demo foundDemo = demoRepository.findById(1001L).get();

        // then
        String expectedDemoTitle = "Prime Audio";
        String actualDemoTitle = foundDemo.getTitle();
        assertEquals(expectedDemoTitle, actualDemoTitle);
    }
}
