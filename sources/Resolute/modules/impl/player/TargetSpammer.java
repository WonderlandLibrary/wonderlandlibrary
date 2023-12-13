// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.modules.impl.combat.KillAura;
import vip.Resolute.events.impl.EventTick;
import vip.Resolute.events.Event;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.modules.Module;

public class TargetSpammer extends Module
{
    public String targetName;
    public boolean sentToTarget;
    public String[] focusKillerMessages;
    public TimerUtils timerUtils;
    
    public TargetSpammer() {
        super("TargetSpam", 0, "Spams whoever you are targeting", Category.PLAYER);
        this.timerUtils = new TimerUtils();
        this.targetName = "";
        this.sentToTarget = false;
        this.focusKillerMessages = new String[] { "SOBADSOBADSOBADSOBADSOBADSOBADSOBADSOBADSOBADSOBADSOBAD", "LOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL", "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", "OUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCH", "HAHAHAHAHAHAHAHAHAHAHAHAHAHAHA", "TRASHTRASHTRASHTRASHTRASHTRASHTRASHTRASH", "YOU ARE SO BAD LLLLLLLLLLLLLLLLLLLLLLLLLLL", "GAMING CHAIRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR", "OOOOOOOOOOOOOOO YAAAAAAAAAAAAAAAAAAAAAAAAA", "Just go get Impulse client you fucking noob", "fragfawehiuwhqiurwheiurqhiuher", "F4GF4GF4GF4GF4GF4GF4GF4GF4GF4GF4GF4GF4G", "FUCKEDFUCKEDFUCKEDFUCKEDFUCKEDFUCKEDFUCKED", "SURPRISE BITCH SURPRISE BITCH SURPRISE BITCH SURPRISE BITCH" };
    }
    
    @Override
    public void onEnable() {
        this.timerUtils.reset();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventTick) {
            if (KillAura.target != null && KillAura.target instanceof EntityPlayer) {
                if (!this.sentToTarget) {
                    TargetSpammer.mc.thePlayer.sendChatMessage("/tell " + KillAura.target.getName() + " " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                    this.sentToTarget = true;
                }
                else {
                    TargetSpammer.mc.thePlayer.sendChatMessage("/r " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                }
            }
            else {
                this.sentToTarget = false;
            }
        }
    }
}
