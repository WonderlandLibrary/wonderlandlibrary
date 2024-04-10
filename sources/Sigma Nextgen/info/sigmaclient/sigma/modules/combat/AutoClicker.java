package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.player.OldHitting;
import info.sigmaclient.sigma.utils.TimerUtil;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoClicker extends Module {
    public NumberValue mcps = new NumberValue("MinCPS", 10, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().intValue() > cps.getValue().intValue()){
                this.pureSetValue(cps.getValue().intValue());
            }
            super.onSetValue();
        }
    };
    public NumberValue cps = new NumberValue("MaxCPS", 12, 0, 20, NumberValue.NUMBER_TYPE.INT){
        @Override
        public void onSetValue() {
            if(this.getValue().intValue() < mcps.getValue().intValue()){
                this.pureSetValue(mcps.getValue().intValue());
            }
            super.onSetValue();
        }
    };
    BooleanValue swordOnly = new BooleanValue("Sword Only", true);
    BooleanValue autoSag = new BooleanValue("AutoBlock", false);
    public AutoClicker() {
        super("AutoClicker", Category.Combat, "Auto click");
     registerValue(mcps);
     registerValue(cps);
     registerValue(swordOnly);
     registerValue(autoSag);
    }
    TimerUtil timerUtil = new TimerUtil();
    long calc_cps;
    boolean lastBlocking = false;
    @Override
    public void onClickEvent(ClickEvent event) {
        boolean doesBlocking = mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem;
        boolean doClick = (!swordOnly.getValue() || mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem) && mc.gameSettings.keyBindAttack.pressed;
        if(autoSag.getValue() && doesBlocking && doClick){
            OldHitting.blocking = true;
        }
        if(timerUtil.hasTimeElapsed(calc_cps)){
            if (autoSag.getValue() && lastBlocking) {
                lastBlocking = false;
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
            if(!doClick) return;
            if(mc.player.isHandActive()) return;
            int cpss = (int)(mcps.getValue().intValue() + (cps.getValue().intValue() - mcps.getValue().intValue() + 1) *
                    new Random().nextDouble());
            if(cpss == 0) return;
            calc_cps = 1000 / cpss;
            mc.clickMouse();
            mc.leftClickCounter = 0;
            timerUtil.reset();
        }else {
            if (autoSag.getValue() && !lastBlocking && doesBlocking) {
                lastBlocking = true;
                mc.gameSettings.keyBindUseItem.pressed = true;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
