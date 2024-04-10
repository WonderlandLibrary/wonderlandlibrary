package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.RenderModule;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.modules.world.Timer;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.utils.render.blurs.GradientRoundRectShader;
import info.sigmaclient.sigma.utils.render.blurs.RoundRectShader;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class TimerIndicator extends RenderModule {
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
    PartialTicksAnim timer = new PartialTicksAnim(0);
    TimerUtil timerUtil = new TimerUtil();
    float packets = 0;
    public TimerIndicator() {
        super("TimerIndicator", Category.Gui, "Timer indicator");
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        timer.interpolate(packets, 5);
        super.onWindowUpdateEvent(event);
    }
    public boolean isHover(double mx, double my) {
        double x = this.x.getValue().floatValue();
        double y = this.y.getValue().floatValue();
        double width = 50;
        double height = 25;
        return ClickUtils.isClickable(x - width, y, x + width, y + height, mx, my);
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        int status;
        float f4 = 100 / Timer.timer.getValue().floatValue();
        float f5 = Math.min(Timer.violation, f4);
        status = (int) (((f4 - f5) / f4) * 100);
        status = MathHelper.clamp(status, 0, 100);
        double width = 50;
        double height = 25;
        double x = this.x.getValue().floatValue();
        double y = this.y.getValue().floatValue();
        RoundRectShader.drawRoundRect((float) (x), (float) y - 1, (float) width, (float) height + 1,5, new Color(30,31,34, 255));
        FontUtil.sfuiFontBold18.drawCenteredString("Timer", (float) (x + width / 2f), (float) (y + 3f), new Color(255, 255, 255).getRGB());
        float perc = status / 100f;
        packets = perc;
        RoundRectShader.drawRoundRect((float) (x + 5), (float) y + 11, (float) (timer.getValue() * (width - 10)), (float) 10,5, new Color(42,166,255, 180));

        FontUtil.sfuiFontBold16.drawCenteredString(status >= 99 ? "100%" : (status + "%"), (float) (x + width / 2f), (float) y + 12 + 2.1f, new Color(255, 255, 255).getRGB());
        super.onRenderEvent(event);
    }
//
//            GL11.glPushMatrix();
//            GlStateManager.translate(x, y + height / 2f, 0);
//            GlStateManager.scale(scale, scale, 1);
//            GlStateManager.translate(-x, -(y + height / 2f), 0);
////            drawHand(()->RenderUtils.drawRoundedRect(x - width, y, x + width, y + height, 4, new Color(10, 10, 10, (int)(255 * alpha)).getRGB()));
//            Shader.drawRoundRectWithGlowing(x - width, y, x + width, y + height, new Color(0.117647059f,0.117647059f,0.117647059f, (int)(alpha)));
//
//    NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(target.getUniqueID());
//
//            if(info != null) {
//        RenderUtils.resetColor();
//        GL11.glColor4f(1,1,1,1);
//        mc.getTextureManager().bindTexture(info.getLocationSkin());
//        StencilUtil.initStencilToWrite();
//        RenderUtils.drawRoundedRect((float) (x - width + 4f), (float) (y + 4f),
//                (float) (x - width + 4f) + (float) 32f, (float) (y + 4f) + (float) 32f,4,-1);
//        StencilUtil.readStencilBuffer(1);
//        GlStateManager.translate((float) (x - width + 4f) + 16, (float) (y + 4f) + 16, 0);
//        GlStateManager.scale(1 - target.hurtTime / 10f * 0.1f, 1 - target.hurtTime / 10f * 0.1f, 1);
//        GlStateManager.translate(-((float) (x - width + 4f) + 16), -((float) (y + 4f) + 16), 0);
//        AbstractGui.drawScaledCustomSizeModalRect((float) (x - width + 4f), (float) (y + 4f), (float) 8.0, (float) 8.0, 8, 8, 32f, 32f, 64.0F, 64.0F);
//        StencilUtil.uninitStencilBuffer();
//        RenderUtils.drawRoundedRect((float) (x - width + 4f), (float) (y + 4f),
//                (float) (x - width + 4f) + (float) 32f, (float) (y + 4f) + (float) 32f,4,
//                new Color(1, 0,0,target.hurtTime / 10f * 0.1f).getRGB());
//    }
//
//    double finalScale = scale;
//    Color c = ColorChanger.getColor(0, 10);
//    c = ColorUtils.reAlpha(c, (float) alpha);
//    Color c2 = ColorChanger.getColor(100, 10);
//    c2 = ColorUtils.reAlpha(c2, (float) alpha);
//    Color finalC = c;
//    double finalX = x;
//            FontUtil.sfuiFontBold18.drawString(target.getName().getUnformattedComponentText(), (float) (x - width + 5 + 32F + 5f),
//            (float) y + 7, new Color(230, 230, 230, (int)(255 * alpha)));
//    String[] armor = new String[4];
//    int i = 0;
//
//            GL11.glPushMatrix();
//    int index = 0;
//    ArrayList<ItemStack> stackList = new ArrayList<>();
//            stackList.add(target.getHeldItemMainhand());
//            stackList.add(target.getHeldItemOffhand());
//            stackList.addAll((Collection<? extends ItemStack>) target.getArmorInventoryList());
//            for(ItemStack a : stackList){
//        RenderUtils.drawRect(
//                (int) (x - width + 5 + 32F + 5f) + index * 12,
//                (int) y + 16,
//                (int) (x - width + 5 + 32F + 5f) + index * 12 + 11f,
//                (int) y + 16 + 11f,
//                new Color(100, 100, 100, 30).getRGB()
//        );
//        mc.ingameGUI.renderHotbarItemCustom((int) (x - width + 5 + 32F + 5f) + index * 12 - 1, (int) y + 16 - 2, event.renderTime, mc.player, a, 0.8f);
//        index++;
//    }
//            GL11.glPopMatrix();
//
//    //            String h = String.join("  ", armor);
////            FontUtil.sfuiFont16.drawString(h, (float) (x - width + 5 + 32F + 5f), (float) y + 16, new Color(150, 150, 150, (int)(255 * alpha)));
//    double dd = (width * 2 - 5 - 32F - 5f) * health.getValue() * 0.9f;
//            FontUtil.sfuiFont12.drawString((int) target.getHealth() + "", (float) (x - width + 3 + 32F + 5f + dd), (float) (y + height - 5), new Color(230, 230, 230, (int)(255 * alpha)));
////            GradientGlowing.drawGradientLR((float)(x - width + 5 + 32F + 5f), (float)(y + height - 11 + 3 - 2f), (float)dd, (float)(3), 1.5f, c, ColorChanger.getColor(100, 10));
//            GradientRoundRectShader.drawRoundRect((float) (x - width + 5 + 32F + 5f), (float) (y + height - 11 + 3 - 2f), (float) dd, (float) (3), 1.5f, c, c2);
////            Shader.drawRoundRectWithGlowing((float)(x - width + 5 + 32F + 5f), (float)(y + height - 11 + 3), (float)dd, (float)(3), c);
//
//            GL11.glPopMatrix();
}
