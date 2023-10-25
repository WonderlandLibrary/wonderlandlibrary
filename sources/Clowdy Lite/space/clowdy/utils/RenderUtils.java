package space.clowdy.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IngameGui;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
     public static void _actress(MatrixStack matrixStack, float float2, float float3, float float4, float float5, int integer) {
          GL11.glEnable(3042);
          GL11.glEnable(2848);
          IngameGui.fill(matrixStack, (int)float2, (int)float3, (int)float4, (int)float5, integer);
          GL11.glScalef(0.5F, 0.5F, 0.5F);
          IngameGui.fill(matrixStack, (int)(float2 * 2.0F - 1.0F), (int)(float3 * 2.0F), (int)(float2 * 2.0F), (int)(float5 * 2.0F - 1.0F), integer);
          IngameGui.fill(matrixStack, (int)(float2 * 2.0F), (int)(float3 * 2.0F - 1.0F), (int)(float4 * 2.0F), (int)(float3 * 2.0F), integer);
          IngameGui.fill(matrixStack, (int)(float4 * 2.0F), (int)(float3 * 2.0F), (int)(float4 * 2.0F + 1.0F), (int)(float5 * 2.0F - 1.0F), integer);
          IngameGui.fill(matrixStack, (int)(float2 * 2.0F), (int)(float5 * 2.0F - 1.0F), (int)(float4 * 2.0F), (int)(float5 * 2.0F), integer);
          GL11.glDisable(2848);
          GL11.glDisable(3042);
          GL11.glScalef(2.0F, 2.0F, 2.0F);
     }
}
