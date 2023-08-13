package com.lotkey.filtermod.client.gui.screens.inventory;

import com.lotkey.filtermod.world.inventory.FilterMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Author: MrCrayfish
 */
public class FilterScreen extends AbstractContainerScreen<FilterMenu> {
    private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation(
            "filtermod:textures/gui/container/filter.png");

    public FilterScreen(FilterMenu container, Inventory playerInventory, Component titleIn) {
        super(container, playerInventory, titleIn);
        this.imageHeight = 133;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title.getString(), 8.0F, 6.0F, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, 8.0F, (float) (this.imageHeight - 96 + 2), 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, HOPPER_GUI_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, startX, startY, 0, 0, this.imageWidth, this.imageHeight);
        Slot slot = this.menu.getSlot(0);
        if (!slot.hasItem()) {
            this.blit(poseStack, this.leftPos + slot.x, this.topPos + slot.y, this.imageWidth, 0, 16, 16);
        }
    }
}
