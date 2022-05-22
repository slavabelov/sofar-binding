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
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link SofarBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Slava Belov - Initial contribution
 */
@NonNullByDefault
public class SofarBindingConstants {

    private static final String BINDING_ID = "sofar";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "sofar");

    // List of all Channel ids
    public static final String CHANNEL_SN = "sn";
    public static final String CHANNEL_MSVN = "msvn";
    public static final String CHANNEL_PV_TYPE = "pv-type";
    public static final String CHANNEL_NOW_P = "now-p";
    public static final String CHANNEL_TODAY_E = "today-e";
    public static final String CHANNEL_TOTAL_E = "total-e";
    public static final String CHANNEL_ALARM = "alarm";
}
