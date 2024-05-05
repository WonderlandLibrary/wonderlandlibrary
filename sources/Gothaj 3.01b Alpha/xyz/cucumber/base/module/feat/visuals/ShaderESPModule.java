package xyz.cucumber.base.module.feat.visuals;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Shader;
import xyz.cucumber.base.utils.render.shaders.GaussianKernel;
import xyz.cucumber.base.utils.render.shaders.Shaders;
@ModuleInfo(category = Category.VISUALS, description = "Outline entities throw walls", name = "Shader ESP")
public class ShaderESPModule extends Mod {

	public static boolean nametags = true;
	
	public Framebuffer framebuffer;
    public Framebuffer glowFrameBuffer;
    private final List<Entity> entities = new ArrayList<>();
    
    private GaussianKernel gaussianKernel = new GaussianKernel(0);
    
    public NumberSettings settings = new NumberSettings("Radius", 7, 0, 15, 1);
    public ModeSettings mode = new ModeSettings("Mode", new String[] {
    		"Glow", "Line", "Line-Other"
    });
    public ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 30);
	public ShaderESPModule() {
		this.addSettings(
				settings,
				mode,
				color
				);
	}
    
    public void createFrameBuffers() {
        framebuffer = createFrameBuffer(framebuffer);
        glowFrameBuffer = createFrameBuffer(glowFrameBuffer);
    }
    
	@EventListener
	public void onRender3D(EventRender3D e) {
		createFrameBuffers();
	    collectEntities();
	    framebuffer.framebufferClear();
	    framebuffer.bindFramebuffer(true);
	    renderEntities(e.getPartialTicks());
	    framebuffer.unbindFramebuffer();
	    mc.getFramebuffer().bindFramebuffer(true);
	    mc.entityRenderer.disableLightmap();
	    GlStateManager.disableLighting();
        final float partialTicks = mc.timer.renderPartialTicks;
	}
	
	@EventListener
	public void onRenderGui(EventRenderGui e) {
		if (framebuffer != null && entities.size() > 0) {
            
            
            glowFrameBuffer.framebufferClear();
            glowFrameBuffer.bindFramebuffer(true);
            
            int radius = (int) settings.getValue();
            
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
            Color c = new Color(ColorUtils.getColor(color, System.nanoTime()/1000000, 0, 5));
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
	
	public Framebuffer createFrameBuffer(Framebuffer framebuffer) {
		if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
		return framebuffer;
    }

    public void renderEntities(float partialTicks) {
    	for (final Entity player : entities) {
            final Render<EntityPlayer> render = mc.getRenderManager().getEntityRenderObject(player);

            if (mc.getRenderManager() == null || !(player instanceof EntityPlayer) || render == null || (player == mc.thePlayer) || !RenderUtils.isInViewFrustrum(player)) {
                continue;
            }

            final double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;

            nametags =  false;
            mc.getRenderManager().renderEntityStatic(player, partialTicks, false);
            nametags = true;
        }

        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap();
    }


    public void collectEntities() {
        entities.clear();
        for (Entity entity : mc.theWorld.playerEntities) {

            if (entity instanceof EntityPlayer) {
                entities.add(entity);
            }
        }
    }
    
    public static void bindTexture(int texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }
	
}
