package nl.tudelft.ewi.gitlab.cse2000.group12b.ruisdaelmonitoringwebui.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DemoServiceTest {

    @Autowired
    public DemoService demoService;

    @DisplayName("JUnit Spring Integration Test Example")
    @Test
    void testDemoServiceString() {
        assertEquals("Hello!", demoService.getDemoString());
    }
}
