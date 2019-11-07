/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
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
