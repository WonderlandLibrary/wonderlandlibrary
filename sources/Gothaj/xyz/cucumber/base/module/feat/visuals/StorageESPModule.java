package xyz.cucumber.base.module.feat.visuals;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Shader;
import xyz.cucumber.base.utils.render.StencilUtils;
import xyz.cucumber.base.utils.render.shaders.GaussianKernel;
import xyz.cucumber.base.utils.render.shaders.Shaders;

@ModuleInfo(category = Category.VISUALS, description = "Allows you see storages throw obsticles", name = "Storage ESP")
public class StorageESPModule extends Mod{

	public ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Box", "Box2D", "Outline"
	});
	
	
	public ColorSettings fillColor = new ColorSettings("Fill Color", "Static", -1, -1, 100);
	
	public ModeSettings outline = new ModeSettings("Outline 2D", new String[] {
			"None", "Full", "Brackets", "Arrows"
	});
	public ColorSettings outlineColor = new ColorSettings("Outline Color", "Static", -1, -1, 100);
	
	
	public Framebuffer framebuffer;
    public Framebuffer glowFrameBuffer;
    private final List<TileEntity> entities = new ArrayList<>();
    
    private GaussianKernel gaussianKernel = new GaussianKernel(0);
	
	
	public StorageESPModule() {
		this.addSettings(
				mode,
				fillColor,
				outline,
				outlineColor
				);
	}
	
	public void createFrameBuffers() {
	        framebuffer = createFrameBuffer(framebuffer);
	        glowFrameBuffer = createFrameBuffer(glowFrameBuffer);
	}
	    
	public Framebuffer createFrameBuffer(Framebuffer framebuffer) {
		if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
		return framebuffer;
    }
	@EventListener
	public void onRender3D(EventRender3D e) {
		if(mode.getMode().toLowerCase().equals("outline")) {
			createFrameBuffers();
		    collectEntities();
		    framebuffer.framebufferClear();
		    framebuffer.bindFramebuffer(true);
		    
		}
		for(TileEntity tile : mc.theWorld.loadedTileEntityList) {
			if(!(tile instanceof TileEntityChest || tile instanceof TileEntityEnderChest)) {
				continue;
			}
			double x = tile.getPos().getX() - mc.getRenderManager().viewerPosX;
			double y = tile.getPos().getY() - mc.getRenderManager().viewerPosY;
			double z = tile.getPos().getZ() - mc.getRenderManager().viewerPosZ;
			AxisAlignedBB bb = new AxisAlignedBB(x,y+1,z,x+1,y,z+1);
			
			switch(mode.getMode().toLowerCase()) {
			case "box":
				GlStateManager.pushMatrix();
				
				RenderUtils.start3D();
				RenderUtils.color(ColorUtils.getColor(fillColor, System.nanoTime()/1000000, 0, 5));
				RenderUtils.renderHitbox(bb, GL11.GL_QUADS);
				RenderUtils.color(ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, 0, 5));
				RenderUtils.renderHitbox(bb, GL11.GL_LINE_LOOP);
				
				RenderUtils.stop3D();
				
				GlStateManager.popMatrix();
				break;
			case "box2d":
	            float f = 1.6F;
	            float f1 = 0.016666668F * f;
	            GlStateManager.pushMatrix();
	            GlStateManager.translate((float)x+0.5, (float)y+0.5, (float)z+0.5);
	            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
	            GlStateManager.rotate(mc.getRenderManager().playerViewX,1.0F, 0F, 0.0F);
	            GlStateManager.scale(-f1, -f1, f1);
	            int i = 0;
	            
	            RenderUtils.drawRect(-25,-25,25,25, ColorUtils.getColor(fillColor, System.nanoTime()/1000000, 0, 5));
	            
	            renderOutline(new PositionUtils(-25,-25,50,50,1));
	            
	            GlStateManager.popMatrix();
				break;
			case "outline":
				GlStateManager.pushMatrix();
				
				RenderUtils.start3D();
				RenderUtils.color(ColorUtils.getColor(fillColor, System.nanoTime()/1000000, 0, 5));
				RenderUtils.renderHitbox(bb, GL11.GL_QUADS);

				
				RenderUtils.stop3D();
				
				GlStateManager.popMatrix();
				
				break;
			}
		}
		if(mode.getMode().toLowerCase().equals("outline")) {
			
			framebuffer.unbindFramebuffer();
		    mc.getFramebuffer().bindFramebuffer(true);
		    mc.entityRenderer.disableLightmap();
		    GlStateManager.disableLighting();
		}
		
	}
	
	public void renderOutline(PositionUtils pos) {
		switch(outline.getMode().toLowerCase()) {
		case "full":
			RenderUtils.drawOutlinedRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), 0x90000000, 1.6f);
			break;
		case "brackets":
			double bw = pos.getWidth()/4;
			renderBracket(new double [] {pos.getX(), pos.getY()}, new double [] {pos.getX(), pos.getY2()}, bw+0.3, 0x90000000, 1.6);

			
			renderBracket(new double [] {pos.getX2(), pos.getY()}, new double [] {pos.getX2(), pos.getY2()}, -bw-0.3, 0x90000000, 1.6);

			break;
		case "arrows":
			double aw = pos.getWidth()/4;
			renderBracket(pos.getX()+aw-0.3, pos.getY(),pos.getX(), pos.getY(),pos.getX(), pos.getY()+aw+0.3, 0x90000000, 1.6);

			renderBracket(pos.getX2()-aw-0.3, pos.getY(),pos.getX2(), pos.getY(),pos.getX2(), pos.getY()+aw+0.3, 0x90000000, 1.6);

			
			renderBracket(pos.getX(), pos.getY2()-aw-0.3, pos.getX(), pos.getY2(),pos.getX()+aw+0.3, pos.getY2(), 0x90000000, 1.6);

			
			renderBracket(pos.getX2(), pos.getY2()-aw-0.3, pos.getX2(), pos.getY2(),pos.getX2()-aw-0.3, pos.getY2(), 0x90000000, 1.6);

			
			break;
		}
		if(!outline.getMode().toLowerCase().equals("none")) {
			StencilUtils.initStencil();
            GL11.glEnable(GL11.GL_STENCIL_TEST);
            StencilUtils.bindWriteStencilBuffer();
            
            switch(outline.getMode().toLowerCase()) {
			case "full":
				 RenderUtils.drawOutlinedRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), -1, 1);
				break;
			case "brackets":
				double bw = pos.getWidth()/4;

				renderBracket(new double [] {pos.getX(), pos.getY()}, new double [] {pos.getX(), pos.getY2()}, bw, -1, 1);
				
				renderBracket(new double [] {pos.getX2(), pos.getY()}, new double [] {pos.getX2(), pos.getY2()}, -bw, -1, 1);
				break;
			case "arrows":
				double aw = pos.getWidth()/4;

				renderBracket(pos.getX()+aw, pos.getY(),pos.getX(), pos.getY(),pos.getX(), pos.getY()+aw, -1, 1);
				

				renderBracket(pos.getX2()-aw, pos.getY(),pos.getX2(), pos.getY(),pos.getX2(), pos.getY()+aw, -1, 1);
				

				renderBracket(pos.getX(), pos.getY2()-aw, pos.getX(), pos.getY2(),pos.getX()+aw, pos.getY2(), -1, 1);

				renderBracket(pos.getX2(), pos.getY2()-aw, pos.getX2(), pos.getY2(),pos.getX2()-aw, pos.getY2(), -1, 1);
				
				break;
			}
    		
    		StencilUtils.bindReadStencilBuffer(1);
    		
    		GL11.glPushMatrix();
    		
    		RenderUtils.start2D();
    		
    		GL11.glShadeModel(GL11.GL_SMOOTH);
    		
    		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
    		GL11.glVertex2d(pos.getX()+pos.getWidth()/2, pos.getY()+pos.getHeight()/2);
    		
    		for(double z = 0; z <=360; z+=5) {
    			RenderUtils.color(ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, z, 5));
    			GL11.glVertex2d(pos.getX()+pos.getWidth()/2+Math.sin(z * Math.PI /180)* Math.sqrt(pos.getWidth()*pos.getWidth()+ pos.getHeight()* pos.getHeight())/2, pos.getY()+pos.getHeight()/2-Math.cos(z * Math.PI /180)*Math.sqrt(pos.getWidth()*pos.getWidth()+ pos.getHeight()* pos.getHeight())/2);
    		}
    		GL11.glEnd();
    		RenderUtils.stop2D();
    		GlStateManager.resetColor();
    		
    		GL11.glPopMatrix();
    		
    		StencilUtils.uninitStencilBuffer();
		}
	}
	
	public void renderBracket(double[] pos1, double[] pos2, double width, int color, double size) {
		GlStateManager.pushMatrix();
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glLineWidth((float) size);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		
		GL11.glVertex2d(pos1[0]+width, pos1[1]);
		GL11.glVertex2d(pos1[0], pos1[1]);
		GL11.glVertex2d(pos2[0], pos2[1]);
		GL11.glVertex2d(pos2[0]+width,  pos2[1]);
		
		GL11.glEnd();
		RenderUtils.color(0xffffffff);
		RenderUtils.stop2D();
		GlStateManager.popMatrix();
	}
	
	public void renderBracket(double x, double y, double x1, double y1, double x2, double y2, int color, double size) {
		GlStateManager.pushMatrix();
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glLineWidth((float) size);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x2, y2);
		
		GL11.glEnd();
		RenderUtils.color(0xffffffff);
		RenderUtils.stop2D();
		GlStateManager.popMatrix();
	}
	
	@EventListener
	public void onRenderGui(EventRenderGui e) {
		if(mode.getMode().toLowerCase().equals("outline")) {
			if (true) {
	            
	            
	            glowFrameBuffer.framebufferClear();
	            glowFrameBuffer.bindFramebuffer(true);
	            
	            int radius = (int) 5;
	            
	            Shader shader = Shaders.bloomESP;
	            
	            shader.startProgram();
	            int programId = shader.getProgramID();
	            if (this.gaussianKernel.getSize() != radius) {
	                this.gaussianKernel = new GaussianKernel(radius);
	                this.gaussianKernel.compute();

	                final FloatBuffer buffer = BufferUtils.createFloatBuffer(radius);
	                buffer.put(this.gaussianKernel.getKernel());
	                buffer.flip();

	                shader.uniform1f(programId, "u_radius", radius);
	                shader.uniformFB(programId, "u_kernel", buffer);
	                shader.uniform1i(programId, "u_diffuse_sampler", 0);
	                shader.uniform1i(programId, "u_other_sampler", 20);
	            }

	            shader.uniform2f(programId, "u_texel_size", 1.0F / e.getScaledResolution().getScaledWidth(), 1.0F / e.getScaledResolution().getScaledHeight());
	            shader.uniform2f(programId, "u_direction", 1F, 0.0F);
	            Color c = new Color(ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, 0, 5));
	            shader.uniform3f(programId, "color", c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F);

	            GlStateManager.enableBlend();
	            GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
	            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
	            framebuffer.bindFramebufferTexture();
	            shader.renderShader(0, 0, e.getScaledResolution().getScaledWidth(),e.getScaledResolution().getScaledHeight());

	            mc.getFramebuffer().bindFramebuffer(true);
	            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	            shader.uniform2f(programId, "u_direction", 0.0F, 1F);
	            glowFrameBuffer.bindFramebufferTexture();
	            GL13.glActiveTexture(GL13.GL_TEXTURE20);
	            framebuffer.bindFramebufferTexture();
	            GL13.glActiveTexture(GL13.GL_TEXTURE0);
	            shader.renderShader(0, 0, e.getScaledResolution().getScaledWidth(), e.getScaledResolution().getScaledHeight());
	            GlStateManager.disableBlend();

	            shader.stopProgram();
			}
		}
	}


    public void collectEntities() {
        entities.clear();
        for (TileEntity tile : mc.theWorld.loadedTileEntityList) {
        	if(!(tile instanceof TileEntityChest || tile instanceof TileEntityEnderChest)) {
				continue;
			}
        	entities.add(tile);
        }
    }
    
    public static void bindTexture(int texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }
	
}
