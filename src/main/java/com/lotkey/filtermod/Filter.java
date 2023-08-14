package com.lotkey.filtermod;

import com.lotkey.filtermod.client.ClientHandler;
import com.lotkey.filtermod.data.LootTableGen;
import com.lotkey.filtermod.init.ModBlockEntities;
import com.lotkey.filtermod.init.ModBlocks;
import com.lotkey.filtermod.init.ModContainers;
import com.lotkey.filtermod.init.ModEntities;
import com.lotkey.filtermod.init.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

/**
 * Author: MrCrayfish
 */
@Mod(Reference.MOD_ID)
public class Filter {
    public Filter() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModEntities.REGISTER.register(bus);
        ModBlockEntities.REGISTER.register(bus);
        ModContainers.REGISTER.register(bus);
        bus.addListener(this::onClientSetup);
        bus.addListener(this::onGatherData);
        bus.addListener(this::onCreativeTabBuilding);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::init);
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new LootTableGen(output));
    }

    private void onCreativeTabBuilding(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
            ModItems.REGISTER.getEntries().stream().map(RegistryObject::get).forEach(event::accept);
        }
    }
}
