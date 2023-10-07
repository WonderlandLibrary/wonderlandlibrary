// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class RenderPlayerOF extends RenderPlayer
{
    public RenderPlayerOF(final RenderManager renderManager, final boolean slimArms) {
        super(renderManager, slimArms);
    }
    
    @Override
    protected void func_177093_a(final EntityLivingBase entityLiving, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ticksExisted, final float headYaw, final float rotationPitch, final float scale) {
        super.func_177093_a(entityLiving, limbSwing, limbSwingAmount, partialTicks, ticksExisted, headYaw, rotationPitch, scale);
        this.renderEquippedItems(entityLiving, scale, partialTicks);
    }
    
    protected void renderEquippedItems(final EntityLivingBase entityLiving, final float scale, final float partialTicks) {
        if (entityLiving instanceof AbstractClientPlayer) {
            final AbstractClientPlayer player = (AbstractClientPlayer)entityLiving;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            final ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
            PlayerConfigurations.renderPlayerItems(modelBipedMain, player, scale, partialTicks);
        }
    }
    
    public static void register() {
        final RenderManager rm = Config.getMinecraft().getRenderManager();
        final Map mapRenderTypes = getMapRenderTypes(rm);
        if (mapRenderTypes == null) {
            Config.warn("RenderPlayerOF init() failed: RenderManager.MapRenderTypes not found");
        }
        else {
            mapRenderTypes.put("default", new RenderPlayerOF(rm, false));
            mapRenderTypes.put("slim", new RenderPlayerOF(rm, true));
        }
    }
    
    private static Map getMapRenderTypes(final RenderManager rm) {
        try {
            final Field[] fields;
            final Field[] e = fields = Reflector.getFields(RenderManager.class, Map.class);
            for (final Field field : fields) {
                final Map map = (Map)field.get(rm);
                if (map != null) {
                    final Object renderSteve = map.get("default");
                    if (renderSteve instanceof RenderPlayer) {
                        return map;
                    }
                }
            }
            return null;
        }
        catch (Exception var6) {
            Config.warn("Error getting RenderManager.mapRenderTypes");
            Config.warn(var6.getClass().getName() + ": " + var6.getMessage());
            return null;
        }
    }
}
