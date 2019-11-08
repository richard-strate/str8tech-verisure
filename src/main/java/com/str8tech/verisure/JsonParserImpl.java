/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.str8tech.verisure.json.ErrorResponse;
import com.str8tech.verisure.json.InstallationResponse;
import com.str8tech.verisure.json.OverviewResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 *
 * @author richard.strate
 */
public class JsonParserImpl implements JsonParser {

  /**
   * Try to parse out a
   * {@link com.str8tech.verisure.json.ErrorResponse ErrorResponse} from the
   * HTTP response body/content, catching any exceptions and returning NULL
   * instead.
   *
   * @param response
   * @return Returns the error response or NULL if something fails
   */
  @Override
  public ErrorResponse parseErrorContent(CloseableHttpResponse response) {
    if (response.getEntity() != null) {
      JsonObject jsonObject;
      try {
        jsonObject = (JsonObject) Jsoner.deserialize(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
      } catch (IOException | UnsupportedOperationException | JsonException ex) {
        return null;
      }
      Mapper mapper = new DozerBeanMapper();
      return mapper.map(jsonObject, ErrorResponse.class);
    } else {
      return null;
    }
  }

  @Override
  public OverviewResponse parseOverviewResponse(String json) throws JsonException {
    JsonObject jsonObject = (JsonObject) Jsoner.deserialize(json);
    Mapper mapper = new DozerBeanMapper();
    return mapper.map(jsonObject, OverviewResponse.class);
  }

  @Override
  public List<InstallationResponse> parseInstallationResponses(String json) throws JsonException {
    JsonArray jsonArray = (JsonArray) Jsoner.deserialize(json);
    Mapper mapper = new DozerBeanMapper();
    List<InstallationResponse> ret = new LinkedList<>();
    jsonArray.forEach((jsonObject) -> {
      ret.add(mapper.map(jsonObject, InstallationResponse.class));
    });
    return ret;
  }

}
