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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.rednesto.fileinventories.FileInventories;
import io.github.rednesto.fileinventories.ItemStackBuilder;
import io.github.rednesto.fileinventories.SkullBuilder;
import io.github.rednesto.fileinventories.api.FileInventoriesService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileInventoriesServiceImpl implements FileInventoriesService, Listener {

    @SuppressWarnings("NullableProblems")
    private Map<String, ItemDefinition> items;
    @SuppressWarnings("NullableProblems")
    private Map<String, InventoryDefinition> inventories;

    private Map<UUID, InventoryDefinition> inventoryCache = new HashMap<>();

    private Map<String, Consumer<PlayerInteractEvent>> rightInteractHandlers = new HashMap<>();
    private Map<String, Consumer<PlayerInteractEvent>> leftInteractHandlers = new HashMap<>();
    private Map<String, Consumer<InventoryClickEvent>> rightClickHandlers = new HashMap<>();
    private Map<String, Consumer<InventoryClickEvent>> leftClickHandlers = new HashMap<>();
    private Map<String, BiConsumer<Player, ItemStack>> createHandlers = new HashMap<>();
    private Map<String, BiConsumer<Player, Inventory>> invCreateHandlers = new HashMap<>();
    private Map<String, Consumer<InventoryClickEvent>> invRightClickHandlers = new HashMap<>();
    private Map<String, Consumer<InventoryClickEvent>> invLeftClickHandlers = new HashMap<>();

    private static final Gson GSON = new Gson();

    public FileInventoriesServiceImpl() {
        Bukkit.getPluginManager().registerEvents(this, FileInventories.instance);
    }

    @Override
    public void load(LoadTarget target, File file) throws IOException {
        switch(target) {
            case LOAD_ITEMS:
                items = file.exists() ? ((List<ItemDefinition>) GSON.fromJson(new FileReader(file), new TypeToken<List<ItemDefinition>>(){}.getType())).stream().collect(Collectors.toMap(ItemDefinition::getId, def -> def)) : new HashMap<>();
                break;
            case LOAD_INVS:
                inventories = file.exists() ? ((List<InventoryDefinition>) GSON.fromJson(new FileReader(file), new TypeToken<List<InventoryDefinition>>(){}.getType())).stream().collect(Collectors.toMap(InventoryDefinition::getId, def -> def)) : new HashMap<>();
                break;
        }
    }

    @Override
    public Optional<Inventory> getInventory(String id, Player player) {
        if(!this.inventories.containsKey(id))
            return Optional.empty();

        InventoryDefinition definition = this.inventories.get(id);

        Inventory inventory = Bukkit.createInventory(null, definition.getRows() * 9, definition.getTitle() != null ? definition.getTitle() : "");

        definition.getItems().forEach((slot, itemId) -> {
            Optional<ItemStack> maybeItem = getItem(itemId, player);
            if(!maybeItem.isPresent()) {
                FileInventories.instance.getLogger().warning("File item with ID " + itemId + " has not been found");
                return;
            }
            inventory.setItem(Integer.parseInt(slot), maybeItem.get());
        });

        if(definition.getOnCreateKey() != null && this.invCreateHandlers.containsKey(definition.getOnCreateKey()))
            this.invCreateHandlers.get(definition.getOnCreateKey()).accept(player, inventory);

        this.inventoryCache.put(player.getUniqueId(), definition);

        return Optional.of(inventory);
    }

    @Override
    public void openInventory(String id, Player player) {
        getInventory(id, player).ifPresent(player::openInventory);
    }

    @Override
    public Optional<ItemStack> getItem(String id, Player player) {
        if(!this.items.containsKey(id))
            return Optional.empty();

        ItemDefinition definition = this.items.get(id);

        ItemStackBuilder builder;

        if(definition.getMaterial().equals("SKULL_ITEM")) {
            builder = new SkullBuilder().owner(player.getName());
        } else {
            builder = new ItemStackBuilder(Material.valueOf(definition.getMaterial()));
        }

        if(definition.getAmount() != null)
            builder.amount(definition.getAmount());

        if(definition.getDisplayname() != null)
            builder.displayName(FileInventories.applyColorCodes(definition.getDisplayname()));

        if(definition.getEnchantments() != null) {
            for(EnchantmentDefinition enchantement : definition.getEnchantments()) {
                builder.enchant(Enchantment.getByName(enchantement.getEnchantment()), enchantement.getLevel());
            }
        }

        if(definition.getLore() != null)
            builder.lore((definition.getLore().stream().map(FileInventories::applyColorCodes).toArray(String[]::new)));

        if(definition.getDurability() != null)
            builder.durability(definition.getDurability().shortValue());

        if(definition.getFlags() != null)
            builder.itemFlags(definition.getFlags().stream().map(ItemFlag::valueOf).collect(Collectors.toList()).toArray(new ItemFlag[0]));

        ItemStack result = FileInventories.adapter.addCustomString(builder.build(), "file_inv_id", definition.getId());

        if(definition.getOnCreateKey() != null && this.createHandlers.containsKey(definition.getOnCreateKey()))
            this.createHandlers.get(definition.getOnCreateKey()).accept(player, result);

        return Optional.of(result);
    }

    @Override
    public void registerRightInteractHandler(String id, Consumer<PlayerInteractEvent> handler) {
        this.rightInteractHandlers.put(id, handler);
    }

    @Override
    public void registerLeftInteractHandler(String id, Consumer<PlayerInteractEvent> handler) {
        this.leftInteractHandlers.put(id, handler);
    }

    @Override
    public void registerRightClickHandler(String id, Consumer<InventoryClickEvent> handler) {
        this.rightClickHandlers.put(id, handler);
    }

    @Override
    public void registerLeftClickHandler(String id, Consumer<InventoryClickEvent> handler) {
        this.leftClickHandlers.put(id, handler);
    }

    @Override
    public void registerCreateHandler(String id, BiConsumer<Player, ItemStack> handler) {
        this.createHandlers.put(id, handler);
    }

    @Override
    public void registerInvCreateHandler(String id, BiConsumer<Player, Inventory> handler) {
        this.invCreateHandlers.put(id, handler);
    }

    @Override
    public void registerInvRightClickHandler(String id, Consumer<InventoryClickEvent> handler) {
        this.invRightClickHandlers.put(id, handler);
    }

    @Override
    public void registerInvLeftClickHandler(String id, Consumer<InventoryClickEvent> handler) {
        this.invLeftClickHandlers.put(id, handler);
    }

    @EventHandler
    private void onInvClick(InventoryClickEvent event) {
        InventoryDefinition definition = this.inventoryCache.get(event.getWhoClicked().getUniqueId());

        switch(event.getClick()) {
            case RIGHT:
            case SHIFT_RIGHT:
                if(definition != null) {
                    Consumer<InventoryClickEvent> invHandler = this.invRightClickHandlers.get(definition.getOnRightClickKey());
                    if (invHandler != null)
                        invHandler.accept(event);
                }

                if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                    break;

                if(FileInventories.adapter == null) {
                    FileInventories.instance.getLogger().warning("FileInventories Adapter has not been loaded, maybe this version of Spigot is not supported.");
                    break;
                }

                Optional<String> itemId = FileInventories.adapter.getCustomString(event.getCurrentItem(), "file_inv_id");
                if(!itemId.isPresent())
                    break;

                Consumer<InventoryClickEvent> itemHandler = this.rightClickHandlers.get(this.items.get(itemId.get()).getOnRightClickKey());
                if(itemHandler != null)
                    itemHandler.accept(event);
                break;
            case LEFT:
            case SHIFT_LEFT:
                if(definition != null) {
                    Consumer<InventoryClickEvent> invHandler = this.invLeftClickHandlers.get(definition.getOnLeftClickKey());
                    if (invHandler != null)
                        invHandler.accept(event);
                }

                if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                    break;

                if(FileInventories.adapter == null) {
                    FileInventories.instance.getLogger().warning("FileInventories Adapter has not been loaded, maybe this version of Spigot is not supported.");
                    break;
                }

                itemId = FileInventories.adapter.getCustomString(event.getCurrentItem(), "file_inv_id");
                if(!itemId.isPresent())
                    break;

                itemHandler = this.leftClickHandlers.get(this.items.get(itemId.get()).getOnLeftClickKey());
                if(itemHandler != null)
                    itemHandler.accept(event);
                break;
            case WINDOW_BORDER_LEFT:
                break;
            case WINDOW_BORDER_RIGHT:
                break;
            case MIDDLE:
                break;
            case NUMBER_KEY:
                break;
            case DOUBLE_CLICK:
                break;
            case DROP:
                break;
            case CONTROL_DROP:
                break;
            case CREATIVE:
                break;
            case UNKNOWN:
                break;
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() == null || event.getItem().getType() == Material.AIR)
            return;

        switch(event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if(FileInventories.adapter == null) {
                    FileInventories.instance.getLogger().warning("FileInventories Adapter has not been loaded, maybe this version of Spigot is not supported.");
                    break;
                }

                Optional<String> itemId = FileInventories.adapter.getCustomString(event.getItem(), "file_inv_id");
                if(!itemId.isPresent())
                    break;

                Consumer<PlayerInteractEvent> itemHandler = this.rightInteractHandlers.get(this.items.get(itemId.get()).getOnInteractRightClickKey());
                if(itemHandler != null)
                    itemHandler.accept(event);
                break;
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if(FileInventories.adapter == null) {
                    FileInventories.instance.getLogger().warning("FileInventories Adapter has not been loaded, maybe this version of Spigot is not supported.");
                    break;
                }

                itemId = FileInventories.adapter.getCustomString(event.getItem(), "file_inv_id");
                if(!itemId.isPresent())
                    break;

                itemHandler = this.leftInteractHandlers.get(this.items.get(itemId.get()).getOnInteractLeftClickKey());
                if(itemHandler != null)
                    itemHandler.accept(event);
                break;
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.inventoryCache.remove(event.getPlayer().getUniqueId());
    }
}
