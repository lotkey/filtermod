package com.lotkey.filtermod.init;

import com.lotkey.filtermod.Reference;
import com.lotkey.filtermod.client.ClientHandler;
import com.lotkey.filtermod.world.entity.vehicle.FilterMinecart;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES,
            Reference.MOD_ID);

    public static final RegistryObject<EntityType<FilterMinecart>> FILTER_MINECART = REGISTER.register(
            "filter_minecart",
            () -> EntityType.Builder.<FilterMinecart>of(FilterMinecart::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .setCustomClientFactory((entity, world) -> {
                        FilterMinecart minecart = new FilterMinecart(
                                ModEntities.FILTER_MINECART.get(), world);
                        ClientHandler.handleFilterMinecartSpawn(minecart);
                        return minecart;
                    }).build("filter_minecart"));
}
