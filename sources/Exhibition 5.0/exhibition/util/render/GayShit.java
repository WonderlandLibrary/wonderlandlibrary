// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.Minecraft;
import java.util.HashMap;

public class GayShit
{
    public static final class Stencil
    {
        private static final Stencil INSTANCE;
        private final HashMap<Integer, StencilFunc> stencilFuncs;
        private int layers;
        private boolean renderMask;
        
        public Stencil() {
            this.stencilFuncs = new HashMap<Integer, StencilFunc>();
            this.layers = 1;
        }
        
        public static Stencil getInstance() {
            return Stencil.INSTANCE;
        }
        
        public void setRenderMask(final boolean renderMask) {
            this.renderMask = renderMask;
        }
        
        public static void checkSetupFBO() {
            final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
            if (fbo != null && fbo.depthBuffer > -1) {
                EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
                final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
                EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
                EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
                EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
                fbo.depthBuffer = -1;
            }
        }
        
        public static void setupFBO(final Framebuffer fbo) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
            final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
        }
        
        public void startLayer() {
            if (this.layers == 1) {
                GL11.glClearStencil(0);
                GL11.glClear(1024);
            }
            GL11.glEnable(2960);
            ++this.layers;
            if (this.layers > this.getMaximumLayers()) {
                System.out.println("StencilUtil: Reached maximum amount of layers!");
                this.layers = 1;
            }
        }
        
        public void stopLayer() {
            if (this.layers == 1) {
                System.out.println("StencilUtil: No layers found!");
                return;
            }
            --this.layers;
            if (this.layers == 1) {
                GL11.glDisable(2960);
            }
            else {
                final StencilFunc lastStencilFunc = this.stencilFuncs.remove(this.layers);
                if (lastStencilFunc != null) {
                    lastStencilFunc.use();
                }
            }
        }
        
        public void clear() {
            GL11.glClearStencil(0);
            GL11.glClear(1024);
            this.stencilFuncs.clear();
            this.layers = 1;
        }
        
