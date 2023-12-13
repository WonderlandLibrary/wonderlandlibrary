/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.combat.KillAura;
import tk.rektsky.utils.entity.EntityUtil;

public class FocusKiller
extends Module {
    public String targetName = "";
    public boolean sentToTarget = false;
    public String[] focusKillerMessages = new String[]{"YetYetYetYetYetYetYetYetYetYetYetYetYetYetYetYet", "LOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL", "TEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEKTEK", "OUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCHOUCH", "OOPS OOPS OOPS OOPS OOPS OOPS OOPS OOPS OOPS OOPS", "RekkedRekkedRekkedRekkedRekkedRekkedRekkedRekkedRekked", "YOU WASTED 2 SECONDS LOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL", "GAMING CHAIRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR", "OOOOOOOOOOOOOOO YAAAAAAAAAAAAAAAAAAAAAAAAA", "HHAAAAIIIIIIIIIIIIIIIIIII YYYYYYYAAAAAAAAAAAAAAAAA", "Uncle roger thinks you need to buy a rice cooker", "uvuvwevwevwe onyentyetyetye ugwemubwim osassss", "Tachanka, will always, stand  by  you", "LLLLLMMMMMMMMMGGGGGGGGG MOUNTTTTTTTTTTTTTTTED", "SURPRISE MDFK SURPRISE MDFK SURPRISE MDFK SURPRISE MDFK"};

    public FocusKiller() {
        super("FocusKiller", "Spam the chat", Category.REKTSKY, false);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WorldTickEvent) {
            KillAura killAura = ModulesManager.getModuleByClass(KillAura.class);
            if (killAura.t != null && this.enabledTicks % 4 == 0 && EntityUtil.getEntitiesWithAntiBot_old().contains(killAura.t) && killAura.t instanceof EntityPlayer && this.targetName.equals("")) {
                if (!killAura.attackPost) {
                    killAura.attackPost = true;
                    this.mc.thePlayer.sendChatMessage("/tell " + killAura.t.getName() + " " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                } else {
                    this.mc.thePlayer.sendChatMessage("/r " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                }
            }
            if (!this.targetName.equals("")) {
                if (!this.sentToTarget) {
                    this.sentToTarget = true;
                    this.mc.thePlayer.sendChatMessage("/tell " + this.targetName + " " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                } else {
                    this.mc.thePlayer.sendChatMessage("/r " + this.focusKillerMessages[new Random().nextInt(this.focusKillerMessages.length)]);
                }
            }
            if (killAura.t == null && killAura.attackPost) {
                killAura.attackPost = false;
            }
        }
    }
}

