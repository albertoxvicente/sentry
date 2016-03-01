package org.apache.solr.sentry;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.carrotsearch.randomizedtesting.ThreadFilter;

public class HadoopThreadFilter implements ThreadFilter {

  @Override
  public boolean reject(Thread t) {
    // background daemon thread that is under normal circumstances, not stopped
    if ("org.apache.hadoop.fs.FileSystem$Statistics$StatisticsDataReferenceCleaner".equals(t.getName())) {
      return true;
    }
    return false;
  }
}