        public void setBuffer() {
            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, this.getMaximumLayers(), 7681, 7680, 7680));
        }
        
        public void setBuffer(final boolean set) {
            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, set ? this.layers : (this.layers - 1), this.getMaximumLayers(), 7681, 7681, 7681));
        }
        
        public void cropOutside() {
            this.setStencilFunc(new StencilFunc(this, 517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
        }
        
        public void cropInside() {
            this.setStencilFunc(new StencilFunc(this, 514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
        }
        
        public void setStencilFunc(final StencilFunc stencilFunc) {
            GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
            GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
            this.stencilFuncs.put(this.layers, stencilFunc);
        }
        
        public StencilFunc getStencilFunc() {
            return this.stencilFuncs.get(this.layers);
        }
        
        public int getLayer() {
            return this.layers;
        }
        
        public int getStencilBufferSize() {
            return GL11.glGetInteger(3415);
        }
        
        public int getMaximumLayers() {
            return (int)(Math.pow(2.0, this.getStencilBufferSize()) - 1.0);
        }
        
        public void createCirlce(final double x, final double y, final double radius) {
            GL11.glBegin(6);
            for (int i = 0; i <= 360; ++i) {
                final double sin = Math.sin(i * 3.141592653589793 / 180.0) * radius;
                final double cos = Math.cos(i * 3.141592653589793 / 180.0) * radius;
                GL11.glVertex2d(x + sin, y + cos);
            }
            GL11.glEnd();
        }
        
        public void createRect(final double x, final double y, final double x2, final double y2) {
            GL11.glBegin(7);
            GL11.glVertex2d(x, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glVertex2d(x2, y);
            GL11.glVertex2d(x, y);
            GL11.glEnd();
        }
        
        static {
            INSTANCE = new Stencil();
        }
        
        public static class StencilFunc
        {
            public static int func_func;
            public static int func_ref;
            public static int func_mask;
            public static int op_fail;
            public static int op_zfail;
            public static int op_zpass;
            
            public StencilFunc(final Stencil paramStencil, final int func_func, final int func_ref, final int func_mask, final int op_fail, final int op_zfail, final int op_zpass) {
                StencilFunc.func_func = func_func;
                StencilFunc.func_ref = func_ref;
                StencilFunc.func_mask = func_mask;
                StencilFunc.op_fail = op_fail;
                StencilFunc.op_zfail = op_zfail;
                StencilFunc.op_zpass = op_zpass;
            }
            
            public void use() {
                GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
                GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
            }
        }
    }
    
    public static class Camera
    {
        private final Minecraft mc;
        private Timer timer;
        private double posX;
        private double posY;
        private double posZ;
        private float rotationYaw;
        private float rotationPitch;
        
        public Camera(final Entity entity) {
            this.mc = Minecraft.getMinecraft();
            if (this.timer == null) {
                this.timer = this.mc.timer;
            }
            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
            this.setRotationYaw(entity.rotationYaw);
            this.setRotationPitch(entity.rotationPitch);
            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
                final EntityPlayer living1 = (EntityPlayer)entity;
                this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
                this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
            }
            else if (entity instanceof EntityLivingBase) {
                final EntityLivingBase living2 = (EntityLivingBase)entity;
                this.setRotationYaw(living2.rotationYawHead);
            }
        }
        
        public Camera(final Entity entity, final double offsetX, final double offsetY, final double offsetZ, final double offsetRotationYaw, final double offsetRotationPitch) {
            this.mc = Minecraft.getMinecraft();
            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks;
            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks;
            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks;
            this.setRotationYaw(entity.rotationYaw);
            this.setRotationPitch(entity.rotationPitch);
            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
                final EntityPlayer player = (EntityPlayer)entity;
                this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
                this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
            }
            this.posX += offsetX;
            this.posY += offsetY;
            this.posZ += offsetZ;
            this.rotationYaw += (float)offsetRotationYaw;
            this.rotationPitch += (float)offsetRotationPitch;
        }
        
        public Camera(final double posX, final double posY, final double posZ, final float rotationYaw, final float rotationPitch) {
            this.mc = Minecraft.getMinecraft();
            this.setPosX(posX);
            this.posY = posY;
            this.posZ = posZ;
            this.setRotationYaw(rotationYaw);
            this.setRotationPitch(rotationPitch);
        }
        
        public double getPosX() {
            return this.posX;
        }
        
        public void setPosX(final double posX) {
            this.posX = posX;
        }
        
        public double getPosY() {
            return this.posY;
        }
        
        public void setPosY(final double posY) {
            this.posY = posY;
        }
        
        public double getPosZ() {
            return this.posZ;
        }
        
        public void setPosZ(final double posZ) {
            this.posZ = posZ;
        }
        
        public float getRotationYaw() {
            return this.rotationYaw;
        }
        
        public void setRotationYaw(final float rotationYaw) {
            this.rotationYaw = rotationYaw;
        }
        
        public float getRotationPitch() {
            return this.rotationPitch;
        }
        
        public void setRotationPitch(final float rotationPitch) {
            this.rotationPitch = rotationPitch;
        }
        
        public static float[] getRotation(final double posX1, final double posY1, final double posZ1, final double posX2, final double posY2, final double posZ2) {
            final float[] rotation = new float[2];
            final double diffX = posX2 - posX1;
            final double diffZ = posZ2 - posZ1;
            final double diffY = posY2 - posY1;
            final double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
            final double pitch = -Math.toDegrees(Math.atan(diffY / dist));
            rotation[1] = (float)pitch;
            double yaw = 0.0;
            if (diffZ >= 0.0 && diffX >= 0.0) {
                yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
            }
            else if (diffZ >= 0.0 && diffX <= 0.0) {
                yaw = Math.toDegrees(-Math.atan(diffX / diffZ));
            }
            else if (diffZ <= 0.0 && diffX >= 0.0) {
                yaw = -90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
            }
            else if (diffZ <= 0.0 && diffX <= 0.0) {
                yaw = 90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
            }
            rotation[0] = (float)yaw;
            return rotation;
        }
    }
}
