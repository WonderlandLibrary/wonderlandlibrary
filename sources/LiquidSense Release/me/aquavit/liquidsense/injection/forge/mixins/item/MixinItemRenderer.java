package me.aquavit.liquidsense.injection.forge.mixins.item;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.module.modules.client.AntiBlind;
import me.aquavit.liquidsense.module.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.aquavit.liquidsense.module.modules.misc.Animations;
import org.lwjgl.opengl.GL11;
import me.aquavit.liquidsense.utils.timer.MSTimer;

import static me.aquavit.liquidsense.module.modules.render.ItemRotate.ItemRenderRotate;
import static me.aquavit.liquidsense.module.modules.misc.Animations.ItemRenderRotation;

@Mixin(ItemRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {


    private static int ticks;
    int f3 = 0;
    float delay = 0.0F;
    MSTimer rotationTimer = new MSTimer();

    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        final ItemRotate ItemRotate = (ItemRotate) LiquidSense.moduleManager.getModule(ItemRotate.class);
        final Animations animations = (Animations) LiquidSense.moduleManager.getModule(Animations.class); //
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP) abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if(this.itemToRender != null) {
            final Aura killAura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
            final EveryThingBlock EveryThingBlock = (EveryThingBlock) LiquidSense.moduleManager.getModule(EveryThingBlock.class);

            if(this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0 || (itemToRender.getItem() instanceof ItemSword && killAura.getBlockingStatus())) {
                EnumAction enumaction = killAura.getBlockingStatus() ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();

                switch(enumaction) {
                    case NONE:
                        //this.transformFirstPersonItem(f, -8.0F);
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f1);
                        break;
                    case BLOCK:
                        if(animations.getState()){
                            float var2 = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
                            float var4 = this.mc.thePlayer.getSwingProgress(partialTicks);
                            float var15 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927f);
                            float var777 = this.mc.thePlayer.prevRotationPitch + (this.mc.thePlayer.rotationPitch - this.mc.thePlayer.prevRotationPitch) * partialTicks;
                            float var888 = this.mc.thePlayer.prevRotationYaw + (this.mc.thePlayer.rotationYaw - this.mc.thePlayer.prevRotationYaw) * partialTicks;
                            switch(animations.getModeValue().get()) {
                                case"PushOther": {
                                    this.push(0.1f, f1);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                }
                                case "SmoothFloat": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    ItemRenderRotation();
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
                                    break;
                                }
                                case "Rotate360": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    ItemRenderRotation();
                                    break;
                                }
                                case "Screw":
                                    Animations.renderblock(f, f1);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    break;
                                case "Sigma":
                                    this.genCustom(var2 * 0.5f, 0);
                                    GlStateManager.rotate(-var15 * 55 / 2.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var15 * 45, 1.0F, var15 / 2, -0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    GL11.glTranslated(1.2, 0.3, 0.5);
                                    GL11.glTranslatef(-1, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                                    GlStateManager.scale(1.2f,1.2f,1.2f);
                                    break;
                                case "Remix":
                                    this.genCustom(f, 0.83f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.2f);
                                    GlStateManager.rotate((-f4) * 0.0f, 0.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate((-f4) * 43.0f, 58.0f, 23.0f, 45.0f);
                                    break;
                                case"Swang":
                                    this.transformFirstPersonItem1(var2 / 2.0f, var4);
                                    GlStateManager.rotate((float)(var15 * 30.0f / 2.0f), (float)(- var15), (float)7.0f, (float)9.0f);
                                    GlStateManager.rotate((float)(var15 * 40.0f), (float)1.0f, (float)((- var15) / 2.0f)+7.0f, (float)-0.0f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                case"Swong":
                                    this.transformFirstPersonItem1(var2 / 2.0f, 0.0f);
                                    GlStateManager.rotate((float)((- var15) * 40.0f / 2.0f), (float)(var15 / 2.0f), (float)-0.0f, (float)9.0f);
                                    GlStateManager.rotate((float)((- var15) * 30.0f), (float)1.0f, (float)(var15 / 2.0f), (float)-0.0f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                case"Swank":
                                    GL11.glTranslated((double)-0.1, (double)0.15, (double)0.);
                                    this.transformFirstPersonItem(var2 / 0.15f, var4);
                                    float cnmmm = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * 3.1415927f));
                                    GlStateManager.rotate((float)(cnmmm * 30.0f), (float)(2.0f), (float)- cnmmm, (float)9.0f);
                                    GlStateManager.rotate((float)(cnmmm * 35.0f), (float)1.0f, (float)(- cnmmm), (float)-0.0f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                case"MeMe":
                                    this.genCustom(0.0F, 0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.doBlockTransformations();
                                    float var111 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.5F, 0.4F, 0.0F);
                                    GlStateManager.rotate(-var111 * 50.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var111 * 70.0F, 1.0F, -0.4F, -0.0F);
                                    break;
                                case"Slide":
                                    this.Jigsaw(f, f1);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                case"Swing":
                                    GL11.glTranslated(-0.1F,0.15F,0.0F);
                                    this.transformFirstPersonItem(1.0F / 2.0F, var4);
                                    float var78 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
                                    GlStateManager.rotate(var78 * 30.0F, -var78, -0.0F, 9.0F);
                                    GlStateManager.rotate(var78 * 40.0F, 1.0F, -var78, -0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    break;
                                case"Jello":
                                    func_178096_b(0.0F, 0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    func_178103_d2();
                                    int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                                    float f5 = (var4 > 0.5D) ? (1.0F - var4) : var4;
                                    GlStateManager.translate(0.3F, -0.0F, 0.4F);
                                    GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
                                    GlStateManager.translate(0.0F, 0.5F, 0.0F);
                                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.translate(0.6F, 0.5F, 0.0F);
                                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.rotate(-10.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.rotate(this.mc.thePlayer.isSwingInProgress ? (-alpha / 5.0F) : 1.0F, 1.0F, -0.0F, 1.0F);
                                    break;
                                case"SigmaOther":
                                    GL11.glTranslated(0.1F,0.02F,0.0F);
                                    this.func_178096_A(var2, 0.0f);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    var15 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
                                    GlStateManager.rotate(var15 * 35.0f, -25.0f, -20.0f, 20.0f);
                                    break;
                                case"Swaing":
                                    GL11.glTranslated((double)-0.1, (double)0.15, (double)0.0);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178096_A(f / 2.0f, -0.2f);
                                    var15 = MathHelper.sin(f1 * f1 * 3.1415927f);
                                    GlStateManager.rotate((- var15) * 1.0f / 19.0f, var15 / 20.0f, -0.0f, 9.0f);
                                    GlStateManager.rotate((- var15) * 30.0f, 10.0f, var15 / 50.0f, 0.0f);
                                    break;
                                case"IDK":
                                    this.func_178096_b(var2, 0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    float var83 = MathHelper.sin(var4 * var4 * 3.1415927F);
                                    float var91 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
                                    GlStateManager.translate(-0.0F, -0.4F, 0.4F);
                                    GlStateManager.rotate(-var91 * 70.0F / 2.0F, -8.0F, -0.0F, 20.0F);
                                    GlStateManager.rotate(-var91 * 30.0F, 1.5F, -0.1F, -0.1F);
                                    break;
                                case"Windmill":
                                    this.genCustom(0.0F, 0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.doBlockTransformations();
                                    GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                    GlStateManager.rotate(MathHelper.sqrt_float(f1) * 10F * 40.0F, 1F, -0.0F, 2F);
                                    break;
                                case"Push":
                                    this.transformFirstPersonItem(f, 0.0F);
                                    if(ItemRotate.getState()){
                                        ItemRenderRotate();
                                    }
                                    this.func_178103_d2();
                                    float var15h = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.0F, 0.4F, 0.3F);
                                    GlStateManager.rotate(-var15h * 35.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var15h * 10.0F, 1.0F, -0.4F, -0.5F);
                                    break;


                            }
                            break;
                        }
                        this.transformFirstPersonItem(f + 0.1F, f1);
                        //GlStateManager.rotate(1.0F, 0.0F, 1.0F, 0.0F);//Left
                        this.doBlockTransformations();
                        //GlStateManager.translate(-0.5F, -3.0F, 0.0F); //Left
                        GlStateManager.translate(-0.5F, 0.2F, 0.0F); //right
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, f1);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            }else{
                if (animations.getState()){
                    if (LiquidSense.moduleManager.getModule(SwingAnimation.class).getState()){
                        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
                        this.transformFirstPersonItem(f, f1);
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                    }else if(LiquidSense.moduleManager.getModule(ItemRotate.class).getState()){
                        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
                        this.transformFirstPersonItem(f, f1);
                        ItemRenderRotate();
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                    }else if(LiquidSense.moduleManager.getModule(EveryThingBlock.class).getState()){
                        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
                        this.transformFirstPersonItem(f, f1);
                        if(this.mc.gameSettings.keyBindUseItem.isKeyDown()){
                            this.doBlockTransformations();
                            GL11.glTranslated(EveryThingBlock.getX().get().doubleValue(), EveryThingBlock.getY().get().doubleValue(), EveryThingBlock.getZ().get().doubleValue());
                        }
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                    } else {
                        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
                        this.doItemUsedTransformations(f1);
                        this.transformFirstPersonItem(f, f1);
                        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
                        //GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);//Left
                    }
                }else{
                    if (LiquidSense.moduleManager.getModule(SwingAnimation.class).getState()){
                        this.transformFirstPersonItem(f, f1);
                    }else if(LiquidSense.moduleManager.getModule(ItemRotate.class).getState()){
                        this.transformFirstPersonItem(f, f1);
                        ItemRenderRotate();
                    }else if(LiquidSense.moduleManager.getModule(EveryThingBlock.class).getState()){
                        this.transformFirstPersonItem(f, f1);
                        if(this.mc.gameSettings.keyBindUseItem.isKeyDown()){
                            this.doBlockTransformations();
                            GL11.glTranslated(EveryThingBlock.getX().get().doubleValue(), EveryThingBlock.getY().get().doubleValue(), EveryThingBlock.getZ().get().doubleValue());
                        }
                    } else {
                        this.doItemUsedTransformations(f1);
                        this.transformFirstPersonItem(f, f1);
                        //GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);//Left

                    }
                }




            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }else if(!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void push(float idk, float idc) {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(0.0F, idk * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * (float)Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * (float)Math.PI);
        GlStateManager.rotate(var3 *  -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void transformFirstPersonItem1(float equipProgress, float swingProgress)
    {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void func_178096_b(float p_178096_1_, float p_178096_2_)
    {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float)Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float)Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }
    private void func_178096_A(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(0.62f, -0.66f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178096_1_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927f);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927f);
        GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }
    private void Jigsaw(float var2, float swingProgress) {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        float smooth = (swingProgress*0.8f - (swingProgress*swingProgress)*0.8f);
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, var2 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 2+smooth*0.5f, smooth*3);
        float var3 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(0f, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.37F, 0.37F, 0.37F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void func_178103_d2()
    {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }
    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(25F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -15F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -10F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -30F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    private void renderPlayerArm(AbstractClientPlayer p_renderPlayerArm_1_, float p_renderPlayerArm_2_, float p_renderPlayerArm_3_) {
        final RenderManager renderManager = mc.getRenderManager();
        final Chams chams = (Chams) LiquidSense.moduleManager.getModule(Chams.class);
        float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_renderPlayerArm_3_) * 3.1415927F);
        float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_renderPlayerArm_3_) * 3.1415927F * 2.0F);
        float f2 = -0.4F * MathHelper.sin(p_renderPlayerArm_3_ * 3.1415927F);
        GlStateManager.translate(f, f1, f2);
        GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
        GlStateManager.translate(0.0F, p_renderPlayerArm_2_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f3 = MathHelper.sin(p_renderPlayerArm_3_ * p_renderPlayerArm_3_ * 3.1415927F);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(p_renderPlayerArm_3_) * 3.1415927F);
        GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.translate(5.6F, 0.0F, 0.0F);
        Render<AbstractClientPlayer> render = renderManager.getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        if (Chams.shouldRenderHand()) {
            Chams.preHandRender();
        } else {
            this.mc.getTextureManager().bindTexture(p_renderPlayerArm_1_.getLocationSkin());
            GL11.glColor4f(1f, 1f, 1f ,1f);
        }
        renderplayer.renderRightArm(this.mc.thePlayer);
        if (Chams.shouldRenderHand()) {
            Chams.postHandRender();
        }
        GlStateManager.enableCull();
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        if(LiquidSense.moduleManager.getModule(AntiBlind.class).getState() && AntiBlind.fireEffect.get()) callbackInfo.cancel();
    }
}