/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sentry.tests.e2e.solr;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;

public class TestQueryOperations extends AbstractSolrSentryTestWithFileProvider {

  private static final Logger LOG = LoggerFactory
      .getLogger(TestQueryOperations.class);
  private static final String COLLECTION_NAME = "sentryCollection";
  private static final List<Boolean> BOOLEAN_VALUES = Arrays.asList(new Boolean[]{true, false});
  private static final String DEFAULT_COLLECTION = "collection1";

  @Test
  public void testQueryOps() throws Exception {
    // Upload configs to ZK
    uploadConfigDirToZk(RESOURCES_DIR + File.separator + DEFAULT_COLLECTION
        + File.separator + "conf");
    setupCollection(COLLECTION_NAME);
    ArrayList<String> testFailures = new ArrayList<String>();

    for (boolean query : BOOLEAN_VALUES) {
      for (boolean update : BOOLEAN_VALUES) {
        for (boolean all : BOOLEAN_VALUES) {
          String test_user = getUsernameForPermissions(COLLECTION_NAME, query, update, all);
          LOG.info("TEST_USER: " + test_user);

          try {
            cleanSolrCollection(COLLECTION_NAME);
            SolrInputDocument solrInputDoc = createSolrTestDoc();
            uploadSolrDoc(COLLECTION_NAME, solrInputDoc);
            if (all || query) {
              verifyQueryPass(test_user, COLLECTION_NAME, ALL_DOCS);
            } else {
              verifyQueryFail(test_user, COLLECTION_NAME, ALL_DOCS);
            }
          } catch (Throwable testException) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            testException.printStackTrace(printWriter);
            testFailures.add("\n\nTestFailure: User -> " + test_user + "\n"
                + stringWriter.toString());
          }
        }
      }
    }

    assertEquals("Total test failures: " + testFailures.size() + " \n\n"
        + testFailures.toString() + "\n\n\n", 0, testFailures.size());
  }
}
