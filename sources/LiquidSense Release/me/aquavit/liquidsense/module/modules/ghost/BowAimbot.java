package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@ModuleInfo(name = "BowAimbot", description = "Automatically aims at players when using a bow.", category = ModuleCategory.GHOST)
public class BowAimbot extends Module {

    private final BoolValue silentValue = new BoolValue("Silent", true);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    private final FloatValue predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction"}, "Direction");
    private final BoolValue markValue = new BoolValue("Mark", true);
    private Entity target;

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        target = null;
        ItemStack itemStack = mc.thePlayer.getItemInUse();
        if ((itemStack != null ? itemStack.getItem() : null) instanceof ItemBow) {
            Entity entity = getTarget(throughWallsValue.get(), priorityValue.get());
            if (entity == null)return;
            target = entity;
            RotationUtils.faceBow(target, silentValue.get(), predictValue.get(), predictSizeValue.get());
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (target != null && markValue.get())
            RenderUtils.drawPlatform(target, new Color(37, 126, 255, 70));
    }

    private Entity getTarget(boolean throughWalls, String priorityMode) {
        List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(it -> it instanceof EntityLivingBase && EntityUtils.isSelected(it, true,false) &&
                (throughWalls || mc.thePlayer.canEntityBeSeen(it))).collect(Collectors.toList());;

        switch (priorityMode.toLowerCase()){
            case "distance":
                targets.sort(Comparator.comparingDouble(value -> mc.thePlayer.getDistanceToEntity(value)));
                break;
            case "direction":
                targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super Entity>) RotationUtils::getRotationDifference));
                break;
            case "health":
                targets.sort(Comparator.comparingDouble(it -> ((EntityLivingBase)it).getHealth()));
                break;
            default:
                targets = null;

        }

        return targets != null ? targets.get(0) : null;
    }

    public boolean hasTarget() {
        return target != null && mc.thePlayer.canEntityBeSeen(target);
    }
}
