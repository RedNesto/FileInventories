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

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.Map;

public class InventoryDefinition {

    private String id;

    @Nullable
    private String title;
    private Integer rows;
    private Map<String, String> items;
    @SerializedName("on_create")
    @Nullable
    private String onCreateKey;
    @SerializedName("on_inv_right_click")
    @Nullable
    private String onRightClickKey;
    @SerializedName("on_inv_left_click")
    @Nullable
    private String onLeftClickKey;

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

    public Map<String, String> getItems() {
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
