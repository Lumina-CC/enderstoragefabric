package org.luminacc.enderstorage.recipe;

import com.google.gson.JsonObject;
import org.luminacc.enderstorage.LinkedStorageMod;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CopyDyeRecipe extends ShapedRecipe {
    private ShapedRecipe shapedRecipe;
    public CopyDyeRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), "enderstorage", shapedRecipe.getCategory(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(),shapedRecipe.getOutput(DynamicRegistryManager.of((Registry) Registries.RECIPE_TYPE)));
        this.shapedRecipe = shapedRecipe;
    }

    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack output = this.getOutput((DynamicRegistryManager) shapedRecipe).copy();
        LinkedInventoryHelper.setItemChannel(LinkedInventoryHelper.getItemChannel(craftingInventory.getStack(4)), output);
        return output;
    }

    public RecipeSerializer<?> getSerializer() {
        return LinkedStorageMod.copyDyeRecipe;
    }

    public static class Serializer implements RecipeSerializer<CopyDyeRecipe> {

        @Override
        public CopyDyeRecipe read(Identifier id, JsonObject json) {
            return new CopyDyeRecipe(ShapedRecipe.Serializer.SHAPED.read(id, json));
        }

        @Override
        public CopyDyeRecipe read(Identifier id, PacketByteBuf buf) {
            return new CopyDyeRecipe(ShapedRecipe.Serializer.SHAPED.read(id, buf));
        }

        @Override
        public void write(PacketByteBuf buf, CopyDyeRecipe recipe) {
            ShapedRecipe.Serializer.SHAPED.write(buf, recipe);
        }
    }
}
