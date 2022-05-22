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

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Authentication;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SofarClient} is responsible for communicating with Sofar device
 *
 * @author Slava Belov - Initial contribution
 */
public class SofarClient {

    private final Logger logger = LoggerFactory.getLogger(SofarClient.class);

    private final String baseUrl;

    private final HttpClient httpClient;

    private final SofarConfiguration config;

    private static class BasicResult implements Authentication.Result {

        private final HttpHeader header;
        private final URI uri;
        private final String value;

        public BasicResult(HttpHeader header, URI uri, String value) {
            this.header = header;
            this.uri = uri;
            this.value = value;
        }

        @Override
        public URI getURI() {
            return this.uri;
        }

        @Override
        public void apply(Request request) {
            request.header(this.header, this.value);
        }

        @Override
        public String toString() {
            return String.format("Basic authentication result for %s", this.uri);
        }
    }

    public SofarClient(HttpClient httpClient, SofarConfiguration config) {
        this.httpClient = httpClient;
        this.config = config;
        this.baseUrl = "http://" + config.getHostname() + "/status.html";
        addPreemptiveAuthentication(httpClient, config);
    }

    private void addPreemptiveAuthentication(HttpClient httpClient, SofarConfiguration config) {
        AuthenticationStore auth = httpClient.getAuthenticationStore();
        URI uri = URI.create(baseUrl);
        auth.addAuthenticationResult(
                new BasicResult(HttpHeader.AUTHORIZATION, uri, "Basic " + Base64.getEncoder().encodeToString(
                        (config.getUsername() + ":" + config.getPassword()).getBytes(StandardCharsets.ISO_8859_1))));
    }

    public SofarInfo getSofarInfo() {
        SofarInfo currentInfo = new SofarInfo();
        currentInfo.setAlarm("");
        try {
            logger.debug("send HTTP GET to: {} ", baseUrl.toString());
            ContentResponse response = httpClient.newRequest(baseUrl).method(HttpMethod.GET)
                    .timeout(30000, TimeUnit.MILLISECONDS).send();
            String responseString = null;
            if (response.getEncoding() == null || response.getEncoding().isBlank()) {
                responseString = new String(response.getContent(), StandardCharsets.ISO_8859_1);
            } else {
                responseString = response.getContentAsString();
            }
            String lines[] = responseString.split("\\r?\\n");
            for (String tmpLine : lines) {
                if (tmpLine.startsWith("var webdata")) {
                    tmpLine = tmpLine.replaceAll("\"", "");
                    tmpLine = tmpLine.replaceAll(";", "");
                    String values[] = tmpLine.split(" ");
                    if (values.length == 4) {
                        switch (values[1]) {
                            case "webdata_sn":
                                currentInfo.setSn(values[3]);
                                break;
                            case "webdata_msvn":
                                currentInfo.setMsvn(values[3]);
                                break;
                            case "webdata_pv_type":
                                currentInfo.setPvType(values[3]);
                                break;
                            case "webdata_now_p":
                                currentInfo.setNowP(Float.parseFloat(values[3]));
                                break;
                            case "webdata_today_e":
                                currentInfo.setTodayE(Float.parseFloat(values[3]));
                                break;
                            case "webdata_total_e":
                                currentInfo.setTotalE(Float.parseFloat(values[3]));
                                break;
                            case "webdata_alarm":
                                currentInfo.setAlarm(values[3]);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            throw new SofarCommunicationException("Could not send command " + baseUrl, e);
        }
        return currentInfo;
    }
}
