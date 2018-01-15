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

import io.github.rednesto.fileinventories.FileInventories;
import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.FileInventoriesService;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractLeftClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractRightClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvLeftClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvRightClickData;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileInventoriesServiceImpl implements FileInventoriesService {

    private Map<String, ItemDefinition> items = new HashMap<>();
    private Map<String, InventoryDefinition> inventories = new HashMap<>();

    private Map<UUID, InventoryDefinition> inventoryCache = new HashMap<>();

    private Map<String, Consumer<InteractEvent>> rightInteractHandlers = new HashMap<>();
    private Map<String, Consumer<InteractEvent>> leftInteractHandlers = new HashMap<>();
    private Map<String, Consumer<ClickInventoryEvent.Secondary>> rightClickHandlers = new HashMap<>();
    private Map<String, Consumer<ClickInventoryEvent.Primary>> leftClickHandlers = new HashMap<>();
    private Map<String, BiConsumer<Player, ItemStack>> createHandlers = new HashMap<>();
    private Map<String, BiConsumer<Player, Inventory>> invCreateHandlers = new HashMap<>();
    private Map<String, Consumer<ClickInventoryEvent.Secondary>> invRightClickHandlers = new HashMap<>();
    private Map<String, Consumer<ClickInventoryEvent.Primary>> invLeftClickHandlers = new HashMap<>();

    public FileInventoriesServiceImpl() {
        Sponge.getEventManager().registerListeners(FileInventories.instance, this);
    }

    @Override
    public void load(LoadTarget target, Path file) throws IOException {
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder().setPath(file).build();
        ConfigurationNode root = loader.load();

        switch(target) {
            case LOAD_ITEMS:
                root.getChildrenList().forEach(child -> {
                    items.put(child.getNode("id").getString(), new ItemDefinition(
                            child.getNode("id").getString(),
                            child.getNode("displayname").getString(),
                            child.getNode("type").getString(),
                            child.getNode("amount").getInt(1),
                            child.getNode("durability").getInt(1),
                            child.getNode("hide_attributes").getBoolean(false),
                            child.getNode("on_interact_right_click").getString(),
                            child.getNode("on_interact_left_click").getString(),
                            child.getNode("on_right_click").getString(),
                            child.getNode("on_left_click").getString(),
                            child.getNode("on_create").getString()
                    ));
                });
                break;
            case LOAD_INVS:
                root.getChildrenList().forEach(child -> {
                    inventories.put(child.getNode("id").getString(), new InventoryDefinition(
                            child.getNode("id").getString(),
                            child.getNode("title").getString(),
                            child.getNode("rows").getInt(),
                            child.getNode("items").getChildrenList().stream()
                                    .map(node -> new SlotDefinition(
                                            node.getNode("x").getInt(),
                                            node.getNode("y").getInt(),
                                            node.getNode("id").getString()))
                                    .collect(Collectors.toList()),
                            child.getNode("on_create").getString(),
                            child.getNode("on_inv_right_click").getString(),
                            child.getNode("on_inv_left_click").getString()
                    ));
                });
                break;
        }
    }

    @Override
    public Optional<Inventory> getInventory(String id, Player player) {
        if(!this.inventories.containsKey(id))
            return Optional.empty();

        InventoryDefinition definition = this.inventories.get(id);
        Inventory inventory = Inventory.builder()
                // TODO manage width too
                .property(InventoryDimension.of(9, definition.getRows()))
                .build(FileInventories.instance);

        definition.getItems().forEach(slotDefinition -> {
            Optional<ItemStack> maybeItem = getItem(slotDefinition.getItem(), player);
            if(!maybeItem.isPresent()) {
                FileInventories.instance.getLogger().warn("File item with ID " + slotDefinition.getItem() + " has not been found");
                return;
            }
            inventory.query(SlotPos.of(slotDefinition.getX(), slotDefinition.getY())).set(maybeItem.get());
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

        ItemStack.Builder builder = ItemStack.builder();

        if(definition.getMaterial() != null) {
            Optional<ItemType> maybeType = Sponge.getRegistry().getType(ItemType.class, definition.getMaterial());
            if(!maybeType.isPresent()) {
                FileInventories.instance.getLogger().warn("ItemType " + definition.getMaterial() + " does not exists in Sponge registry");
                return Optional.empty();
            }
            builder.itemType(maybeType.get());
        }

        if(definition.getAmount() != null)
            builder.quantity(definition.getAmount());

        if(definition.getDisplayname() != null)
            //noinspection deprecation
            builder.add(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize(definition.getDisplayname()));

        if(definition.getEnchantments() != null) {
            builder.add(Keys.ITEM_ENCHANTMENTS, definition.getEnchantments().stream()
                    .map(def -> Enchantment.of(def.getEnchantment(), def.getLevel()))
                    .collect(Collectors.toList()));
        }

        if(definition.getLore() != null)
            builder.add(Keys.ITEM_LORE, definition.getLore().stream().map(Text::of).collect(Collectors.toList()));

        if(definition.getDurability() != null)
            builder.add(Keys.ITEM_DURABILITY, definition.getDurability());

        // TODO add more HIDE_*
        builder.add(Keys.HIDE_ATTRIBUTES, definition.hideAttributes());

        builder.itemData(new OnInteractRightClickData())
                .itemData(new OnInteractLeftClickData())
                .itemData(new OnInvRightClickData())
                .itemData(new OnInvLeftClickData());

        ItemStack result = builder.build();

        if(definition.getOnInteractRightClickKey() != null)
            result.offer(FileInvKeys.ON_INTERACT_RIGHT_CLICK, definition.getOnInteractRightClickKey());

        if(definition.getOnInteractLeftClickKey() != null)
            result.offer(FileInvKeys.ON_INTERACT_LEFT_CLICK, definition.getOnInteractLeftClickKey());

        if(definition.getOnInvRightKey() != null)
            result.offer(FileInvKeys.ON_INV_RIGHT_CLICK, definition.getOnInvRightKey());

        if(definition.getOnInvLeftKey() != null)
            result.offer(FileInvKeys.ON_INV_LEFT_CLICK, definition.getOnInvLeftKey());

        if(definition.getOnCreateKey() != null && this.createHandlers.containsKey(definition.getOnCreateKey()))
            this.createHandlers.get(definition.getOnCreateKey()).accept(player, result);

        return Optional.of(result);
    }

    @Override
    public void registerRightInteractHandler(String id, Consumer<InteractEvent> handler) {
        this.rightInteractHandlers.put(id, handler);
    }

    @Override
    public void registerLeftInteractHandler(String id, Consumer<InteractEvent> handler) {
        this.leftInteractHandlers.put(id, handler);
    }

    @Override
    public void registerRightClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler) {
        this.rightClickHandlers.put(id, handler);
    }

    @Override
    public void registerLeftClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler) {
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
    public void registerInvRightClickHandler(String id, Consumer<ClickInventoryEvent.Secondary> handler) {
        this.invRightClickHandlers.put(id, handler);
    }

    @Override
    public void registerInvLeftClickHandler(String id, Consumer<ClickInventoryEvent.Primary> handler) {
        this.invLeftClickHandlers.put(id, handler);
    }


    @Listener
    public void onRightClick(InteractItemEvent.Secondary event) {
        event.getItemStack().get(FileInvKeys.ON_INTERACT_RIGHT_CLICK).ifPresent(key -> {
            if(rightInteractHandlers.containsKey(key))
                rightInteractHandlers.get(key).accept(event);
        });
    }

    @Listener
    public void onLeftClick(InteractItemEvent.Primary event) {
        event.getItemStack().get(FileInvKeys.ON_INTERACT_LEFT_CLICK).ifPresent(key -> {
            if(leftInteractHandlers.containsKey(key))
                leftInteractHandlers.get(key).accept(event);
        });
    }

    // TODO those two event listeners are workaround, waiting for Inventory.Builder#listener fix
    @Listener
    public void onSecondaryInvClick(ClickInventoryEvent.Secondary event, @First(typeFilter = Player.class) Player player) {
        InventoryDefinition definition = this.inventoryCache.get(player.getUniqueId());
        if(definition.getOnRightClickKey() != null)
            this.invRightClickHandlers.get(definition.getOnRightClickKey()).accept(event);

        if(event.getTransactions().size() < 1)
            return;

        Optional<String> key = event.getTransactions().get(0).getOriginal().get(FileInvKeys.ON_INV_RIGHT_CLICK);
        if(key.isPresent() && this.rightClickHandlers.containsKey(key.get()))
            this.rightClickHandlers.get(key.get()).accept(event);
    }

    @Listener
    public void onPrimaryInvClick(ClickInventoryEvent.Primary event, @First(typeFilter = Player.class) Player player) {
        InventoryDefinition definition = this.inventoryCache.get(player.getUniqueId());
        if(definition.getOnLeftClickKey() != null && this.invLeftClickHandlers.containsKey(definition.getOnLeftClickKey()))
            this.invLeftClickHandlers.get(definition.getOnLeftClickKey()).accept(event);

        if(event.getTransactions().size() < 1)
            return;

        Optional<String> key = event.getTransactions().get(0).getOriginal().get(FileInvKeys.ON_INV_LEFT_CLICK);
        if(key.isPresent() && this.leftClickHandlers.containsKey(key.get()))
            this.leftClickHandlers.get(key.get()).accept(event);
    }
}
