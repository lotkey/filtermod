package com.lotkey.filtermod.init;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.lotkey.filtermod.Reference;
import com.lotkey.filtermod.world.level.block.FilterBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Reference.MOD_ID);

    public static final RegistryObject<Block> FILTER = register("filter",
            () -> new FilterBlock(Block.Properties.of(Material.METAL).strength(2.0F)));

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> block) {
        return register(id, block, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> block,
            @Nullable Function<T, BlockItem> supplier) {
        RegistryObject<T> registryObject = REGISTER.register(id, block);
        if (supplier != null) {
            ModItems.REGISTER.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }
}
