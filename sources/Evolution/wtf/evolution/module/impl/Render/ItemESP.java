package wtf.evolution.module.impl.Render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "ItemESP", type = Category.Render)
public class ItemESP extends Module {

    @EventTarget
    public void onRender(EventRender e) {
        for (EntityItem item : mc.world.getEntities(EntityItem.class, Entity::isEntityAlive)) {
            RenderUtil.renderItem(item, new Color(255, 255, 255, 100), e.pt);
        }
    }

}
