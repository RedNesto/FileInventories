package io.github.rednesto.fileinventories.adapters;

import io.github.rednesto.fileinventories.FileInvAdapter;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class SpigotAdapter_1_9_4_R0_1 extends FileInvAdapter {

    @Override
    public ItemStack addCustomString(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        if(!nms.hasTag())
            nms.setTag(new NBTTagCompound());

        nms.getTag().setString(key, value);

        return CraftItemStack.asBukkitCopy(nms);
    }

    @Override
    public Optional<String> getCustomString(ItemStack itemStack, String key) {
        net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        if(!nms.hasTag() || nms.getTag().getString(key).isEmpty())
            return Optional.empty();

        return Optional.of(nms.getTag().getString(key));
    }
}
