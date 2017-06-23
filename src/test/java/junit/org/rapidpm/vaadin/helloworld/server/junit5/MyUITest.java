package junit.org.rapidpm.vaadin.helloworld.server.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverSeleniumExtension;
import junit.org.rapidpm.vaadin.helloworld.server.junit5.vaadin.VaadinTest;

/**
 *
 */

@VaadinTest
public class MyUITest {

  @DisplayName("functional style")
  @ParameterizedTest(name = "{index} ==> ''{0}''")
  @ArgumentsSource(WebDriverSeleniumExtension.PageObjectProvider.class)
  void test001(final MyUIPageObject pageObject) {
    pageObject.loadPage();

    pageObject.inputA.get().sendKeys("5");
    pageObject.inputB.get().sendKeys("5");

    pageObject.button.get().click();
    String value = pageObject.output.get().getAttribute("value");
//    Assertions.assertEquals("10", value);
    Assertions.assertEquals("55", value);
  }
  //    Parameters.setScreenshotReferenceDirectory("_data/reference_screenshots/");
}
