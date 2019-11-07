/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
 */
package com.str8tech.verisure;

import com.str8tech.verisure.json.InstallationResponse;
import com.str8tech.verisure.json.OverviewResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.strate
 */
public class ClientImplIT {

  public ClientImplIT() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of open method, of class ClientImpl.
   */
  @Test
  public void testOpen() throws Exception {
  }

  /**
   * Test of close method, of class ClientImpl.
   */
  @Test
  public void testClose() {
  }

  /**
   * Test of unlock method, of class ClientImpl.
   */
  @Test
  public void testUnlock() throws Exception {
  }

  /**
   * Test of lock method, of class ClientImpl.
   */
  @Test
  public void testLock() throws Exception {
  }

  /**
   * Test of requestOverview method, of class ClientImpl.
   */
  @Test
  public void testRequestOverview() throws Exception {
  }

  /**
   * Test of requestInstallations method, of class ClientImpl.
   */
  @Test
  public void testRequestInstallations() throws Exception {
  }

  /**
   * Test of login method, of class ClientImpl.
   */
  @Test
  public void testLogin() throws Exception {
  }

  private static final Logger LOG = LoggerFactory.getLogger(ClientImplIT.class);

  @Test
  public void testUnlockThenLock() throws Exception {
    if (!Boolean.getBoolean("verisure.it.unlock")) {
      LOG.warn("Integration test 'testUnlockThenLock' is ignored as 'verisure.it.unlock' isn't set. This is default behaviour since a failing test might leave a door unlocked!");
      return;
    }
    String userName = System.getProperty("verisure.it.userName");
    String password = System.getProperty("verisure.it.password");
    String doorCode = System.getProperty("verisure.it.doorCode");
    assert userName != null : "System property 'verisure.it.userName' required for perform integration test";
    assert password != null : "System property 'verisure.it.password' required for perform integration test";
    assert doorCode != null : "System property 'verisure.it.doorCode' required for perform integration test";
    try (ClientImpl clientImpl = new ClientImpl(userName, password)) {
      clientImpl.open();
      List<InstallationResponse> requestInstallations = clientImpl.requestInstallations();
      assertFalse("At least 1 installation requried to performn integration test", requestInstallations.isEmpty());
      for (InstallationResponse requestInstallation : requestInstallations) {
        String installationGiid = requestInstallation.getGiid();
        OverviewResponse overview = clientImpl.requestOverview(installationGiid);
        for (OverviewResponse.DoorLockStatus doorLockStatus : overview.getDoorLockStatusList()) {
          String deviceLabel = doorLockStatus.getDeviceLabel();
          String area = doorLockStatus.getArea();
          assertTrue("Failed to unlock door '" + deviceLabel + "'", clientImpl.unlock(installationGiid, deviceLabel, doorCode));
          TimeUnit.SECONDS.sleep(10);
          assertTrue(
                  "\r\n\r\n********************* WARNING! DOOR IS LEFT UNLOCKED WARNING! *********************\r\n"
                  + "The integration test just unlocked a door but failed to re-lock it!\r\n"
                  + "This means that the door may currently be unlocked and accessible to anyone!\r\n"
                  + "The door is in area '" + area + "' and labeled '" + deviceLabel + "'\r\n"
                  + "********************* WARNING! DOOR IS LEFT UNLOCKED WARNING! *********************\r\n\r\n",
                  clientImpl.lock(installationGiid, deviceLabel, doorCode)
          );
          return;
        }
      }
    }
    fail("At least one (1) is required to run lock/unlock integration test");
  }
}
