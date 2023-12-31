package com.lotkey.filtermod.world.entity.vehicle;

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.lotkey.filtermod.init.ModBlocks;
import com.lotkey.filtermod.init.ModEntities;
import com.lotkey.filtermod.init.ModItems;
import com.lotkey.filtermod.world.inventory.FilterMenu;
import com.lotkey.filtermod.world.level.block.entity.AbstractHopperBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;

/**
 * Author: MrCrayfish
 */
public class FilterMinecart extends AbstractMinecartContainer implements Hopper, WorldlyContainer {
    private boolean blocked = true;
    private int transferTicker = -1;
    private final BlockPos lastPosition = BlockPos.ZERO;

    public FilterMinecart(EntityType<?> type, Level level) {
        super(type, level);
    }

    public FilterMinecart(Level level, double x, double y, double z) {
        super(ModEntities.FILTER_MINECART.get(), x, y, z, level);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setTransferTicker(int transferTicker) {
        this.transferTicker = transferTicker;
    }

    public boolean canTransfer() {
        return this.transferTicker > 0;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) {
        return new FilterMenu(windowId, playerInventory, this);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return ModBlocks.FILTER.get().defaultBlockState();
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 1;
    }

    @Override
    protected Item getDropItem() {
        return ModItems.FILTER_MINECART.get();
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FILTER_MINECART.get());
    }

    @Override
    public Type getMinecartType() {
        return Type.HOPPER;
    }

    @Override
    public double getLevelX() {
        return this.getX();
    }

    @Override
    public double getLevelY() {
        return this.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return this.getZ();
    }

    @Override
    public int getContainerSize() {
        return 50;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index < 5 && itemIsInFilter(stack.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, 5).toArray();
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

    @Override
    public void activateMinecart(int x, int y, int z, boolean receivingPower) {
        if (receivingPower == this.isBlocked()) {
            this.setBlocked(!receivingPower);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isAlive() && this.isBlocked()) {
            BlockPos pos = this.blockPosition();
            if (pos.equals(this.lastPosition)) {
                this.transferTicker--;
            } else {
                this.setTransferTicker(0);
            }

            if (!this.canTransfer()) {
                this.setTransferTicker(0);
                if (this.captureDroppedItems()) {
                    this.setTransferTicker(4);
                    this.setChanged();
                }
            }
        }

    }

    private boolean captureDroppedItems() {
        if (HopperBlockEntity.suckInItems(this.level, this)) {
            return true;
        }
        List<ItemEntity> list = this.level.getEntitiesOfClass(ItemEntity.class,
                this.getBoundingBox().inflate(0.25D, 0.0D, 0.25D), EntitySelector.ENTITY_STILL_ALIVE);
        if (!list.isEmpty()) {
            this.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(handler -> {
                AbstractHopperBlockEntity.addItemEntity(this, handler, list.get(0));
            });
        }
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TransferCooldown", this.transferTicker);
        compound.putBoolean("Enabled", this.blocked);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.transferTicker = compound.getInt("TransferCooldown");
        this.blocked = !compound.contains("Enabled") || compound.getBoolean("Enabled");
    }

    @Override
    public void destroy(DamageSource source) {
        super.destroy(source);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.spawnAtLocation(ModBlocks.FILTER.get());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public int getComparatorLevel() {
        float filled = IntStream.range(1, this.getContainerSize())
                .mapToObj(this::getItem)
                .filter(stack -> !stack.isEmpty())
                .map(stack -> stack.getCount() / (float) Math.min(this.getMaxStackSize(), stack.getMaxStackSize()))
                .reduce(0F, Float::sum);
        filled /= (this.getContainerSize() - 1.0F);
        return Mth.floor(filled * 14.0F) + (filled > 0 ? 1 : 0);
    }

    private boolean itemIsInFilter(Item item) {
        return this.getItemStacks().subList(5, 50).stream()
                .anyMatch(stack -> (!stack.isEmpty() && stack.getItem() == item));
    }

    private boolean filterIsFull() {
        return this.getItemStacks().subList(5, 50).stream().allMatch(stack -> !stack.isEmpty());
    }
}
