package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.RotationUtil;
import net.minecraft.tileentity.TileEntity;
import java.util.ArrayList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.client.Minecraft;

@Module.Mod (displayName = "ChestAura")
public class ChestAura extends Module
{
    @Op(name = "Range", min = 1.0, max = 7.0, increment = 0.01)
    private float range;
    Minecraft mc;
    TileEntityChest chest;
    private ArrayList<TileEntityChest> openedChests;
    
    public ChestAura() {
        this.mc = Minecraft.getMinecraft();
        this.chest = null;
        this.openedChests = new ArrayList<TileEntityChest>();
        this.range = 4;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent ev) {
        for (final Object o : this.mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)o;
                if (this.mc.thePlayer.getDistanceToTileEntity(chest) > range || this.openedChests.contains(chest)) {
                    continue;
                }
                if (this.mc.currentScreen != null) {
                    continue;
                }
                this.chest = chest;
                if (mc.thePlayer.ticksExisted % 3 != 0) {
                    continue;
                }
                if (this.mc.thePlayer.getDistanceToTileEntity(this.chest) > range) {
                    continue;
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.chest.getPos(), EnumFacing.UP.getIndex(), this.mc.thePlayer.getHeldItem(), 1.0f, 1.0f, 1.0f));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                this.openedChests.add(chest);
            }
        }
    }
    
    @EventTarget
    public void onPacketSend(final PacketSendEvent e) {
        if (e.getPacket() instanceof C03PacketPlayer && this.chest != null && !this.openedChests.contains(this.chest) && this.mc.thePlayer.getDistanceToTileEntity(this.chest) <= range) {
            final C03PacketPlayer p = (C03PacketPlayer)e.getPacket();
            final float[] prot = RotationUtil.faceTileEntity(this.chest, 1000.0f, 1000.0f);
            if (prot != null) {
                C03PacketPlayer.yaw = prot[0];
                C03PacketPlayer.pitch = prot[1];
                mc.thePlayer.rotationYawHead = prot[0];
                mc.thePlayer.renderYawOffset = prot[0];
            }
        }
    }
}
