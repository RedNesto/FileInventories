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
package io.github.rednesto.fileinventories.api;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
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
     * Loads the file (if a file), or all the files contained (if a directory) from the object passed as argument.
     *
     * @param file a file, can be an inventory/item file, as well as a directory containing inventory/item files.
     */
    void load(LoadTarget target, Path file) throws IOException;

    /**
     * Generates the inventory bound to the given ID, and returns it.
     *
     * @param id the ID of the wanted file inventory.
     * @param player the player the generated inventory is destinated to.
     *
     * @return an Optional containing the inventory, or {@link Optional#empty()} if the ID has not been found.
     */
    Optional<Inventory> getInventory(String id, Player player);

    /**
     * Opens the inventory bound to the given ID to the given Player.
     *
     * @param id the ID of the inventory to open.
     * @param player the player to open the inventory to.
     */
    void openInventory(String id, Player player);

    /**
     * Generates the item bound to the given ID, and returns it.
     *
     * @param id the ID of the wanted file item.
     * @param player the player the generated item is destinated to.
     *
     * @return an Optional containing the item, or {@link Optional#empty()} if the ID has not been found.
     */
    Optional<ItemStack> getItem(String id, Player player);

    /**
     * Registers the handler bound to the given handler ID. When a player interacts with the item in-world.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerRightInteractHandler(String id, Consumer<InteractEvent> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player interacts with the item in-world.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerLeftInteractHandler(String id, Consumer<InteractEvent> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player right clicks the item in an inventory.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerRightClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler);

    /**
     * Registers the handler bound to the given handler ID. When a player left clicks the item in an inventory.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerLeftClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler);

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
    void registerInvRightClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler);

    /**
     * Registers the handler bound to the given handler ID. When the Inventory is left clicked.
     *
     * @param id the ID of the target handler.
     * @param handler the Handler bound to the ID.
     */
    void registerInvLeftClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler);

    enum LoadTarget {

        LOAD_ITEMS,
        LOAD_INVS
    }
}
