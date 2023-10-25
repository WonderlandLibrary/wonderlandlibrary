package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBedRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityBed;
import net.optifine.reflect.Reflector;

public class ModelAdapterBed extends ModelAdapter
{
    public ModelAdapterBed()
    {
        super(TileEntityBed.class, "bed", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelBed();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelBed))
        {
            return null;
        }
        else
        {
            ModelBed modelbed = (ModelBed)model;

            if (modelPart.equals("head"))
            {
                return modelbed.headPiece;
            }
            else if (modelPart.equals("foot"))
            {
                return modelbed.footPiece;
            }
            else if (modelPart.equals("leg1"))
            {
                return modelbed.legs[0];
            }
            else if (modelPart.equals("leg2"))
            {
                return modelbed.legs[1];
            }
            else if (modelPart.equals("leg3"))
            {
                return modelbed.legs[2];
            }
            else
            {
                return modelPart.equals("leg4") ? modelbed.legs[3] : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "foot", "leg1", "leg2", "leg3", "leg4"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getRenderer(TileEntityBed.class);

        if (!(tileentityspecialrenderer instanceof TileEntityBedRenderer))
        {
            return null;
        }
        else
        {
            if (tileentityspecialrenderer.getEntityClass() == null)
            {
                tileentityspecialrenderer = new TileEntityBedRenderer();
                tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
            }

            if (!Reflector.TileEntityBedRenderer_model.exists())
            {
                Config.warn("Field not found: TileEntityBedRenderer.model");
                return null;
            }
            else
            {
                Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityBedRenderer_model, modelBase);
                return tileentityspecialrenderer;
            }
        }
    }
}
