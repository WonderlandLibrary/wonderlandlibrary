package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.AntiBot;
import info.sigmaclient.sigma.utils.font.CustomFont;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static info.sigmaclient.sigma.utils.render.RenderUtils.createFrameBuffer;
import static org.lwjgl.opengl.GL11.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NameTags extends Module {
    public ModeValue type = new ModeValue("Size", "Big", new String[]{"Big", "Small"});
    int ꁈ陬Ꮀ啖䄟 = 霥瀳놣㠠釒(贞䩉㥇딨햖(-65794, -16711423, 75.0f), 0.5f);
    BooleanValue magnify = new BooleanValue("Magnify", true);
    BooleanValue linear = new BooleanValue("Linear", true);
    static BooleanValue player = new BooleanValue("Player", false);
    static BooleanValue item = new BooleanValue("Item", false);
    static BooleanValue monster = new BooleanValue("Monster", false);
    static BooleanValue animals = new BooleanValue("Animals", false);
    static BooleanValue healthBypass = new BooleanValue("Health Bypass", false);
    public NameTags() {
        super("NameTags", Category.Render, "Shows entities better nametags");
     registerValue(type);
     registerValue(magnify);
//     registerValue(linear);
     registerValue(player);
     registerValue(item);
     registerValue(monster);
     registerValue(animals);
     registerValue(healthBypass);
    }

    public static int 贞䩉㥇딨햖(final int n, final int n2, final float n3) {
        final int n4 = n >> 24 & 0xFF;
        final int n5 = n >> 16 & 0xFF;
        final int n6 = n >> 8 & 0xFF;
        final int n7 = n & 0xFF;
        final int n8 = n2 >> 24 & 0xFF;
        final int n9 = n2 >> 16 & 0xFF;
        final int n10 = n2 >> 8 & 0xFF;
        final int n11 = n2 & 0xFF;
        final float n12 = 1.0f - n3;
        return (int)(n4 * n3 + n8 * n12) << 24 | ((int)(n5 * n3 + n9 * n12) & 0xFF) << 16 | ((int)(n6 * n3 + n10 * n12) & 0xFF) << 8 | ((int)(n7 * n3 + n11 * n12) & 0xFF);
    }
    public static boolean doRenderNametags(Entity e) {
        return SigmaNG.getSigmaNG().moduleManager.getModule(NameTags.class).enabled &&
                (
                        (e instanceof PlayerEntity && player.isEnable())
                        || (e instanceof ItemEntity && item.isEnable())
                        || (e instanceof MonsterEntity && monster.isEnable())
                        || (e instanceof AnimalEntity && animals.isEnable())
                ) && !AntiBot.isServerBots(e) && !mc.player.equals(e);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        for(Entity PlayerEntity : mc.world.getLoadedEntityList()){
            if(doRenderNametags(PlayerEntity)){
                renderNametags(PlayerEntity);
            }
        }
        super.onRender3DEvent(event);
    }

    public static int 霥瀳놣㠠釒(final int n, final float n2) {
        return (int)(n2 * 255.0f) << 24 | (n & 0xFFFFFF);
    }
    public void renderNametags(Entity entity) {
        if (entity == null) return;
        try {
            start();
            float health = 0;
            if(entity instanceof LivingEntity) {
                health = ((LivingEntity) entity).getHealth();
            }
            if(healthBypass.isEnable() && entity instanceof PlayerEntity player) {
                Scoreboard scoreboard = player.getWorldScoreboard();
                ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

                if (scoreobjective != null) {
                    Score score = scoreboard.getOrCreateScore(player.getScoreboardName(), scoreobjective);
                    health = score.getScorePoints();
                }
            }
            String 걾掬鞞娍曞 = entity.getName().getUnformattedComponentText();
            if(entity instanceof ItemEntity){
                걾掬鞞娍曞 = ((ItemEntity) entity).getItem().getDisplayName().getString();
            }
            float f = RenderUtils.getRenderPos().playerViewY;
            float f1 = RenderUtils.getRenderPos().playerViewX;

            double camX = RenderUtils.getRenderPos().renderPosX;
            double camY = RenderUtils.getRenderPos().renderPosY;
            double camZ = RenderUtils.getRenderPos().renderPosZ;
            final double n = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * mc.timer.renderPartialTicks;
            final double ex = n - camX;
            final double n2 = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * mc.timer.renderPartialTicks;
            final double ey = n2 - camY;
            final double n3 = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
            final double ez = n3 - camZ;

            double dist = entity.getDistanceToEntityLast(Minecraft.getInstance().player);
            float scale = 1.0f;
            if (magnify.isEnable()) {
                scale = (float)Math.max(1.0, Math.sqrt(dist / 30.0));
            }
            if(!(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA)){
                scale *= 2;
            }
            GL11.glAlphaFunc(519, 0.0f);
            if (SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA) {
            GL11.glTranslated(ex, ey + entity.getHeight() + 0.6f - 0.33333334f * (1.0f - scale), ez);
            GL11.glRotatef(-f, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);

            GL11.glScalef(-0.009f * scale, -0.009f * scale, -0.009f * scale);
                int n8 = this.ꁈ陬Ꮀ啖䄟;
                final int 霥瀳놣㠠釒 = 霥瀳놣㠠釒((entity instanceof PlayerEntity) ? new Color(entity.getTeamColor()).getRGB() : -65794, 0.5f);
                final int n9 = (int) (JelloFontUtil.jelloFont25.getStringWidthNoScale(걾掬鞞娍曞) / 2);
//                if (!阢属酭ᢻ뼢.室螜鷏嘖䎰.containsKey(걾掬鞞娍曞)) {
                    RenderUtils.sigma_drawShadow((float)(-n9 - 10), -25.0f, (float)(n9 * 2 + 20), (float)(JelloFontUtil.jelloFont25.getHeightNoScale() + 27), 20.0f, 0.5f);
//                }
//                else {
//                    㕠鄡呓ᢻ낛.㹔펊콵湗贞聛((float)(-n9 - 10 - 31), -25.0f, (float)(JelloFontUtil.jelloFont25.getHeightSigma5() + 27), (float)(JelloFontUtil.jelloFont25.getHeightSigma5() + 27), 阢属酭ᢻ뼢.室螜鷏嘖䎰.get(걾掬鞞娍曞), 霥瀳놣㠠釒(Color.getHSBColor(System.currentTimeMillis() % 10000L / 10000.0f, 0.5f, 1.0f).getRGB(), 0.7f));
//                    㕠鄡呓ᢻ낛.㹔펊콵湗贞聛((float)(-n9 - 10 - 31 + JelloFontUtil.jelloFont25.getHeightSigma5() + 27), -25.0f, 14.0f, (float)(JelloFontUtil.jelloFont25.getHeightSigma5() + 27), 뚔弻缰硙柿.室唟钘觯곻, 霥瀳놣㠠釒(-65794, 0.6f));
//                    㕠鄡呓ᢻ낛.drawShadow1((float)(-n9 - 10 - 31), -25.0f, (float)(n9 * 2 + 20 + 31 + 27), (float)(JelloFontUtil.jelloFont25.getHeightSigma5() + 27), 20.0f, 0.5f);
//                    GL11.glTranslatef(27.0f, 0.0f, 0.0f);
//                }

                final String string = Math.round(health * 10.0f) / 10.0f + "";
                final float min = Math.max(Math.min(health / ((LivingEntity)entity).getMaxHealth(), 1.0f), 0.0f);
                RenderUtils.drawRect((float)(-n9 - 10), -25.0f, (float)(n9 + 10), (float)(JelloFontUtil.jelloFont25.getHeightNoScale() + 2), n8);
                RenderUtils.drawRect((float)(-n9 - 10), JelloFontUtil.jelloFont25.getHeightNoScale() - 1 - ((LivingEntity)entity).hurtTime / 3.0f, Math.min((n9 * 2 + 20) * (min - 0.5f), (float)(n9 + 10)), (float)(JelloFontUtil.jelloFont25.getHeightNoScale() + 2), 霥瀳놣㠠釒);
//                GL11.glPushMatrix();
                GL11.glTranslated(-JelloFontUtil.jelloFont25.getStringWidthNoScale(걾掬鞞娍曞) / 2, 0.0, 0.0);
                final int getStringWidthNoScale = (int) JelloFontUtil.jelloFont14.getStringWidthNoScale("Health: 20.0");
                String str = "Health: ";
                if (getStringWidthNoScale > JelloFontUtil.jelloFont25.getStringWidthNoScale(걾掬鞞娍曞)) {
                    str = "H: ";
                }
                JelloFontUtil.jelloFont25.drawStringNoScaleSB(걾掬鞞娍曞, 0.0f, -20.0f, -65794, linear.isEnable());
                JelloFontUtil.jelloFont14.drawStringNoScaleSB(str + string, 0.0f, 10.0f, -65794, linear.isEnable());
//                final 괠㥇顸罡贞 ꮤၝ堧堧ศ = SigmaMain.鼒釒쇽Ꮤ鶊().늦ศ댠泹鞞().婯ศ쇽顸筕.Ꮤၝ堧堧ศ(璧室䖼頉啖);
//                if (ꮤၝ堧堧ศ != null) {
//                    JelloFontUtil.jelloFont14.drawString(0.0f, -30.0f, ꮤၝ堧堧ศ.湗㹔Ꮤ樽㦖, -65794);
//                }
            } else {
                GL11.glTranslated(ex, ey + entity.getHeight() + 0.6f - 0.33333334f * (1.0f - scale), ez);
                GL11.glRotatef(-f, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
                float sc = type.is("Small") ? 1.2f : 1.5f;
                GL11.glScalef(-0.009f * scale * sc, -0.009f * scale * sc, -0.009f * scale * sc);
                // new
                CustomFont font = FontUtil.sfuiFont18;
                double x = 0;
                double y = 10;
                double height = 10;
                if(entity instanceof PlayerEntity) {
                    PlayerEntity PlayerEntity = (PlayerEntity) entity;
                    String healbar = " \u00a7a[" + String.format("%.1f", health) + "]";
                    double width = font.getStringWidth(PlayerEntity.getName().getUnformattedComponentText() + healbar) + 1;
                    width = Math.max(58, width); // LOL
//                RenderUtils.drawShadow(x - width / 2, y - height / 2, x + width / 2, y + height / 2 - 2, 100 / 255f);
                    RenderUtils.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2 - 2, new Color(0.117647059f, 0.117647059f, 0.117647059f, 0.6f).getRGB());
                    font.drawSmoothString(PlayerEntity.getName().getUnformattedComponentText() + healbar, (float) (x - width / 2 + 1), (float) (y - height / 2 + 1),
                            -1);
                }else if(entity instanceof ItemEntity){
                    String[] str = ((ItemEntity)entity).getItem().getTranslationKey().split("\\.");
                    String n23 = str[str.length-1] + " x"+((ItemEntity)entity).getItem().getStackSize();
                    double width = mc.fontRenderer.getStringWidth(n23) + 1;
                    font.drawSmoothString(n23, (float) (x - width / 2 + 1), (float) (y - height / 2 + 1),
                            -1);
                } else {
                    String n23 = (entity).getName().getUnformattedComponentText();
                    double width = mc.fontRenderer.getStringWidth(n23) + 1;
                    font.drawSmoothString(n23, (float) (x - width / 2 + 1), (float) (y - height / 2 + 1),
                            -1);
                }
            }
            stop();
        } catch (Exception ignore) {
            stop();
        }
    }
    public void start(){
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL_DEPTH_TEST);
//        GL11.glDisable(GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
    }
    public void stop(){
        GL11.glPopMatrix();
        GL11.glEnable(GL_DEPTH_TEST);
//        GL11.glEnable(GL_LIGHTING);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.disableLighting();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
