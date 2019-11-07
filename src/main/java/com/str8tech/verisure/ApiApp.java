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
package com.str8tech.verisure;

import com.github.cliftonlabs.json_simple.JsonException;
import com.str8tech.verisure.json.InstallationResponse;
import com.str8tech.verisure.json.OverviewResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author richard.strate
 */
public class ApiApp {

  public void run(String[] args) throws IOException, JsonException, InterruptedException {
    if (args.length < 3) {
      throw new IllegalArgumentException("Too few arguments");
    }
    String userName = args[0];
    String password = args[1];
    String doorCode = args[2];
    try (ClientImpl clientImpl = new ClientImpl(userName, password)) {
      clientImpl.open();
      List<InstallationResponse> requestInstallations = clientImpl.requestInstallations();
      String installationGiid = requestInstallations.get(0).getGiid();
      OverviewResponse overview = clientImpl.requestOverview(installationGiid);
      String deviceLabel = overview.getDoorLockStatusList().get(0).getDeviceLabel();
      System.out.println("Unlock: " + clientImpl.unlock(installationGiid, deviceLabel, doorCode));
      TimeUnit.SECONDS.sleep(3);
      System.out.println("Lock: " + clientImpl.lock(installationGiid, deviceLabel, doorCode));
    }
  }

  public static void main(String[] args) {
    try {
      new ApiApp().run(args);
      System.exit(0);
    } catch (JsonException | IOException | InterruptedException | RuntimeException ex) {
      ex.printStackTrace(System.err);
      System.err.println();
      System.err.println("Usage:");
      System.err.println("  " + ApiApp.class.getSimpleName() + " <user-name> <password> <door-code>");
      System.out.println("Parameters");
      System.err.println(String.format("%-20s: %s", "user-name", "Verisure user-name (eg e-mail address)"));
      System.err.println(String.format("%-20s: %s", "password", "Verisure password"));
      System.err.println(String.format("%-20s: %s", "door-code", "Code to lock/unlock door"));
      System.exit(1);

    }
  }

}
