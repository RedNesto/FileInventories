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
package io.github.rednesto.fileinventories.example;

import io.github.rednesto.fileinventories.api.FileInventoriesService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.asset.AssetManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.inject.Inject;

@Plugin(id = "file-inv-example",
        name = "File Inventories Example",
        dependencies = {@Dependency(id = "file-inventories")},
        authors = {"RedNesto"})
public class FileInvExample {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        Optional<FileInventoriesService> maybeService = Sponge.getServiceManager().provide(FileInventoriesService.class);
        if(!maybeService.isPresent()) {
            logger.error("FileInventoriesService was not provided! Please be sure you installed FileInventories and it has been loaded successfully.");
            return;
        }

        FileInventoriesService service = maybeService.get();

        service.registerSecondaryInteractHandler("test_item_onsecondaryinteract", event1 -> event1.getCause().first(Player.class).ifPresent(player -> {
            player.sendMessages(Text.of("Interact Secondary"));
            service.openInventory("test_inv", player);
        }));
        service.registerPrimaryInteractHandler("test_item_onprimaryinteract", event1 -> event1.getCause().first(Player.class).ifPresent(player -> {
            player.sendMessages(Text.of("Interact Primary"));
            service.openInventory("test_inv", player);
        }));

        service.registerSecondaryClickHandler("test_item_onsecondaryclick", event1 ->
                event1.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Secondary"))));
        service.registerPrimaryClickHandler("test_item_onprimaryclick", event1 ->
                event1.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Primary"))));
        service.registerMiddleClickHandler("test_item_onmiddleclick", event1 ->
                event1.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Middle"))));

        service.registerInvSecondaryClickHandler("test_inv_oninvsecondaryclick", event1 -> {
            event1.setCancelled(true);
            event1.getCause().first(Player.class).ifPresent(player -> player.sendMessages(Text.of("Inventory Secondary")));
        });
        service.registerInvPrimaryClickHandler("test_inv_oninvprimaryclick", event1 -> {
            event1.setCancelled(true);
            event1.getCause().first(Player.class).ifPresent(player -> player.sendMessages(Text.of("Inventory Primary")));
        });
        service.registerInvMiddleClickHandler("test_inv_oninvmiddleclick", event1 -> {
            event1.setCancelled(true);
            event1.getCause().first(Player.class).ifPresent(player -> player.sendMessages(Text.of("Inventory Middle")));
        });

        try {
            Files.createDirectories(configDir);

            AssetManager assetManager = Sponge.getAssetManager();
            Path itemsFile = configDir.resolve("items.json");
            if (!Files.exists(itemsFile)) {
                Optional<Asset> itemsAsset = assetManager.getAsset(this, itemsFile.getFileName().toString());
                if (!itemsAsset.isPresent())
                    throw new RuntimeException("The asset 'items.json' could not be found");

                itemsAsset.get().copyToDirectory(configDir);
            }

            Path inventoriesFile = configDir.resolve("inventories.json");
            if (!Files.exists(inventoriesFile)) {
                Optional<Asset> inventoriesAsset = assetManager.getAsset(this, inventoriesFile.getFileName().toString());
                if (!inventoriesAsset.isPresent())
                    throw new RuntimeException("The asset 'inventories.json' could not be found");

                inventoriesAsset.get().copyToDirectory(configDir);
            }

            service.load(FileInventoriesService.LoadTarget.ITEMS, itemsFile);
            service.load(FileInventoriesService.LoadTarget.INVENTORIES, inventoriesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this, CommandSpec.builder().executor((src, args) -> {
            if(!(src instanceof Player))
                return CommandResult.empty();

            Sponge.getServiceManager().provide(FileInventoriesService.class).ifPresent(service -> {
                service.offerItem("test_item", (Player) src, ((Player) src).getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class)));
                service.offerItem("custom_shovel", (Player) src, ((Player) src).getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class)));
            });

            return CommandResult.success();
        }).build(), "exitem");
    }
}
