/*
 * Copyright (C) 2019 Straight Technologies
 * All rights reserved.
 * https://www.str8tech.com
 * 
 * This program is free software under the MIT License
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
