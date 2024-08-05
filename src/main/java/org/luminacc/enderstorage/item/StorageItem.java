package org.luminacc.enderstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.luminacc.enderstorage.LinkedStorageMod;
import org.luminacc.enderstorage.block.StorageBlock;
import org.luminacc.enderstorage.inventory.LinkedContainer;
import org.luminacc.enderstorage.util.DyeChannel;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class StorageItem extends Item {
    public StorageItem(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registries.ITEM, Identifier.of(LinkedStorageMod.MOD_ID, "ender_pouch"), this);

    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            PlayerEntity playerEntity = context.getPlayer();
            if (playerEntity.isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof StorageBlock) {
                DyeChannel channel = LinkedInventoryHelper.getBlockChannel(context.getWorld(), context.getBlockPos());
                LinkedInventoryHelper.setItemChannel(channel, context.getStack());
            } else use(context.getWorld(), context.getPlayer(), context.getHand());
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        if (!world.isClient) {
            playerEntity.openHandledScreen(LinkedContainer.createScreenHandlerFactory(LinkedInventoryHelper.getItemChannel(stack)));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext options) {
        DyeChannel channel = LinkedInventoryHelper.getItemChannel(stack);
        for (Text text : channel.getCleanName()) {
            tooltip.add(((MutableText) text).formatted(Formatting.GRAY));
        }
    }
}
