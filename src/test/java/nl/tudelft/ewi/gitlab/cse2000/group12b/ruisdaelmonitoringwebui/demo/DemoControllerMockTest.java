package nl.tudelft.ewi.gitlab.cse2000.group12b.ruisdaelmonitoringwebui.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

@SpringBootTest
public class DemoControllerMockTest {

    @Autowired
    public DemoController demoController;

    @Mock
    public Model model;

    //In case another spring service or controller uses the mocked object you can use @InjectMocks when instantiating the object, for example as such
    //@InjectMocks
    //public DemoControllerUser dmu = new DemoControllerUser();

    @BeforeEach
    void setMockOutput() {
        when(model.addAttribute(anyString(), any())).thenReturn(model);
    }

    @DisplayName("Test Mocking")
    @Test
    void mockTest() {
        assertEquals("demo/helloworld", demoController.thymeleafExample("", model));
    }

    //I'm just lazy for demo purposes here, these names should be descriptive of course.
    @DisplayName("Test Mocking")
    @Test
    void mockTest2() {
        assertEquals("demo/helloworld-custom", demoController.thymeleafExamplePost(new Greeting("", ""), model));
    }
}
