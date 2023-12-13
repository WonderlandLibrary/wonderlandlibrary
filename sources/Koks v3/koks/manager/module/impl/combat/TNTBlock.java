package koks.manager.module.impl.combat;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;

/**
 * @author kroko
 * @created on 06.10.2020 : 19:06
 */

@ModuleInfo(name = "TNTBlock", description = "its block for you when your in a range from a tnt", category = Module.Category.COMBAT)
public class TNTBlock extends Module {

    public Setting blockFuse = new Setting("BlockTime", 5 , 3, 80, true, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if(getPlayer().getCurrentEquippedItem() != null) {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                        if (entity instanceof EntityTNTPrimed) {
                            EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed) entity;
                            if (entity.getDistanceToEntity(getPlayer()) <= 8) {
                                if (entityTNTPrimed.fuse == 0) {
                                    getGameSettings().keyBindUseItem.pressed = false;
                                } else if (entityTNTPrimed.fuse <= blockFuse.getCurrentValue()) {
                                    getGameSettings().keyBindUseItem.pressed = true;
                                } else {
                                    getGameSettings().keyBindUseItem.pressed = false;
                                }
                            } else {
                                getGameSettings().keyBindUseItem.pressed = false;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
