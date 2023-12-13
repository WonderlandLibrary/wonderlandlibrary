package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import me.earth.earthhack.impl.util.render.Interpolation;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderListener extends ModuleListener<ServerAutoCrystal, Render3DEvent>
{
    protected RenderListener(ServerAutoCrystal module)
    {
        super(module, Render3DEvent.class);
    }

    @Override
    public void invoke(Render3DEvent event)
    {
        if (module.renderPos != null)
        {
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);

            AxisAlignedBB bb = Interpolation.interpolatePos(module.renderPos, 1.0f);
            Color boxColor = new Color(1.0f, 1.0f, 1.0f, 0.9F);
            RenderUtil.startRender();
            RenderUtil.drawOutline(bb, 1.5F, boxColor);
            RenderUtil.endRender();
            boxColor = new Color(1.0f, 1.0f, 1.0f, 0.3F);
            RenderUtil.startRender();
            RenderUtil.drawBox(bb, boxColor);
            RenderUtil.endRender();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

}
