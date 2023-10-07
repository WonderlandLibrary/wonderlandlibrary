package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlockOutline extends AbstractModule {

    public BlockOutline(@NotNull ModuleManager moduleManager) {
        super(moduleManager, "BlocksOutline","Blocks Outline",EnumModuleType.VISUALS,"Outlines the block you're looking at");
    }

    @EventTarget
    public void onRender3D(Render3DEvent event){
        if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            RenderUtils.pre3D();
            RenderUtils.setColor(new Color(152, 217, 0));
            double x = pos.getX() - mc.getRenderManager().renderPosX;
            double y = pos.getY() - mc.getRenderManager().renderPosY;
            double z = pos.getZ() - mc.getRenderManager().renderPosZ;
            double height = mc.world.getBlockState(pos).getBlock().getBlockBoundsMaxY() - mc.world.getBlockState(pos).getBlock().getBlockBoundsMinY();
            GL11.glLineWidth(1);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y,z);
            GL11.glVertex3d(x,y + height,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y,z);
            GL11.glVertex3d(x + 1,y + height,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y,z + 1);
            GL11.glVertex3d(x + 1,y + height,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y,z + 1);
            GL11.glVertex3d(x,y + height,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y,z);
            GL11.glVertex3d(x + 1,y,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y + height,z);
            GL11.glVertex3d(x + 1,y + height,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y,z);
            GL11.glVertex3d(x,y,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y + height,z);
            GL11.glVertex3d(x,y + height,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y,z + 1);
            GL11.glVertex3d(x + 1,y,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y + height,z + 1);
            GL11.glVertex3d(x + 1,y + height,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y,z + 1);
            GL11.glVertex3d(x + 1,y,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x + 1,y + height,z + 1);
            GL11.glVertex3d(x + 1,y + height,z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y,z + 1);
            GL11.glVertex3d(x + 1,y,z + 1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x,y + height,z + 1);
            GL11.glVertex3d(x + 1,y + height,z + 1);
            GL11.glEnd();

//            GL11.glBegin(GL11.GL_LINE_STRIP);
//            GL11.glVertex3d(x,y,z);
//            GL11.glVertex3d(x,y + 1,z);
//            GL11.glVertex3d(x + 1,y + 1,z);
//            GL11.glVertex3d(x + 1,y,z);
//            GL11.glVertex3d(x,y,z);
//            GL11.glEnd();
//            GL11.glBegin(GL11.GL_LINE_STRIP);
//            GL11.glVertex3d(x + 1,y,z);
//            GL11.glVertex3d(x + 1,y,z + 1);
//            GL11.glVertex3d(x + 1,y + 1,z + 1);
//            GL11.glVertex3d(x + 1,y + 1,z);
//            GL11.glEnd();
//            GL11.glBegin(GL11.GL_LINE_STRIP);
//            GL11.glVertex3d(x + 1,y,z + 1);
//            GL11.glVertex3d(x, y,z + 1);
//            GL11.glVertex3d(x,y + 1,z + 1);
//            GL11.glVertex3d(x + 1,y + 1,z + 1);
//            GL11.glEnd();
//            GL11.glBegin(GL11.GL_LINE_STRIP);
//            GL11.glVertex3d(x,y,z);
//            GL11.glv

            RenderUtils.post3D();
        }
    }

}
