package com.lotkey.filtermod.init;

import com.lotkey.filtermod.Reference;
import com.lotkey.filtermod.world.level.item.FilterMinecartItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,
            Reference.MOD_ID);

    public static final RegistryObject<Item> FILTER_MINECART = REGISTER.register("filter_minecart",
            () -> new FilterMinecartItem(new Item.Properties().stacksTo(1)));
}
