package summer.cheat.cheats.render;


import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.base.manager.Selection;
import summer.cheat.cheats.combat.KillAura;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.eventsystem.events.render.EventRender3D;
import summer.cheat.guiutil.Setting;

public class DamageParticles extends Cheats {
    private ArrayList<hit> hits = new ArrayList<hit>();
    private float lastHealth;
    private EntityLivingBase lastTarget = null;

    public static Setting particleHue;
    public static Setting rainbowParticles;

    public DamageParticles() {
        super("DamageParticles", "HIT PARTICLES", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(particleHue = new Setting("Particle Hue", this, 0.8F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(rainbowParticles = new Setting("Rainbow Particles", this, false));
    }

    @EventTarget
    public void a(EventUpdate event) {
        if (KillAura.target == null) {
            this.lastHealth = 20;
            lastTarget = null;
            return;
        }
        if (this.lastTarget == null || KillAura.target != this.lastTarget) {
            this.lastTarget = KillAura.target;
            this.lastHealth = KillAura.target.getHealth();
            return;
        }
        if (KillAura.target.getHealth() != this.lastHealth) {
            if (KillAura.target.getHealth() < this.lastHealth) {
                this.hits.add(new hit(KillAura.target.getPosition().add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5), ThreadLocalRandom.current().nextDouble(1, 1.5), ThreadLocalRandom.current().nextDouble(-0.5, 0.5)), this.lastHealth - KillAura.target.getHealth()));
            }
            this.lastHealth = KillAura.target.getHealth();
        }
    }

    @EventTarget
    public void b(EventRender3D event) {
        try {
            for (hit h : hits) {
                if (h.isFinished()) {
                    hits.remove(h);
                } else {
                    h.onRender();
                }
            }
        } catch (Exception e) {
        }
    }
}

class hit {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private long startTime = System.currentTimeMillis();
    private BlockPos pos;
    private double healthVal;
    private long maxTime = 1000;

    public hit(BlockPos pos, double healthVal) {
        this.startTime = System.currentTimeMillis();
        this.pos = pos;
        this.healthVal = healthVal;
    }

    public void onRender() {
        final double x = this.pos.getX() + (this.pos.getX() - this.pos.getX()) * mc.timer.renderPartialTicks - RenderManager.viewerPosX + 1.5;
        final double y = this.pos.getY() + (this.pos.getY() - this.pos.getY()) * mc.timer.renderPartialTicks - RenderManager.viewerPosY;
        final double z = this.pos.getZ() + (this.pos.getZ() - this.pos.getZ()) * mc.timer.renderPartialTicks - RenderManager.viewerPosZ;

        final float var10001 = (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        final double size = (2.5);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(mc.getRenderManager().playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(-0.01666666753590107 * size, -0.01666666753590107 * size, 0.01666666753590107 * size);
        float sizePercentage;
        long timeLeft = (this.startTime + this.maxTime) - System.currentTimeMillis();
        float yPercentage = 0;
        if (timeLeft < 75) {
            sizePercentage = Math.min((float) timeLeft / 75F, 1F);
            yPercentage = Math.min((float) timeLeft / 75F, 1F);
        } else {
            sizePercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 300F, 1F);
            yPercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 600F, 1F);
        }
        GlStateManager.scale(0.8 * sizePercentage, 0.8 * sizePercentage, 0.8 * sizePercentage);
        Gui.drawRect(-100, -100, 100, 100, new Color(255, 0, 0, 0).getRGB());
        Color c = Color.getHSBColor(DamageParticles.particleHue.getValFloat(), 1.0F, 1.0F);
        if (DamageParticles.rainbowParticles.getValBoolean()) {
            Summer.INSTANCE.fontManager.getFont("ROBO 15").drawStringWithShadow(new DecimalFormat("#.#").format(this.healthVal), 0, -(yPercentage * 1), CustomGlint.getRainbow(6000, -15));
        } else
            Summer.INSTANCE.fontManager.getFont("ROBO 15").drawStringWithShadow(new DecimalFormat("#.#").format(this.healthVal), 0, -(yPercentage * 1), c.getRGB());
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - this.startTime >= maxTime;
    }
}