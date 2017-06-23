package junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.openqa.selenium.By.id;
import static org.rapidpm.frp.StringFunctions.notEmpty;
import static org.rapidpm.frp.StringFunctions.notStartsWith;
import static org.rapidpm.frp.Transformations.not;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.rapidpm.frp.Transformations;
import org.rapidpm.frp.functions.CheckedSupplier;

/**
 *
 */
public interface WebDriverFunctions {

  Supplier<String> ipSupplierLocalIP = () -> {
    final CheckedSupplier<Enumeration<NetworkInterface>> checkedSupplier = NetworkInterface::getNetworkInterfaces;

    return Transformations.<NetworkInterface>enumToStream()
        .apply(checkedSupplier.getOrElse(Collections::emptyEnumeration))
        .map(NetworkInterface::getInetAddresses)
        .flatMap(iaEnum -> Transformations.<InetAddress>enumToStream().apply(iaEnum))
        .filter(inetAddress -> inetAddress instanceof Inet4Address)
        .filter(not(InetAddress::isMulticastAddress))
        .map(InetAddress::getHostAddress)
        .filter(notEmpty())
        .filter(adr -> notStartsWith().apply(adr, "127"))
        .filter(adr -> notStartsWith().apply(adr, "169.254"))
        .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr, "0.0.0.0"))
        //            .filter(adr -> range(224, 240).noneMatch(nr -> adr.startsWith(valueOf(nr))))
        .findFirst().orElse("localhost");
  };

  // not nice to copy all this stuff
  static Supplier<Optional<WebDriver>> newWebDriverChromeRemote() {
    return () -> {
      try {
        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
//        final ChromeDriver webDriver = new ChromeDriver(chromeCapabilities);
        final URL url = new URL("http://" + "127.0.0.1" + ":4444/wd/hub");
        final RemoteWebDriver webDriver = new RemoteWebDriver(url, chromeCapabilities);
        webDriver.manage().window().maximize();
        return Optional.of(webDriver);
      } catch (Exception e) {
        e.printStackTrace();
        return empty();
      }
    };
  }
  static Supplier<Optional<WebDriver>> newWebDriverChrome() {
    return () -> {
      try {
        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
        final ChromeDriver chromeDriver = new ChromeDriver(chromeCapabilities);
        chromeDriver.manage().window().maximize();
        return Optional.of(chromeDriver);
      } catch (Exception e) {
        e.printStackTrace();
        return empty();
      }
    };
  }

  static Supplier<Optional<WebDriver>> newWebDriverFirefox() {
    return () -> {
      try {
        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.firefox();
        final FirefoxDriver driver = new FirefoxDriver(chromeCapabilities);
        driver.manage().window().maximize();
        return Optional.of(driver);
      } catch (Exception e) {
        e.printStackTrace();
        return empty();
      }
    };
  }


  static BiFunction<WebDriver, String, Optional<WebElement>> elementFor() {
    return (driver, id) -> ofNullable(driver.findElement(id(id)));
  }

  static Consumer<WebDriver> takeScreenShot() {
    return (webDriver) -> {
      //take Screenshot
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
        outputStream.write(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES));
        //write to target/screenshot-[timestamp].jpg
        final FileOutputStream out = new FileOutputStream("target/screenshot-" + LocalDateTime.now() + ".png");
        out.write(outputStream.toByteArray());
        out.flush();
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

}
