/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure;

import com.github.cliftonlabs.json_simple.JsonException;
import com.str8tech.verisure.json.ErrorResponse;
import com.str8tech.verisure.json.InstallationResponse;
import com.str8tech.verisure.json.OverviewResponse;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author richard.strate
 */
public interface JsonParser {

  /**
   * Try to parse out a
   * {@link com.str8tech.verisure.json.ErrorResponse ErrorResponse} from the
   * HTTP response body/content, catching any exceptions and returning NULL
   * instead.
   *
   * @param response
   * @return Returns the error response or NULL if something fails
   */
  ErrorResponse parseErrorContent(CloseableHttpResponse response);

  OverviewResponse parseOverviewResponse(String json) throws JsonException;

  List<InstallationResponse> parseInstallationResponses(String json) throws JsonException;
  
}
