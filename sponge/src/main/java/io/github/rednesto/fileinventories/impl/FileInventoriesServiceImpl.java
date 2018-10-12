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

import com.google.common.reflect.TypeToken;
import io.github.rednesto.fileinventories.FileInventories;
import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.FileInventoriesService;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractSecondaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvMiddleClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvSecondaryClickData;
import io.github.rednesto.fileinventories.api.serialization.InventoryDefinition;
import io.github.rednesto.fileinventories.api.serialization.ItemDefinition;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileInventoriesServiceImpl implements FileInventoriesService {

    private final Map<String, ItemDefinition> items = new HashMap<>();
    private final Map<String, InventoryDefinition> inventories = new HashMap<>();

    private final Map<UUID, InventoryDefinition> inventoryCache = new HashMap<>();

    private final Map<String, Consumer<InteractItemEvent.Secondary>> secondaryInteractHandlers = new HashMap<>();
    private final Map<String, Consumer<InteractItemEvent.Primary>> primaryInteractHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Secondary>> secondaryClickHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Primary>> primaryClickHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Middle>> middleClickHandlers = new HashMap<>();
    private final Map<String, BiConsumer<Player, ItemStack>> createHandlers = new HashMap<>();
    private final Map<String, BiConsumer<Player, Inventory>> invCreateHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Secondary>> invSecondaryClickHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Primary>> invPrimaryClickHandlers = new HashMap<>();
    private final Map<String, Consumer<ClickInventoryEvent.Middle>> invMiddleClickHandlers = new HashMap<>();

    public FileInventoriesServiceImpl() {
        Sponge.getEventManager().registerListeners(FileInventories.getInstance(), this);
    }

    @Override
    public void load(LoadTarget target, boolean recursive, Path... paths) throws IOException {
        for (Path path : paths) {
            if (Files.isDirectory(path)) {
                List<Path> subPaths = Files.list(path).collect(Collectors.toList());
                for (Path subPath : subPaths) {
                    load(target, recursive, subPath);
                }
            } else {
                loadFile(target, path);
            }
        }
    }

    private void loadFile(LoadTarget target, Path path) throws IOException {
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder().setPath(path).build();
        ConfigurationNode root = loader.load();

        switch (target) {
            case ITEMS:
                try {
                    // @formatter:off
                    List<ItemDefinition> items = root.getValue(new TypeToken<List<ItemDefinition>>() {}, Collections.emptyList());
                    // @formatter:on

                    for (ItemDefinition item : items)
                        this.items.put(item.id, item);
                } catch (ObjectMappingException e) {
                    throw new IOException(e);
                }
                break;
            case INVENTORIES:
                try {
                    // @formatter:off
                    List<InventoryDefinition> inventories = root.getValue(new TypeToken<List<InventoryDefinition>>() {}, Collections.emptyList());
                    // @formatter:on

                    for (InventoryDefinition inventory : inventories)
                        this.inventories.put(inventory.id, inventory);
                } catch (ObjectMappingException e) {
                    throw new IOException(e);
                }
                break;
        }
    }

    @Override
    public Optional<Inventory> getInventory(String id, Player player) {
        InventoryDefinition definition = this.inventories.get(id);
        if (definition == null)
            return Optional.empty();

        if (definition.rows < 1)
            throw new IllegalArgumentException("Inventory 'rows' cannot be lesser than 1. FileInventory id: " + definition.id);

        Inventory inventory = Inventory.builder()
                .property(InventoryDimension.of(9, definition.rows))
                .build(FileInventories.getInstance());

        definition.items.forEach(slot -> {
            if (slot.x < 0)
                throw new IllegalArgumentException("Slot 'x' cannot be lesser than zero. FileInventory id: " + definition.id);

            if (slot.y < 0)
                throw new IllegalArgumentException("Slot 'y' cannot be lesser than zero. FileInventory id: " + definition.id);

            ItemStack itemStack = null;
            if (!slot.id.isEmpty()) {
                Optional<ItemStack> maybeItem = getItem(slot.id, player);

                if (!maybeItem.isPresent())
                    throw new IllegalArgumentException("Unknown FileItem id: " + slot.id + ". FileInventory id: " + id);
                itemStack = maybeItem.get();
            } else if (!slot.type.isEmpty()) {
                Optional<ItemType> maybeItemType = Sponge.getRegistry().getType(ItemType.class, slot.type);

                if (!maybeItemType.isPresent())
                    throw new IllegalArgumentException("Unknown item id: " + slot.type + ". FileInventory id: " + id);

                itemStack = ItemStack.of(maybeItemType.get(), 1);
            }

            if (itemStack == null)
                throw new NullPointerException("The slot ItemStack is null. This situation is impossible in normal conditions.");

            inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(slot.x, slot.y))).set(itemStack);
        });

        if (!definition.onCreateKey.isEmpty() && this.invCreateHandlers.containsKey(definition.onCreateKey))
            this.invCreateHandlers.get(definition.onCreateKey).accept(player, inventory);

        this.inventoryCache.put(player.getUniqueId(), definition);

        return Optional.of(inventory);
    }

    @Override
    public Optional<ItemStack> getItem(String id, Player player) {
        ItemDefinition definition = this.items.get(id);
        if (definition == null)
            return Optional.empty();

        ItemStack.Builder builder = ItemStack.builder();

        if (definition.type.isEmpty())
            throw new IllegalArgumentException("'type' is a required item property. FileItem id: " + id);

        Optional<ItemType> maybeType = Sponge.getRegistry().getType(ItemType.class, definition.type);
        if (!maybeType.isPresent())
            throw new IllegalArgumentException("Unknown 'type' id: " + definition.type + ". FileItem id: " + id);

        builder.itemType(maybeType.get());

        if (definition.amount > 0)
            builder.quantity(definition.amount);


        if (!definition.onInteractPrimaryClickKey.isEmpty())
            builder.itemData(new OnInteractSecondaryClickData());

        if (!definition.onInteractPrimaryClickKey.isEmpty())
            builder.itemData(new OnInteractPrimaryClickData());


        if (!definition.onInvSecondaryClickKey.isEmpty())
            builder.itemData(new OnInvSecondaryClickData());

        if (!definition.onInvPrimaryClickKey.isEmpty())
            builder.itemData(new OnInvPrimaryClickData());

        if (!definition.onInvMiddleClickKey.isEmpty())
            builder.itemData(new OnInvMiddleClickData());

        ItemStack result = builder.build();

        if (!definition.displayname.isEmpty())
            result.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize(definition.displayname));

        if (!definition.enchantments.isEmpty()) {
            result.offer(Keys.ITEM_ENCHANTMENTS, definition.enchantments.stream()
                    .map(enchantment -> {
                        if (enchantment.type.isEmpty())
                            throw new IllegalArgumentException("You must specify and enchantment id");

                        EnchantmentType enchantmentType = Sponge.getRegistry().getType(EnchantmentType.class, enchantment.type).orElseThrow(() ->
                                new IllegalArgumentException("Unknown enchantment id: " + enchantment.type + ". Item id: " + id));
                        return Enchantment.of(
                                enchantmentType,
                                enchantment.level);
                    })
                    .collect(Collectors.toList()));
        }

        if (!definition.lore.isEmpty())
            result.offer(Keys.ITEM_LORE, definition.lore.stream().map(TextSerializers.FORMATTING_CODE::deserialize).collect(Collectors.toList()));

        if (definition.durability > 0)
            result.offer(Keys.ITEM_DURABILITY, definition.durability);


        if (definition.hideAttributes)
            result.offer(Keys.HIDE_ATTRIBUTES, true);

        if (definition.hideEnchantments)
            result.offer(Keys.HIDE_ENCHANTMENTS, true);

        if (definition.hideUnbreakable)
            result.offer(Keys.HIDE_UNBREAKABLE, true);

        if (definition.hideCanDestroy)
            result.offer(Keys.HIDE_CAN_DESTROY, true);

        if (definition.hideCanPlace)
            result.offer(Keys.HIDE_CAN_PLACE, true);

        if (definition.hideMisc)
            result.offer(Keys.HIDE_MISCELLANEOUS, true);


        if (!definition.breakableBlocks.isEmpty()) {
            result.offer(Keys.BREAKABLE_BLOCK_TYPES, definition.breakableBlocks
                    .stream()
                    .map(blockId -> Sponge.getRegistry().getType(BlockType.class, blockId)
                            .orElseThrow(() -> new IllegalArgumentException("Unknown block id: " + blockId + ". Item id: " + id)))
                    .collect(Collectors.toSet()));
        }

        if (!definition.placeableBlocks.isEmpty()) {
            result.offer(Keys.PLACEABLE_BLOCKS, definition.placeableBlocks
                    .stream()
                    .map(blockId -> Sponge.getRegistry().getType(BlockType.class, blockId)
                            .orElseThrow(() -> new IllegalArgumentException("Unknown block id: " + blockId + ". Item id: " + id)))
                    .collect(Collectors.toSet()));
        }


        if (!definition.onInteractSecondaryClickKey.isEmpty())
            result.offer(FileInvKeys.ON_INTERACT_SECONDARY_CLICK, definition.onInteractSecondaryClickKey);

        if (!definition.onInteractPrimaryClickKey.isEmpty())
            result.offer(FileInvKeys.ON_INTERACT_PRIMARY_CLICK, definition.onInteractPrimaryClickKey);


        if (!definition.onInvSecondaryClickKey.isEmpty())
            result.offer(FileInvKeys.ON_INV_SECONDARY_CLICK, definition.onInvSecondaryClickKey);

        if (!definition.onInvPrimaryClickKey.isEmpty())
            result.offer(FileInvKeys.ON_INV_PRIMARY_CLICK, definition.onInvPrimaryClickKey);

        if (!definition.onInvMiddleClickKey.isEmpty())
            result.offer(FileInvKeys.ON_INV_MIDDLE_CLICK, definition.onInvMiddleClickKey);

        if (!definition.onCreateKey.isEmpty() && this.createHandlers.containsKey(definition.onCreateKey))
            this.createHandlers.get(definition.onCreateKey).accept(player, result);

        return Optional.of(result);
    }

    @Override
    public void registerSecondaryInteractHandler(String id, Consumer<InteractItemEvent.Secondary> handler) {
        this.secondaryInteractHandlers.put(id, handler);
    }

    @Override
    public void registerPrimaryInteractHandler(String id, Consumer<InteractItemEvent.Primary> handler) {
        this.primaryInteractHandlers.put(id, handler);
    }

    @Override
    public void registerSecondaryClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler) {
        this.secondaryClickHandlers.put(id, handler);
    }

    @Override
    public void registerPrimaryClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler) {
        this.primaryClickHandlers.put(id, handler);
    }

    @Override
    public void registerMiddleClickHandler(String id, Consumer<ClickInventoryEvent.Middle> handler) {
        this.middleClickHandlers.put(id, handler);
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
    public void registerInvSecondaryClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler) {
        this.invSecondaryClickHandlers.put(id, handler);
    }

    @Override
    public void registerInvPrimaryClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler) {
        this.invPrimaryClickHandlers.put(id, handler);
    }

    @Override
    public void registerInvMiddleClickHandler(String id, Consumer<ClickInventoryEvent.Middle> handler) {
        this.invMiddleClickHandlers.put(id, handler);
    }


    @Listener
    public void onSecondaryClick(InteractItemEvent.Secondary event) {
        event.getItemStack().get(FileInvKeys.ON_INTERACT_SECONDARY_CLICK).ifPresent(key -> {
            if (secondaryInteractHandlers.containsKey(key))
                secondaryInteractHandlers.get(key).accept(event);
        });
    }

    @Listener
    public void onPrimaryClick(InteractItemEvent.Primary event) {
        event.getItemStack().get(FileInvKeys.ON_INTERACT_PRIMARY_CLICK).ifPresent(key -> {
            if (primaryInteractHandlers.containsKey(key))
                primaryInteractHandlers.get(key).accept(event);
        });
    }


    // TODO those two event listeners are workaround, waiting for Inventory.Builder#listener fix
    @Listener
    public void onSecondaryInvClick(ClickInventoryEvent.Secondary event, @First Player player) {
        InventoryDefinition definition = this.inventoryCache.get(player.getUniqueId());

        if (definition == null)
            return;

        if (!definition.onSecondaryClickKey.isEmpty()) {
            Consumer<ClickInventoryEvent.Secondary> eventConsumer = this.invSecondaryClickHandlers.get(definition.onSecondaryClickKey);
            if (eventConsumer != null)
                eventConsumer.accept(event);
        }

        event.getTransactions().forEach(transaction -> {
            Optional<String> key = transaction.getOriginal().get(FileInvKeys.ON_INV_SECONDARY_CLICK);

            if (key.isPresent()) {
                Consumer<ClickInventoryEvent.Secondary> eventConsumer = this.secondaryClickHandlers.get(key.get());
                if (eventConsumer != null)
                    eventConsumer.accept(event);
            }
        });
    }

    @Listener
    public void onPrimaryInvClick(ClickInventoryEvent.Primary event, @First Player player) {
        InventoryDefinition definition = this.inventoryCache.get(player.getUniqueId());

        if (definition == null)
            return;

        if (!definition.onPrimaryClickKey.isEmpty()) {
            Consumer<ClickInventoryEvent.Primary> eventConsumer = this.invPrimaryClickHandlers.get(definition.onPrimaryClickKey);
            if (eventConsumer != null)
                eventConsumer.accept(event);
        }

        event.getTransactions().forEach(transaction -> {
            Optional<String> key = transaction.getOriginal().get(FileInvKeys.ON_INV_PRIMARY_CLICK);

            if (key.isPresent()) {
                Consumer<ClickInventoryEvent.Primary> eventConsumer = this.primaryClickHandlers.get(key.get());
                if (eventConsumer != null)
                    eventConsumer.accept(event);
            }
        });
    }

    @Listener
    public void onMiddleInvClick(ClickInventoryEvent.Middle event, @First Player player) {
        InventoryDefinition definition = this.inventoryCache.get(player.getUniqueId());

        if (definition == null)
            return;

        if (!definition.onMiddleClickKey.isEmpty()) {
            Consumer<ClickInventoryEvent.Middle> eventConsumer = this.invMiddleClickHandlers.get(definition.onMiddleClickKey);
            if (eventConsumer != null)
                eventConsumer.accept(event);
        }

        event.getTransactions().forEach(transaction -> {
            Optional<String> key = transaction.getOriginal().get(FileInvKeys.ON_INV_MIDDLE_CLICK);

            if (key.isPresent()) {
                Consumer<ClickInventoryEvent.Middle> eventConsumer = this.middleClickHandlers.get(key.get());
                if (eventConsumer != null)
                    eventConsumer.accept(event);
            }
        });
    }
}
