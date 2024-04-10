package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.RenderModule;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.sigma5.utils.Stencil;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.client.util.InputMappings;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class KeyBinds extends RenderModule {
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
    public KeyBinds() {
        super("KeyBinds", Category.Gui, "Keybind", true);
    }
    HashMap<Module, Sigma5AnimationUtil> animationUtilHashMap = new HashMap<>();
    public boolean isHover(double mx, double my){
        float width = widthAnim.getValueNoTrans(), height = heightAnim.getValueNoTrans();
        float x = this.x.getValue().floatValue(), y = this.y.getValue().floatValue();
        return ClickUtils.isClickable(x, y, x + width, y + height, mx, my);
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        float width = 80, height = 15;
        for(Module p : SigmaNG.getSigmaNG().moduleManager.modules){
            if(!animationUtilHashMap.containsKey(p)){
                continue;
            }
            Sigma5AnimationUtil animationUtil = animationUtilHashMap.get(p);
            if(p.key == -1 || animationUtil.getAnim() == 0)
                continue;
            float ani = animationUtil.getAnim();
            width = Math.max(FontUtil.sfuiFont16.getStringWidth(p.name + "     [" + InputMappings.getInputByCode(p.key) + "]"), width);
            height += 12 * ani * ani;
        }
        heightAnim.setValue(height);
        widthAnim.interpolate(width, 5);
        width = widthAnim.getValue();
        float x = this.x.getValue().floatValue(), y = this.y.getValue().floatValue();
        float finalY = y;
        float finalWidth = width;
        Shader.drawRoundRectWithGlowing(x, finalY, x + finalWidth, finalY + height, new Color(0.117647059f,0.117647059f,0.117647059f));
        RenderUtils.drawRoundedRect(x, y, x + width, y + height, 4, new Color(0.117647059f,0.117647059f,0.117647059f).getRGB());
        FontUtil.sfuiFontBold17.drawCenteredString("KeyBinds", x + width / 2, y + 6f, new Color(255, 255, 255), HUD.gradient.isEnable());
//        StencilUtil.initStencilToWrite();
//        RenderUtils.drawRoundedRect(x, y, x + width, y + height, 4, new Color(0.117647059f,0.117647059f,0.117647059f).getRGB());
//        StencilUtil.readStencilBuffer(1);
        y += 15;
        for(Module p : SigmaNG.getSigmaNG().moduleManager.modules){
            if(!animationUtilHashMap.containsKey(p)){
                animationUtilHashMap.put(p, new Sigma5AnimationUtil(150, 150));
            }
            Sigma5AnimationUtil animationUtil = animationUtilHashMap.get(p);
            animationUtil.animTo(p.enabled ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
            float ani = animationUtil.getAnim();
            if(p.key == -1 || ani == 0) continue;
            float alpha = Math.min(ani * ((animationUtil.isAnim == Sigma5AnimationUtil.AnimState.SLEEPING ? (ani * 0.5f) : 1)), 1);
            GL11.glPushMatrix();
            GL11.glTranslatef(x + width / 2f, y + 3,0);
            GL11.glScalef(1, Math.min(ani * ani, 1),1);
            GL11.glTranslatef(-(x + width / 2f), -(y + 3),0);
            FontUtil.sfuiFont16.drawString(p.remapName, x + 3, y + 3,
                    new Color(255, 255, 255, (int)(alpha * 255)));
            FontUtil.sfuiFont16.drawString("["+InputMappings.getInputByCode(p.key).toUpperCase()+"]", x + width - FontUtil.sfuiFont16.getStringWidth("["+InputMappings.getInputByCode(p.key).toUpperCase()+"]") - 3, y + 3,
                    new Color(255, 255, 255, (int)(alpha * 255)));
            GL11.glPopMatrix();
            y += 12 * ani * ani;
        }
//        StencilUtil.uninitStencilBuffer();
        super.onRenderEvent(event);
    }
}
