package junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium;

import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.ipSupplierLocalIP;
import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.newWebDriverChrome;
import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.newWebDriverChromeRemote;
import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.newWebDriverFirefox;
import static junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverFunctions.takeScreenShot;
import static junit.org.rapidpm.vaadin.helloworld.server.junit5.vaadin.VaadinPageObject.KEY_VAADIN_SERVER_IP;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ContainerExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ObjectArrayArguments;
import org.openqa.selenium.WebDriver;
import junit.org.rapidpm.vaadin.helloworld.server.junit5.MyUIPageObject;

/**
 *
 */
public class WebDriverSeleniumExtension
    implements AfterTestExecutionCallback, BeforeAllCallback, TestExecutionExceptionHandler {

  public static final String WEBDRIVER = "webdriver";

  static Function<ExtensionContext, Namespace> namespaceFor() {
    return (ctx) -> Namespace.create(WebDriverSeleniumExtension.class,
                                     ctx.getTestClass().get().getName(),
                                     ctx.getTestMethod().get().getName());
  }

  static Function<ExtensionContext, ExtensionContext.Store> store() {
    return (context) -> context.getStore(namespaceFor().apply(context));
  }

  static Function<ExtensionContext, WebDriver> webdriver() {
    return (context) -> store().apply(context).get(WEBDRIVER, WebDriver.class);
  }

  static BiConsumer<ExtensionContext, WebDriver> storeWebDriver() {
    return (context, webDriver) -> store().apply(context).put(WEBDRIVER, webDriver);
  }

  @Override
  public void afterTestExecution(TestExtensionContext context) throws Exception {
    final WebDriver webdriver = webdriver().apply(context);

    webdriver.quit();

    store().apply(context).remove(WEBDRIVER);
  }


  @Override
  public void beforeAll(ContainerExtensionContext context) throws Exception {

    System.setProperty(KEY_VAADIN_SERVER_IP, ipSupplierLocalIP.get());
//    System.setProperty("webdriver.chrome.driver", "_data/webdrivers/chromedriver-mac-64bit");
//    System.setProperty("webdriver.opera.driver", "_data/webdrivers/operadriver-mac-64bit");
//    System.setProperty("webdriver.gecko.driver", "_data/webdrivers/geckodriver-mac-64bit");
  }

  @Override
  public void handleTestExecutionException(TestExtensionContext context, Throwable throwable) throws Throwable {

    takeScreenShot().accept(webdriver().apply(context));

    throw throwable;
  }


  public static class PageObjectProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> arguments(ContainerExtensionContext context) {
      return Stream
          .of(
              newWebDriverChromeRemote()//,
//              newWebDriverChrome()//,
//              newWebDriverFirefox()
          )
          .map(Supplier::get)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .peek(d -> storeWebDriver().accept(context, d))
          .map(MyUIPageObject::new)
          .map(ObjectArrayArguments::create);
    }
  }
}
