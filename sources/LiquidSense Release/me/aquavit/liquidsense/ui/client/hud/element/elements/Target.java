package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.utils.render.TargetHudParticles;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@ElementInfo(name = "Target")
public class Target extends Element {

    public Target(){
        super(40,100, 1f, new Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN));
    }

    private FloatValue fadeSpeed = new FloatValue("FadeSpeed", 2F, 1F, 9F);
    private ListValue Mode = new ListValue("Particles", new String[] {"Circle", "Rect"}, "Circle");
    private ListValue backGroundColorValue = new ListValue("BackGroundColor", new String[] {"Blur", "Custom"},"Blur");
    private Value<Integer> backGroundR = new IntegerValue("BackGround-R",0,0,255).displayable(() ->
            backGroundColorValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> backGroundG = new IntegerValue("BackGround-G",0,0,255).displayable(() ->
            backGroundColorValue.get().equalsIgnoreCase("Custom"));;
    private Value<Integer> backGroundB = new IntegerValue("BackGround-B",0,0,255).displayable(() ->
            backGroundColorValue.get().equalsIgnoreCase("Custom"));;
    private IntegerValue backGroundA = new IntegerValue("BackGround-A",180,0,225);

    List<TargetHudParticles> particlesString = new ArrayList<TargetHudParticles>();
    List<TargetHudParticles> particles = new ArrayList<TargetHudParticles>();
    private MSTimer addTimer = new MSTimer();
    private float easingHealth= 0F;
    private Entity lastTarget = null;

    @Override
    public Border drawElement() {

        for (int i = 0; i < particles.size(); i++){
            TargetHudParticles update = particles.get(i);
            if (update.getAnimaitonA() >= 255) particles.remove(update);
        }

        for (int i = 0; i < particlesString.size(); i++){
            TargetHudParticles update = particlesString.get(i);
            if (update.getAnimaitonA() >= 255) particlesString.remove(update);
        }

        EntityLivingBase target = ((Aura) LiquidSense.moduleManager.getModule(Aura.class)).getTarget();
        if (target != null) {
            if (target != lastTarget || easingHealth < 0 || easingHealth > target.getMaxHealth() || Math.abs(easingHealth - target.getHealth()) < 0.01)
                easingHealth = target.getHealth();

            float x = target.getDisplayName().getUnformattedText() != null && ColorUtils.stripColor(target.getDisplayName().getUnformattedText()).length() >= 13 ?
                    Fonts.font18.getStringWidth(ColorUtils.stripColor(target.getDisplayName().getUnformattedText())) + 50f : 110f;

            if (target.hurtTime > 0) {

                if (addTimer.hasTimePassed(500)) {
                    for (int i = 0; i <= 15; ++i) {
                        TargetHudParticles Hitparticles = new TargetHudParticles();
                        Hitparticles.setX(RandomUtils.nextFloat(-60.0f, 60.0f));
                        Hitparticles.setY(RandomUtils.nextFloat(-50.0f, 50.0f));
                        Hitparticles.setSize(RandomUtils.nextFloat(1.5f, 3.0f));
                        Hitparticles.setRed(RandomUtils.nextInt(30, 255));
                        Hitparticles.setGreen(RandomUtils.nextInt(30, 255));
                        Hitparticles.setBlue(RandomUtils.nextInt(30, 255));
                        Hitparticles.setA(255.0f);
                        this.particles.add(Hitparticles);
                    }
                    addTimer.reset();
                }
            }

            if(backGroundColorValue.get().equalsIgnoreCase("Custom")){
                RenderUtils.drawShader(1f, 0f, x + 29f, 51f);
                RenderUtils.drawRect(1f, 0f, x + 30f, 51.0f, new Color(backGroundR.get(), backGroundG.get(), backGroundB.get(), backGroundA.get()).getRGB());
            }else{
                BlurBuffer.blurArea((int)(1f + getRenderX() * getScale()),
                        (int)(getRenderY() * getScale()),
                        (x + 30)*getScale(),
                        51*getScale(),true);
                if (!this.getInfo().disableScale())
                    GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

                GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);

                RenderUtils.drawShader(1f, 0f, 141f, 52f);
                RenderUtils.drawRect(1f, 0f, 142f, 52f, new Color(0, 0, 0, backGroundA.get()).getRGB());
            }

            for (TargetHudParticles module : particles) {
                if (module.getX() > 0)
                    module.setAnimaitonX(module.getAnimaitonX() + Math.abs((module.getX() - (module.getX() - Math.abs(module.getX() - module.getAnimaitonX()))) / (float) 150) * (float) RenderUtils.deltaTime);
                else if (module.getX() < 0)
                    module.setAnimaitonX(module.getAnimaitonX() - Math.abs((module.getX() - (module.getX() - Math.abs(module.getX() - module.getAnimaitonX()))) / (float) 150) * (float) RenderUtils.deltaTime);

                if (Math.abs(module.getAnimaitonX()) >= Math.abs(module.getX())) module.setAnimaitonX(module.getX());

                if (module.getY() > 0)
                    module.setAnimaitonY(module.getAnimaitonY() + Math.abs((module.getY() - (module.getY() - Math.abs(module.getY() - module.getAnimaitonY()))) / (float) 150) * (float) RenderUtils.deltaTime);
                else if (module.getY() < 0)
                    module.setAnimaitonY(module.getAnimaitonY() - Math.abs((module.getY() - (module.getY() - Math.abs(module.getY() - module.getAnimaitonY()))) / (float) 150) * (float) RenderUtils.deltaTime);
                if (Math.abs(module.getAnimaitonY()) >= Math.abs(module.getY())) module.setAnimaitonY(module.getY());

                if (module.getA() > 0) {
                    module.setAnimaitonA(module.getAnimaitonA() + 0.2f * (float) RenderUtils.deltaTime);
                    if (module.getAnimaitonA() >= module.getA())
                        module.setAnimaitonA(module.getA());
                }

                if (Mode.get().equalsIgnoreCase("Rect")) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(20f + module.getAnimaitonX(), 20f + module.getAnimaitonY(), 20f + module.getAnimaitonX());
                    GlStateManager.rotate(module.getAnimaitonX() * 50, 0f, 0f, 1f);
                    RenderUtils.drawRect(0f, 0f, 5f, 5f, new Color(module.getRed(), module.getGreen(), module.getBlue(), 255 - (int) module.getAnimaitonA()).getRGB());
                    GlStateManager.popMatrix();
                } else {
                    RenderUtils.drawFullCircle(20 + module.getAnimaitonX(), 20 + module.getAnimaitonY(),
                            module.getSize(), 0f, new Color(module.getRed(), module.getGreen(), module.getBlue(), 255 - (int) module.getAnimaitonA()));

                }
            }

            GL11.glPushMatrix();
            GL11.glTranslated(2.5 + (target.hurtTime * 0.1), 2.5 + (target.hurtTime * 0.1), 0.0);
            GL11.glScaled(1.0 - (target.hurtTime * 0.01), 1.0 - (target.hurtTime * 0.01), 1.0 - (target.hurtTime * 0.01));
            GL11.glColor4f(1.0f, 1.0f - (target.hurtTime * 0.05f), 1.0f - (target.hurtTime * 0.05f), 1.0f);

            NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
            if (playerInfo != null) {
                ResourceLocation locationSkin = playerInfo.getLocationSkin();
                mc.getTextureManager().bindTexture(locationSkin);
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, 33, 33, 64F, 64F);
                GL11.glColor4f(1F, 1F, 1F, 1F);
            }

            boolean DmageHealth = easingHealth > target.getHealth();
            boolean HealHealth = easingHealth < target.getHealth();

            GL11.glPopMatrix();
            GlStateManager.resetColor();
            Fonts.font18.drawString("Name " + ColorUtils.stripColor(target.getDisplayName().getUnformattedText()), 40f, 11f, -1);
            Fonts.font18.drawString("Dist " + new DecimalFormat("0.0").format(mc.thePlayer.getDistanceToEntity(target))+" Hurt "+target.hurtTime, 40f, 25f, -1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(5.0, 0.0, 0.0);

            if (DmageHealth)
                RenderUtils.drawRect(0F, 43F, (easingHealth / target.getMaxHealth()) * x, 47F, new Color(252, 185, 65).getRGB());

            // Health bar
            RenderUtils.drawGradientSideways(0.0, 43.0, (target.getHealth() / target.getMaxHealth()) * (double)x,
                    47.0, ColorUtils.rainbow(5000000000L).getRGB(), ColorUtils.rainbow(500L).getRGB());

            if (HealHealth) {
                if (addTimer.hasTimePassed(500)) {
                    for (int i = 0; i <= 5; ++i) {
                        TargetHudParticles stringparticles = new TargetHudParticles();
                        stringparticles.setStringx(RandomUtils.nextFloat(-20.0f, 20.0f));
                        stringparticles.setStringy(RandomUtils.nextFloat(-20.0f, 20.0f));
                        stringparticles.setStringA(255.0f);
                        particlesString.add(stringparticles);
                    }
                    addTimer.reset();
                }
                RenderUtils.drawRect((easingHealth / target.getMaxHealth()) * x, 44F, (target.getHealth() / target.getMaxHealth()) * 110, 47F, new Color(44, 201, 144).getRGB());
            }

            GlStateManager.popMatrix();
            easingHealth += Math.max((target.getHealth() - easingHealth) / 2.0F, 10.0F - fadeSpeed.get()) * RenderUtils.deltaTime;

            String Hpstring = new DecimalFormat("0.0").format((easingHealth / target.getMaxHealth()) * 20);
            Fonts.font18.drawString(Hpstring, (easingHealth / target.getMaxHealth()) * x + (float) Fonts.font18.getStringWidth(Hpstring) / 2, 42f,
                    DmageHealth ? new Color(255, 111, 111).getRGB() : new Color(255, 255 ,255).getRGB());
        }

        lastTarget = target;
        return new Border(0f, 0f, 100f, 43f);
    }
}
