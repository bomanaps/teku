/*
 * Copyright 2022 ConsenSys AG.
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

package tech.pegasys.teku.infrastructure.json.types;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ByteTypeDefinitionTest {
  @ParameterizedTest
  @MethodSource("testValues")
  void shouldSerializeAsHex(final byte value, final String serialized) {
    assertThat(CoreTypes.BYTE_TYPE.serializeToString(value)).isEqualTo(serialized);
  }

  @ParameterizedTest
  @MethodSource("testValues")
  void shouldDeserializeFromHex(final byte value, final String serialized) {
    assertThat(CoreTypes.BYTE_TYPE.deserializeFromString(serialized)).isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("testValues")
  void shouldDeserializeFromHexWithoutPrefix(final byte value, final String serialized) {
    assertThat(CoreTypes.BYTE_TYPE.deserializeFromString(serialized.substring("0x".length())))
        .isEqualTo(value);
  }

  static Stream<Arguments> testValues() {
    return Stream.of(
        Arguments.of(Byte.MIN_VALUE, "0x80"),
        Arguments.of((byte) -1, "0xff"),
        Arguments.of((byte) 0, "0x00"),
        Arguments.of((byte) 1, "0x01"),
        Arguments.of(Byte.MAX_VALUE, "0x7f"));
  }
}
