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

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author richard.strate
 */
public class LockRequest implements Jsonable {

  private String code;

  public LockRequest() {
  }

  public LockRequest(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toJson() {
    final StringWriter writable = new StringWriter();
    try {
      this.toJson(writable);
    } catch (final IOException ex) {
      throw new RuntimeException("Failed to serialize to json", ex);
    }
    return writable.toString();
  }

  @Override
  public void toJson(Writer writer) throws IOException {
    final JsonObject json = new JsonObject();
    json.put("code", this.getCode());
    json.toJson(writer);
  }
}
