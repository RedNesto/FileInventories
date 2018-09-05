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
package io.github.rednesto.fileinventories.api.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ConfigSerializable
public class ItemDefinition {

    @Setting("id")
    public String id = "";

    @Setting("type")
    public String type = "";

    @Setting("displayname")
    public String displayname = "";

    @Setting("amount")
    public int amount;

    @Setting("durability")
    public int durability;

    @Setting("enchantments")
    public List<EnchantmentDefinition> enchantments = Collections.emptyList();

    @Setting("lore")
    public List<String> lore = Collections.emptyList();

    @Setting("breakable_blocks")
    public Set<String> breakableBlocks = Collections.emptySet();
    @Setting("placeable_blocks")
    public Set<String> placeableBlocks = Collections.emptySet();

    @Setting("hide_attribute")
    public boolean hideAttributes;
    @Setting("hide_enchantments")
    public boolean hideEnchantments;
    @Setting("hide_unbreakable")
    public boolean hideUnbreakable;
    @Setting("hide_can_destroy")
    public boolean hideCanDestroy;
    @Setting("hide_can_place")
    public boolean hideCanPlace;
    @Setting("hide_misc")
    public boolean hideMisc;

    @Setting("on_interact_secondary_click")
    public String onInteractSecondaryClickKey = "";
    @Setting("on_interact_primary_click")
    public String onInteractPrimaryClickKey = "";

    @Setting("on_secondary_click")
    public String onInvSecondaryClickKey = "";
    @Setting("on_primary_click")
    public String onInvPrimaryClickKey = "";
    @Setting("on_middle_click")
    public String onInvMiddleClickKey = "";

    @Setting("on_create")
    public String onCreateKey = "";
}
