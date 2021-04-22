/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.samza.system.elasticsearch.client;

import org.apache.samza.SamzaException;
import org.apache.samza.config.ElasticsearchConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * A {@link ClientFactory} that creates a {@link org.elasticsearch.node.Node} client that connects
 * and joins an Elasticsearch cluster.
 *
 * <p>
 * This requires both the host and port properties to be set.
 * Other settings can be configured via Elasticsearch settings in the properties of the Samza job.
 * </p>
 *
 */
public class TransportClientFactory implements ClientFactory {
  private final Map<String, String> clientSettings;
  private final InetAddress transportHost;
  private final int transportPort;

  public TransportClientFactory(ElasticsearchConfig config) {
    clientSettings = config.getElasticseachSettings();
    try {
      transportHost = InetAddress.getByName(config.getTransportHost().orElseThrow(() ->
              new SamzaException("You must specify the transport host for TransportClientFactory with the Elasticsearch system.")));
    } catch (UnknownHostException e) {
      throw new RuntimeException("Transport host is invalid", e);
    }

    transportPort = config.getTransportPort().orElseThrow(() ->
      new SamzaException("You must specify the transport port for TransportClientFactory with the Elasticsearch system."));
  }

  @Override
  public Client getClient() {
    Settings.Builder settingsBuilder =  Settings.builder();
    for (String setting:clientSettings.keySet()) {
      settingsBuilder.put(setting,clientSettings.get(setting));
    }

    TransportAddress address = new TransportAddress(transportHost, transportPort);

    TransportClient client = new PreBuiltTransportClient(settingsBuilder.build());
    client.addTransportAddress(address);

    return client;
  }
}
