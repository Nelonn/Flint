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

public interface Key extends Path {

    static @NotNull Key of(@NotNull String namespace, @NotNull String value) {
        return new KeyImpl(namespace, value);
    }

    static @NotNull Key of(@NotNull Pair<String, String> pair) {
        return of(pair.left(), pair.right());
    }

    static @NotNull Key withFallback(@NotNull String input, char delimiter, @NotNull String fallbackNamespace) {
        return of(Path.decompose(input, delimiter, fallbackNamespace));
    }

    static @NotNull Key withFallback(@NotNull String input, @NotNull String fallbackNamespace) {
        return withFallback(input, DEFAULT_DELIMITER, fallbackNamespace);
    }

    static @NotNull Key of(@NotNull String input, char delimiter) {
        return withFallback(input, delimiter, DEFAULT_NAMESPACE);
    }

    static @NotNull Key of(@NotNull String input) {
        return of(input, DEFAULT_DELIMITER);
    }

    static @Nullable Key tryOrNull(@NotNull String input) {
        try {
            return of(input);
        } catch (PathException e) {
            return null;
        }
    }

    static @Nullable Key tryOrNull(@NotNull String namespace, @NotNull String value) {
        try {
            return of(namespace, value);
        } catch (PathException e) {
            return null;
        }
    }

    static boolean isAllowedInKeyValue(char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }

}
