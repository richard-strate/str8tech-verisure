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
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a simple API client towards the Verisure cloud API.
 * After creating an instance, call the {@link #open() open()}-method before
 * calling other "remote" methods like {@link #requestInstallations()}.
 *
 * The class basically uses {@link JsonHttpClient} to execute all requests and
 * adds parsing via a {@link JsonParser}.
 *
 * Usage:
 *
 * <pre>
 * try (ClientImpl client = new ClientImpl("userName", "password")) {
 *   client.open();
 *   client.requestInstallations();
 *   ..
 * }
 * </pre>
 *
 * @author richard.strate
 */
public class ClientImpl implements Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);
  private final String baseUrl = "https://e-api01.verisure.com/xbn/2";
  private final String cookieUrl = baseUrl + "/cookie";

  private final JsonHttpClient jsonClient;
  private final JsonParser jsonParser;

  /**
   * Create the client with the supplied credentials. Will create an instance of
   * default classes {@link JsonParserImpl} and {@link JsonHttpClient}.
   *
   * @param email Verisure e-mail (eg user name)
   * @param password Verisure password
   */
  public ClientImpl(String email, String password) {
    this(new JsonHttpClient(email, password), new JsonParserImpl());
  }

  /**
   * Create the client with a custom {@link JsonHttpClient} and
   * {@link JsonParser}.
   *
   * @param jsonClient
   * @param jsonParser
   */
  public ClientImpl(JsonHttpClient jsonClient, JsonParser jsonParser) {
    this.jsonClient = jsonClient;
    this.jsonParser = jsonParser;
  }

  /**
   * Call this to prepare the client (eg authorized) before using other "remote"
   * methods.
   *
   * @throws IOException
   */
  public void open() throws IOException {
    jsonClient.open();
  }

  @Override
  public void close() {
    jsonClient.close();
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
    try {
      jsonClient.requestUnlock(installationGiid, deviceLabel, doorCode);
      return true;
    } catch (RemoteException ex) {
      if ((ex.getErrorResponse() != null) && ex.getErrorResponse().getErrorCode().equals(ErrorResponse.EC_DOORLOCK_STATE_ALREADY_SET)) {
        return false;
      }
      throw new IOException("Failed to unlock the door '" + deviceLabel + "'", ex);
    }
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
    try {
      jsonClient.requestLock(installationGiid, deviceLabel, doorCode);
      return true;
    } catch (RemoteException ex) {
      if ((ex.getErrorResponse() != null) && ex.getErrorResponse().getErrorCode().equals(ErrorResponse.EC_DOORLOCK_STATE_ALREADY_SET)) {
        return false;
      }
      throw new IOException("Failed to lock the door '" + deviceLabel + "'", ex);
    }
  }

  /**
   * Request an overview of the specified installation. The available
   * installations can be listed via {@link #requestInstallations() }-method.
   *
   * Before calling this method the client must be {@link #open() opened}.
   *
   * @param installationGiid
   * @return
   * @throws IOException
   * @throws JsonException
   */
  public OverviewResponse requestOverview(String installationGiid) throws IOException, JsonException {
    String json;
    try {
      json = jsonClient.requestOverview(installationGiid);
    } catch (RemoteException ex) {
      throw new IOException("Failed to request overview for installation '" + installationGiid + "'", ex);
    }
    return jsonParser.parseOverviewResponse(json);
  }

  /**
   * Request a listing of all available installations.
   *
   * Before calling this method the client must be {@link #open() opened}.
   *
   * @return
   * @throws IOException
   * @throws JsonException
   */
  public List<InstallationResponse> requestInstallations() throws IOException, JsonException {
    String json;
    try {
      json = jsonClient.requestInstallations();
    } catch (RemoteException ex) {
      throw new IOException("Failed to request installations", ex);
    }
    return jsonParser.parseInstallationResponses(json);
  }

}
