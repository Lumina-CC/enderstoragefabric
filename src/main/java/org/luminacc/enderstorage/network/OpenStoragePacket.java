package org.luminacc.enderstorage.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.luminacc.enderstorage.LinkedStorageMod;
import org.luminacc.enderstorage.inventory.LinkedContainer;
import org.luminacc.enderstorage.util.LinkedInventoryHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OpenStoragePacket {
    private static final Identifier OPEN_STORAGE_PACKET = new Identifier(LinkedStorageMod.MOD_ID, "openpacket");

    public static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(OPEN_STORAGE_PACKET, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(() -> {
                World world = player.getEntityWorld();
                player.openHandledScreen(LinkedContainer.createScreenHandlerFactory(LinkedInventoryHelper.getBlockChannel(world, pos)));
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(OPEN_STORAGE_PACKET, new PacketByteBuf(buf)));
    }
}
