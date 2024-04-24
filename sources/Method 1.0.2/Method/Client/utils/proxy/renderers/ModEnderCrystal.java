package Method.Client.utils.proxy.renderers;

import Method.Client.module.combat.CrystalRender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static Method.Client.utils.visual.ColorUtils.colorcalc;

public class ModEnderCrystal extends RenderEnderCrystal {

    public ModEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    public static boolean istoggled = false;

    public static void Toggle() {
        istoggled = !istoggled;
    }

    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    private final ModelBase modelEnderCrystal = new CustomModelEnderCrystal(0.0F, true);
    private final ModelBase modelEnderCrystalNoBase = new CustomModelEnderCrystal(0.0F, false);

    @Override
    public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (istoggled) {
            float f = (float) entity.innerRotation + partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y, (float) z);
            this.bindTexture(ENDER_CRYSTAL_TEXTURES);
            float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
            f1 = f1 * f1 + f1;
            GlStateManager.scale(CrystalRender.scale.getValDouble(), CrystalRender.scale.getValDouble(), CrystalRender.scale.getValDouble());
// Insert Outlines
            if (CrystalRender.Outlines.getValBoolean()) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }
            if (CrystalRender.ChangeColor.getValBoolean()) {
                GlStateManager.color(colorcalc(CrystalRender.Color.getcolor(), 16), colorcalc(CrystalRender.Color.getcolor(), 8), colorcalc(CrystalRender.Color.getcolor(), 0), colorcalc(CrystalRender.Color.getcolor(), 24));
            }
            //   /****** INSERT SIZE   /******
            GlStateManager.scale(CrystalRender.scale.getValDouble(), CrystalRender.scale.getValDouble(), CrystalRender.scale.getValDouble());
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
            } else {
                this.modelEnderCrystalNoBase.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
            }

            if (CrystalRender.Outlines.getValBoolean()) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.popMatrix();
            BlockPos blockpos = entity.getBeamTarget();

            if (blockpos != null) {
                this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
                float f2 = (float) blockpos.getX() + 0.5F;
                float f3 = (float) blockpos.getY() + 0.5F;
                float f4 = (float) blockpos.getZ() + 0.5F;
                double d0 = (double) f2 - entity.posX;
                double d1 = (double) f3 - entity.posY;
                double d2 = (double) f4 - entity.posZ;
                RenderDragon.renderCrystalBeams(x + d0, y - 0.3D + (double) (f1 * 0.4F) + d1, z + d2, partialTicks, (double) f2, (double) f3, (double) f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
            }
        } else super.doRender(entity, x, y, z, entityYaw, partialTicks);

    }
}
