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

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The service used to interact with File Inventories.
 */
public interface FileInventoriesService {

    /**
     * Loads files (if a file), or all the files (if a directory) recursively from the given Path.
     * You can give as many paths as you want. They will all be loaded. You can mix files and folders paths.
     *
     * @param target the type of the elements to load.
     * @param paths files, they can be inventory/item files, as well as directories containing inventory/item files.
     */
    default void load(LoadTarget target, Path... paths) throws IOException {
        load(target, true, paths);
    }

    /**
     * Loads files (if a file), or all the files (if a directory) recursively (if recursive is true) from the given Path.
     * You can give as many paths as you want. They will all be loaded. You can mix files and folders paths.
     *
     * @param target the type of the elements to load.
     * @param paths files, they can be inventory/item files, as well as directories containing inventory/item files.
     * @param recursive whether the implementation will load contained directories too if file denotes a directory.
     */
    void load(LoadTarget target, boolean recursive, Path... paths) throws IOException;

    /**
     * Generates the inventory bound to the given ID, and returns it.
     *
     * @param id the ID of the wanted file inventory.
     * @param player the player to whom the generated inventory is destined to.
     *
     * @return an Optional containing the inventory, or {@link Optional#empty()} if the ID has not been found.
     */
    Optional<Inventory> getInventory(String id, Player player);

    /**
     * Opens the inventory bound to the given ID to the given Player.
     *
     * @param id the ID of the inventory to open.
     * @param player the player to whom open the inventory.
     *
     * @return the inventory that has been created and opened, or {@link Optional#empty()} if the ID has not been found.
     */
    default Optional<Inventory> openInventory(String id, Player player) {
        Optional<Inventory> maybeInventory = getInventory(id, player);
        maybeInventory.ifPresent(player::openInventory);

        return maybeInventory;
    }

    /**
     * Generates the item bound to the given ID, and returns it.
     *
     * @param id the ID of the wanted file item.
     * @param player the player the generated item is destined to.
     *
     * @return an Optional containing the item, or {@link Optional#empty()} if the ID has not been found.
     */
    Optional<ItemStack> getItem(String id, Player player);

    /**
     * Offers the itemstack generated via {@link #getItem(String, Player)} and offers it to the given Player.
     *
     * @param id the ID of the item to generate.
     * @param player the player to whom offer the generated itemstack.
     *
     * @return the generated itemstack, or {@link Optional#empty()} if the ID has not been found.
     */
    default Optional<ItemStack> offerItem(String id, Player player) {
        Optional<ItemStack> maybeItem = getItem(id, player);
        maybeItem.ifPresent(itemStack -> player.getInventory().offer(itemStack));

        return maybeItem;
    }

    /**
     * Offers the itemstack generated via {@link #getItem(String, Player)} and offers it to the given Player.
     *
     * @param id the ID of the item to generate.
     * @param player the player the generated item is destined to.
     * @param inventory the inventory that will be offered the generated itemstack
     *
     * @return the generated itemstack, or {@link Optional#empty()} if the ID has not been found.
     */
    default Optional<ItemStack> offerItem(String id, Player player, Inventory inventory) {
        Optional<ItemStack> maybeItem = getItem(id, player);
        maybeItem.ifPresent(inventory::offer);

        return maybeItem;
    }

    /**
     * Registers the handler bound to the given handler ID. When a player interacts with the item in-world.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerSecondaryInteractHandler(String id, Consumer<InteractItemEvent.Secondary> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player interacts with the item in-world.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerPrimaryInteractHandler(String id, Consumer<InteractItemEvent.Primary> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player right clicks the item in an inventory.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerSecondaryClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player left clicks the item in an inventory.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerPrimaryClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler);


    /**
     * Registers the handler bound to the given handler ID. When a player middle clicks the item in an inventory.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerMiddleClickHandler(String id, Consumer<ClickInventoryEvent.Middle> handler);

    /**
     * Registers the handler bound to the given handler ID. When the item is created.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerCreateHandler(String id, BiConsumer<Player, ItemStack> handler);

    /**
     * Registers the handler bound to the given handler ID. When the inventory is created.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerInvCreateHandler(String id, BiConsumer<Player, Inventory> handler);

    /**
     * Registers the handler bound to the given handler ID. When the Inventory is right clicked.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerInvSecondaryClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler);

    /**
     * Registers the handler bound to the given handler ID. When the Inventory is left clicked.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerInvPrimaryClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler);

    /**
     * Registers the handler bound to the given handler ID. When the Inventory is middle clicked.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerInvMiddleClickHandler(String id, Consumer<ClickInventoryEvent.Middle> handler);

    enum LoadTarget {

        ITEMS,
        INVENTORIES
    }
}
