package com.lotkey.filtermod.init;

import com.lotkey.filtermod.Reference;
import com.lotkey.filtermod.world.inventory.FilterMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
public class ModContainers {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            Reference.MOD_ID);

    public static final RegistryObject<MenuType<FilterMenu>> FILTER = register("filter",
            FilterMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id,
            IContainerFactory<T> factory) {
        return REGISTER.register(id, () -> IForgeMenuType.create(factory));
    }
}
