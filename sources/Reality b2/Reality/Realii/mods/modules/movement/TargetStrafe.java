
package Reality.Realii.mods.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.entity.EntityValidator;
import Reality.Realii.utils.entity.impl.VoidCheck;
import Reality.Realii.utils.entity.impl.WallCheck;
import Reality.Realii.utils.render.RenderUtil;

import java.awt.*;

public final class TargetStrafe
        extends Module {
    private final Numbers<Number> radius = new Numbers<Number>("Radius", 2.0, 0.1, 4.0, 0.1);
    private final Option render = new Option("Render", true);
    private final Option directionKeys = new Option("Direction Keys", true);
    private Mode mode = new Mode("Mode", "Mode", new String[]{"OnMove","Auto"}, "OnMove");
    private final EntityValidator targetValidator;
    private Killaura aura;
    private int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", ModuleType.Movement);
        this.addValues(this.radius, this.render, this.directionKeys, this.mode);
        this.targetValidator = new EntityValidator();
        this.targetValidator.add(new VoidCheck());
        this.targetValidator.add(new WallCheck());
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = ((Killaura) ModuleManager.getModuleByClass(Killaura.class));
        }
    }

    @EventHandler
    public final void onUpdate(EventMove event) {
    	
       
        if (mc.thePlayer.isCollidedHorizontally || (!new VoidCheck().validate(mc.thePlayer) )) 
           
        
        if (mc.gameSettings.keyBindLeft.isPressed()) {
            this.direction = 1;
        }
        if (mc.gameSettings.keyBindRight.isPressed()) {
            this.direction = -1;
        }
    }

    private void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
    }
    
    public void strafe(EventMove event, double moveSpeed) {
        if (canStrafe() && Killaura.target != null) {
        	if (this.mode.getValue().equals("OnMove")) {
        	if (mc.gameSettings.keyBindForward.isKeyDown()) {
            EntityLivingBase target = Killaura.target;
            float[] rotations = PlayerUtils.getRotations(target);
            if ((double) mc.thePlayer.getDistanceToEntity(target) <= this.radius.getValue().floatValue()) {
                PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0);
            } else {
                PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0);
            }
        	}
        }
        	if (this.mode.getValue().equals("Auto")) {
        		
                    EntityLivingBase target = Killaura.target;
                    float[] rotations = PlayerUtils.getRotations(target);
                    if ((double) mc.thePlayer.getDistanceToEntity(target) <= this.radius.getValue().floatValue()) {
                        PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0);
                    } else {
                        PlayerUtils.setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0);
                    }
                	}
        		
        		
        	}
        }
    

    @EventHandler
    public void onRender3D(EventRender3D event) {
    
        if (this.canStrafe() && ((boolean) this.render.getValue() && Killaura.target != null)) {
        	
            this.drawCircle(Killaura.target, this.radius.getValue().floatValue() + 1f, 0xFF000000);
            //this.drawCircle(Killaura.target, this.radius.getValue().floatValue() , 0x800080);
        	this.drawCircle(Killaura.target, this.radius.getValue().floatValue() , 0xffffff);
        	
           
            
        
    	}
    }

    private void drawCircle(Entity entity, float lineWidth, int color) {
    	   if (entity == null) return;
    	   
    	   GL11.glPushMatrix();
           Render2.color(color, (float) (radius.getValue().doubleValue()));
           GL11.glDisable(GL11.GL_TEXTURE_2D);
           GL11.glDisable(GL11.GL_DEPTH_TEST);
           GL11.glDepthMask(false);
           GL11.glLineWidth(lineWidth);
           GL11.glEnable(GL11.GL_BLEND);
           GL11.glEnable(GL11.GL_LINE_SMOOTH);

           GL11.glBegin(GL11.GL_LINE_STRIP);
           EntityLivingBase target = Killaura.target;
           float partialTicks = mc.timer.elapsedPartialTicks;
           double rad = radius.getValue().doubleValue();
           //to change how square it is
           
           double d = (Math.PI * 2.0) / 26;

           double posX = target.posX, posY = target.posY, posZ = target.posZ;
           double lastTickX = target.lastTickPosX, lastTickY = target.lastTickPosY, lastTickZ = target.lastTickPosZ;
           double renderPosX = mc.getRenderManager().renderPosX, renderPosY = mc.getRenderManager().renderPosY, renderPosZ = mc.getRenderManager().renderPosZ;

           double y = lastTickY + (posY - lastTickY) * partialTicks - renderPosY;
           for (double i = 0; i < Math.PI * 2.0; i += d) {
               double x = lastTickX + (posX - lastTickX) * partialTicks + StrictMath.sin(i) * rad - renderPosX,
                       z = lastTickZ + (posZ - lastTickZ) * partialTicks + StrictMath.cos(i) * rad - renderPosZ;
               GL11.glVertex3d(x, y, z);
           }
           double x = lastTickX + (posX - lastTickX) * partialTicks - renderPosX,
                   z = lastTickZ + (posZ - lastTickZ) * partialTicks + rad - renderPosZ;
           GL11.glVertex3d(x, y, z);
           GL11.glEnd();

           GL11.glDisable(GL11.GL_BLEND);
           GL11.glDisable(GL11.GL_LINE_SMOOTH);
           GL11.glDepthMask(true);
           GL11.glEnable(GL11.GL_DEPTH_TEST);
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glColor4f(1, 1, 1, 1);
           GL11.glPopMatrix();

        
    }

    public boolean canStrafe() {
        return Killaura.target != null && this.isEnabled() || mc.gameSettings.keyBindJump.isPressed();
    }
}

