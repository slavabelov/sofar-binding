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

import static org.openhab.binding.sofar.internal.SofarBindingConstants.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SofarHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Slava Belov - Initial contribution
 */
@NonNullByDefault
public class SofarHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(SofarHandler.class);

    private @Nullable SofarConfiguration config;

    private @Nullable ScheduledFuture<?> pollingJob;

    private @Nullable HttpClient httpClient;

    private @Nullable SofarClient sofarClient;

    public SofarHandler(Thing thing, HttpClient httpClient) {
        super(thing);
        this.httpClient = httpClient;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            refreshChannels(channelUID);
        } else {
            sendCommand(channelUID, command);
        }
        updateStatus(ThingStatus.ONLINE);
    }

    private void sendCommand(ChannelUID channelUID, Command command) {
        logger.info("sendCommand {} {}", channelUID.getId(), command.toString());
    }

    private void refreshChannels(ChannelUID channelUID) {
        logger.info("refreshChannels {}", channelUID.getId());
        refreshSofarInfo();
    }

    private void refreshSofarInfo() {
        logger.info("Refreshing Sofar data");
        SofarInfo currentInfo = sofarClient.getSofarInfo();
        updateState(CHANNEL_SN, new StringType(new String(currentInfo.getSn())));
        updateState(CHANNEL_MSVN, new StringType(new String(currentInfo.getMsvn())));
        updateState(CHANNEL_PV_TYPE, new StringType(new String(currentInfo.getPvType())));
        updateState(CHANNEL_NOW_P, new DecimalType(currentInfo.getNowP()));
        updateState(CHANNEL_TODAY_E, new DecimalType(currentInfo.getTodayE()));
        updateState(CHANNEL_TOTAL_E, new DecimalType(currentInfo.getTotalE()));
        updateState(CHANNEL_ALARM, new StringType(new String(currentInfo.getAlarm())));
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void initialize() {
        config = getConfigAs(SofarConfiguration.class);

        updateStatus(ThingStatus.UNKNOWN);

        try {
            httpClient.start();
            sofarClient = new SofarClient(httpClient, config);
        } catch (Exception e) {
            logger.error("Exception while trying to start http client", e);
            throw new RuntimeException("Exception while trying to start http client", e);
        }

        Runnable runnable = new SofarChannelPoller();
        int pollInterval = config.getRefreshInterval();
        pollingJob = scheduler.scheduleWithFixedDelay(runnable, 0, pollInterval, TimeUnit.SECONDS);
    }

    @Override
    public void dispose() {
        if (pollingJob != null) {
            pollingJob.cancel(true);
            pollingJob = null;
        }
        httpClient = null;
    }

    private class SofarChannelPoller implements Runnable {
        public SofarChannelPoller() {
        }

        @Override
        public void run() {
            refreshSofarInfo();
        }
    }
}
