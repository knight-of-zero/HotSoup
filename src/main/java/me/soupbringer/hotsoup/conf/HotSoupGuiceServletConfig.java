package me.soupbringer.hotsoup.conf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * A Listener which Tomcat will execute upon starting the webapp.
 * This creates our single Injector which gets used to inject dependencies
 * throughout the lifetime of the webapp.
 */
public final class HotSoupGuiceServletConfig extends GuiceServletContextListener {
  
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new HotSoupURLMappings(), new HotSoupModule());
  }

}
