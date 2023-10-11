/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.MoveEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovementInput;
import org.lwjgl.opengl.GL11;

public class Blink
extends Module {
    Timer timer = new Timer();
    static int count = 0;
    private final List<Packet> packets = new CopyOnWriteArrayList<Packet>();
    private final List<double[]> positions = new ArrayList<double[]>();
    public static ArrayList<double[]> positionsList = new ArrayList();
    private double[] startingPosition;

    public Blink() {
        super("Blink", 48, Category.PLAYER, 6069759, true, "Simulate lag and skip from where you enabled to where you disabled", new String[]{"blnk", "blk", "bnk"});
    }

    @EventTarget
    public void onRender(Event3D e2) {
        this.positions.add(new double[]{Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ});
        GL11.glPushMatrix();
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        R2DUtils.glColor(-1862314667);
        GL11.glBegin(3);
        GL11.glVertex3d(this.startingPosition[0] - RenderManager.renderPosX, this.startingPosition[1] - RenderManager.renderPosY, this.startingPosition[2] - RenderManager.renderPosZ);
        GL11.glVertex3d(this.startingPosition[0] - RenderManager.renderPosX, this.startingPosition[1] + (double)Wrapper.mc.thePlayer.height - RenderManager.renderPosY, this.startingPosition[2] - RenderManager.renderPosZ);
        R2DUtils.glColor(-1711276033);
        for (double[] position : this.positions) {
            double x2 = position[0] - RenderManager.renderPosX;
            double y2 = position[1] - RenderManager.renderPosY;
            double z2 = position[2] - RenderManager.renderPosZ;
            GL11.glVertex3d(x2, y2, z2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    @EventTarget
    public void onPacketSent(EventPacketSent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (Wrapper.getPlayer().movementInput.moveForward != 0.0f || Wrapper.getPlayer().movementInput.moveStrafe != 0.0f) {
                this.packets.add(event.getPacket());
                this.setDisplayName("Blink\u00a77 - " + this.packets.size());
            }
            event.setCancelled(true);
        } else if (event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C09PacketHeldItemChange || event.getPacket() instanceof C02PacketUseEntity) {
            this.packets.add(event.getPacket());
            this.setDisplayName("Blink\u00a77 - " + this.packets.size());
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (++count >= 50) {
            count = 0;
            if (positionsList.size() > 5) {
                positionsList.remove(0);
            }
        }
        for (Object o : Wrapper.getWorld().playerEntities) {
            boolean shouldBreadCrumb;
            if (!(o instanceof EntityPlayer)) continue;
            EntityPlayer player1 = (EntityPlayer)o;
            boolean bl2 = shouldBreadCrumb = player1 == Wrapper.getPlayer() && (Wrapper.getPlayer().movementInput.moveForward != 0.0f || Wrapper.getPlayer().movementInput.moveStrafe != 0.0f);
            if (!shouldBreadCrumb) continue;
            double x2 = RenderManager.renderPosX;
            double y2 = RenderManager.renderPosY;
            double z2 = RenderManager.renderPosZ;
            positionsList.add(new double[]{x2, y2 - (double)player1.height, z2});
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Packet packet : this.packets) {
            Wrapper.getMinecraft().getNetHandler().addToSendQueue(packet);
        }
        this.packets.clear();
        this.positions.clear();
        positionsList.clear();
    }

    public static double posit(double val) {
        return val == 0.0 ? val : (val < 0.0 ? val * -1.0 : val);
    }

    @Override
    public void toggleModule() {
        super.toggleModule();
        if (this.getState()) {
            this.startingPosition = new double[]{Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ};
        }
    }
}

