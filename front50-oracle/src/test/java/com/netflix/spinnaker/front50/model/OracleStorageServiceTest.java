/*
 * Copyright 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.front50.model;

import com.netflix.spinnaker.front50.config.OracleProperties;
import com.netflix.spinnaker.front50.model.application.Application;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

public class OracleStorageServiceTest {
  private String TEST_BUCKET_NAME = "_OracleStorageServiceTestBucket";

  private OracleStorageService getOracleStorageService() throws IOException {
    OracleProperties oracleProperties = new OracleProperties();
    oracleProperties.setCompartmentId("ocid1.compartment.oc1..aaaaaaaaqrqs4c36h2zicdfriufct6memet5n4radcv3nwughmtf6wcfp6sa");
    oracleProperties.setBucketName(TEST_BUCKET_NAME);
    oracleProperties.setNamespace("odx-pipelines");
    oracleProperties.setRegion("us-ashburn-1");
    oracleProperties.setUserId("ocid1.user.oc1..aaaaaaaav55sqkvw5axwb2rw3orbsbi7hsqh4bx3ptqz2pekbrbchbbfxkra");
    oracleProperties.setFingerprint("49:77:32:a6:09:e5:ba:3a:62:2e:15:f3:d8:21:0b:ca");
    oracleProperties.setFingerprint("12:22:b1:6a:da:24:53:93:6e:30:81:c4:c2:2b:f5:ad");
    oracleProperties.setSshPrivateKeyFilePath("//Users/guzhang/oci/oci_api_key.pem");
    oracleProperties.setSshPrivateKeyFilePath("//Users/guzhang/oci/gz_api_key_passphrase.pem");
    oracleProperties.setPrivateKeyPassphrase("welcome");
    oracleProperties.setTenancyId("ocid1.tenancy.oc1..aaaaaaaax35wv5a7d7xtbdi62tz25kw2so3mg2oiiexredhsxq27rfiqilva");
    return new OracleStorageService(oracleProperties);
  }

  @Test
  public void testOracleStorageService() throws IOException {
    OracleStorageService service = getOracleStorageService();

    service.ensureBucketExists();

    Map<String, Long> keys = service.listObjectKeys(ObjectType.APPLICATION);
    assertThat(keys.isEmpty());

    String objectKey = "TESTKEY";
    Application object = new Application();
    object.setName(objectKey);
    service.storeObject(ObjectType.APPLICATION, objectKey, object);
    keys = service.listObjectKeys(ObjectType.APPLICATION);
    assertThat(!keys.isEmpty());


    Application app = service.loadObject(ObjectType.APPLICATION, objectKey);
    System.out.println(app.toString());

    service.deleteObject(ObjectType.APPLICATION, objectKey);

    keys = service.listObjectKeys(ObjectType.APPLICATION);
    assertThat(keys.isEmpty());
  }
}
