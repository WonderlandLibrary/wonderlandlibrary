package io.github.raze.modules.collection.misc;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.utilities.collection.visual.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;

public class HackerDetector extends AbstractModule {

    private final BooleanSetting checkNoFall,checkCFly,checkAirWalk;

    public HackerDetector() {
        super("HackerDetector", "Tries to catch hackers.", ModuleCategory.MISC);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                checkNoFall = new BooleanSetting(this, "Check NoFall", true),
                checkCFly = new BooleanSetting(this, "Check Creative Fly", true),
                checkAirWalk = new BooleanSetting(this, "Check AirWalk Fly", true)

        );

    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            for(Object object1 : mc.theWorld.loadedEntityList) {
                if(object1 instanceof EntityPlayer) {
                    EntityPlayer susUser = (EntityPlayer) object1;
                    if(!susUser.isHacker && susUser != mc.thePlayer) {
                        if (susUser.fallDistance > 3 && susUser.onGround && !susUser.capabilities.isCreativeMode && checkNoFall.get()) {
                            ChatUtil.addChatMessage(susUser.getName() + " is using NoFall!", true);
                            susUser.isHacker = true;
                        }
                        if (susUser.capabilities.isFlying && !susUser.capabilities.isCreativeMode && checkCFly.get()) {
                            ChatUtil.addChatMessage(susUser.getName() + " is using Creative Fly!", true);
                            susUser.isHacker = true;
                        }
                        if ((susUser.motionY == 0 && susUser.isAirBorne) && !susUser.capabilities.isCreativeMode && checkAirWalk.get()) {
                            ChatUtil.addChatMessage(susUser.getName() + " is using AirWalk!", true);
                            susUser.isHacker = true;
                        }
                    }
                }
            }
        }
    }
}
