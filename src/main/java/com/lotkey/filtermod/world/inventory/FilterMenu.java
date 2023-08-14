package com.lotkey.filtermod.world.inventory;

import com.lotkey.filtermod.init.ModContainers;
import com.lotkey.filtermod.world.level.block.entity.FilterBlockEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class FilterMenu extends AbstractContainerMenu {
    private final Container hopperInventory;
    private static final int Size = 50;
    private static final int HopperOffsetX = 44;
    private static final int HopperOffsetY = 18;
    private static final int FilterOffsetX = 8;
    private static final int FilterOffsetY = 54;
    private static final int InventoryOffsetX = 8;
    private static final int InventoryOffsetY = 158;
    private static final int HotbarOffsetX = InventoryOffsetX;
    private static final int HotbarOffsetY = 216;

    public FilterMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, new SimpleContainer(Size));
    }

    public FilterMenu(int windowId, Inventory playerInventory, FilterBlockEntity block) {
        this(windowId, playerInventory, (Container) block);
    }

    public FilterMenu(int windowId, Inventory playerInventory, Container hopperInventory) {
        super(ModContainers.FILTER.get(), windowId);
        this.hopperInventory = hopperInventory;
        checkContainerSize(hopperInventory, Size);
        hopperInventory.startOpen(playerInventory.player);

        for (int i = 0; i < 5; i++) {
            // add hopper slots
            this.addSlot(new Slot(hopperInventory, i, HopperOffsetX + (i * 18), HopperOffsetY));
        }

        // add filter slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(
                        new Slot(hopperInventory, (i * 9) + j + 5, FilterOffsetX + (j * 18), FilterOffsetY + (i * 18)) {
                            @Override
                            public int getMaxStackSize() {
                                return 1;
                            }
                        });
            }
        }

        // add inventory slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, (i * 9) + j + 9, InventoryOffsetX + (j * 18),
                        InventoryOffsetY + (i * 18)));
            }
        }

        // add hotbar slots
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, HotbarOffsetX + (i * 18), HotbarOffsetY));
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return this.hopperInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();
            if (index < this.hopperInventory.getContainerSize()) {
                if (!this.moveItemStackTo(slotStack, this.hopperInventory.getContainerSize(), this.slots.size(),
                        true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 1, this.hopperInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.hopperInventory.stopOpen(player);
    }
}
