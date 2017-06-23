package junit.org.rapidpm.vaadin.helloworld.server.junit5.vaadin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import junit.org.rapidpm.vaadin.helloworld.server.junit5.container.ServletContainerExtension;
import junit.org.rapidpm.vaadin.helloworld.server.junit5.selenium.WebDriverSeleniumExtension;

/**
 *
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
//Order is important top / down
@ExtendWith(ServletContainerExtension.class)
@ExtendWith(WebDriverSeleniumExtension.class)
public @interface VaadinTest {
}
