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
import com.str8tech.verisure.json.LockRequest;
import com.str8tech.verisure.json.OverviewResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.strate
 */
public class ClientImpl implements Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);
  private final String email;
  private final String password;
  private final String baseUrl = "https://e-api01.verisure.com/xbn/2";
  private final String cookieUrl = baseUrl + "/cookie";

  private CloseableHttpClient client;

  public ClientImpl(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public void open() throws IOException {
    login();
  }

  @Override
  public void close() {
  }

  /**
   * Unlock the door.
   *
   * @param installationGiid Installation giid, can be found in response to
   * {@link #requestInstallations() requestInstallations}
   * @param deviceLabel Device label, can be found in response to
   * {@link #requestOverview(java.lang.String) requestOverview}
   * @param doorCode PIN code used for door lock
   * @return TRUE if door was unlocked, FALSE if door already was unlocked
   * @throws IOException
   */
  public boolean unlock(String installationGiid, String deviceLabel, String doorCode) throws IOException {
    return this.doLockAction(LockAction.unlock, installationGiid, deviceLabel, doorCode);
  }

  /**
   * Lock the door.
   *
   * @param installationGiid Installation giid, can be found in response to
   * {@link #requestInstallations() requestInstallations}
   * @param deviceLabel Device label, can be found in response to
   * {@link #requestOverview(java.lang.String) requestOverview}
   * @param doorCode PIN code used for door lock
   * @return TRUE if door was unlocked, FALSE if door already was unlocked
   * @throws IOException
   */
  public boolean lock(String installationGiid, String deviceLabel, String doorCode) throws IOException {
    return this.doLockAction(LockAction.lock, installationGiid, deviceLabel, doorCode);
  }

  enum LockAction {
    lock,
    unlock
  }

  private boolean doLockAction(LockAction lockAction, String installationGiid, String deviceLabel, String doorCode) throws IOException {
    String url = baseUrl + "/installation/" + installationGiid + "/device/" + URLEncoder.encode(deviceLabel, StandardCharsets.UTF_8) + "/" + lockAction.name();
    LOG.debug("Request {} using url: '{}'", lockAction.name(), url);
    HttpPut request = new HttpPut(url);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    LockRequest lockRequest = new LockRequest(doorCode);
    String json = Jsoner.serialize(lockRequest);
    request.setEntity(new StringEntity(json));
    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = parseErrorContent(response);
        if (errorResponse != null && errorResponse.getErrorCode().equals(ErrorResponse.EC_DOORLOCK_STATE_ALREADY_SET)) {
          return false;
        }
        if (errorResponse != null) {
          throw new IOException("Failed to " + lockAction.name() + "  the door with label '" + deviceLabel + "' of installation '" + installationGiid + "', response: " + errorResponse);
        } else {
          throw new IOException("Failed to " + lockAction.name() + "  the door with label '" + deviceLabel + "' of installation '" + installationGiid + "', status code: " + response.getStatusLine().getStatusCode());
        }
      }
    }
    return true;
  }

  public OverviewResponse requestOverview(String installationGiid) throws IOException, JsonException {
    String url = baseUrl + "/installation/" + installationGiid + "/overview";
    LOG.debug("Request overview of '{}' using url: '{}'", installationGiid, url);
    HttpGet request = new HttpGet(url);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    //request.setHeader("APPLICATION_ID", "VS_APP_IPHONE");
    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = parseErrorContent(response);
        if (errorResponse != null) {
          throw new IOException("Failed to fetch overview of installation '" + installationGiid + "', response: " + errorResponse);
        } else {
          throw new IOException("Failed to fetch overview of installation '" + installationGiid + "', status code: " + response.getStatusLine().getStatusCode());
        }
      }
      JsonObject jsonObject = (JsonObject) Jsoner.deserialize(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
      Mapper mapper = new DozerBeanMapper();
      return mapper.map(jsonObject, OverviewResponse.class);
    }
  }

  public List<InstallationResponse> requestInstallations() throws IOException, JsonException {
    String url = baseUrl + "/webaccount/" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "/installation";
    LOG.debug("Listing installations using url: '{}'", url);
    HttpGet request = new HttpGet(url);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = parseErrorContent(response);
        if (errorResponse != null) {
          throw new IOException("Failed to fetch installations, response: " + errorResponse);
        } else {
          throw new IOException("Failed to fetch installations, status code: " + response.getStatusLine().getStatusCode());
        }
      }
      JsonArray jsonArray = (JsonArray) Jsoner.deserialize(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
      Mapper mapper = new DozerBeanMapper();
      List<InstallationResponse> ret = new LinkedList<>();
      jsonArray.forEach((jsonObject) -> {
        ret.add(mapper.map(jsonObject, InstallationResponse.class));
      });
      return ret;
    }
  }

  public void login() throws IOException {
    LOG.debug("Request login cookie for user '{}' {} password using url: '{}'", email, password == null ? "without" : "with", cookieUrl);
    // Crate a cookie
    client = HttpClientBuilder
            .create()
            .build();
    HttpPost request = new HttpPost(cookieUrl);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    String authBasicValue = Base64.getEncoder().encodeToString(("CPE/" + email + ":" + (password == null ? "" : password)).getBytes(StandardCharsets.UTF_8));
    request.setHeader("Authorization", "Basic " + authBasicValue);
    try (final CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = parseErrorContent(response);
        if (errorResponse != null) {
          throw new IOException("Failed to login as '" + email + "', response: " + errorResponse);
        } else {
          throw new IOException("Failed to login as '" + email + "', status code: " + response.getStatusLine().getStatusCode());
        }
      }
    }

  }

  private ErrorResponse parseErrorContent(CloseableHttpResponse response) {
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

}
