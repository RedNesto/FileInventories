/*
 * MIT License
 *
 * Copyright (c) 2017
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
package io.github.rednesto.fileinventories.impl;

import javax.annotation.Nullable;
import java.util.List;

public class InventoryDefinition {

    private String id;

    @Nullable
    private String title;
    private Integer rows;
    private List<SlotDefinition> items;
    @Nullable
    private String onCreateKey;
    @Nullable
    private String onRightClickKey;
    @Nullable
    private String onLeftClickKey;

    public InventoryDefinition(String id,
                               @Nullable String title,
                               Integer rows,
                               List<SlotDefinition> items,
                               @Nullable String onCreateKey,
                               @Nullable String onRightClickKey,
                               @Nullable String onLeftClickKey
                               ) {
        this.id = id;
        this.title = title;
        this.rows = rows;
        this.items = items;
        this.onCreateKey = onCreateKey;
        this.onRightClickKey = onRightClickKey;
        this.onLeftClickKey = onLeftClickKey;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public Integer getRows() {
        return rows;
    }

    public List<SlotDefinition> getItems() {
        return items;
    }

    @Nullable
    public String getOnCreateKey() {
        return onCreateKey;
    }

    @Nullable
    public String getOnRightClickKey() {
        return onRightClickKey;
    }

    @Nullable
    public String getOnLeftClickKey() {
        return onLeftClickKey;
    }
}
