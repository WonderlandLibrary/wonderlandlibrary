package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.sigma5.utils.㱙롤웎钘㐈;
import info.sigmaclient.sigma.sigma5.utils.꿩婯㐖쥦꿩;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.ࡅ揩柿괠竁頉;
import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.骰괠啖㕠挐酋;
import static info.sigmaclient.sigma.sigma5.utils.㱙롤웎钘㐈.殢嶗퉧竬鼒ศ;
import static info.sigmaclient.sigma.sigma5.utils.㱙롤웎钘㐈.霥뚔玑䬾頉蒕;
import static info.sigmaclient.sigma.utils.render.RenderUtils.霥瀳놣㠠釒;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Projectiles extends Module {
    public 꿩婯㐖쥦꿩 뎫붛㻣㱙랾;
    public 꿩婯㐖쥦꿩 掬陂㹔ꪕ褕;
    public 꿩婯㐖쥦꿩 鶊鷏䈔놣酭;
    public Projectiles() {
        super("Projectiles", Category.Render, "Predict the path of a projectile");
        this.뎫붛㻣㱙랾 = new 꿩婯㐖쥦꿩(0.0f, 0.0f, 0.0f);
        this.掬陂㹔ꪕ褕 = new 꿩婯㐖쥦꿩(0.0f, 0.0f, 0.0f);
        this.鶊鷏䈔놣酭 = new 꿩婯㐖쥦꿩(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        final 㱙롤웎钘㐈 柿鱀쿨亟 = 㱙롤웎钘㐈.柿鱀쿨亟(mc.player.getHeldItemMainhand().getItem());
        if (柿鱀쿨亟 != null) {
            final float n = (float) Math.toRadians(mc.player.rotationPitch - 25.0f);
            final float n2 = (float) Math.toRadians(mc.player.rotationPitch);
            final double n3 = 0.20000000298023224;
            final double n4 = 0;
            final double n5 = 霥뚔玑䬾頉蒕(n) * n4;
            final double n6 = 殢嶗퉧竬鼒ศ(n) * n4;
            final double n7 = mc.player.lastTickPosX + (mc.player.getPosX() - mc.player.lastTickPosX) * mc.timer.renderPartialTicks;
            final double n8 = mc.player.lastTickPosY + (mc.player.getPosY() - mc.player.lastTickPosY) * mc.timer.renderPartialTicks;
            final double n9 = mc.player.lastTickPosZ + (mc.player.getPosZ() - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks;
            GL11.glPushMatrix();
            GL11.glEnable(GL_LINE_SMOOTH);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
//            GL11.glEnable(32925);
            GL11.glDisable(2896);
            GL11.glShadeModel(7425);
            GL11.glDepthMask(false);
            GL11.glLineWidth(10.0f);
            GL11.glColor4d(0.0, 0.0, 0.0, 0.05000000074505806);
            GL11.glAlphaFunc(519, 0.0f);
            GL11.glBegin(3);
            final List<Vector3d> 뫤顸鷏㼜瀧 = 柿鱀쿨亟.뫤顸鷏㼜瀧();
            RenderUtils.renderPos r = RenderUtils.getRenderPos();
            for (int i = 0; i < 뫤顸鷏㼜瀧.size(); ++i) {
                final Vector3d 竬㞈㔢洝鶊 = 뫤顸鷏㼜瀧.get(i);
                final double n10 = n5 - (i + 1) / (float) 뫤顸鷏㼜瀧.size() * n5;
                final double n11 = n6 - (i + 1) / (float) 뫤顸鷏㼜瀧.size() * n6;
                final double n12 = n3 - (i + 1) / (float) 뫤顸鷏㼜瀧.size() * n3;
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.05f * Math.min(1, i));
                GL11.glVertex3d(
                        竬㞈㔢洝鶊.x - r.renderPosX - n10,
                        竬㞈㔢洝鶊.y - r.renderPosY - n12,
                        竬㞈㔢洝鶊.z - r.renderPosZ - n11);
            }
            GL11.glEnd();
            GL11.glLineWidth(2.0f * SigmaNG.lineWidth);
            GL11.glColor4d(1.0, 1.0, 1.0, 0.75);
            GL11.glBegin(3);
            for (int j = 0; j < 뫤顸鷏㼜瀧.size(); ++j) {
                final Vector3d 竬㞈㔢洝鶊2 = 뫤顸鷏㼜瀧.get(j);
                final double n13 = n5 - (j + 1) / (float) 뫤顸鷏㼜瀧.size() * n5;
                final double n14 = n6 - (j + 1) / (float) 뫤顸鷏㼜瀧.size() * n6;
                final double n15 = n3 - (j + 1) / (float) 뫤顸鷏㼜瀧.size() * n3;
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.75f * Math.min(1, j));
                GL11.glVertex3d(
                        竬㞈㔢洝鶊2.x - r.renderPosX - n13,
                        竬㞈㔢洝鶊2.y - r.renderPosY - n15,
                        竬㞈㔢洝鶊2.z - r.renderPosZ - n14
                );
            }
            GL11.glEnd();
            GL11.glDisable(2929);
            if (柿鱀쿨亟.쇽쟗콗㥇渺 == null) {
                if (柿鱀쿨亟.䩉핇韤䡸쿨 != null) {
                    final double n16 = 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosX +
                            (柿鱀쿨亟.䩉핇韤䡸쿨.getPosX() - 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosX) * mc.timer.renderPartialTicks -
                            r.renderPosX;
                    final double n17 = 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosY +
                            (柿鱀쿨亟.䩉핇韤䡸쿨.getPosY() - 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosY) * mc.timer.renderPartialTicks -
                            r.renderPosY;
                    final double n18 = 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosZ +
                            (柿鱀쿨亟.䩉핇韤䡸쿨.getPosZ() - 柿鱀쿨亟.䩉핇韤䡸쿨.lastTickPosZ) * mc.timer.renderPartialTicks -
                            r.renderPosZ;
                    final double n19 = 柿鱀쿨亟.䩉핇韤䡸쿨.getWidth() / 2.0f + 0.2f;
                    final AxisAlignedBB 骰㕠驋嘖渺 = new AxisAlignedBB(n16 - n19, n17, n18 - n19, n16 + n19, n17 +
                            (柿鱀쿨亟.䩉핇韤䡸쿨.getHeight() + 0.1f), n18 + n19);
                    ࡅ揩柿괠竁頉(骰㕠驋嘖渺, 霥瀳놣㠠釒(-16723258, 0.1f));
                    骰괠啖㕠挐酋(骰㕠驋嘖渺, 1.8f * SigmaNG.lineWidth, 霥瀳놣㠠釒(-16723258, 0.1f));
                }
            } else {
                final double n20 = 柿鱀쿨亟.ꪕ셴錌ศ딨 - r.renderPosX;
                final double n21 = 柿鱀쿨亟.髾玑䆧疂躚 - r.renderPosY;
                final double n22 = 柿鱀쿨亟.ಽ뚔붃室骰 - r.renderPosZ;
                GL11.glPushMatrix();
                GL11.glTranslated(n20, n21, n22);
                final BlockPos 酭ꦱ岋刃䬾Ꮺ = new BlockPos(0, 0, 0).offset(((BlockRayTraceResult)柿鱀쿨亟.쇽쟗콗㥇渺).getFace());
                GL11.glRotatef(45.0f, this.뎫붛㻣㱙랾.鷏蚳甐嶗轐((float) 酭ꦱ岋刃䬾Ꮺ.getX()), this.뎫붛㻣㱙랾.㥇掬卫曞ᔎ((float) (-酭ꦱ岋刃䬾Ꮺ.getY())), this.뎫붛㻣㱙랾.鷏躚騜콗婯((float) 酭ꦱ岋刃䬾Ꮺ.getZ()));
                GL11.glRotatef(90.0f, this.掬陂㹔ꪕ褕.鷏蚳甐嶗轐((float) 酭ꦱ岋刃䬾Ꮺ.getZ()), this.掬陂㹔ꪕ褕.㥇掬卫曞ᔎ((float) 酭ꦱ岋刃䬾Ꮺ.getY()), this.掬陂㹔ꪕ褕.鷏躚騜콗婯((float) (-酭ꦱ岋刃䬾Ꮺ.getX())));
                GL11.glTranslatef(-0.5f, 0.0f, -0.5f);
                final AxisAlignedBB 骰㕠驋嘖渺2 = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0, 1.0);
                ࡅ揩柿괠竁頉(骰㕠驋嘖渺2, 霥瀳놣㠠釒(-21931, 0.1f));
                骰괠啖㕠挐酋(骰㕠驋嘖渺2, 1.8f * SigmaNG.lineWidth, 霥瀳놣㠠釒(-21931, 0.1f));
                GL11.glPopMatrix();
            }
            GL11.glDisable(2896);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
//            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
//            GL11.glDisable(32925);
            GL11.glDisable(2848);
            GL11.glPopMatrix();

        }
        GlStateManager.disableLighting();
        RenderUtils.startBlend();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4d(1, 1, 1, 1);
        super.onRender3DEvent(event);
    }
}
