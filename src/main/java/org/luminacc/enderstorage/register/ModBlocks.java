package org.luminacc.enderstorage.register;

import org.luminacc.enderstorage.block.StorageBlock;
import net.minecraft.block.Block;

public class ModBlocks {
    public static Block storageBlock;

    public static void register() {
        storageBlock = new StorageBlock(Block.Settings.create().strength(2.5f, 2.5f));
    }
}
