package me.soupbringer.hotsoup.actions;

import com.opensymphony.xwork2.ActionSupport;

/**
 * A simple action, to see whether or not struts is functional.
 */
public class SimpleAction extends ActionSupport {

  private String givenName;
  
  @Override
  public String execute() {
    
    return SUCCESS;
  }
  
  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }
}
