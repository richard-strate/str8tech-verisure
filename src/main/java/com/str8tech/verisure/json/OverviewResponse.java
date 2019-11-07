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

import java.util.List;

/**
 *
 * @author richard.strate
 */
public class OverviewResponse {

  public static class DoorLockStatus {

    private String area;
    private String deviceLabel;

    public String getDeviceLabel() {
      return deviceLabel;
    }

    public void setDeviceLabel(String deviceLabel) {
      this.deviceLabel = deviceLabel;
    }

    public String getArea() {
      return area;
    }

    public void setArea(String area) {
      this.area = area;
    }

  }

  private List<DoorLockStatus> doorLockStatusList;

  public List<DoorLockStatus> getDoorLockStatusList() {
    return doorLockStatusList;
  }

  public void setDoorLockStatusList(List<DoorLockStatus> doorLockStatusList) {
    this.doorLockStatusList = doorLockStatusList;
  }

}
