package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.event.events.Render3DEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

public class ModuleSpawns
        extends Module {
    public Setting<Integer> red = register(new Setting("Red", 116, 0, 255));
    public Setting<Integer> green = register(new Setting("Green",85, 0, 255));
    public Setting<Integer> blue = register(new Setting("Blue", 185, 0, 255));
    public Setting<Integer> alpha = register(new Setting("Alpha", 51, 0, 255));
    public static Color color8;
    public static HashMap<UUID, Thingering> thingers;

    public ModuleSpawns() {
        super("Spawns", "Spawns", Category.Render,false,false);
    }

    @Override
    public void onUpdate() {
        color8 = new Color(red.getValue(),green.getValue(),blue.getValue(),alpha.getValue());
        for (Entity entity : ModuleSpawns.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || thingers.containsKey(entity.getUniqueID())) continue;
            thingers.put(entity.getUniqueID(), new Thingering(this, entity));
            ModuleSpawns.thingers.get((Object)entity.getUniqueID()).starTime = System.currentTimeMillis();
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onRender3D(Render3DEvent event) {
        if (ModuleSpawns.mc.player == null || ModuleSpawns.mc.world == null) {
            return;
        }
        for (Map.Entry<UUID, Thingering> entry : thingers.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime >= ((long)180056445 ^ 0xABB74A1L)) continue;
            float opacity = Float.intBitsToFloat(Float.floatToIntBits(1.2886874E38f) ^ 0x7EC1E66F);
            long time = System.currentTimeMillis();
            long duration = time - entry.getValue().starTime;
            if (duration < ((long)-1747803867 ^ 0xFFFFFFFF97D2A4F9L)) {
                opacity = Float.intBitsToFloat(Float.floatToIntBits(13.7902155f) ^ 0x7EDCA4B9) - (float)duration / Float.intBitsToFloat(Float.floatToIntBits(6.1687006E-4f) ^ 0x7E9A3573);
            }
            ModuleSpawns.drawCircle(entry.getValue().entity, event.getPartialTicks(), Double.longBitsToDouble(Double.doubleToLongBits(205.3116845075892) ^ 0x7F89A9F951C9D87FL), (float)(System.currentTimeMillis() - entry.getValue().starTime) / Float.intBitsToFloat(Float.floatToIntBits(0.025765074f) ^ 0x7E1B1147), opacity);
        }
    }

    public static void drawCircle(final Entity entity, final float partialTicks, final double rad, final float plusY, final float alpha) {
        final Color color = new Color(ModuleSpawns.color8.getRed(), ModuleSpawns.color8.getGreen(), ModuleSpawns.color8.getBlue(), ModuleSpawns.color8.getAlpha());
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Float.intBitsToFloat(Float.floatToIntBits(1.5f) ^ 0x7F11B410));
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - ModuleSpawns.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - ModuleSpawns.mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - ModuleSpawns.mc.getRenderManager().viewerPosZ;
        final float r = Float.intBitsToFloat(Float.floatToIntBits(3180.4917f) ^ 0x7EC6475F) * color.getRed();
        final float g = Float.intBitsToFloat(Float.floatToIntBits(4554.3037f) ^ 0x7E0ED2EF) * color.getGreen();
        final float b = Float.intBitsToFloat(Float.floatToIntBits(29994.996f) ^ 0x7D6AD57F) * color.getBlue();
        final double pix2 = Double.longBitsToDouble(Double.doubleToLongBits(0.12418750450734782) ^ 0x7FA6EB3BC22A7D2FL);
        for (int i = 0; i <= 90; ++i) {
            GL11.glColor4f(r, g, b, alpha);
            GL11.glVertex3d(x + rad * Math.cos(i * Double.longBitsToDouble(Double.doubleToLongBits(0.038923223119235344) ^ 0x7FBACC45F0F011C7L) / Double.longBitsToDouble(Double.doubleToLongBits(0.010043755046771538) ^ 0x7FC211D1FBA3AC6BL)), y + plusY / Float.intBitsToFloat(Float.floatToIntBits(0.13022153f) ^ 0x7F2558CB), z + rad * Math.sin(i * Double.longBitsToDouble(Double.doubleToLongBits(0.012655047216797511) ^ 0x7F90CB18FB234FBFL) / Double.longBitsToDouble(Double.doubleToLongBits(0.00992417958121009) ^ 0x7FC2D320D5ED6BD3L)));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }


    public static void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    static {
        thingers = new HashMap();
    }

    public class Thingering {
        public Entity entity;
        public long starTime;
        public ModuleSpawns this$0;

        public Thingering(ModuleSpawns this$0, Entity entity) {
            this.this$0 = this$0;
            this.entity = entity;
            this.starTime = (long)1417733199 ^ 0x5480E44FL;
        }
    }
}