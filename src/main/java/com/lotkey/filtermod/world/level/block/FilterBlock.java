package com.lotkey.filtermod.world.level.block;

import javax.annotation.Nullable;

import com.lotkey.filtermod.init.ModBlockEntities;
import com.lotkey.filtermod.world.level.block.entity.AbstractHopperBlockEntity;
import com.lotkey.filtermod.world.level.block.entity.FilterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Author: MrCrayfish
 */
public class FilterBlock extends AbstractHopperBlock {
    public FilterBlock(Block.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FilterBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends AbstractHopperBlockEntity> getBlockEntityType() {
        return ModBlockEntities.FILTER.get();
    }
}
