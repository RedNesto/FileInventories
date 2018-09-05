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
package io.github.rednesto.fileinventories.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ItemDefinition {

    private String id;

    @Nullable
    private String displayname;
    private String material;
    @Nullable
    private Integer amount;
    @Nullable
    private Integer durability;
    @Nullable
    private List<EnchantmentDefinition> enchantments = new ArrayList<>();
    @Nullable
    private List<String> lore = new ArrayList<>();
    @Nullable
    private List<String> flags = new ArrayList<>();
    private boolean hideAttributes;

    @SerializedName("on_interact_right_click")
    @Nullable
    private String onInteractRightClickKey;
    @SerializedName("on_interact_left_click")
    @Nullable
    private String onInteractLeftClickKey;
    @SerializedName("on_right_click")
    @Nullable
    private String onRightClickKey;
    @SerializedName("on_left_click")
    @Nullable
    private String onLeftClickKey;
    @SerializedName("on_create")
    @Nullable
    private String onCreateKey;

    public String getId() {
        return id;
    }

    @Nullable
    public String getDisplayname() {
        return displayname;
    }

    public String getMaterial() {
        return material;
    }

    @Nullable
    public Integer getAmount() {
        return amount;
    }

    @Nullable
    public Integer getDurability() {
        return durability;
    }

    @Nullable
    public List<EnchantmentDefinition> getEnchantments() {
        return enchantments;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    @Nullable
    public List<String> getFlags() {
        return flags;
    }

    public boolean isHideAttributes() {
        return hideAttributes;
    }

    @Nullable
    public String getOnInteractRightClickKey() {
        return onInteractRightClickKey;
    }

    @Nullable
    public String getOnInteractLeftClickKey() {
        return onInteractLeftClickKey;
    }

    @Nullable
    public String getOnRightClickKey() {
        return onRightClickKey;
    }

    @Nullable
    public String getOnLeftClickKey() {
        return onLeftClickKey;
    }

    @Nullable
    public String getOnCreateKey() {
        return onCreateKey;
    }
}
