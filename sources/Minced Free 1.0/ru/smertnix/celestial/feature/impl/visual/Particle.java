package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventAttackSilent;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.render.NewPalleteHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Particle extends Feature {
    ArrayList<partical> particals = new ArrayList();
    public ListSetting fparticlesMode = new ListSetting("Particle Mode", "Astolfo", () -> true, "Astolfo", "Rainbow", "Pulsive",  "Static");
    public ColorSetting staticColor = new ColorSetting("Particles Color", (new Color(255, 255, 255)).getRGB(), () -> {
        return this.fparticlesMode.currentMode.equalsIgnoreCase("Static");
    });

    public Particle() {
        super("Particle", "Красивые летающие партиклы после удара", FeatureCategory.Combat);
        this.addSettings(new Setting[]{this.fparticlesMode, this.staticColor});
    }

    @EventTarget
    public void update(EventAttackSilent e) {
        if (mc.player != null && mc.world != null && this.isEnabled()) {
            int i;
            if (!e.getTargetEntity().isDead && !e.getTargetEntity().isInvisible()) {
                for(i = 0; i <= 8; ++i) {
                    this.particals.add(new partical(e.getTargetEntity().posX, (double) MathematicHelper.randomizeFloat((float)(e.getTargetEntity().posY + (double)e.getTargetEntity().height), (float)(e.getTargetEntity().posY + 0.10000000149011612)), e.getTargetEntity().posZ));
                }
            }

            for(i = 0; i < this.particals.size(); ++i) {
                if (System.currentTimeMillis() - ((partical)this.particals.get(i)).getTime() >= 5000L) {
                    this.particals.remove(i);
                }
            }
        }

    }

    @EventTarget
    public void render(EventRender3D event) {
        String mode = this.fparticlesMode.getOptions();
        if (mc.player != null && mc.world != null && this.isEnabled()) {
            Iterator var3 = this.particals.iterator();

            while(var3.hasNext()) {
                partical partical = (partical)var3.next();
                float step = (float)((System.currentTimeMillis() - partical.time) / 10L);
                switch (mode) {
                    case "Static":
                        partical.render(this.staticColor.getColorValue());
                        break;
                    case "Rainbow":
                        partical.render(NewPalleteHelper.rainbow(1, 0.55F, 1.0F).getRGB());
                        break;
                    case "Astolfo":
                        partical.render(NewPalleteHelper.astolfo(1.0F, 2000.0F + step, 0.7F, 7.0F).getRGB());
                        break;
                    case "Pulsive":
                        partical.render(NewPalleteHelper.TwoColorEffect(new Color(255, 50, 50), new Color(79, 9, 9), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0 + (double)(6.0F * (step / 16.0F) / 60.0F)).getRGB());
                }
            }
        }

    }

    public static class partical {
        double x;
        double y;
        double z;
        double motionX;
        double motionY;
        double motionZ;
        long time;

        public partical(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.motionX = (double)MathematicHelper.randomizeFloat(-0.02F, 0.02F);
            this.motionY = (double)MathematicHelper.randomizeFloat(-0.01F, 0.01F);
            this.motionZ = (double)MathematicHelper.randomizeFloat(-0.02F, 0.02F);
            this.time = System.currentTimeMillis();
        }

        public long getTime() {
            return this.time;
        }

        public void update() {
            double yEx = 0.0;
            double sp = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0;
            this.x += this.motionX;
            this.y += this.motionY;
            this.z += this.motionZ;
            if (this.posBlock(this.x, this.y, this.z)) {
                this.motionY = -this.motionY / 1.1;
            } else if (this.posBlock(this.x, this.y, this.z) || this.posBlock(this.x, this.y - yEx, this.z) || this.posBlock(this.x, this.y + yEx, this.z) || this.posBlock(this.x - sp, this.y, this.z - sp) || this.posBlock(this.x + sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z - sp) || this.posBlock(this.x - sp, this.y, this.z + sp) || this.posBlock(this.x + sp, this.y, this.z) || this.posBlock(this.x - sp, this.y, this.z) || this.posBlock(this.x, this.y, this.z + sp) || this.posBlock(this.x, this.y, this.z - sp) || this.posBlock(this.x - sp, this.y - yEx, this.z - sp) || this.posBlock(this.x + sp, this.y - yEx, this.z + sp) || this.posBlock(this.x + sp, this.y - yEx, this.z - sp) || this.posBlock(this.x - sp, this.y - yEx, this.z + sp) || this.posBlock(this.x + sp, this.y - yEx, this.z) || this.posBlock(this.x - sp, this.y - yEx, this.z) || this.posBlock(this.x, this.y - yEx, this.z + sp) || this.posBlock(this.x, this.y - yEx, this.z - sp) || this.posBlock(this.x - sp, this.y + yEx, this.z - sp) || this.posBlock(this.x + sp, this.y + yEx, this.z + sp) || this.posBlock(this.x + sp, this.y + yEx, this.z - sp) || this.posBlock(this.x - sp, this.y + yEx, this.z + sp) || this.posBlock(this.x + sp, this.y + yEx, this.z) || this.posBlock(this.x - sp, this.y + yEx, this.z) || this.posBlock(this.x, this.y + yEx, this.z + sp) || this.posBlock(this.x, this.y + yEx, this.z - sp)) {
                this.motionX = -this.motionX + this.motionZ;
                this.motionZ = -this.motionZ + this.motionX;
            }

            this.motionX /= 1.001;
            this.motionZ /= 1.001;
            this.motionY -= 1.5E-6;
        }

        public void render(int color) {
            this.update();
            float scale = 0.04F;
            GlStateManager.disableDepth();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);

            try {
                double posX = this.x - Helper.mc.getRenderManager().viewerPosX;
                double posY = this.y - Helper.mc.getRenderManager().viewerPosY;
                double posZ = this.z - Helper.mc.getRenderManager().viewerPosZ;
                double distanceFromPlayer = Helper.mc.player.getDistance(this.x, this.y - 1.0, this.z);
                int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
                if (quality > 350) {
                    quality = 350;
                }

                GL11.glPushMatrix();
                GL11.glTranslated(posX, posY, posZ);
                GL11.glScalef(-scale, -scale, -scale);
                GL11.glRotated((double)(-Helper.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GL11.glRotated((double)Helper.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
                Color c = new Color(color);
                drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
                if (distanceFromPlayer < 4.0) {
                    drawFilledCircleNoGL(0, 0, 1.4, (new Color(c.getRed(), c.getGreen(), c.getBlue(), 50)).hashCode(), quality);
                }

                if (distanceFromPlayer < 20.0) {
                    drawFilledCircleNoGL(0, 0, 2.3, (new Color(c.getRed(), c.getGreen(), c.getBlue(), 30)).hashCode(), quality);
                }

                GL11.glScalef(0.8F, 0.8F, 0.8F);
                GL11.glPopMatrix();
            } catch (ConcurrentModificationException var13) {
            }

            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GlStateManager.enableDepth();
            GL11.glColor3d(255.0, 255.0, 255.0);
        }

        public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
            float f = (float)(c >> 24 & 255) / 255.0F;
            float f1 = (float)(c >> 16 & 255) / 255.0F;
            float f2 = (float)(c >> 8 & 255) / 255.0F;
            float f3 = (float)(c & 255) / 255.0F;
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glBegin(6);

            for(int i = 0; i <= 360 / quality; ++i) {
                double x2 = Math.sin((double)(i * quality) * Math.PI / 180.0) * r;
                double y2 = Math.cos((double)(i * quality) * Math.PI / 180.0) * r;
                GL11.glVertex2d((double)x + x2, (double)y + y2);
            }

            GL11.glEnd();
        }

        private boolean posBlock(double x, double y, double z) {
            return !(Helper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAir) && !(Helper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockLiquid) && !(Helper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockBush);
        }
    }
}

