package today.flux.gui.clickgui.classic.component.color;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.clickgui.classic.component.Component;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.ColorValue;
import today.flux.utility.ColorUtils;
import today.flux.utility.TimeHelper;

import java.awt.*;

/**
 * Made by Royalty on 2022/01/02.
 */
public class ColorPicker extends Component {

    TimeHelper clickTimer = new TimeHelper();

    float hue, saturation, brightness;

    boolean expanded, draggingHue, draggingSaturation, draggingBrightness;

    float expandedPosX = 0, expandedPosY = 0;

    ColorValue colorValue;

    private int getColor() {
        return colorValue.getColorInt();
    }

    public ColorPicker(ColorValue colorValue, Window parent, int offX, int offY, String title) {
        super(parent, offX, offY, title);
        super.width = 36;
        super.height = 10;
        this.type = "Colour Picker";
        this.colorValue = colorValue;
    }

    private float[] getColourPos() {
        return new float[] {super.x + 78,super.y + 3.5f};
    }

    private float[] getExpandedPos() {
        return new float[] {this.x + expandedPosX, this.y + expandedPosY};
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float colourPosX = getColourPos()[0];
        float colourPosY = getColourPos()[1];
        FontManager.tiny.drawString(colorValue.getKey(),this.x + 10,this.y + 3, Hud.isLightMode ? ColorUtils.GREY.c : 0xFFFFFFFF);

        int BorderColor = new Color(getColor()).darker().darker().getRGB();
        if (this == Flux.INSTANCE.getClickGUI().getCurrentColourPicker() && expanded) {
            BorderColor = 0xFFFFFFFF;
        }
        Gui.drawRect(colourPosX - 6.5F, colourPosY - 0.5f, colourPosX + 5.25f, colourPosY + 5.5f, BorderColor);
        Gui.drawRect(colourPosX - 6,colourPosY,colourPosX+5.25f,colourPosY+5,getColor());
    }
    boolean heldMouse;

    //I got horny while making this - coinful
    //i got horny again

    boolean held;
    public void mouseUpdates(int mouseX, int mouseY, boolean var3) {
        if (var3) {
            //System.out.println("hue "+draggingHue+" sat "+draggingSaturation+" brightness "+draggingBrightness);
            if (isHoveredColour(mouseX,mouseY) && !held) {
                clickTimer.reset();
                expanded = !expanded;
                if (expanded) {
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                        colorValue.setRainbow(!colorValue.isRainbow());
                    else {
                        Flux.INSTANCE.getClickGUI().setCurrentColourPicker(this);
                        expandedPosX = mouseX - this.x;
                        expandedPosY = mouseY - this.y;
                    }
                }
            }
            held = true;
        } else {
            held = false;
        }
    }
    public boolean isHoveredPicker(int mouseX, int mouseY) {
        float[] expandedPos = getExpandedPos();
        float x = expandedPos[0];
        float y = expandedPos[1];
        float width = 100;
        float height = 65;
        if (mouseX >= x && mouseX <= x+width) {
            if (mouseY >= y && mouseY <= y+height) {
                return true;
            }
        }
        return false;


    }
    public void renderColourPicker() {
        float[] scissorData = GuiRenderUtils.getScissor();
        if (scissorData[0] == -1) {
            GuiRenderUtils.endCrop();
        }



        updateValues();
        float[] expandedPos = getExpandedPos();
        float x = expandedPos[0];
        float y = expandedPos[1];
        RenderUtil.drawRoundedRect(x,y,x+100,y+65,3f, ClickGUI.backgroundColor);

        /*
        HUE
        OPACITY
        DARKNESS
         */
        // HUE = RED, BLUE, GREEN, RED


        drawGradientRect(x+5, y+5, x+35,y+20,true,0xFFFF0000,0xFF00FF00);
        drawGradientRect(x+35, y+5, x+65,y+20,true,0xFF00FF00,0xFF0000FF);
        drawGradientRect(x+65, y+5, x+95,y+20,true,0xFF0000FF,0xFFFF0000);

        drawGradientRect(x+5,y+25,x+95,y+40,true,0xFFFFFFFF,getColor());

        drawGradientRect(x+5,y+45,x+95,y+60,true, 0xFF000000,getColor());
        updateValues();
        GuiRenderUtils.drawBorderedRect(x+5+(hue*90)-1,y+5,2,15,1,0xFFFFFFFF,0xFF000000);
        GuiRenderUtils.drawBorderedRect(x+5+(saturation*90)-1,y+25,2,15,1,0xFFFFFFFF,0xFF000000);
        GuiRenderUtils.drawBorderedRect(x+5+(brightness*90)-1,y+45,2,15,1,0xFFFFFFFF,0xFF000000);

        if (scissorData[0] == -1) {
            GuiRenderUtils.beginCrop(scissorData[0], scissorData[1], scissorData[2], scissorData[3], scissorData[4]);
        }
    }

    public void mouseHeldColour(int mouseX, int mouseY) {
         if (expanded) {
            float[] pos = getExpandedPos();
            float PosX = pos[0];
            float PosY = pos[1];
            float HuePosY = PosY+5;
             float SatPosY = PosY+25;
            float BriPosY = PosY+45;

            if (mouseX >= PosX+5 && mouseX <= PosX+95) {
                if (mouseY >= HuePosY && mouseY <= HuePosY+15) {
                    draggingHue = true;
                    draggingSaturation = false;
                    draggingBrightness = false;
                } else if (mouseY >= BriPosY && mouseY <= BriPosY+15) {
                    draggingBrightness = true;
                    draggingHue = false;
                    draggingSaturation = false;
                } else if (mouseY >= SatPosY && mouseY <= SatPosY+15) {
                    draggingSaturation = true;
                    draggingHue = false;
                    draggingBrightness = false;
                }

                float deltaX = mouseX-(PosX+5);
                float currentPercentage = 0f;

                currentPercentage = Math.min(Math.max(0,deltaX),90)/90;

                if (draggingHue) {
                    hue = currentPercentage;
                } else if (draggingSaturation) {
                    saturation = currentPercentage;
                } else if (draggingBrightness) {
                    brightness = currentPercentage;
                } else {
                    return;
                }

                setColorValue(hue,saturation,brightness);
                updateValues();
            } else {
                draggingBrightness = false;
                draggingSaturation = false;
                draggingHue = false;
            }
        }
    }

    private boolean isHoveredColour(int mouseX, int mouseY) {

        if (mouseX >= getColourPos()[0]-0.5f && mouseX <= getColourPos()[0]+12.5) {
            if (mouseY >= getColourPos()[1]-0.5f && mouseY <= getColourPos()[1]+5.5) {
                return true;
            }
        }

        return false;

    }

    public void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        RenderUtil.color(startColor);
        if (sideways) {
            GL11.glVertex2d(left, top);
            GL11.glVertex2d(left, bottom);
            RenderUtil.color(endColor);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
        } else {
            GL11.glVertex2d(left, top);
            RenderUtil.color(endColor);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            RenderUtil.color(startColor);
            GL11.glVertex2d(right, top);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void mouseReleased() {
        draggingBrightness = false;
        draggingSaturation = false;
        draggingHue = false;
    }

    private void updateValues() {
        float[] colour = colorValue.getColorHSB();
        this.hue = colour[0];
        this.saturation = colour[1];
        this.brightness = colour[2];
    }

    private void setColorValue(float hue, float saturation, float brightness) {
        colorValue.setValueInt(Color.getHSBColor(hue,saturation,brightness).getRGB());
    }

    public boolean isExpanded() {
        return this.expanded;
    }

}
