package com.lotkey.filtermod.init;

import java.util.function.Supplier;

import com.lotkey.filtermod.Reference;
import com.lotkey.filtermod.world.level.block.entity.FilterBlockEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

        public static final RegistryObject<BlockEntityType<FilterBlockEntity>> FILTER = register(
                        "filter", FilterBlockEntity::new, () -> new Block[] { ModBlocks.FILTER.get() });

        private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id,
                        BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block[]> validBlocksSupplier) {
                return REGISTER.register(id,
                                () -> BlockEntityType.Builder.of(supplier, validBlocksSupplier.get()).build(null));
        }
}
