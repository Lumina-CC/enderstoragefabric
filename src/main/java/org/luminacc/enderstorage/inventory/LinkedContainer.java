package org.luminacc.enderstorage.inventory;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import org.luminacc.enderstorage.LinkedStorageMod;
import org.luminacc.enderstorage.item.StorageItem;
import org.luminacc.enderstorage.network.ChannelViewers;
import org.luminacc.enderstorage.util.DyeChannel;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class LinkedContainer extends GenericContainerScreenHandler {
    DyeChannel dyeChannel;

    public LinkedContainer(int syncId, PlayerInventory playerInventory, DyeChannel channel) {
        this(syncId, playerInventory, LinkedStorageMod.getInventory(channel));
        this.dyeChannel = channel;
    }

    public LinkedContainer(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, 3);
    }

    public LinkedContainer(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new LinkedInventory());
        dyeChannel = DyeChannel.fromBuf(buf);
    }

    @Override
    public void onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity player) {
        if (slotId > -1 && this.getSlot(slotId).getStack().getItem() instanceof StorageItem && getSlot(slotId).inventory instanceof PlayerInventory)
            if (dyeChannel.equals(LinkedInventoryHelper.getItemChannel(getSlot(slotId).getStack())))
                return;

        super.onSlotClick(slotId, clickData, actionType, player);
    }

    public static ExtendedScreenHandlerFactory createScreenHandlerFactory(DyeChannel channel) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
                ChannelViewers.addViewerFor(channel.getChannelName(), player);
                return new LinkedContainer(syncId, inventory, channel);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("container.enderstorage");
            }

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                channel.toBuf(buf);
            }
        };
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE;
    }
}
