package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import today.flux.event.UIRenderEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.AntiBots;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.ESP2D;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;

import java.awt.*;


public class ESP extends Module {
    public static ModeValue mode = new ModeValue("ESP", "Mode", "CSGO", "CSGO", "Outline", "2DBox", "3DBox", "Cylinder");

    public static ColorValue espColours = new ColorValue("ESP", "Box Color", Color.GRAY);
    public static ColorValue healthColors = new ColorValue("ESP", "HP Bar Color", Color.MAGENTA);

    public static BooleanValue tags = new BooleanValue("ESP", "Tags", true);
    public static BooleanValue armor = new BooleanValue("ESP", "Armor Bar", true);
    public static BooleanValue health = new BooleanValue("ESP", "HP Bar", true);
    public static ModeValue healthColorMode = new ModeValue("ESP", "HP Bar Color Mode", "Health", "Static");
    public static BooleanValue box = new BooleanValue("ESP", "Box", true);

    public static BooleanValue invisible = new BooleanValue("ESP", "Invisible", false);
    public static BooleanValue player = new BooleanValue("ESP", "Player", true);
    public static BooleanValue mob = new BooleanValue("ESP", "Mob", false);

    public ESP() {
        super("ESP", Category.Render, mode);
    }

    @EventTarget(Priority.LOW)
    public void onRenderGui(UIRenderEvent event) {
        if(mode.isCurrentMode("CSGO")) {
            ESP2D.INSTANCE.renderBox(mc);
        }
    }

    Color c = espColours.getColor();

    @EventTarget
    private void onRender3D(final WorldRenderEvent event) {
        if (mode.isCurrentMode("3DBox")) {
            for (EntityLivingBase entity : WorldUtil.getLivingEntities()) {
                if (!isValidForESP(entity)) {
                    continue;
                }

                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosX();
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosY();
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosZ();

                WorldRenderUtils.drawOutlinedEntityESP(posX, posY, posZ, 0.4, entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
            }
        }

        if (mode.isCurrentMode("2DBox")) {
            for (EntityLivingBase entity : WorldUtil.getLivingEntities()) {
                try {
                    if (!isValidForESP(entity)) {
                        continue;
                    }

                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosX();
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosY();
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosZ();

                    GL11.glPushMatrix();

                    GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);

                    double width = 0.25;
                    double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY;

                    WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width));
                    WorldRenderUtils.renderOne();
                    WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width));
                    WorldRenderUtils.renderTwo();
                    WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width));
                    WorldRenderUtils.renderThree();
                    WorldRenderUtils.renderFour(entity);
                    WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width));
                    WorldRenderUtils.renderFive();

                    GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);

                    GL11.glPopMatrix();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (mode.isCurrentMode("Cylinder")) {
            for (EntityLivingBase entity : WorldUtil.getLivingEntities()) {
                if (!isValidForESP(entity)) {
                    continue;
                }

                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosX();
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosY();
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosZ();

                WorldRenderUtils.drawWolframEntityESP(entity, espColours.getColorInt(), posX, posY, posZ);
            }
        }

        if (mode.isCurrentMode("CSGO")) {
            ESP2D.INSTANCE.updatePositions(mc);
        }
    }

    public static boolean isValidForESP(EntityLivingBase entity) {
        if(ModuleManager.noRenderMod.isEnabled() && ModuleManager.noRenderMod.players.getValueState() && entity instanceof EntityPlayer && entity != mc.thePlayer) return false;

        if (entity == null) {
            return false;
        }

        if ((entity instanceof EntityWaterMob || entity instanceof EntityCreature || entity instanceof EntityAmbientCreature || entity instanceof EntityArmorStand || entity instanceof EntityGhast) && mob.getValue()) {
            return true;
        }

        if (entity instanceof EntitySlime)
            return false;

        if (entity.isDead || entity.noClip) {
            return false;
        }

        if (entity.isInvisible() && !invisible.getValue()) {
            return false;
        }

        if (entity instanceof EntityPlayer && !player.getValue()) {
            return false;
        }

        if (AntiBots.isHypixelNPC(entity)) {
            return false;
        }

        return true;
    }
}
