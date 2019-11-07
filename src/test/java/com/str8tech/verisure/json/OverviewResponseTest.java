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

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author richard.strate
 */
public class OverviewResponseTest {

  public OverviewResponseTest() {
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
   * Test of getDoorLockStatusList method, of class OverviewResponse.
   */
  @Test
  public void testGetDoorLockStatusList() {
  }

  /**
   * Test of setDoorLockStatusList method, of class OverviewResponse.
   */
  @Test
  public void testSetDoorLockStatusList() {
  }

  @Test
  public void testParse() throws JsonException {
    JsonObject jsonObject = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("OverviewResponse.json"), StandardCharsets.UTF_8));
    Mapper mapper = new DozerBeanMapper();
    OverviewResponse overviewResponse = mapper.map(jsonObject, OverviewResponse.class);
    assertEquals("2AYJ SYLB", overviewResponse.getDoorLockStatusList().get(0).getDeviceLabel());
    assertEquals("Ytterdörr", overviewResponse.getDoorLockStatusList().get(0).getArea());
  }
}
