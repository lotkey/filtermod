package com.lotkey.filtermod.world.level.block.entity;

import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.lotkey.filtermod.init.ModBlockEntities;
import com.lotkey.filtermod.items.wrapper.FilterItemHandler;
import com.lotkey.filtermod.world.inventory.FilterMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Author: MrCrayfish
 */
public class FilterBlockEntity extends AbstractHopperBlockEntity implements WorldlyContainer {
    private static final int TRANSFER_COOLDOWN = 8;
    private static final int CONTAINER_SIZE = 50;
    public MutableBoolean allow = new MutableBoolean(true);

    public FilterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILTER.get(), pos, state, TRANSFER_COOLDOWN);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public int[] getTransferableSlots() {
        return IntStream.range(0, 5).toArray();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.filtermod.filter");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) {
        return new FilterMenu(windowId, playerInventory, this);
    }

    @Override
    protected net.minecraftforge.items.IItemHandler createUnSidedHandler() {
        return new FilterItemHandler(this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return this.getTransferableSlots();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index < 5 && itemIsInFilter(stack.getItem());
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        if (index < 5) {
            return itemIsInFilter(stack.getItem());
        }
        return !(itemIsInFilter(stack.getItem()) || filterIsFull());
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index < 5;
    }

    private boolean itemIsInFilter(Item item) {
        return this.items.subList(5, 50).stream().anyMatch(stack -> (!stack.isEmpty() && stack.getItem() == item));
    }

    private boolean filterIsFull() {
        return this.items.subList(5, 50).stream().allMatch(stack -> !stack.isEmpty());
    }
}
