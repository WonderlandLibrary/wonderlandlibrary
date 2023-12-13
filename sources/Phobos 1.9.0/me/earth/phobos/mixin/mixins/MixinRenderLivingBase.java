package me.earth.phobos.mixin.mixins;

import java.awt.Color;
import me.earth.phobos.event.events.RenderEntityModelEvent;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.render.Chams;
import me.earth.phobos.features.modules.render.ESP;
import me.earth.phobos.features.modules.render.Skeleton;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    private static final ResourceLocation glint = new ResourceLocation("textures/shinechams.png");

    public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
    }

    @Redirect(method={"renderModel"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Color visibleColor;
        boolean cancel = false;
        if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (Skeleton.getInstance().isEnabled()) {
                Skeleton.getInstance().onRenderModel(event);
            }
            if (ESP.getInstance().isEnabled()) {
                ESP.getInstance().onRenderModel(event);
                if (event.isCanceled()) {
                    cancel = true;
                }
            }
        }
        if (Chams.getInstance().isEnabled() && entityIn instanceof EntityPlayer && Chams.getInstance().colored.getValue().booleanValue() && !Chams.getInstance().textured.getValue().booleanValue()) {
            if (!Chams.getInstance().textured.getValue().booleanValue()) {
                GL11.glPushAttrib((int)1048575);
                GL11.glDisable((int)3008);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glLineWidth((float)1.5f);
                GL11.glEnable((int)2960);
                if (Chams.getInstance().rainbow.getValue().booleanValue()) {
                    Color rainbowColor1 = Chams.getInstance().colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(Chams.getInstance().speed.getValue() * 100, 0, (float)Chams.getInstance().saturation.getValue().intValue() / 100.0f, (float)Chams.getInstance().brightness.getValue().intValue() / 100.0f));
                    Color rainbowColor = EntityUtil.getColor(entityIn, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    GL11.glColor4f((float)((float)rainbowColor.getRed() / 255.0f), (float)((float)rainbowColor.getGreen() / 255.0f), (float)((float)rainbowColor.getBlue() / 255.0f), (float)((float)Chams.getInstance().alpha.getValue().intValue() / 255.0f));
                    modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                } else if (Chams.getInstance().xqz.getValue().booleanValue()) {
                    Color hiddenColor = Chams.getInstance().colorSync.getValue() != false ? EntityUtil.getColor(entityIn, Chams.getInstance().hiddenRed.getValue(), Chams.getInstance().hiddenGreen.getValue(), Chams.getInstance().hiddenBlue.getValue(), Chams.getInstance().hiddenAlpha.getValue(), true) : EntityUtil.getColor(entityIn, Chams.getInstance().hiddenRed.getValue(), Chams.getInstance().hiddenGreen.getValue(), Chams.getInstance().hiddenBlue.getValue(), Chams.getInstance().hiddenAlpha.getValue(), true);
                    Color visibleColor2 = Chams.getInstance().colorSync.getValue() != false ? EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true) : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    GL11.glColor4f((float)((float)hiddenColor.getRed() / 255.0f), (float)((float)hiddenColor.getGreen() / 255.0f), (float)((float)hiddenColor.getBlue() / 255.0f), (float)((float)Chams.getInstance().alpha.getValue().intValue() / 255.0f));
                    modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glColor4f((float)((float)visibleColor2.getRed() / 255.0f), (float)((float)visibleColor2.getGreen() / 255.0f), (float)((float)visibleColor2.getBlue() / 255.0f), (float)((float)Chams.getInstance().alpha.getValue().intValue() / 255.0f));
                    modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                } else {
                    visibleColor = Chams.getInstance().colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)Chams.getInstance().alpha.getValue().intValue() / 255.0f));
                    modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
                GL11.glEnable((int)3042);
                GL11.glEnable((int)2896);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)3008);
                GL11.glPopAttrib();
            }
        } else if (Chams.getInstance().textured.getValue().booleanValue()) {
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            visibleColor = Chams.getInstance().colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, Chams.getInstance().red.getValue(), Chams.getInstance().green.getValue(), Chams.getInstance().blue.getValue(), Chams.getInstance().alpha.getValue(), true);
            GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)Chams.getInstance().alpha.getValue().intValue() / 255.0f));
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
        } else if (!cancel) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    public void doRenderPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && !Chams.getInstance().colored.getValue().booleanValue() && entity != null) {
            GL11.glEnable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    public void doRenderPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && !Chams.getInstance().colored.getValue().booleanValue() && entity != null) {
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
        }
    }
}

