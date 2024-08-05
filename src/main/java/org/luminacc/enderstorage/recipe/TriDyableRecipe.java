package org.luminacc.enderstorage.recipe;

import org.luminacc.enderstorage.LinkedStorageMod;
import org.luminacc.enderstorage.block.StorageBlock;
import org.luminacc.enderstorage.item.StorageItem;
import org.luminacc.enderstorage.util.DyeChannel;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TriDyableRecipe extends SpecialCraftingRecipe {
    public TriDyableRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        Item center = inv.getStack(4).getItem();
        return (inv.getStack(0).getItem() instanceof DyeItem ||
                inv.getStack(1).getItem() instanceof DyeItem ||
                inv.getStack(2).getItem() instanceof DyeItem) &&
                (center instanceof StorageItem || (center instanceof BlockItem && ((BlockItem) center).getBlock() instanceof StorageBlock));
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        ItemStack newStack = inv.getStack(4).copy();
        DyeChannel dyeChannel = LinkedInventoryHelper.getItemChannel(newStack).clone();
        for (int i = 0; i < 3; i++)
            if (inv.getStack(i).getItem() instanceof DyeItem)
                dyeChannel.setSlot(i, (byte) ((DyeItem) inv.getStack(i).getItem()).getColor().getId());
        LinkedInventoryHelper.setItemChannel(dyeChannel, newStack);
        return newStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LinkedStorageMod.triDyeRecipe;
    }
}
