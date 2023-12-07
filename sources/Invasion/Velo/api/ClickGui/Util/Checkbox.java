package Velo.api.ClickGui.Util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import Velo.api.Util.Other.Colors;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.impl.Settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Checkbox extends Component{

	private boolean hovered;
	private BooleanSetting op;



	private int x;
	private int y;

	public int offset;
	public Checkbox(BooleanSetting option, int x, int y) {
		offset = 0;
		this.op = option;
		this.x = x;
		this.y = y;
	
	}

    @Override
	public void renderComponent() {
    	//  RenderUtil.drawRect(x, y, 7, 7, new Color(10, 10, 10, 200).getRGB());
  //        RenderUtil.drawBorderedRect1(x + 2, y + 2, 12,12,0.5f, new Color(0,0,0, 255).getRGB(),new Color(5,5,5, 255).getRGB());
   //       Minecraft.getMinecraft().fontRendererObj.drawString(op.getName(), x + 16, y + 6, -1);
      //    if (getBooleanValue().isEnabled())
      //        RenderUtil.drawCheckMark(x + 8, y,10,0xffF136DB);
		  //if(ufr == null)
	         // ufr = UnicodeFontRenderer.getFontOnPC("fontname", 15, Font.PLAIN, 20, 10);
	//	Gui.drawRect(this.x, this.y -  offset, this.x + 10, this.y + 10 -  offset, hovered ? op.getValBoolean() ? Color.GRAY.darker().getRGB() : Color.GRAY.getRGB() : op.getValBoolean() ? Color.GRAY.darker().getRGB() : Color.LIGHT_GRAY.getRGB());
		//Gui.drawRect(30, 30 + 2, 30 + 12, 30, 0xFF111111);
		//GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
    	
    	if(op.isEnabled()) {
        	Fonts.notides.drawString(this.op.getName(), x , y , -1);
    		GlStateManager.color(255, 255, 255);
    		//Gui.drawRect(x + Fonts.notides.getStringWidth(op.getName()) + 6.5, y + 1.5, x + Fonts.notides.getStringWidth(op.getName()) + 17.5, y + 12.5, new Color(0, 0, 0).getRGB());    		
    		Gui.drawRect(x + Fonts.notides.getStringWidth(op.getName() + 1), y + 2 , x + Fonts.notides.getStringWidth(op.getName()) + 14, y + 12, new Color(10, 134, 255).getRGB());
//RenderUtil.(new ResourceLocation("Velo/check"), x + Fonts.notides.getStringWidth(op.getName() + 5), y , x + Fonts.notides.getStringWidth(op.getName()) + 21, y + 14, Color.white);
  
    	//  	GlStateManager.color(255, 255, 255);

    	   //	GlStateManager.color(255, 255, 255);
//RenderUtil.drawBorderedRect(x + Fonts.notides.getStringWidth(op.getName() + 5), y, x + Fonts.notides.getStringWidth(op.getName() + 5), y, y + 12, x + Fonts.notides.getStringWidth(op.getName()) + 17, -1);
    	//	RenderUtil.rectangleBordered((double)(x + 3.0F), (double)(y + 3.0F), (double)(x + (float)16 - 3.0F), (double)(y + (float)16 - 3.0F), 0.5D, -1, -1);

    		//drawRoundedRect(x + Fonts.notides.getStringWidth(op.getName() + 4), y, 20 + 10,19, 13, new Color(0, 255, 0));
    	//	RenderUtil.drawFilledCircle(x + 36+ Fonts.notides.getStringWidth(op.getName()), y + 12, 6, -1);
    	}else if(!op.isEnabled()){
        	Fonts.notides.drawString(this.op.getName(), x , y , -1);
    		GlStateManager.color(255, 255, 255);
    		Gui.drawRect(x + Fonts.notides.getStringWidth(op.getName() + 1), y + 2 , x + Fonts.notides.getStringWidth(op.getName()) + 14, y + 12, new Color(102, 102, 102).getRGB());
    		}
    	
    	GlStateManager.pushMatrix();

    //	GlStateManager.scale(1.6, 1.6, 1.6);
    	//Minecraft.getMinecraft().fontRendererObj.drawString(this.op.isEnabled() ? "✓" : "", x- 67 , y, -1);

   


     	GlStateManager.popMatrix();
		//GL11.glPopMatrix();
		//Gui.drawRect(30 + 3 + 4, 30 + 3, 30 + 9 + 4, 30 + 30 + 9, 0xFF999999);
		//if(this.op.getValBoolean())
			//Gui.drawRect(30 + 4 + 4, 30 + 4, 30 + 8 + 4,30 + 8, 0xFF666666);
	}
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
	
			this.op.setEnabled(!op.isEnabled());;
		}
	}
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
	}
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x + 10 && x < this.x + 30 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
    private void drawRoundedRect(int x, int y, int width, int height, int cornerRadius, Color color) {
    	Gui.drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color.getRGB());
    	Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color.getRGB());
    	Gui.drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color.getRGB());
        drawArc(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, color);
        drawArc(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, color);
        drawArc(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, color);
        drawArc(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);
    }
    
    
    
    private void drawArc(int x, int y, int radius, int startAngle, int endAngle, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y, 0).endVertex();

        for (int i = (int) (startAngle / 360.0 * 100); i <= (int) (endAngle / 360.0 * 100); i++) {
            double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            worldRenderer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).endVertex();
        }
        
        Tessellator.getInstance().draw();
        
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    
}
