/*
 * Copyright Consensys Software Inc., 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.beaconrestapi.handlers.v1.beacon;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.pegasys.teku.infrastructure.http.HttpStatusCodes.SC_NO_CONTENT;
import static tech.pegasys.teku.infrastructure.http.HttpStatusCodes.SC_SERVICE_UNAVAILABLE;
import static tech.pegasys.teku.infrastructure.restapi.MetadataTestUtil.verifyMetadataEmptyResponse;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import tech.pegasys.teku.beaconrestapi.AbstractPostBlockTest;
import tech.pegasys.teku.infrastructure.http.ContentTypes;
import tech.pegasys.teku.infrastructure.restapi.endpoints.RestApiEndpoint;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.datastructures.blocks.SignedBeaconBlock;
import tech.pegasys.teku.spec.datastructures.blocks.versions.deneb.SignedBlockContents;

public class PostBlockTest extends AbstractPostBlockTest {

  @Override
  public RestApiEndpoint getHandler() {
    return new PostBlock(validatorDataProvider, syncDataProvider, spec, schemaDefinitionCache);
  }

  @Override
  public boolean isBlinded() {
    return false;
  }

  @Test
  void shouldAcceptBlockAsSsz() throws Exception {
    final SignedBeaconBlock data = dataStructureUtil.randomSignedBeaconBlock(3);
    final SignedBeaconBlock result =
        handler
            .getMetadata()
            .getRequestBody(
                new ByteArrayInputStream(data.sszSerialize().toArrayUnsafe()),
                Optional.of(ContentTypes.OCTET_STREAM));
    assertThat(result).isEqualTo(data);
  }

  @Test
  void shouldAcceptBlockContentsAsSsz() throws Exception {
    setupDeneb();
    final SignedBlockContents data = dataStructureUtil.randomSignedBlockContents(UInt64.ONE);
    final SignedBlockContents result =
        handler
            .getMetadata()
            .getRequestBody(
                new ByteArrayInputStream(data.sszSerialize().toArrayUnsafe()),
                Optional.of(ContentTypes.OCTET_STREAM));
    assertThat(result).isEqualTo(data);
  }

  @Test
  void metadata_shouldHandle204() {
    verifyMetadataEmptyResponse(handler, SC_NO_CONTENT);
  }

  @Test
  void metadata_shouldHandle503() {
    verifyMetadataEmptyResponse(handler, SC_SERVICE_UNAVAILABLE);
  }
}
