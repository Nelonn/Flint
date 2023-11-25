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

public class KeyImpl extends AbstractPath implements Key {
    public static final String REGEXP = "[a-z0-9_.-]";

    @ApiStatus.Internal
    public KeyImpl(@NotNull String namespace, @NotNull String value) {
        super(namespace, value);
    }

    @Override
    protected @NotNull String getExceptionValueRegexp() {
        return REGEXP;
    }

    @Override
    protected boolean isAllowedInValue(char character) {
        return Key.isAllowedInKeyValue(character);
    }
}
