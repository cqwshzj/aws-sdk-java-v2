/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.enhanced.dynamodb.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.BiConsumer;
import java.util.function.Function;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;

@RunWith(MockitoJUnitRunner.class)
public class ImmutableAttributeTest {
    private static final Function<Object, String> TEST_GETTER = x -> "test-getter";
    private static final BiConsumer<Object, String> TEST_SETTER = (x, y) -> {};

    @Mock
    private StaticAttributeTag mockTag;

    @Mock
    private StaticAttributeTag mockTag2;

    @Mock
    private AttributeConverter<String> attributeConverter;

    @Test
    public void build_maximal() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .tags(mockTag)
                              .attributeConverter(attributeConverter)
                              .build();

        assertThat(immutableAttribute.name()).isEqualTo("test-attribute");
        assertThat(immutableAttribute.getter()).isSameAs(TEST_GETTER);
        assertThat(immutableAttribute.setter()).isSameAs(TEST_SETTER);
        assertThat(immutableAttribute.tags()).containsExactly(mockTag);
        assertThat(immutableAttribute.type()).isEqualTo(EnhancedType.of(String.class));
        assertThat(immutableAttribute.attributeConverter()).isSameAs(attributeConverter);
    }

    @Test
    public void build_minimal() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .build();

        assertThat(immutableAttribute.name()).isEqualTo("test-attribute");
        assertThat(immutableAttribute.getter()).isSameAs(TEST_GETTER);
        assertThat(immutableAttribute.setter()).isSameAs(TEST_SETTER);
        assertThat(immutableAttribute.tags()).isEmpty();
        assertThat(immutableAttribute.type()).isEqualTo(EnhancedType.of(String.class));
    }

    @Test
    public void build_missing_name() {
        assertThatThrownBy(() -> ImmutableAttribute.builder(Object.class, Object.class, String.class)
                                                   .getter(TEST_GETTER)
                                                   .setter(TEST_SETTER)
                                                   .build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("name");
    }

    @Test
    public void build_missing_getter() {
        assertThatThrownBy(() -> ImmutableAttribute.builder(Object.class, Object.class, String.class)
                                                   .name("test-attribute")
                                                   .setter(TEST_SETTER)
                                                   .build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("getter");
    }

    @Test
    public void build_missing_setter() {
        assertThatThrownBy(() -> ImmutableAttribute.builder(Object.class, Object.class, String.class)
                                                   .name("test-attribute")
                                                   .getter(TEST_GETTER)
                                                   .build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("setter");
    }

    @Test
    public void toBuilder() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .tags(mockTag, mockTag2)
                              .attributeConverter(attributeConverter)
                              .build();

        ImmutableAttribute<Object, Object, String> clonedAttribute = immutableAttribute.toBuilder().build();

        assertThat(clonedAttribute.name()).isEqualTo("test-attribute");
        assertThat(clonedAttribute.getter()).isSameAs(TEST_GETTER);
        assertThat(clonedAttribute.setter()).isSameAs(TEST_SETTER);
        assertThat(clonedAttribute.tags()).containsExactly(mockTag, mockTag2);
        assertThat(clonedAttribute.type()).isEqualTo(EnhancedType.of(String.class));
        assertThat(clonedAttribute.attributeConverter()).isSameAs(attributeConverter);
    }

    @Test
    public void build_addTag_single() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .addTag(mockTag)
                              .build();

        assertThat(immutableAttribute.tags()).containsExactly(mockTag);
    }

    @Test
    public void build_addTag_multiple() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .addTag(mockTag)
                              .addTag(mockTag2)
                              .build();

        assertThat(immutableAttribute.tags()).containsExactly(mockTag, mockTag2);
    }

    @Test
    public void build_addAttributeConverter() {
        ImmutableAttribute<Object, Object, String> immutableAttribute =
            ImmutableAttribute.builder(Object.class, Object.class, String.class)
                              .name("test-attribute")
                              .getter(TEST_GETTER)
                              .setter(TEST_SETTER)
                              .attributeConverter(attributeConverter)
                              .build();

        AttributeConverter<String> attributeConverterR = immutableAttribute.attributeConverter();
        assertThat(attributeConverterR).isEqualTo(attributeConverter);
    }
}