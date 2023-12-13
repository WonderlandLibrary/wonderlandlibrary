package summer.cheat.cheats.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.RenderUtils;
import summer.base.utilities.RotationUtils;
import summer.cheat.cheats.combat.AntiBot;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.guiutil.Setting;

import java.awt.*;

/**
 * alerithe
 */
public class Radar extends Cheats {
    private final Setting players;
    private final Setting monsters;
    private final Setting animals;
    private final Setting passives;
    private final Setting invisibles;
    private final Setting items;
    private final Setting x;
    private final Setting y;
    private final Setting size;

    public Radar() {
        super("Radar", "Draws dots at nearby player positions", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(players = new Setting("Players", this, true));
        Summer.INSTANCE.settingsManager.Property(monsters = new Setting("Monsters", this, false));
        Summer.INSTANCE.settingsManager.Property(animals = new Setting("Animals", this, false));
        Summer.INSTANCE.settingsManager.Property(passives = new Setting("Passives", this, false));
        Summer.INSTANCE.settingsManager.Property(invisibles = new Setting("Invisibles", this, true));
        Summer.INSTANCE.settingsManager.Property(items = new Setting("Items", this, false));
        Summer.INSTANCE.settingsManager.Property(x = new Setting("X", this, 2, 0, 897, true));
        Summer.INSTANCE.settingsManager.Property(y = new Setting("Y", this, 27, 0, 340, true));
        Summer.INSTANCE.settingsManager.Property(size = new Setting("Size", this, 70, 25, 200, true));
    }

    @EventTarget
    private void onOverlayDraw(EventRender2D event) {
        if (!mc.gameSettings.showDebugInfo) {
            GL11.glPushMatrix();

            int x = this.x.getValInt();
            int y = this.y.getValInt();
            int width = this.size.getValInt();
            int height = this.size.getValInt();
            float cx = x + (width / 2f);
            float cy = y + (height / 2f);

            RenderUtils.drawBorderedRect(x, y, x + width, y + height, 1, 0xFF444444, 0xFF222222);
            RenderUtils.drawRectSized(x + (width / 2f) - 0.5f, y, 1, height, 0xFF444444);
            RenderUtils.drawRectSized(x, y + (height / 2f) - 0.5f, width, 1, 0xFF444444);
            RenderUtils.drawRectSized(cx - 1, cy - 1, 2, 2, 0xFFFFFF00);

            int maxDist = size.getValInt() / 2;
            for (Entity entity : Minecraft.theWorld.loadedEntityList) {
                if (qualifies(entity)) {
                    // X difference
                    double dx = RenderUtils.lerp(entity.prevPosX, entity.posX, event.getTicks())
                            - RenderUtils.lerp(Minecraft.thePlayer.prevPosX, Minecraft.thePlayer.posX,
                            event.getTicks());
                    // Z difference
                    double dz = RenderUtils.lerp(entity.prevPosZ, entity.posZ, event.getTicks())
                            - RenderUtils.lerp(Minecraft.thePlayer.prevPosZ, Minecraft.thePlayer.posZ,
                            event.getTicks());

                    // Make sure they're within the available rendering range
                    if ((dx * dx + dz * dz) <= (maxDist * maxDist)) {
                        float dist = MathHelper.sqrt_double(dx * dx + dz * dz);
                        double[] vector = getLookVector(
                                RotationUtils.getRotations(entity)[0] - (float) RenderUtils.lerp(Minecraft.thePlayer.prevRotationYawHead, Minecraft.thePlayer.rotationYawHead, event.getTicks()));
                        if (entity instanceof EntityMob) {
                            RenderUtils.drawRectSized(cx - 1 - ((float) vector[0] * dist), cy - 1 - ((float) vector[1] * dist), 2, 2,
                                    new Color(0, 252, 103).getRGB());
                        } else if (entity instanceof EntityPlayer) {
                            RenderUtils.drawRectSized(cx - 1 - ((float) vector[0] * dist), cy - 1 - ((float) vector[1] * dist), 2, 2,
                                    new Color(248, 0, 0).getRGB());
                        } else if (entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityVillager || entity instanceof EntityGolem) {
                            RenderUtils.drawRectSized(cx - 1 - ((float) vector[0] * dist), cy - 1 - ((float) vector[1] * dist), 2, 2,
                                    new Color(248, 178, 0).getRGB());
                        } else if (entity instanceof EntityItem) {
                            RenderUtils.drawRectSized(cx - 1 - ((float) vector[0] * dist), cy - 1 - ((float) vector[1] * dist), 2, 2,
                                    new Color(0, 147, 241).getRGB());
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
    }


    public double[] getLookVector(float yaw) {
        yaw *= MathHelper.deg2Rad;
        return new double[]{
                -MathHelper.sin(yaw),
                MathHelper.cos(yaw)
        };
    }

    private boolean qualifies(Entity entity) {
        return ((entity instanceof EntityPlayer && this.players.getValBoolean() && !AntiBot.isBot((EntityPlayer) entity))
                || (entity instanceof EntityMob && this.monsters.getValBoolean())
                || ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && this.animals.getValBoolean())
                || ((entity instanceof EntityVillager || entity instanceof EntityGolem) && this.passives.getValBoolean())
                || (entity instanceof EntityItem && items.getValBoolean()))
                && (!entity.isInvisible() || this.invisibles.getValBoolean()) && entity != Minecraft.thePlayer;
    }
}
