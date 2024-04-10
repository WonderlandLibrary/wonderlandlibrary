package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.AntiBot;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.M3DUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Triangle extends Module {
    public static NumberValue range = new NumberValue("Radius", 30, 1, 100, NumberValue.NUMBER_TYPE.INT);
    public static NumberValue alpha = new NumberValue("Alpha", 1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    static BooleanValue name = new BooleanValue("Name", false);
    static BooleanValue player = new BooleanValue("Player", false);
    static BooleanValue item = new BooleanValue("Item", false);
    static BooleanValue monster = new BooleanValue("Monster", false);
    static BooleanValue animals = new BooleanValue("Animals", false);
    public Triangle() {
        super("Triangle", Category.Render, "Warning when entity close to you");
     registerValue(range);
     registerValue(alpha);
     registerValue(name);
     registerValue(player);
     registerValue(item);
     registerValue(monster);
     registerValue(animals);
    }

    public static boolean doRenderNametags(Entity e) {
        return SigmaNG.getSigmaNG().moduleManager.getModule(Triangle.class).enabled &&
                (
                        (e instanceof PlayerEntity && player.isEnable())
                        || (e instanceof ItemEntity && item.isEnable())
                        || (e instanceof MonsterEntity && monster.isEnable())
                        || (e instanceof AnimalEntity && animals.isEnable())
                ) && !AntiBot.isServerBots(e) && !mc.player.equals(e);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        for(Entity PlayerEntity : mc.world.getLoadedEntityList()){
            if(doRenderNametags(PlayerEntity)){
                renderNametags(PlayerEntity);
            }
        }
    }
    public void renderNametags(Entity entity) {
        if (entity == null) return;
//        if(entity.getDistanceSqToEntity(mc.player) < range.getValue().intValue() * range.getValue().intValue()){
            float angel = RotationUtils.toRotation(new Vector3d(M3DUtil.getInterpolatedPos(entity))).getYaw() + 90F;
            float aa = angel - MathHelper.wrapAngleTo180_float(mc.player.rotationYaw) - 90F;
//            if(aa > -90 && aa < 90) return;
            GlStateManager.pushMatrix();
            ScaledResolution sr = new ScaledResolution(mc);
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.rotate(aa, 0, 0, 1);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
            if(name.isEnable() && entity instanceof PlayerEntity){
                String str = entity.getName().getUnformattedComponentText() + " ("+((int)entity.getDistance(mc.player))+"m)";
                FontUtil.sfuiFont16.drawSmoothString(str,
                        sr.getScaledWidth() / 2f - FontUtil.sfuiFont16.getStringWidth(entity.getName().getUnformattedComponentText()) / 2f,
                        sr.getScaledHeight() / 2f - 20 - range.getValue().floatValue(), -1);
            }
            RenderUtils.drawTextureLocationZoom(sr.getScaledWidth() / 2f - 32 * 0.6f * 0.5f, sr.getScaledHeight() / 2f - 30 - 32 * 0.6f * 0.5f - range.getValue().floatValue(), 32 * 0.6f, 32 * 0.6f, "arrow", ColorUtils.reAlpha(-1, alpha.getValue().floatValue()));
            GlStateManager.popMatrix();
//        }
    }
}
