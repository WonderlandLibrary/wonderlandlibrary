// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.event.events.impl.EventDisplay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.tuskevich.util.movement.MovementUtils;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventLivingUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "FreeCam", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class FreeCamera extends Module
{
    public SliderSetting speed;
    private double oldPosX;
    private double oldPosY;
    private double oldPosZ;
    private float oldFlySpeed;
    
    public FreeCamera() {
        this.speed = new SliderSetting("Flight Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
        this.add(this.speed);
    }
    
    @EventTarget
    public void onLiving(final EventLivingUpdate eventLivingUpdate) {
        final Minecraft mc = FreeCamera.mc;
        Minecraft.player.noClip = true;
        final Minecraft mc2 = FreeCamera.mc;
        Minecraft.player.onGround = false;
        final Minecraft mc3 = FreeCamera.mc;
        Minecraft.player.capabilities.setFlySpeed(this.speed.getFloatValue() / 5.0f);
        final Minecraft mc4 = FreeCamera.mc;
        Minecraft.player.capabilities.isFlying = true;
    }
    
    @EventTarget
    public void onMotion(final EventMotion eventMotion) {
        final Minecraft mc = FreeCamera.mc;
        Minecraft.player.motionY = 9.999999747378752E-5;
        MovementUtils.setSpeed(0.800000011920929);
        Label_0091: {
            if (!FreeCamera.mc.gameSettings.keyBindJump.isKeyDown()) {
                final Minecraft mc2 = FreeCamera.mc;
                if (!Minecraft.player.collidedHorizontally) {
                    if (FreeCamera.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        final Minecraft mc3 = FreeCamera.mc;
                        Minecraft.player.motionY = -0.40000001192092893;
                    }
                    break Label_0091;
                }
            }
            final Minecraft mc4 = FreeCamera.mc;
            Minecraft.player.motionY = 0.45;
        }
        if (FreeCamera.mc.getCurrentServerData() != null && FreeCamera.mc.getCurrentServerData().serverIP != null && !FreeCamera.mc.getCurrentServerData().serverIP.contains("sunrise")) {
            final Minecraft mc5 = FreeCamera.mc;
            if (Minecraft.player.ticksExisted % 10 == 0) {
                final Minecraft mc6 = FreeCamera.mc;
                final NetHandlerPlayClient connection = Minecraft.player.connection;
                final Minecraft mc7 = FreeCamera.mc;
                connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
            }
        }
        eventMotion.cancel();
    }
    
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        final ScaledResolution sr = eventDisplay.sr;
        final StringBuilder append = new StringBuilder().append("");
        final double oldPosX = this.oldPosX;
        final Minecraft mc = FreeCamera.mc;
        final String posX = append.append((int)(-(oldPosX - Minecraft.player.posX))).toString();
        final StringBuilder append2 = new StringBuilder().append("");
        final double oldPosY = this.oldPosY;
        final Minecraft mc2 = FreeCamera.mc;
        final String posY = append2.append((int)(-(oldPosY - Minecraft.player.posY))).toString();
        final StringBuilder append3 = new StringBuilder().append("");
        final double oldPosZ = this.oldPosZ;
        final Minecraft mc3 = FreeCamera.mc;
        final String posZ = append3.append((int)(-(oldPosZ - Minecraft.player.posZ))).toString();
        final String plusOrMinusX = (!posX.contains("-") && !posX.equals("0")) ? "+" : "";
        final String plusOrMinusY = (!posY.contains("-") && !posY.equals("0")) ? "+" : "";
        final String plusOrMinusZ = (!posZ.contains("-") && !posZ.equals("0")) ? "+" : "";
        final String clipValue = "X: " + plusOrMinusX + posX + " Y: " + plusOrMinusY + posY + " Z: " + plusOrMinusZ + posZ;
        Fonts.MONTSERRAT16.drawString(clipValue, sr.getScaledWidth() / 2.0f - 25.0f, sr.getScaledHeight() / 2.0f + 7.0f, -1);
    }
    
    @Override
    public void onEnable() {
        final Minecraft mc = FreeCamera.mc;
        this.oldPosX = Minecraft.player.posX;
        final Minecraft mc2 = FreeCamera.mc;
        this.oldPosY = Minecraft.player.posY;
        final Minecraft mc3 = FreeCamera.mc;
        this.oldPosZ = Minecraft.player.posZ;
        final Minecraft mc4 = FreeCamera.mc;
        this.oldFlySpeed = Minecraft.player.capabilities.getFlySpeed();
        final WorldClient world = FreeCamera.mc.world;
        final Minecraft mc5 = FreeCamera.mc;
        final EntityOtherPlayerMP entityOtherPlayerMP;
        final EntityOtherPlayerMP player = entityOtherPlayerMP = new EntityOtherPlayerMP(world, Minecraft.player.getGameProfile());
        final Minecraft mc6 = FreeCamera.mc;
        entityOtherPlayerMP.copyLocationAndAnglesFrom(Minecraft.player);
        final EntityOtherPlayerMP entityOtherPlayerMP2 = player;
        final Minecraft mc7 = FreeCamera.mc;
        entityOtherPlayerMP2.rotationYawHead = Minecraft.player.rotationYawHead;
        FreeCamera.mc.world.addEntityToWorld(68812, player);
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        final Minecraft mc = FreeCamera.mc;
        Minecraft.player.capabilities.isFlying = false;
        final Minecraft mc2 = FreeCamera.mc;
        Minecraft.player.capabilities.setFlySpeed(this.oldFlySpeed);
        final Minecraft mc3 = FreeCamera.mc;
        final EntityPlayerSP player = Minecraft.player;
        final double oldPosX = this.oldPosX;
        final double oldPosY = this.oldPosY;
        final double oldPosZ = this.oldPosZ;
        final Minecraft mc4 = FreeCamera.mc;
        final float rotationYaw = Minecraft.player.rotationYaw;
        final Minecraft mc5 = FreeCamera.mc;
        player.setPositionAndRotation(oldPosX, oldPosY, oldPosZ, rotationYaw, Minecraft.player.rotationPitch);
        FreeCamera.mc.world.removeEntityFromWorld(68812);
        final Minecraft mc6 = FreeCamera.mc;
        Minecraft.player.motionZ = 0.0;
        final Minecraft mc7 = FreeCamera.mc;
        Minecraft.player.motionX = 0.0;
        super.onDisable();
    }
}
