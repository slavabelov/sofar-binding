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
 * This exception is thrown if there was an error in communication with the device.
 * 
 * @author Slava Belov - Initial contribution
 */
public class SofarCommunicationException extends RuntimeException {
    public SofarCommunicationException(String message) {
        super(message);
    }

    public SofarCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
