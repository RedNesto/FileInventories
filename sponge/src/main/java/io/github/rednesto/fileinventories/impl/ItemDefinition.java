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

public class ItemDefinition {

    private String id;

    @Nullable
    private String displayname;
    @Nullable
    private String material;
    @Nullable
    private Integer amount;
    @Nullable
    private Integer durability;
    @Nullable
    private List<EnchantmentDefinition> enchantments;
    @Nullable
    private List<String> lore;
    private boolean hideAttributes;
    private boolean hideEnchantments;
    private boolean hideUnbreakable;
    private boolean hideCanDestroy;
    private boolean hideCanPlace;
    private boolean hideMisc;
    @Nullable
    private String onInteractRightClickKey;
    @Nullable
    private String onInteractLeftClickKey;
    @Nullable
    private String onInvRightKey;
    @Nullable
    private String onInvLeftKey;
    @Nullable
    private String onCreateKey;

    public ItemDefinition(String id,
                          @Nullable String displayname,
                          @Nullable String material,
                          @Nullable Integer amount,
                          @Nullable Integer durability,
                          List<EnchantmentDefinition> enchantments,
                          List<String> lore,
                          boolean hideAttributes,
                          boolean hideEnchantments,
                          boolean hideUnbreakable,
                          boolean hideCanDestroy,
                          boolean hideCanPlace,
                          boolean hideMisc,
                          @Nullable String onInteractRightClickKey,
                          @Nullable String onInteractLeftClickKey,
                          @Nullable String onInvRightKey,
                          @Nullable String onInvLeftKey,
                          @Nullable String onCreateKey) {
        this.id = id;
        this.displayname = displayname;
        this.material = material;
        this.amount = amount;
        this.durability = durability;
        this.enchantments = enchantments;
        this.lore = lore;
        this.hideAttributes = hideAttributes;
        this.hideEnchantments = hideEnchantments;
        this.hideUnbreakable = hideUnbreakable;
        this.hideCanDestroy = hideCanDestroy;
        this.hideCanPlace = hideCanPlace;
        this.hideMisc = hideMisc;
        this.onInteractRightClickKey = onInteractRightClickKey;
        this.onInteractLeftClickKey = onInteractLeftClickKey;
        this.onInvRightKey = onInvRightKey;
        this.onInvLeftKey = onInvLeftKey;
        this.onCreateKey = onCreateKey;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getDisplayname() {
        return displayname;
    }

    @Nullable
    public String getMaterial() {
        return material;
    }

    @Nullable
    public Integer getAmount() {
        return amount;
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
    public Integer getDurability() {
        return durability;
    }

    @Nullable
    public String getOnCreateKey() {
        return onCreateKey;
    }

    @Nullable
    public String getOnInteractLeftClickKey() {
        return onInteractLeftClickKey;
    }

    @Nullable
    public String getOnInteractRightClickKey() {
        return onInteractRightClickKey;
    }

    public boolean hideAttributes() {
        return hideAttributes;
    }

    @Nullable
    public String getOnInvRightKey() {
        return onInvRightKey;
    }

    @Nullable
    public String getOnInvLeftKey() {
        return onInvLeftKey;
    }

    public boolean hideEnchantments() {
        return hideEnchantments;
    }

    public boolean hideUnbreakable() {
        return hideUnbreakable;
    }

    public boolean hideCanDestroy() {
        return hideCanDestroy;
    }

    public boolean hideCanPlace() {
        return hideCanPlace;
    }

    public boolean hideMisc() {
        return hideMisc;
    }
}
