package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.RenderModule;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class PotionHUD extends RenderModule {
    public NumberValue x = new NumberValue("X", 0, 0, 10000, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public NumberValue y = new NumberValue("Y", 0, 0, 10000, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public void setX(float v){
        x.setValue(v);
    }
    public void setY(float v){
        y.setValue(v);
    }
    public float getX(){
        return x.getValue().floatValue();
    }
    public float getY(){
        return y.getValue().floatValue();
    }
    PartialTicksAnim widthAnim = new PartialTicksAnim(0);
    PartialTicksAnim heightAnim = new PartialTicksAnim(0);
    class APotion{
        public String name;
        public EffectInstance instance;
        public Sigma5AnimationUtil animationUtil;
    }
    APotion getPotion(String n){
        for(APotion a : currentPotion){
            if(a.instance.getEffectName().equals(n)) return a;
        }
        return null;
    }
    CopyOnWriteArrayList<APotion> currentPotion = new CopyOnWriteArrayList<>();
    public PotionHUD() {
        super("PotionHUD", Category.Gui, "Potion hud", true);
    }

    public boolean isHover(double mx, double my){
        float width = widthAnim.getValue(), height = heightAnim.getValue();
        float x = this.x.getValue().floatValue(), y = this.y.getValue().floatValue();
        return ClickUtils.isClickable(x, y, x + width, y + height, mx, my);
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        float width = 80, height = 15, split = 12;
        for (EffectInstance p : mc.player.getActivePotionEffects()) {
            APotion ap = getPotion(p.getEffectName());
            if (ap != null) {
                ap.instance = p;
            } else {
                APotion np = new APotion();
                np.instance = p;
                np.name = p.getEffectName();
                np.animationUtil = new Sigma5AnimationUtil(150, 150);
                currentPotion.add(np);
            }
        }
        for (APotion p : currentPotion) {
            EffectInstance effectInstance = p.instance;
            Sigma5AnimationUtil animationUtil = p.animationUtil;

            IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getEffectName());
            float ani = animationUtil.getAnim();

            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String timeFormat = effectInstance.getDuration() / 20 > 60 * 60 ? "*" : formatter.format(new Date(effectInstance.getDuration() * 50L));
            width = Math.max(FontUtil.sfuiFont16.getStringWidth(iformattabletextcomponent.getString() + (effectInstance.getAmplifier() == 0 ? "" : " \u00a7c" + (effectInstance.getAmplifier() + 1)) + " " +
                    timeFormat + "") + 20, width);
            height += split * ani * ani;
        }
        widthAnim.interpolate(width, 5);
        heightAnim.setValue(height + 3);

        width = widthAnim.getValue();
        height = heightAnim.getValue();
        float x = this.x.getValue().floatValue(), y = this.y.getValue().floatValue();
        float finalY = y;
        Shader.drawRoundRectWithGlowing(x, finalY, x + width, finalY + height, new Color(0.117647059f, 0.117647059f, 0.117647059f));
        RenderUtils.drawRoundedRect(x, y, x + width, y + height, 4, new Color(0.117647059f, 0.117647059f, 0.117647059f).getRGB());

        FontUtil.sfuiFontBold17.drawCenteredString("Potions", x + width / 2, y + 6f, new Color(255, 255, 255), HUD.gradient.isEnable());
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect(x, y, x + width, y + height, 4, new Color(0.117647059f, 0.117647059f, 0.117647059f).getRGB());
        StencilUtil.readStencilBuffer(1);
        y += 15;
        // Update Map
        // END UPDATE

        for (APotion p : currentPotion) {
            EffectInstance effectInstance = p.instance;
            Sigma5AnimationUtil animationUtil = p.animationUtil;
            // Need disappear
            AtomicBoolean in = new AtomicBoolean(false);
            mc.player.getActivePotionEffects().forEach((ps) -> {
                if (ps.getEffectName().equals(effectInstance.getEffectName())) {
                    in.set(true);
                }
            });
            // end
            animationUtil.animTo(in.get() ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
            // Anim
            float ani = animationUtil.getAnim();
            if (ani == 0) continue;
            float alpha = Math.min(ani * ((animationUtil.isAnim == Sigma5AnimationUtil.AnimState.SLEEPING ? (ani * 0.5f) : 1)), 1);

            GL11.glPushMatrix();
            GL11.glTranslatef(x + width / 2f, y + 3, 0);
            GL11.glScalef(1, Math.min(ani * ani, 1), 1);
            GL11.glTranslatef(-(x + width / 2f), -(y + 3), 0);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String timeFormat = effectInstance.getDuration() / 20 > 60 * 60 ? "*" : formatter.format(new Date(effectInstance.getDuration() * 50L));
            IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getEffectName());
            FontUtil.sfuiFont16.drawString(iformattabletextcomponent.getString() + (effectInstance.getAmplifier() == 0 ? "" : " \u00a7c" + (effectInstance.getAmplifier() + 1)), x + 3, y + 3, new Color(255, 255, 255, (int) (255 * alpha)));
            FontUtil.sfuiFont16.drawString(timeFormat, x + width - FontUtil.sfuiFont16.getStringWidth(timeFormat) - 3, y + 3, new Color(255, 255, 255, (int) (255 * alpha)));
            y += 12 * ani * ani;
            GL11.glPopMatrix();
        }
        StencilUtil.uninitStencilBuffer();
        super.onRenderEvent(event);
    }
}
