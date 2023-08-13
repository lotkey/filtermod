package com.lotkey.filtermod.client;

import com.lotkey.filtermod.client.gui.screens.inventory.FilterScreen;
import com.lotkey.filtermod.init.ModContainers;
import com.lotkey.filtermod.init.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * Author: MrCrayfish
 */
public class ClientHandler {
    public static void init() {
        MenuScreens.register(ModContainers.FILTER.get(), FilterScreen::new);
        EntityRenderers.register(ModEntities.FILTER_MINECART.get(),
                context -> new MinecartRenderer<>(context, ModelLayers.HOPPER_MINECART));
    }

    public static void handleFilterMinecartSpawn(Entity entity) {
        if (entity != null) {
            if (entity instanceof AbstractMinecart) {
                Minecraft.getInstance().getSoundManager().play(new MinecartSoundInstance((AbstractMinecart) entity));
            }
        }
    }
}
