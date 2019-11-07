/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure.json;

/**
 *
 * @author richard.strate
 */
public class ErrorResponse {
  //{"errorGroup":"BAD_REQUEST","errorCode":"VAL_00819","errorMessage":"The requested doorlock state is not possible to apply due to state already set"}

  public static String EC_DOORLOCK_STATE_ALREADY_SET = "VAL_00819";

  private String errorGroup;
  private String errorCode;
  private String errorMessage;

  public String getErrorGroup() {
    return errorGroup;
  }

  public void setErrorGroup(String errorGroup) {
    this.errorGroup = errorGroup;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" + "errorGroup=" + errorGroup + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + '}';
  }

}
