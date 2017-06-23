package junit.org.rapidpm.vaadin.helloworld.server.junit5.vaadin;

import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.elementFor;

import java.util.function.Function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.ui.AbstractComponent;

/**
 *
 */
public abstract class AbstractVaadinPageObject
    extends TestBenchTestCase
    implements VaadinPageObject {

  public AbstractVaadinPageObject(WebDriver webDriver) {
    setDriver(webDriver);
  }

  public void switchToDebugMode() {
    getDriver().get(url().get() + "?debug&restartApplication");
  }

  public void restartApplication() {
    getDriver().get(urlRestartApp().get());
  }

  public void loadPage() {
    getDriver().get(url().get());
  }


  /**
   * We are using now the Testbench methods
   */
  @Deprecated
  public Function<String, WebElement> element
      = (id) -> elementFor()
      .apply(getDriver(), id)
      .orElseThrow(() -> new RuntimeException("WebElement with the ID " + id + " is not available"));

}
