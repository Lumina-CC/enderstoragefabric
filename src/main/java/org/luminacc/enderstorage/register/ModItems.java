package org.luminacc.enderstorage.register;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import org.luminacc.enderstorage.item.StorageItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class ModItems {
    public static Item storageItem;

    public static void register() {
        storageItem = new StorageItem(new Item.Settings().maxCount(1));
	ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(storageItem));
    }
}
