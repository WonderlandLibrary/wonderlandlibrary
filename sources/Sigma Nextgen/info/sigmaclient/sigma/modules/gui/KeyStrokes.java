
package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.sigma5.utils.SingleKeyStroke;
import info.sigmaclient.sigma.event.player.KeyEvent;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;

import top.fl0wowp4rty.phantomshield.annotations.Native;


public class KeyStrokes extends Module {
    ArrayList<SingleKeyStroke> singleKeyStrokes = new ArrayList<>();
    public KeyStrokes() {
        super("KeyStrokes", Category.Gui, "Show your key press");
        singleKeyStrokes.addAll(Arrays.asList(SingleKeyStroke.values()));
    }

    public static class CircleManager {

        public final List<Circle> circles = new ArrayList<Circle>();

        public void addCircle(double x, double y, double rad, double speed, int key) {
            circles.add(new Circle(x, y, rad, 4.5f, key));
        }

        public void runCircle(Circle c){
            //if(c.progress < 2){
            c.lastProgress = c.progress;
            if(c.keyCode != Minecraft.getInstance().gameSettings.keyBindAttack.keyCode.getKeyCode() &&
                    c.keyCode != Minecraft.getInstance().gameSettings.keyBindUseItem.keyCode.getKeyCode() &&
                    c.progress > c.topRadius * 0.67 &&
                    InputMappings.isKeyDown(c.keyCode)) {
                Object[] lastCount = circles.stream().filter((c2) -> c2.keyCode == c.keyCode).toArray();
                bre:
                {
                    if (lastCount[lastCount.length - 1] != c) break bre;
                    return;
                }
            }
            {
                c.progress += (c.topRadius - c.progress) / (c.speed) + 0.01;
                if (c.progress >= c.topRadius - 0.1F) {
                    c.complete = true;
                }
            }
            //}
        }

        public void runCircles(){
            List<Circle> completes = new ArrayList<Circle>();
            for(Circle c : circles){
                if(!c.complete){
                    runCircle(c);
                }else{
                    completes.add(c);
                }
            }
            synchronized(circles){
                circles.removeAll(completes);
            }
        }

        public void drawCircles(){
            for(Circle c : circles){
                if(!c.complete)
                    drawCircle(c);
            }
        }

