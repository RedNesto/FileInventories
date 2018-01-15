package io.github.rednesto.fileinventories;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public abstract class FileInvAdapter {

    public abstract ItemStack addCustomString(ItemStack itemStack, String key, String value);

    public abstract Optional<String> getCustomString(ItemStack itemStack, String key);
}
