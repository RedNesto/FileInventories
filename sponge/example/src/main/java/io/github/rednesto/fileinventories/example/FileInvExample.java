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

import io.github.rednesto.fileinventories.api.FileInventoriesService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            logger.error("FileInventoriesService has not been provided! Please be sure you installed FileInventories and it has been loaded successfully.");
            return;
        }

        FileInventoriesService service = maybeService.get();

        service.registerRightInteractHandler("test_item_onrightinteract", event1 -> event1.getCause().first(Player.class).ifPresent(player -> {
            player.sendMessages(Text.of("Interact Right"));
            service.openInventory("test_inv", player);
        }));
        service.registerLeftInteractHandler("test_item_onleftinteract", event1 -> event1.getCause().first(Player.class).ifPresent(player -> {
            player.sendMessages(Text.of("Interact Left"));
            service.openInventory("test_inv", player);
        }));

        service.registerRightClickHandler("test_item_onrightclick", event1 ->
                event1.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Right"))));
        service.registerLeftClickHandler("test_item_onleftclick", event1 ->
                event1.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Left"))));

        service.registerInvRightClickHandler("test_inv_oninvrightclick", event1 -> {
            event1.setCancelled(true);
            event1.getCause().first(Player.class).ifPresent(player -> player.sendMessages(Text.of("Inventory Right")));
        });
        service.registerInvLeftClickHandler("test_inv_oninvleftclick", event1 -> {
            event1.setCancelled(true);
            event1.getCause().first(Player.class).ifPresent(player -> player.sendMessages(Text.of("Inventory Left")));
        });

        try {
            Files.createDirectories(configDir);

            // TODO replace it with the AssetManager when I figure out how to use it
            File items = new File(configDir.toFile(), "items.json");
            if(!items.exists()) {
                Files.copy(getClass().getResourceAsStream("/items.json"), items.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            File inventories = new File(configDir.toFile(), "inventories.json");
            if(!inventories.exists()) {
                Files.copy(getClass().getResourceAsStream("/inventories.json"), inventories.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            service.load(FileInventoriesService.LoadTarget.LOAD_ITEMS, items.toPath());
            service.load(FileInventoriesService.LoadTarget.LOAD_INVS, inventories.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this, new CommandCallable() {
            @Override
            public CommandResult process(CommandSource source, String arguments) {
                if(!(source instanceof Player))
                    return CommandResult.empty();

                Sponge.getServiceManager().provide(FileInventoriesService.class).ifPresent(service ->
                        service.getItem("test_item", (Player) source).ifPresent(item -> ((Player) source).getInventory().set(item)));

                return CommandResult.success();
            }

            @Override
            public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) {
                return Collections.emptyList();
            }

            @Override
            public boolean testPermission(CommandSource source) {
                return true;
            }

            @Override
            public Optional<Text> getShortDescription(CommandSource source) {
                return Optional.empty();
            }

            @Override
            public Optional<Text> getHelp(CommandSource source) {
                return Optional.empty();
            }

            @Override
            public Text getUsage(CommandSource source) {
                return Text.of();
            }
        }, "exitem");
    }
}
