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
package io.github.rednesto.fileinventories.example;

import io.github.rednesto.fileinventories.api.FileInventories;
import io.github.rednesto.fileinventories.api.FileInventoriesService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class FileInventoriesExample extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        try {
            if(!new File(getDataFolder(), "items.json").exists())
                Files.copy(getClass().getResourceAsStream("/items.json"), new File(getDataFolder(), "items.json").toPath(), StandardCopyOption.REPLACE_EXISTING);
            if(!new File(getDataFolder(), "inventories.json").exists())
                Files.copy(getClass().getResourceAsStream("/inventories.json"), new File(getDataFolder(), "inventories.json").toPath(), StandardCopyOption.REPLACE_EXISTING);

            FileInventories.getService().load(FileInventoriesService.LoadTarget.LOAD_ITEMS, new File(getDataFolder(), "items.json"));
            FileInventories.getService().load(FileInventoriesService.LoadTarget.LOAD_INVS, new File(getDataFolder(), "inventories.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInventories.getService().registerRightClickHandler("test_item_onrightclick", event -> {
            event.getWhoClicked().sendMessage("Right");
        });
        FileInventories.getService().registerLeftClickHandler("test_item_onleftclick", event -> {
            event.getWhoClicked().sendMessage("Left");
        });

        FileInventories.getService().registerInvRightClickHandler("test_item_oninvrightclick", event -> {
            event.getWhoClicked().sendMessage("Inventory Right");
        });
        FileInventories.getService().registerInvLeftClickHandler("test_item_oninvleftclick", event -> {
            event.getWhoClicked().sendMessage("Inventory Left");
        });

        FileInventories.getService().registerRightInteractHandler("test_item_onrightinteract", event -> {
            event.getPlayer().sendMessage("Interact Right");
            FileInventories.getService().openInventory("test_inv", event.getPlayer());
        });
        FileInventories.getService().registerLeftInteractHandler("test_item_onleftinteract", event -> {
            event.getPlayer().sendMessage("Interact Left");
            FileInventories.getService().openInventory("test_inv", event.getPlayer());
        });

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            event.getPlayer().getInventory().clear();
            Optional<ItemStack> maybeItem = FileInventories.getService().getItem("test_item", event.getPlayer());
            maybeItem.ifPresent(itemStack -> event.getPlayer().getInventory().setItem(0, itemStack));
        }, 1);
    }
}
