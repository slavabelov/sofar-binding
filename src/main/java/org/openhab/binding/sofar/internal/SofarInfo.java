/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.sofar.internal;

/**
 * The {@link SofarInfo} is responsible for storing Sofar status data
 *
 * @author Slava Belov - Initial contribution
 */
public class SofarInfo {
    private String sn;
    private String msvn;
    private String pvType;
    private float nowP;
    private float todayE;
    private float totalE;
    private String alarm;

    public String getSn() {
        return sn;
    }

    public String getMsvn() {
        return msvn;
    }

    public String getPvType() {
        return pvType;
    }

    public float getNowP() {
        return nowP;
    }

    public float getTodayE() {
        return todayE;
    }

    public float getTotalE() {
        return totalE;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setMsvn(String msvn) {
        this.msvn = msvn;
    }

    public void setPvType(String pvType) {
        this.pvType = pvType;
    }

    public void setNowP(float nowP) {
        this.nowP = nowP;
    }

    public void setTodayE(float todayE) {
        this.todayE = todayE;
    }

    public void setTotalE(float totalE) {
        this.totalE = totalE;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
