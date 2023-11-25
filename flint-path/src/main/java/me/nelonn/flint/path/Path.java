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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.OptionalInt;

public interface Path extends Comparable<Path> {
    @NotNull
    String namespace();

    @NotNull
    String value();

    static @NotNull Path of(@NotNull String namespace, @NotNull String value) {
        return new PathImpl(namespace, value);
    }

    static @NotNull Path of(@NotNull Pair<String, String> pair) {
        return of(pair.left(), pair.right());
    }

    static @NotNull Path withFallback(@NotNull String input, char delimiter, @NotNull String fallbackNamespace) {
        return of(decompose(input, delimiter, fallbackNamespace));
    }

    static @NotNull Path withFallback(@NotNull String input, @NotNull String fallbackNamespace) {
        return withFallback(input, DEFAULT_DELIMITER, fallbackNamespace);
    }
    
    static @NotNull Path of(@NotNull String input, char delimiter) {
        return withFallback(input, delimiter, DEFAULT_NAMESPACE);
    }

    static @NotNull Path of(@NotNull String input) {
        return of(input, DEFAULT_DELIMITER);
    }

    static @Nullable Path tryOrNull(@NotNull String input) {
        try {
            return of(input);
        } catch (PathException e) {
            return null;
        }
    }

    static @Nullable Path tryOrNull(@NotNull String namespace, @NotNull String value) {
        try {
            return of(namespace, value);
        } catch (PathException e) {
            return null;
        }
    }

    String DEFAULT_NAMESPACE = "minecraft";
    char DEFAULT_DELIMITER = ':';
    Comparator<? super Path> COMPARATOR = Comparator.comparing(Path::value).thenComparing(Path::namespace);

    static @NotNull Pair<String, String> decompose(@NotNull String input, char delimiter, @NotNull String fallbackNamespace) {
        int index = input.indexOf(delimiter);
        String namespace = index >= 1 ? input.substring(0, index) : fallbackNamespace;
        String value = index >= 0 ? input.substring(index + 1) : input;
        return new Pair<>(namespace, value);
    }

    static @NotNull OptionalInt checkNamespace(@NotNull String namespace) {
        for (int i = 0, length = namespace.length(); i < length; i++) {
            if (!isAllowedInNamespace(namespace.charAt(i))) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    static boolean isAllowedInNamespace(char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }

    @Override
    default int compareTo(@NotNull Path path) {
        return COMPARATOR.compare(this, path);
    }
}
