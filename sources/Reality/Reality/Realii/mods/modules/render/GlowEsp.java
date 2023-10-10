
package Reality.Realii.mods.modules.render;
import Reality.Realii.utils.cheats.RenderUtills.ShaderUtil;
import Reality.Realii.event.EventHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.event.events.rendering.EventPostRenderPlayer;
import Reality.Realii.event.events.rendering.EventPreRenderPlayer;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.ibm.icu.impl.Utility;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.player.Helper;
import libraries.optifine.MathUtils;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

public class GlowEsp
extends Module {
	 
	private Framebuffer dataFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    private final ShaderUtil shaderProgram = new ShaderUtil("client/glow/fade_outline.glsl");
    private static final Frustum frustrum = new Frustum();
    public static boolean runningShader;
  
 
    
    public GlowEsp(){
        super("GlowEsp", ModuleType.Render);
        
        addValues();
    }
    
    @Override
   	public void onDisable() {
           runningShader = false;
       }
    
    public static boolean isInViewFrustrum(final Entity entity) {
        return (isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = Helper.mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }
    
    
   
    
    @EventHandler
    public void onShader3DEvent(final Shader3DEvent event) {
        runningShader = true;
        setupBuffers();
        
        dataFBO.bindFramebuffer(true);
        
        RendererLivingEntity.NAME_TAG_RANGE = 0;
        RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 0;

        final float partialTicks = event.getPartialTicks();
        
        int count = 0;

        for (final EntityPlayer player : mc.theWorld.playerEntities) {

            final Render<EntityPlayer> render = mc.getRenderManager().getEntityRenderObject(player);

            if (mc.getRenderManager() == null || render == null || player == null || player == mc.thePlayer || !isInViewFrustrum(player))
                continue;


            final double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
            int i = 0;
            Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
            Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
            Pair<Color, Color> colors = Pair.of(startColor, endColor);
            Color color3 = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
            final Color color = color3;

            //maybe use anotehr color method lol
            //RendererLivingEntity.setShaderBrightness(player,Color.cyan);
            //  if(mc.thePlayer.hurtTime > -1) {
            RendererLivingEntity.setShaderBrightness(player, player.hurtTime > 0 ? Color.RED : new Color(150,50,255));
            //or manually
            // }

            //MAYBE DONT USE HURTIME CHECK AND FIX THE COLOR THING

            //RendererLivingEntity.setShaderBrightness(player, player.hurtTime > 0 || player.hurtTime == 1 ? color.red : color);

            //  RendererLivingEntity.setShaderBrightness(player, player.hurtTime > 0 || player.hurtTime == 1 ? color:color);

            render.doRender(player, x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ, yaw, partialTicks);
            RendererLivingEntity.unsetBrightness();

            count++;

        }

            RendererLivingEntity.NAME_TAG_RANGE = 64;
            RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 32;

            RenderHelper.disableStandardItemLighting();
            mc.entityRenderer.disableLightmap();
            mc.getFramebuffer().bindFramebuffer(true);
            runningShader = false;

    }
    
   

    
    @EventHandler
    public void onRender2DEvent(final EventRender2D event) {
        if (mc.gameSettings.showDebugInfo)
            return;
      
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int programID = shaderProgram.getProgramID();
        
        // TODO: you can create values for those variables passed to the shader
        int i = 0;
 		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
 		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
   	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
   	  Color color3 = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
        final Color color = color3;
   	  	
        final int radius = 9;
        //7
        
        
        final int fading = 150;
        //150
        //100
        //300
        
        dataFBO.bindFramebuffer(true);
        //final FadingOutlineEvent fadingOutlineEvent = new FadingOutlineEvent();
        //fadingOutlineEvent.call();
        mc.getFramebuffer().bindFramebuffer(true);
        
        shaderProgram.init();
        passUniforms(programID, scaledResolution,  new Color(150,50,255), radius, fading);

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableBlend();
        dataFBO.bindFramebufferTexture();
        ShaderUtil.drawQuads(scaledResolution);
        shaderProgram.unload();
        GlStateManager.disableBlend();
    }

    private void passUniforms(final int programID, final ScaledResolution scaledResolution, final Color color, final int radius, final int fading) {
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_diffuse_sampler"), 0);
        GL20.glUniform2f(GL20.glGetUniformLocation(programID, "u_texel_size"), 1F / dataFBO.framebufferWidth, 1F / dataFBO.framebufferHeight);
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_radius"), radius);
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_fading"), fading);
    }

    private void setupBuffers() {
        try {
            dataFBO.framebufferClear();

            if (mc.displayWidth != dataFBO.framebufferWidth || mc.displayHeight != dataFBO.framebufferHeight) {
                dataFBO.deleteFramebuffer();
                dataFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
            }
        } catch (final Exception exception) {
        }
    }


    
    

    
    
    
    
    }


    
    
   
    
    

