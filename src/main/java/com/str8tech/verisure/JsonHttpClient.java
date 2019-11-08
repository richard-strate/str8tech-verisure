/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure;

import com.github.cliftonlabs.json_simple.Jsoner;
import com.str8tech.verisure.json.ErrorResponse;
import com.str8tech.verisure.json.LockRequest;
import java.io.Closeable;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple implementation of the client API. Before using the remote*-methods
 * make sure to {@link open()} the client (and {@link close()} is afterwards).
 *
 * @author richard.strate
 */
public class JsonHttpClient implements Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(JsonHttpClient.class);
  private final String userName;
  private final String password;
  private final String baseUrl = "https://e-api01.verisure.com/xbn/2";
  private final String cookieUrl = baseUrl + "/cookie";

  private CloseableHttpClient client;

  /**
   * Create the client with the supplied credentials.
   *
   * @param userName Verisure user name (eg e-mail)
   * @param password Verisure password
   */
  public JsonHttpClient(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  /**
   * Call this to prepare the client (eg authorized) before using other "remote"
   * methods.
   *
   * @throws IOException
   */
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
   * @return JSON response
   * @throws IOException
   * @throws RemoteException
   */
  public String requestUnlock(String installationGiid, String deviceLabel, String doorCode) throws IOException, RemoteException {
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
   * @return JSON response
   * @throws IOException
   * @throws RemoteException
   */
  public String requestLock(String installationGiid, String deviceLabel, String doorCode) throws IOException, RemoteException {
    return this.doLockAction(LockAction.lock, installationGiid, deviceLabel, doorCode);
  }

  enum LockAction {
    lock,
    unlock
  }

  private String doLockAction(LockAction lockAction, String installationGiid, String deviceLabel, String doorCode) throws IOException, RemoteException {
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
        ErrorResponse errorResponse = new JsonParserImpl().parseErrorContent(response);
        throw new RemoteException(response.getStatusLine().getStatusCode(), errorResponse, "Failed to " + lockAction.name() + "  the door with label '" + deviceLabel + "' of installation '" + installationGiid + "'");
      }
      return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Request an overview of the specified installation.The available
   * installations can be listed via {@link #requestInstallations()}-method.
   * Before calling this method the client must be {@link #open() opened}.
   *
   * @param installationGiid
   * @return JSON response
   * @throws IOException
   * @throws RemoteException
   */
  public String requestOverview(String installationGiid) throws IOException, RemoteException {
    String url = baseUrl + "/installation/" + installationGiid + "/overview";
    LOG.debug("Request overview of '{}' using url: '{}'", installationGiid, url);
    HttpGet request = new HttpGet(url);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    //request.setHeader("APPLICATION_ID", "VS_APP_IPHONE");
    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = new JsonParserImpl().parseErrorContent(response);
        throw new RemoteException(response.getStatusLine().getStatusCode(), errorResponse, "Failed to fetch overview of installation '" + installationGiid + "'");
      }
      return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Request a listing of all available installations.Before calling this method
   * the client must be {@link #open() opened}.
   *
   *
   * @return
   * @throws IOException
   * @throws RemoteException
   */
  public String requestInstallations() throws IOException, RemoteException {
    String url = baseUrl + "/webaccount/" + URLEncoder.encode(userName, StandardCharsets.UTF_8) + "/installation";
    LOG.debug("Listing installations using url: '{}'", url);
    HttpGet request = new HttpGet(url);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = new JsonParserImpl().parseErrorContent(response);
        throw new RemoteException(response.getStatusLine().getStatusCode(), errorResponse, "Failed to fetch installations");
      }
      return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Execute a login-scenario using credentials supplied at construct, this
   * generates an authorized cookies which is registered by the HTTP client.
   *
   * @throws IOException
   */
  private void login() throws IOException {
    LOG.debug("Request login cookie for user '{}' {} password using url: '{}'", userName, password == null ? "without" : "with", cookieUrl);
    // Crate a cookie
    client = HttpClientBuilder
            .create()
            .build();
    HttpPost request = new HttpPost(cookieUrl);
    request.setHeader("Content-Type", "application/json;charset=UTF-8");
    request.setHeader("Accept", "application/json");
    String authBasicValue = Base64.getEncoder().encodeToString(("CPE/" + userName + ":" + (password == null ? "" : password)).getBytes(StandardCharsets.UTF_8));
    request.setHeader("Authorization", "Basic " + authBasicValue);
    try (final CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        ErrorResponse errorResponse = new JsonParserImpl().parseErrorContent(response);
        if (errorResponse != null) {
          throw new IOException("Failed to login as '" + userName + "', response: " + errorResponse);
        } else {
          throw new IOException("Failed to login as '" + userName + "', status code: " + response.getStatusLine().getStatusCode());
        }
      }
    }

  }

}
