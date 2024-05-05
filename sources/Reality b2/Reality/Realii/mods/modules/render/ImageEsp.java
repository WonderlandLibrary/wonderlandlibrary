package Reality.Realii.mods.modules.render;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.player.Teams;
import Reality.Realii.utils.render.RenderUtil;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;

import java.awt.*;

public class ImageEsp
        extends Module {
    
	public static Mode mode = new Mode("Mode", "Mode", new String[]{"Box","New", "Csgo", "Box2","ShaderEsp"}, "Box");
  
  

    public ImageEsp() {
        super("ImageEsp", ModuleType.Render);
        this.addValues(mode);
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }
  
    @EventHandler
    public void onRender(EventRender3D event) {
    	
     
       
            this.doOther2DESP();
   

    }

   


    private boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f && entity != mc.thePlayer) {
            return true;
        }
        return false;
    
    
    }
    
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        
    	 if (ArrayList2.Sufix.getValue().equals("On")) {
         	
             
     		this.setSuffix("da");
     	}
     		
     	 
     	 if (ArrayList2.Sufix.getValue().equals("Off")) {
          	
     	        
      		this.setSuffix("");
      	}
           
            
        
}

    private void doOther2DESP() {
        for (EntityPlayer entity : this.mc.theWorld.playerEntities) {
            if (!this.isValid(entity)) continue;
            if(entity.isInvisible()) continue;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            float partialTicks = this.mc.timer.renderPartialTicks;
            this.mc.getRenderManager();
            double x2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - RenderManager.renderPosZ;
            float DISTANCE = mc.thePlayer.getDistanceToEntity(entity);
            float DISTANCE_SCALE = Math.min(DISTANCE * 0.000f, 0.000f);
            float SCALE = 0.035f;
            float xMid = (float) x2;
            float yMid = (float) y2 + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f);
            float zMid = (float) z2;
            GlStateManager.translate((float) x2, (float) y2 + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f), (float) z2);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            this.mc.getRenderManager();
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glScalef(-SCALE, -SCALE, -(SCALE /= 2.0f));
            Tessellator tesselator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tesselator.getWorldRenderer();
            float HEALTH = entity.getHealth();
            int COLOR = -1;
            COLOR = (double) HEALTH > 20.0 ? -65292 : ((double) HEALTH >= 10.0 ? -16711936 : ((double) HEALTH >= 3.0 ? -23296 : -65536));
            Color gray = new Color(123, 23, 255);
            double thickness = 1.5 + DISTANCE * 0.000f;
            double xLeft = -15.0;
            double xRight = 15.0;
            double yUp = 10.0;
            double yDown = 70.0;
            
            double size = 10.0;
            Color color = new Color(255, 255, 255,000);
             if (entity.isInvisible()) {
                color = new Color(255, 255, 0);
            }
           // drawBorderedRect((float) xLeft, (float) yUp, (float) xRight, (float) yDown, (float) thickness + 0.2f, Color.BLACK.getRGB(), 0);
             
           drawBorderedRect((float) xLeft, (float) yUp, (float) xRight, (float) yDown, (float) thickness, color.getRGB(), 0);
           RenderUtil.drawCustomImage((float) xLeft, (float) yUp, (float) xRight + 15, (float) yDown, new ResourceLocation("client/roblox.png"));
            //drawBorderedRect((float) xLeft - 2.0f - DISTANCE * 0.1f, (float) yDown - (float) (yDown - yUp), (float) xLeft - 2.0f, (float) yDown, 0.15f, new Color(100, 100, 100).getRGB(), new Color(100, 100, 100).getRGB());
             
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glNormal3f(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }


    public static void drawBorderedRect(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        ImageEsp.drawRect(x2, y2, x22, y22, col2);
        float f2 = (float) (col1 >> 24 & 255) / 255.0f;
        float f1 = (float) (col1 >> 16 & 255) / 255.0f;
        float f22 = (float) (col1 >> 8 & 255) / 255.0f;
        float f3 = (float) (col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f22, f3, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x22, y2);
        GL11.glVertex2d(x2, y22);
        GL11.glVertex2d(x22, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    

    public static void drawRect(float g2, float h2, float i2, float j2, int col1) {
        float f2 = (float) (col1 >> 24 & 255) / 255.0f;
        float f1 = (float) (col1 >> 16 & 255) / 255.0f;
        float f22 = (float) (col1 >> 8 & 255) / 255.0f;
        float f3 = (float) (col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f22, f3, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(i2, h2);
        GL11.glVertex2d(g2, h2);
        GL11.glVertex2d(g2, j2);
        GL11.glVertex2d(i2, j2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    
    
    public void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
}

