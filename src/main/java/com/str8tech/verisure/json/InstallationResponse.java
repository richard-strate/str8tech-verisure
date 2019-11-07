/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This is a legal notice from Straight Technologies,
 * registered in Sweden.
 * 
 * This document is proprietary, confidential and is legally privileged.
 * Any un-authorised access to it is prohibited. Its disclosure, copying,
 * distribution or any action taken or omitted to be taken in reliance on
 * it is prohibited and may be unlawful.
 */
package com.str8tech.verisure.json;

/**
 *
 * @author richard.strate
 */
public class InstallationResponse {

  // [{"giid":"98396143840","locale":"sv_SE","alias":"SkogsklÃ¶verbacken","customerType":"HOUSEHOLD"}]
  private String giid;
  private String locale;
  private String alias;
  private String customerType; // HOUSEHOLD

  public String getGiid() {
    return giid;
  }

  public void setGiid(String giid) {
    this.giid = giid;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getCustomerType() {
    return customerType;
  }

  public void setCustomerType(String customerType) {
    this.customerType = customerType;
  }

  @Override
  public String toString() {
    return "InstallationResponse{" + "giid=" + giid + ", locale=" + locale + ", alias=" + alias + ", customerType=" + customerType + '}';
  }

}
