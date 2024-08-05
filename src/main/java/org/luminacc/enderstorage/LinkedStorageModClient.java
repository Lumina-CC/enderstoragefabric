package org.luminacc.enderstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityModelLayerImpl;
import org.luminacc.enderstorage.block.StorageBlock;
import org.luminacc.enderstorage.client.LinkedChestModel;
import org.luminacc.enderstorage.client.StorageBlockRenderer;
import org.luminacc.enderstorage.network.ChannelViewers;
import org.luminacc.enderstorage.network.UpdateViewerList;
import org.luminacc.enderstorage.register.ModBlocks;
import org.luminacc.enderstorage.register.ModItems;
import org.luminacc.enderstorage.util.DyeChannel;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import org.luminacc.enderstorage.util.PlayerDyeChannel;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;


@Environment(EnvType.CLIENT)
public class LinkedStorageModClient implements ClientModInitializer {
    public static final Identifier TEXTURE = Identifier.of(LinkedStorageMod.MOD_ID, "block/ender_chest");
    public static final EntityModelLayer LINKEDCHESTMODELLAYER = new EntityModelLayer(Identifier.of(LinkedStorageMod.MOD_ID, "ender_chest"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerImpl.PROVIDERS.put(LINKEDCHESTMODELLAYER, LinkedChestModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(StorageBlock.blockEntity, StorageBlockRenderer::new);
        FabricModelPredicateProviderRegistry.register(ModItems.storageItem, new Identifier("open"), (stack, world, entity, seed) -> {
            String channel = LinkedInventoryHelper.getItemChannel(stack).getChannelName();
            return ChannelViewers.getViewersFor(channel) ? 1 : 0;
        });
        ScreenRegistry.register(LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE, GenericContainerScreen::new);
        ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
            DyeChannel dyeChannel = LinkedInventoryHelper.getItemChannel(stack);
            if (layer > 0 && layer < 4) {
                byte[] colors = dyeChannel.dyeChannel;
                return DyeColor.byId(colors[layer - 1]).getMapColor().color;
            }
            if (layer == 4 && dyeChannel instanceof PlayerDyeChannel)
                return DyeColor.LIGHT_BLUE.getMapColor().color;
            return DyeColor.WHITE.getMapColor().color;
        }, ModItems.storageItem, ModBlocks.storageBlock);
        UpdateViewerList.registerReceivePacket();
    }

}
