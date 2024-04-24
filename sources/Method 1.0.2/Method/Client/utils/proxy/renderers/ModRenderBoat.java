package Method.Client.utils.proxy.renderers;

import Method.Client.Main;
import Method.Client.utils.visual.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;

import static Method.Client.utils.visual.ColorUtils.glColor;

public class ModRenderBoat extends RenderBoat {

    public ModRenderBoat(RenderManager renderManager) {
        super(renderManager);
    }

    public static Boolean Vanish = false;
    public static Boolean Rainbow = false;
    public static Boolean Blend = false;

    public static Boolean Carpet = false;

    boolean carpetset = false;

    final protected static ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[]{
            new ResourceLocation("textures/entity/boat/boat_oak.png"),
            new ResourceLocation("textures/entity/boat/boat_spruce.png"),
            new ResourceLocation("textures/entity/boat/boat_birch.png"),
            new ResourceLocation("textures/entity/boat/boat_jungle.png"),
            new ResourceLocation("textures/entity/boat/boat_acacia.png"),
            new ResourceLocation("textures/entity/boat/boat_darkoak.png")
    };

    @Override
    public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {

        if (!carpetset && Carpet) {
            carpetset = true;
            RenderBoat.BOAT_TEXTURES = new ResourceLocation[]{
                    new ResourceLocation(Main.MODID, "carpet.png"),
                    new ResourceLocation(Main.MODID, "carpet.png"),
                    new ResourceLocation(Main.MODID, "carpet.png"),
                    new ResourceLocation(Main.MODID, "carpet.png"),
                    new ResourceLocation(Main.MODID, "carpet.png"),
                    new ResourceLocation(Main.MODID, "carpet.png")};
        } else if (!Carpet && carpetset) {
            carpetset = false;
            RenderBoat.BOAT_TEXTURES = BOAT_TEXTURES;
        }
        if (Vanish)
            return;

        if (Blend)
            GlStateManager.enableBlend();

        if (Rainbow)
            glColor(ColorUtils.rainbow().getRGB());

        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (Blend)
            GlStateManager.disableBlend();
    }
}