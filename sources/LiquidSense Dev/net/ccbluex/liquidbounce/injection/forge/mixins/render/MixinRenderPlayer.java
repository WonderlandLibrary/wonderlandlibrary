package net.ccbluex.liquidbounce.injection.forge.mixins.render;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPlayer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRenderPlayer extends MixinRender {
    @Shadow
    public abstract ModelPlayer getMainModel();

    @Overwrite
    private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
        ModelPlayer modelplayer = this.getMainModel();
        Chams chams = (Chams) LiquidBounce.moduleManager.getModule(Chams.class);
        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        } else {
            if(chams.getState() && chams.getOnlyhead().get()){
                modelplayer.setInvisible(false);
                modelplayer.bipedHead.showModel = true;
                modelplayer.bipedHeadwear.showModel = true;
            } else {
                ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
                modelplayer.setInvisible(true);
                modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
                modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
                modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
                modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
                modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
                modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
                modelplayer.heldItemLeft = 0;
                modelplayer.aimedBow = false;
                modelplayer.isSneak = clientPlayer.isSneaking();
                if (itemstack == null) {
                    modelplayer.heldItemRight = 0;
                } else {
                    modelplayer.heldItemRight = 1;
                    if (clientPlayer.getItemInUseCount() > 0) {
                        EnumAction enumaction = itemstack.getItemUseAction();
                        if (enumaction == EnumAction.BLOCK) {
                            modelplayer.heldItemRight = 3;
                        } else if (enumaction == EnumAction.BOW) {
                            modelplayer.aimedBow = true;
                        }
                    }
                }
            }
        }

    }
}
