/*
 * MIT License
 *
 * Copyright (c) 2017 RedNesto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.rednesto.fileinventories.api;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class FileInvKeys {

    private FileInvKeys() {}

    public static final Key<Value<String>> ON_INTERACT_SECONDARY_CLICK = Key.builder()
            .type(new TypeToken<Value<String>>() {})
            .id("on_interact_secondary_click")
            .name("OnInteractSecondaryClick")
            .query(DataQuery.of("on_interact_secondary_click"))
            .build();
    public static final Key<Value<String>> ON_INTERACT_PRIMARY_CLICK = Key.builder()
            .type(new TypeToken<Value<String>>() {})
            .id("on_interact_primary_click")
            .name("OnInteractPrimaryClick")
            .query(DataQuery.of("on_interact_primary_click"))
            .build();

    public static final Key<Value<String>> ON_INV_SECONDARY_CLICK = Key.builder()
            .type(new TypeToken<Value<String>>() {})
            .id("on_inv_secondary_click")
            .name("OnInvSecondaryClick")
            .query(DataQuery.of("on_inv_secondary_click"))
            .build();
    public static final Key<Value<String>> ON_INV_PRIMARY_CLICK = Key.builder()
            .type(new TypeToken<Value<String>>() {})
            .id("on_inv_primary_click")
            .name("OnInvPrimaryClick")
            .query(DataQuery.of("on_inv_primary_click"))
            .build();
    public static final Key<Value<String>> ON_INV_MIDDLE_CLICK = Key.builder()
            .type(new TypeToken<Value<String>>() {})
            .id("on_inv_middle_click")
            .name("OnInvMiddleClick")
            .query(DataQuery.of("on_inv_middle_click"))
            .build();
}
