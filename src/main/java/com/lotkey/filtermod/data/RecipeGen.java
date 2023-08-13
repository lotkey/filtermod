package com.lotkey.filtermod.data;

import java.util.function.Consumer;

import com.lotkey.filtermod.init.ModBlocks;
import com.lotkey.filtermod.init.ModItems;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class RecipeGen extends RecipeProvider {
    public RecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.FILTER.get())
                .pattern("ICI")
                .pattern("IHI")
                .pattern("RIR")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('R', Items.REDSTONE)
                .define('H', Items.HOPPER)
                .define('C', Items.COMPARATOR)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .unlockedBy("has_comparator", has(Items.COMPARATOR))
                .unlockedBy("has_hopper", has(Items.HOPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.FILTER_MINECART.get())
                .pattern("A")
                .pattern("B")
                .define('A', ModBlocks.FILTER.get())
                .define('B', Items.MINECART)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_minecart", has(Items.MINECART))
                .save(consumer);
    }
}
