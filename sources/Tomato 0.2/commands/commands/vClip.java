package cow.milkgod.cheese.commands.commands;

import cow.milkgod.cheese.commands.*;
import cow.milkgod.cheese.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import net.minecraft.entity.*;
import com.darkmagician6.eventapi.*;

public class vClip extends Command
{
    private boolean damage;
    
    public vClip() {
        super("vClip", null, new String[] { "vc" }, "<blocks>", "Teleports some blocks vertically");
        this.damage = false;
    }
    
    @Override
    public void Toggle() {
        if (this.damage) {
            Cheese.getInstance();
            Cheese.commandManager.getCommandbyName("Damage").Toggle();
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + 0.01, Wrapper.mc.thePlayer.posZ, false));
        }
        super.Toggle();
    }
    
    @EventTarget
    public void onChatSend(final EventTick event) {
        if (this.getState()) {
            try {
                final double blocks = Double.parseDouble(EventChatSend.getMessage().split(" ")[1]);
                final boolean isNegative = blocks < 0.0;
                final Entity e = this.mc.thePlayer.ridingEntity;
                final double yPos = this.mc.thePlayer.posY + (blocks + 0.002);
                this.mc.thePlayer.setLocationAndAngles(this.mc.thePlayer.posX, yPos, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
                if (e != null) {
                    e.setPosition(e.posX, yPos, e.posZ);
                }
                Cheese.getInstance();
                if (!Cheese.moduleManager.getModState("Freecam")) {
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, yPos - (isNegative ? 0.1 : 0.0), this.mc.thePlayer.posZ, true));
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - (isNegative ? 1 : 0), this.mc.thePlayer.posZ, false));
                }
                Logger.logChat("Teleported \"" + blocks + "\" blocks.");
            }
            catch (Exception E) {
                Logger.logChat("U fucked up! vclip <blocks>");
            }
            this.Toggle();
        }
    }
}
