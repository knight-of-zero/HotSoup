package me.soupbringer.hotsoup.conf;

import me.soupbringer.hotsoup.servlets.TestServlet;

import com.google.inject.servlet.ServletModule;

public class HotSoupURLMappings extends ServletModule {
  
  /**
   * Set up our URL Filters and Servlets for the webapp.
   */
  @Override
  protected void configureServlets() {
    serve("/*").with(TestServlet.class);
  }

}
