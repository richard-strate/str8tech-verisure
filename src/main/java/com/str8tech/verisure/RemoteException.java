/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure;

import com.str8tech.verisure.json.ErrorResponse;

/**
 * Exception used for to carry the causing HTTP status response code as well as
 * a possible error response object (may be NULL).
 *
 * @author richard.strate
 */
public class RemoteException extends Exception {

  private final int statusCode;
  private final ErrorResponse errorResponse;

  public RemoteException(int statusCode, ErrorResponse errorResponse) {
    this.statusCode = statusCode;
    this.errorResponse = errorResponse;
  }

  public RemoteException(int statusCode, ErrorResponse errorResponse, String message) {
    super(message);
    this.statusCode = statusCode;
    this.errorResponse = errorResponse;
  }

  public RemoteException(int statusCode, ErrorResponse errorResponse, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
    this.errorResponse = errorResponse;
  }

  public RemoteException(int statusCode, ErrorResponse errorResponse, Throwable cause) {
    super(cause);
    this.statusCode = statusCode;
    this.errorResponse = errorResponse;
  }

  public RemoteException(int statusCode, ErrorResponse errorResponse, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
    this.errorResponse = errorResponse;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public ErrorResponse getErrorResponse() {
    return errorResponse;
  }

}
