// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.utils.ColorObject;
import net.minecraft.entity.Entity;
import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class HitboxModule extends SCModule
{
    public static HitboxModule INSTANCE;
    public final Setting linePattern;
    public final Setting playerGroup;
    public final Setting showPlayerHitbox;
    public final Setting playerLineWidth;
    public final Setting playerLineColor;
    public final Setting playerShowLookVector;
    public final Setting itemGroup;
    public final Setting showItemHitbox;
    public final Setting itemLineWidth;
    public final Setting itemLineColor;
    public final Setting itemShowLookVector;
    public final Setting projectileGroup;
    public final Setting showProjectileHitbox;
    public final Setting projectileLineWidth;
    public final Setting projectileLineColor;
    public final Setting projectileShowLookVector;
    public final Setting mobGroup;
    public final Setting showMobHitbox;
    public final Setting mobLineWidth;
    public final Setting mobLineColor;
    public final Setting mobShowLookVector;
    public final Setting xpOrbGroup;
    public final Setting showXpOrbHitbox;
    public final Setting xpOrbLineWidth;
    public final Setting xpOrbLineColor;
    public final Setting xpOrbShowLookVector;
    
    public HitboxModule() {
        super("Hitbox", "Customize the hitboxes for all entities (F3+B)");
        HitboxModule.INSTANCE = this;
        new Setting(this, "General Options");
        this.linePattern = new Setting(this, "Line Pattern").setDefault(0).setRange("Solid", "Dashed", "Dotted");
        this.playerGroup = new Setting(this, "Player Hitbox Options").setDefault(new Setting.CompoundSettingGroup("Player Hitbox Options", new Setting[] { this.showPlayerHitbox = new Setting(null, "Show Hitbox", "hitbox.player.visible").setDefault(true), this.playerShowLookVector = new Setting(null, "Show Look Vector", "hitbox.player.lookvec").setDefault(true), this.playerLineWidth = new Setting(null, "Line Width", "hitbox.player.linewidth").setDefault(1.0f).setRange(1.0f, 5.0f, 0.1f), this.playerLineColor = new Setting(null, "Line Color", "hitbox.player.linecolor").setDefault(new Color(255, 255, 255, 255).getRGB(), 0) }));
        this.mobGroup = new Setting(this, "Mob Hitbox Options").setDefault(new Setting.CompoundSettingGroup("Mob Hitbox Options", new Setting[] { this.showMobHitbox = new Setting(null, "Show Hitbox", "hitbox.mob.visible").setDefault(true), this.mobShowLookVector = new Setting(null, "Show Look Vector", "hitbox.mob.lookvec").setDefault(true), this.mobLineWidth = new Setting(null, "Line Width", "hitbox.mob.linewidth").setDefault(1.0f).setRange(1.0f, 5.0f, 0.1f), this.mobLineColor = new Setting(null, "Line Color", "hitbox.mob.linecolor").setDefault(new Color(255, 255, 255, 255).getRGB(), 0) }));
        this.itemGroup = new Setting(this, "Item Hitbox Options").setDefault(new Setting.CompoundSettingGroup("Item Hitbox Options", new Setting[] { this.showItemHitbox = new Setting(null, "Show Hitbox", "hitbox.item.visible").setDefault(true), this.itemShowLookVector = new Setting(null, "Show Look Vector", "hitbox.item.lookvec").setDefault(true), this.itemLineWidth = new Setting(null, "Line Width", "hitbox.item.linewidth").setDefault(1.0f).setRange(1.0f, 5.0f, 0.1f), this.itemLineColor = new Setting(null, "Line Color", "hitbox.item.linecolor").setDefault(new Color(255, 255, 255, 255).getRGB(), 0) }));
        this.xpOrbGroup = new Setting(this, "Experience Orb Hitbox Options").setDefault(new Setting.CompoundSettingGroup("Experience Orb Hitbox Options", new Setting[] { this.showXpOrbHitbox = new Setting(null, "Show Hitbox", "hitbox.xpOrb.visible").setDefault(true), this.xpOrbShowLookVector = new Setting(null, "Show Look Vector", "hitbox.xpOrb.lookvec").setDefault(true), this.xpOrbLineWidth = new Setting(null, "Line Width", "hitbox.xpOrb.linewidth").setDefault(1.0f).setRange(1.0f, 5.0f, 0.1f), this.xpOrbLineColor = new Setting(null, "Line Color", "hitbox.xpOrb.linecolor").setDefault(new Color(255, 255, 255, 255).getRGB(), 0) }));
        this.projectileGroup = new Setting(this, "Projectile Hitbox Options").setDefault(new Setting.CompoundSettingGroup("Projectile Hitbox Options", new Setting[] { this.showProjectileHitbox = new Setting(null, "Show Hitbox", "hitbox.projectile.visible").setDefault(true), this.projectileShowLookVector = new Setting(null, "Show Look Vector", "hitbox.projectile.lookvec").setDefault(true), this.projectileLineWidth = new Setting(null, "Line Width", "hitbox.projectile.linewidth").setDefault(1.0f).setRange(1.0f, 5.0f, 0.1f), this.projectileLineColor = new Setting(null, "Line Color", "hitbox.projectile.linecolor").setDefault(new Color(255, 255, 255, 255).getRGB(), 0) }));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    public void renderPlayerHitbox(final Entity entity, final double x, final double y, final double z, final float look) {
        this.renderHitbox(entity, x, y, z, look, this.showPlayerHitbox.getBoolean(), this.playerShowLookVector.getBoolean(), this.playerLineWidth.getFloat(), this.playerLineColor.getColorObject());
    }
    
    public void renderMobHitbox(final Entity entity, final double x, final double y, final double z, final float look) {
        this.renderHitbox(entity, x, y, z, look, this.showMobHitbox.getBoolean(), this.mobShowLookVector.getBoolean(), this.mobLineWidth.getFloat(), this.mobLineColor.getColorObject());
    }
    
    public void renderItemHitbox(final Entity entity, final double x, final double y, final double z, final float look) {
        this.renderHitbox(entity, x, y, z, look, this.showItemHitbox.getBoolean(), this.itemShowLookVector.getBoolean(), this.itemLineWidth.getFloat(), this.itemLineColor.getColorObject());
    }
    
    public void renderXpOrbHitbox(final Entity entity, final double x, final double y, final double z, final float look) {
        this.renderHitbox(entity, x, y, z, look, this.showXpOrbHitbox.getBoolean(), this.xpOrbShowLookVector.getBoolean(), this.xpOrbLineWidth.getFloat(), this.xpOrbLineColor.getColorObject());
    }
    
    public void renderProjectileHitbox(final Entity entity, final double x, final double y, final double z, final float look) {
        this.renderHitbox(entity, x, y, z, look, this.showProjectileHitbox.getBoolean(), this.projectileShowLookVector.getBoolean(), this.projectileLineWidth.getFloat(), this.projectileLineColor.getColorObject());
    }
    
    private void renderHitbox(final Entity entity, final double x, final double y, final double z, final float look, final boolean showHitbox, final boolean lookVector, final float lineWidth, final ColorObject color) {
        if (!showHitbox) {
            return;
        }
        GlStateManager.depthMask(false);
        GlStateManager.func_179090_x();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        final float var10 = entity.width / 2.0f;
        final AxisAlignedBB var11 = entity.getEntityBoundingBox();
        final AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        GL11.glLineWidth(lineWidth);
        RenderGlobal.drawOutlinedBoundingBox(var12, color.getColor());
        GL11.glLineWidth(1.0f);
        if (entity instanceof EntityLivingBase && lookVector) {
            final float var13 = 0.01f;
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x - var10, y + entity.getEyeHeight() - 0.009999999776482582, z - var10, x + var10, y + entity.getEyeHeight() + 0.009999999776482582, z + var10), 16711680);
        }
        if (lookVector) {
            final Tessellator var14 = Tessellator.getInstance();
            final WorldRenderer var15 = var14.getWorldRenderer();
            final Vec3 var16 = entity.getLook(look);
            var15.startDrawing(3);
            var15.func_178991_c(255);
            var15.addVertex(x, y + entity.getEyeHeight(), z);
            var15.addVertex(x + var16.xCoord * 2.0, y + entity.getEyeHeight() + var16.yCoord * 2.0, z + var16.zCoord * 2.0);
            var14.draw();
        }
        GlStateManager.func_179098_w();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
}
