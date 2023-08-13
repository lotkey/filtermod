package com.mrcrayfish.goldenhopper.items.wrapper;

import javax.annotation.Nonnull;

import com.mrcrayfish.goldenhopper.world.level.block.entity.GoldenHopperBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * Author: MrCrayfish
 */
public class GoldenHopperItemHandler extends SidedInvWrapper {
    private final GoldenHopperBlockEntity hopper;

    public GoldenHopperItemHandler(GoldenHopperBlockEntity hopper) {
        super(hopper, null);
        this.hopper = hopper;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (simulate) {
            return super.insertItem(slot, stack, true);
        }

        boolean wasEmpty = this.inv.isEmpty();
        int originalStackSize = stack.getCount();
        stack = super.insertItem(slot, stack, false);
        if (wasEmpty && originalStackSize > stack.getCount()) {
            if (!this.hopper.isCoolingDown()) {
                this.hopper.setTransferCooldown(8);
            }
        }
        return stack;
    }
}
