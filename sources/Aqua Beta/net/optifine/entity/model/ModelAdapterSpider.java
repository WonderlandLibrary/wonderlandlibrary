package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;

public class ModelAdapterSpider
extends ModelAdapter {
    public ModelAdapterSpider() {
        super(EntitySpider.class, "spider", 1.0f);
    }

    protected ModelAdapterSpider(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    public ModelBase makeModel() {
        return new ModelSpider();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelSpider)) {
            return null;
        }
        ModelSpider modelspider = (ModelSpider)model;
        return modelPart.equals((Object)"head") ? modelspider.spiderHead : (modelPart.equals((Object)"neck") ? modelspider.spiderNeck : (modelPart.equals((Object)"body") ? modelspider.spiderBody : (modelPart.equals((Object)"leg1") ? modelspider.spiderLeg1 : (modelPart.equals((Object)"leg2") ? modelspider.spiderLeg2 : (modelPart.equals((Object)"leg3") ? modelspider.spiderLeg3 : (modelPart.equals((Object)"leg4") ? modelspider.spiderLeg4 : (modelPart.equals((Object)"leg5") ? modelspider.spiderLeg5 : (modelPart.equals((Object)"leg6") ? modelspider.spiderLeg6 : (modelPart.equals((Object)"leg7") ? modelspider.spiderLeg7 : (modelPart.equals((Object)"leg8") ? modelspider.spiderLeg8 : null))))))))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "neck", "body", "leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7", "leg8"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderSpider renderspider = new RenderSpider(rendermanager);
        renderspider.mainModel = modelBase;
        renderspider.shadowSize = shadowSize;
        return renderspider;
    }
}
