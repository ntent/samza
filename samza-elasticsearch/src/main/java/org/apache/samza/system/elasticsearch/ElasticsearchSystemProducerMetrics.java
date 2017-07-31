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
package org.apache.samza.system.elasticsearch;

import org.apache.samza.metrics.Counter;
import org.apache.samza.metrics.MetricsBase;
import org.apache.samza.metrics.MetricsRegistry;

public class ElasticsearchSystemProducerMetrics extends MetricsBase {
    public final Counter bulkSendSuccess;
    public final Counter inserts;
    public final Counter updates;
    public final Counter conflicts;
    public final Counter deletes;

    public ElasticsearchSystemProducerMetrics(String systemName, MetricsRegistry registry) {
        super(systemName + "-", registry);

        bulkSendSuccess = newCounter("bulk-send-success");
        inserts = newCounter("docs-inserted");
        updates = newCounter("docs-updated");
        conflicts = newCounter("version-conflicts");
        deletes = newCounter("docs-deleted");
    }
}
