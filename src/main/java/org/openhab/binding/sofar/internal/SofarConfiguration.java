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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link SofarConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Slava Belov - Initial contribution
 */
@NonNullByDefault
public class SofarConfiguration {

    /**
     * Sample configuration parameters. Replace with your own.
     */
    public String hostname = "";
    public String username = "";
    public String password = "";
    public int refreshInterval = 600;

    public String getHostname() {
        return this.hostname;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getRefreshInterval() {
        return this.refreshInterval;
    }
}
