package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S34PacketMaps;

public class MapData.MapInfo {
    public final EntityPlayer entityplayerObj;
    private boolean field_176105_d = true;
    private int minX = 0;
    private int minY = 0;
    private int maxX = 127;
    private int maxY = 127;
    private int field_176109_i;
    public int field_82569_d;

    public MapData.MapInfo(EntityPlayer player) {
        this.entityplayerObj = player;
    }

    public Packet getPacket(ItemStack stack) {
        if (this.field_176105_d) {
            this.field_176105_d = false;
            return new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
        }
        return this.field_176109_i++ % 5 == 0 ? new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
    }

    public void update(int x, int y) {
        if (this.field_176105_d) {
            this.minX = Math.min((int)this.minX, (int)x);
            this.minY = Math.min((int)this.minY, (int)y);
            this.maxX = Math.max((int)this.maxX, (int)x);
            this.maxY = Math.max((int)this.maxY, (int)y);
        } else {
            this.field_176105_d = true;
            this.minX = x;
            this.minY = y;
            this.maxX = x;
            this.maxY = y;
        }
    }
}
