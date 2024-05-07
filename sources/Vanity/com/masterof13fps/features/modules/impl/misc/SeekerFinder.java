package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;

@ModuleInfo(name = "SeekerFinder", category = Category.MISC, description = "Finds the seeker in Hide&Seek")
public class SeekerFinder extends Module {

    @Override
    public void onToggle() {
    }

    @Override
    public void onEnable() {
        for (Entity e : mc.theWorld.loadedEntityList) {
            try {
                if (((e instanceof EntityPlayer)) && ((((EntityPlayer) e).getCurrentEquippedItem().getItem() instanceof ItemSword))) {
                    notify.notification("Seeker erkannt!", "Achtung! " + e.getName() + " ist der Seeker!",
                            NotificationType.INFO, 5);
                }
            } catch (NullPointerException ignored) {
            }
        }
        getModuleManager().getModule(SeekerFinder.class).setState(false);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEvent(Event event) {
    }
}
