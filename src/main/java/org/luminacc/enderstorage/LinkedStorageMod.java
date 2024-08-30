package org.luminacc.enderstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import org.luminacc.enderstorage.inventory.LinkedContainer;
import org.luminacc.enderstorage.inventory.LinkedInventory;
import org.luminacc.enderstorage.network.ChannelViewers;
import org.luminacc.enderstorage.network.OpenStoragePacket;
import org.luminacc.enderstorage.network.SetDyePacket;
import org.luminacc.enderstorage.recipe.CopyDyeRecipe;
import org.luminacc.enderstorage.recipe.TriDyableRecipe;
import org.luminacc.enderstorage.register.ModBlocks;
import org.luminacc.enderstorage.register.ModItems;
import org.luminacc.enderstorage.util.ChannelManager;
import org.luminacc.enderstorage.util.DyeChannel;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedStorageMod implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("enderstorage_fabric");
    public static final String MOD_ID = "enderstorage";
    public static SpecialRecipeSerializer<TriDyableRecipe> triDyeRecipe;
    public static RecipeSerializer<CopyDyeRecipe> copyDyeRecipe;
    private static ChannelManager CMAN; //lol

    public static final ScreenHandlerType<LinkedContainer> LINKED_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "enderstorage"), LinkedContainer::new);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
        ChannelViewers.registerChannelWatcher();
        triDyeRecipe = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "tri_dyable_recipe"), new SpecialRecipeSerializer<>(TriDyableRecipe::new));
        copyDyeRecipe = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "copy_dye_recipe"), new CopyDyeRecipe.Serializer());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CMAN = (ChannelManager) server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(ChannelManager::fromNbt, ChannelManager::new, MOD_ID);
        });
    }

    public static LinkedInventory getInventory(DyeChannel dyeChannel) {
        if (CMAN == null) return new LinkedInventory();
        return CMAN.getInv(dyeChannel);
    }
}
