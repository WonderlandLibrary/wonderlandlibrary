package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventMotion;
import summer.cheat.eventsystem.events.player.EventUpdate;

import java.util.ArrayList;

public class Speed extends Cheats {
    // simple ass speed kek
    public Minecraft mc = Minecraft.getMinecraft();
    private int stage;
    private int hops;
    private double moveSpeed;
    private double lastDist;
    private String mode;
    private TargetStrafe targetStrafe;

    public Speed() {
        super("Speed", "Bhop", Selection.MOVEMENT, false);
    }

    @Override
    public void onSetup() {
        final ArrayList<String> options = new ArrayList<String>();
    }
    @EventTarget
    public void Packet(final EventPacket ep) {
        if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
        	this.toggle();
        }
    }
    @EventTarget
    public void onUpdate(final EventMotion eventMove) {
    	double motionY, lastDist, n, lastDist2;
        EntityPlayerSP thePlayer;
        if (this.targetStrafe == null)
            this.targetStrafe = (TargetStrafe) CheatManager.getInstance(TargetStrafe.class);
        switch (this.stage) {
            case 0:
                this.stage++;
                this.lastDist = 0.0D;
                break;
            case 2:
                this.lastDist = 0.0D;
                motionY = 0.41199998688697814D;
                if (Minecraft.thePlayer.isMoving() && Minecraft.thePlayer.onGround) {
                    if (Minecraft.thePlayer.isPotionActive(Potion.jump))
                        motionY += ((Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                    eventMove.setY(Minecraft.thePlayer.motionY = motionY);
                    this.hops++;
                    this.moveSpeed *= 1.90D;
                }
                break;
            case 3:
                lastDist = this.lastDist;
                n = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? (Minecraft.thePlayer.isPotionActive(Potion.jump) ? 0.54D : 0.655D) : 0.7025D;
                lastDist2 = this.lastDist;
                thePlayer = Minecraft.thePlayer;
                this.moveSpeed = lastDist - n * (lastDist2 - EntityPlayerSP.getBaseMoveSpeed());
                break;
            default:
                if ((mc.theWorld.getCollidingBoundingBoxes((Entity)Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, Minecraft.thePlayer.motionY, 0.0D)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) && this.stage > 0)
                    this.stage = mc.thePlayer.isMoving() ? 1 : 0;
                this.moveSpeed = this.lastDist - this.lastDist  / 69.0D;
                break;
        }
        MoveUtils.setMotion(eventMove, this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.defaultSpeed() + 0.011219999998D));
        this.stage++;
        if (this.targetStrafe.canStrafe())
            this.targetStrafe.strafe(eventMove, this.moveSpeed);
    }

    public static boolean isNotCollidingBelow(double paramDouble) {
        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -paramDouble, 0.0D)).isEmpty()) {
            return true;
        }
        return false;
    }


    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.motionZ = 0.0;
        Minecraft.thePlayer.motionX = 0.0;
        Timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.lastDist = 0.0D;
        this.hops = 1;
        this.stage = 0;
        this.moveSpeed = EntityPlayerSP.getBaseMoveSpeed() * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.0D : 1.34D);
    }
}