        public void drawCircle(Circle c){
            float progress = (float) (c.progress * Minecraft.getInstance().timer.renderPartialTicks + (c.lastProgress * (1.0f - Minecraft.getInstance().timer.renderPartialTicks)));
            if(!c.complete)
                drawBorderedCircle((int)c.x, (int)c.y, progress,
                        new Color(1f, 1f, 1f, 1 - Math.min(1f, Math.max(0f, (float)(progress/c.topRadius)))).getRGB(),
                        new Color(1f, 1f, 1f, 1 - Math.min(1f, Math.max(0f, (float)(progress/c.topRadius)))).getRGB());
            }
    }
    public static void drawBorderedCircle(float x, float y, float radius, int outsideC, int insideC) {
        //  GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        float scale = 0.1f;
        GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
        drawCircle(x *= 10F, y *= 10F, radius *= 10.0f, insideC);
         drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
        GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        //  GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }
    public static void drawCircle(double x, double y, float radius, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
            ++i;
        }
        GL11.glEnd();
    }
    public static void drawUnfilledCircle(double x, double y, float radius, float lineWidth, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glLineWidth((float)lineWidth);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
    public static class Circle {

        public double x;
        public double y;
        public double topRadius;
        public double speed;

        public int keyCode;

        public double progress;
        public double lastProgress;
        public boolean complete;

        public Circle(double x, double y, double rad, double speed, int key){
            this.x = x;
            this.y = y;
            topRadius = rad;
            this.speed = speed;
            keyCode = key;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getTopRadius() {
            return topRadius;
        }

        public void setTopRadius(double topRadius) {
            this.topRadius = topRadius;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(!InputMappings.isKeyDown(mc.gameSettings.keyBindForward.keyCode.getKeyCode())){
            W = false;
        }
        if(!InputMappings.isKeyDown(mc.gameSettings.keyBindLeft.keyCode.getKeyCode())){
            A = false;
        }
        if(!InputMappings.isKeyDown(mc.gameSettings.keyBindRight.keyCode.getKeyCode())){
            D = false;
        }
        if(!InputMappings.isKeyDown(mc.gameSettings.keyBindBack.keyCode.getKeyCode())){
            S = false;
        }
        for(SingleKeyStroke s : singleKeyStrokes){
            s.circleManager.runCircles();
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onMouseClickEvent(MouseClickEvent event) {
        if (event.isAttack()){
            SingleKeyStroke.L.circleManager.addCircle(SingleKeyStroke.L.getCX(), SingleKeyStroke.L.getCY(), 35, 5, mc.gameSettings.keyBindAttack.keyCode.getKeyCode());
        }else{
            SingleKeyStroke.R.circleManager.addCircle(SingleKeyStroke.R.getCX(), SingleKeyStroke.R.getCY(), 35, 5, mc.gameSettings.keyBindUseItem.keyCode.getKeyCode());
        }
        super.onMouseClickEvent(event);
    }

    boolean W = false,A = false, S = false,D = false;

    @Override
    public void onKeyEvent(KeyEvent e) {
        if(e.getKey() == mc.gameSettings.keyBindForward.keyCode.getKeyCode()) {
            if (W) {
                return;
            }
            W = true;
            SingleKeyStroke.W.circleManager.addCircle(SingleKeyStroke.W.getCX(), SingleKeyStroke.W.getCY(), 26, 5, mc.gameSettings.keyBindForward.keyCode.getKeyCode());
        }
        if(e.getKey() == mc.gameSettings.keyBindLeft.keyCode.getKeyCode()){
            if(A) {
                return;
            }
            A = true;
            SingleKeyStroke.A.circleManager.addCircle(SingleKeyStroke.A.getCX(), SingleKeyStroke.A.getCY(), 26, 5, mc.gameSettings.keyBindLeft.keyCode.getKeyCode());
        }
        if(e.getKey() == mc.gameSettings.keyBindBack.keyCode.getKeyCode()){
            if(S) {
                return;
            }
            S = true;
            SingleKeyStroke.S.circleManager.addCircle(SingleKeyStroke.S.getCX(), SingleKeyStroke.S.getCY(), 26, 5, mc.gameSettings.keyBindBack.keyCode.getKeyCode());
        }
        if(e.getKey() == mc.gameSettings.keyBindRight.keyCode.getKeyCode()){
            if(D) {
                return;
            }
            D = true;
            SingleKeyStroke.D.circleManager.addCircle(SingleKeyStroke.D.getCX(), SingleKeyStroke.D.getCY(), 26, 5, mc.gameSettings.keyBindRight.keyCode.getKeyCode());
        }
    }
    public static int offset = 0;

    private void drawTexture(float x, float y, float w, float h, float w2, float h2) {
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, w2, h2);
    }
    public void addBlurWithRect(SingleKeyStroke singleKeyStroke){
        CircleManager circleManager = singleKeyStroke.circleManager;
        boolean s = Shader.isEnable();
        float x = singleKeyStroke.getX();
        float y = singleKeyStroke.getY();
        float x2 = singleKeyStroke.getX2();
        float y2 = singleKeyStroke.getY2();
        float multi = 1;
        if(s) {
            multi = 0.5f;
        }
        RenderUtils.drawRect(x, y, x2, y2, ColorUtils.reAlpha(-16711423, 0.5f * multi).getRGB());
        RenderUtils.sigma_drawShadow(x, y, x2 - x, y2 - y, 5.0f, multi);
//        RenderUtils.drawOutinShadow(x, y, x2, y2, 0.75f * (s ? 0.5f : 1f));

        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x2, y2, -1);
        StencilUtil.readStencilBuffer(1);
        GlStateManager.enableBlend();
        circleManager.drawCircles();
        StencilUtil.uninitStencilBuffer();
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        if (Minecraft.getInstance().isF3Enabled()) {
            return;
        }
        int yOff = offset;
        int keyStrokeX = 5;
        int keyStrokeY = 130;
        boolean s = Shader.isEnable();
        for (SingleKeyStroke singleKeyStroke : singleKeyStrokes) {
            singleKeyStroke.offsetY = offset + 129.5f;
        }
        // blur?
        if(s) {
            for (SingleKeyStroke singleKeyStroke : singleKeyStrokes) {
                float x = singleKeyStroke.getX();
                float y = singleKeyStroke.getY();
                float x2 = singleKeyStroke.getX2();
                float y2 = singleKeyStroke.getY2();
                Shader.addBlur(x, y, x2, y2);
            }
        }
        for(SingleKeyStroke singleKeyStroke : singleKeyStrokes){
            addBlurWithRect(singleKeyStroke);
        }
//        GlStateManager.disableAlpha();
//        GlStateManager.enableBlend();
//        mc.getTextureManager().bindTexture(new ResourceLocation("sigma/keystrokes_nb2.png"));
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.6F);
//        drawTexture(0.5f - 1, 251 / 2f - 1 + yOff, 172 / 2f, 172 / 2f, 172 / 2f, 172 / 2f);
//        GlStateManager.disableAlpha();
//        GlStateManager.enableBlend();

        JelloFontUtil.jelloFont18.drawString(mc.gameSettings.keyBindForward.keyCode.getTranslationKey().replace("key.keyboard.", "").toUpperCase(), (keyStrokeX + 26.5f - 1) + 8, keyStrokeY + yOff + 8.5f, -1);
        JelloFontUtil.jelloFont18.drawString(mc.gameSettings.keyBindLeft.keyCode.getTranslationKey().replace("key.keyboard.", "").toUpperCase(), keyStrokeX + 9f, keyStrokeY + 26.5f - 1 + yOff + 8.5f, -1);
        JelloFontUtil.jelloFont18.drawString(mc.gameSettings.keyBindBack.keyCode.getTranslationKey().replace("key.keyboard.", "").toUpperCase(), (keyStrokeX + 51 / 2f) + 9f, keyStrokeY + 26.5f - 1 + yOff + 8.5f, -1);
        JelloFontUtil.jelloFont18.drawString(mc.gameSettings.keyBindRight.keyCode.getTranslationKey().replace("key.keyboard.", "").toUpperCase(), (keyStrokeX + 51 / 2f + 51 / 2f) + 9f, keyStrokeY + 26.5f - 1 + yOff + 8.5f, -1);
        JelloFontUtil.jelloFont18.drawString("L", keyStrokeX + 16, keyStrokeY + 26.5f + 51 / 2f - 1 + yOff + 8.5f, -1);
        JelloFontUtil.jelloFont18.drawString("R", keyStrokeX + 77 / 2f + 15f, keyStrokeY + 26.5f + 51 / 2f - 1 + yOff + 8.5f, -1);
    }
    public void onEnable(){
        singleKeyStrokes.clear();
        singleKeyStrokes.addAll(Arrays.asList(SingleKeyStroke.values()));
//        Cords.yOffset = 80;
    }
    public void onDisable(){
//        Cords.yOffset = 80;
    }

}
