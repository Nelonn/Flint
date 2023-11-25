/*
 * Copyright 2023 Michael Neonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.nelonn.flint.path;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.OptionalInt;

public abstract class AbstractPath implements Path {
    private final String namespace;
    private final String value;

    @ApiStatus.Internal
    public AbstractPath(@NotNull String namespace, @NotNull String value) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(value, "value");
        OptionalInt index = Path.checkNamespace(namespace);
        if (index.isPresent()) {
            int indexValue = index.getAsInt();
            char character = namespace.charAt(indexValue);
            throw new PathException(namespace, value,
                    "Non [a-z0-9_.-] character in namespace at index " + indexValue + " ('" + character + "')");
        }
        index = checkValue(value);
        if (index.isPresent()) {
            int indexValue = index.getAsInt();
            char character = value.charAt(indexValue);
            throw new PathException(namespace, value,
                    "Non [a-z0-9_.-] character in value at index " + indexValue + " ('" + character + "')");
        }
        this.namespace = namespace;
        this.value = value;
    }

    protected @NotNull OptionalInt checkValue(@NotNull String value) {
        for (int i = 0, length = value.length(); i < length; i++) {
            if (!isAllowedInValue(value.charAt(i))) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    protected abstract boolean isAllowedInValue(char character);

    @Override
    public @NotNull String namespace() {
        return namespace;
    }

    @Override
    public @NotNull String value() {
        return value;
    }

    @Override
    public String toString() {
        return namespace() + ':' + value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path that)) return false;
        return Objects.equals(this.namespace(), that.namespace()) && Objects.equals(this.value(), that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }
}
