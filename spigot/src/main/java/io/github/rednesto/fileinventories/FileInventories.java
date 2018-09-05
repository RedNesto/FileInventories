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

import io.github.rednesto.fileinventories.adapters.*;
import io.github.rednesto.fileinventories.impl.FileInventoriesServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public final class FileInventories extends JavaPlugin {

    public static FileInventories instance;

    @Nullable
    public static FileInvAdapter adapter;

    @Override
    public void onEnable() {
        instance = this;
        io.github.rednesto.fileinventories.api.FileInventories.setService(new FileInventoriesServiceImpl());
        switch(Bukkit.getBukkitVersion().replaceAll("-SNAPSHOT", "")) {
            case "1.8.8-R0.1":
                adapter = new SpigotAdapter_1_8_8_R0_1();
                break;
            case "1.9.4-R0.1":
                adapter = new SpigotAdapter_1_9_4_R0_1();
                break;
            case "1.10.2-R0.1":
                adapter = new SpigotAdapter_1_10_2_R0_1();
                break;
            case "1.11.2-R0.1":
                adapter = new SpigotAdapter_1_11_2_R0_1();
                break;
            case "1.12.2-R0.1":
                adapter = new SpigotAdapter_1_12_2_R0_1();
                break;
        }
    }

    public static String applyColorCodes(String message) {
        return message
                .replaceAll("&r", ChatColor.RESET.toString())
                .replaceAll("&0", ChatColor.BLACK.toString())
                .replaceAll("&1", ChatColor.DARK_BLUE.toString())
                .replaceAll("&2", ChatColor.DARK_GREEN.toString())
                .replaceAll("&3", ChatColor.DARK_AQUA.toString())
                .replaceAll("&4", ChatColor.DARK_RED.toString())
                .replaceAll("&5", ChatColor.DARK_PURPLE.toString())
                .replaceAll("&6", ChatColor.GOLD.toString())
                .replaceAll("&7", ChatColor.GRAY.toString())
                .replaceAll("&8", ChatColor.DARK_GRAY.toString())
                .replaceAll("&9", ChatColor.BLUE.toString())
                .replaceAll("&a", ChatColor.GREEN.toString())
                .replaceAll("&b", ChatColor.AQUA.toString())
                .replaceAll("&c", ChatColor.RED.toString())
                .replaceAll("&d", ChatColor.LIGHT_PURPLE.toString())
                .replaceAll("&e", ChatColor.YELLOW.toString())
                .replaceAll("&f", ChatColor.WHITE.toString())
                .replaceAll("&l", ChatColor.BOLD.toString())
                .replaceAll("&o", ChatColor.ITALIC.toString())
                .replaceAll("&m", ChatColor.STRIKETHROUGH.toString())
                .replaceAll("&n", ChatColor.UNDERLINE.toString())
                .replaceAll("&k", ChatColor.MAGIC.toString());
    }
}
