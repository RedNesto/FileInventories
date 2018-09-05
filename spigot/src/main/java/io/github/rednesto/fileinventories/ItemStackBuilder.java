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
package io.github.rednesto.fileinventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStackBuilder implements Listener {

    protected ItemStack itemStack;
    protected ItemMeta itemMeta;


    /**
     * @param itemStack l'ItemStack servant de base au builder
     */
    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.hasItemMeta() ? this.itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.itemStack.getType());
    }

    /**
     * @param material le matériel de l'ItemStack
     */
    public ItemStackBuilder(Material material) { this(new ItemStack(material)); }

    /**
     * @param material le matériel de l'ItemStack
     * @param amount le nombre d'items de l'ItemStack
     */
    public ItemStackBuilder(Material material, int amount) { this(new ItemStack(material, amount)); }

    /**
     * @param material le matériel de l'ItemStack
     * @param amount le nombre d'items de l'ItemStack
     * @param damage les dégâts infligés à l'ItemStack
     */
    public ItemStackBuilder(Material material, int amount, short damage) { this(new ItemStack(material, amount, damage)); }


    /**
     * Définit le nom de l'ItemStack qui sera affiché en jeu.
     * Il sera également utilisé lors du processing des events.
     *
     * @param displayName le nom de l'ItemStack qui sera affiché en jeu. La valeur null reset le displayName.
     *
     * @return ce builder
     */
    public ItemStackBuilder displayName(@Nullable String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    /**
     * Réinitialise le nom de l'ItemStack.
     * Équivaut à {@code #displayName(null)}
     *
     * @return ce builder
     */
    public ItemStackBuilder resetDisplayName() {
        return this.displayName(null);
    }

    /**
     * Ajoute un enchantement à cet ItemStack.
     *
     * @param enchantment l'enchantement à ajouter
     * @param level le niveau de cet enchantement
     *
     * @return ce builder
     */
    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Ajoute un enchantement pouvant être unsafe sur cet ItemStack.
     *
     * @param enchantment l'enchantement à ajouter
     * @param level le niveau de cet enchantement
     *
     * @return ce builder
     */
    public ItemStackBuilder unsafeEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Définit le type de l'ItemStack.
     *
     * @param type le type de l'ItemStack
     *
     * @return ce builder
     */
    public ItemStackBuilder type(Material type) {
        this.itemStack.setType(type);
        return this;
    }

    /**
     * Définit le nombre d'item sur cet ItemStack.
     *
     * @param amount le nombre d'items sur cet ItemStack
     *
     * @return ce builder
     */
    public ItemStackBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Définit la durabilité de l'ItemStack.
     *
     * @param durability la durabilité de l'ItemStack.
     *
     * @return ce builder
     */
    public ItemStackBuilder durability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    /**
     * Ajoute des {@link ItemFlag}s à l'ItemStack.
     *
     * @param itemFlags les ItemFlags à ajouter
     *
     * @return ce builder
     */
    public ItemStackBuilder itemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Indique si oui ou non l'ItemStack est indestructible.
     * Il est souvent plus pratique d'utiliser {@link #unbreakable()}
     *
     *
     * @param unbreakable si, et seulement si true, l'ItemStack sera indestructible.
     * @return ce builder
     */
    public ItemStackBuilder unbreakable(boolean unbreakable) {
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Définit l'ItemStack comme indestructible. Équivalent à {@code #unbreakable(true)}.
     *
     * @return ce builder
     */
    public ItemStackBuilder unbreakable() {
        return this.unbreakable(true);
    }

    /**
     * Définit le lore donnée en paramètre.
     *
     * @param lore le lore qu'aura l'{@link ItemStack}
     *
     * @return ce builder
     */
    public ItemStackBuilder lore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Ajoute au lore déjà présent les lignes données en paramètre.
     *
     * @param loreToAdd les lignes à ajouter au lore
     *
     * @return ce builder
     */
    public ItemStackBuilder addToLore(String... loreToAdd) {
        List<String> lore = this.itemMeta.hasLore() ? this.itemMeta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(loreToAdd));
        this.itemMeta.setLore(lore);

        return this;
    }


    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);

        Bukkit.getPluginManager().registerEvents(this, FileInventories.instance);

        return this.itemStack;
    }
}
