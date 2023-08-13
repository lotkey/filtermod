package com.lotkey.filtermod.items.wrapper;

import javax.annotation.Nonnull;

import com.lotkey.filtermod.world.level.block.entity.FilterBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * Author: MrCrayfish
 */
public class FilterItemHandler extends SidedInvWrapper {
    private final FilterBlockEntity hopper;

    public FilterItemHandler(FilterBlockEntity hopper) {
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
