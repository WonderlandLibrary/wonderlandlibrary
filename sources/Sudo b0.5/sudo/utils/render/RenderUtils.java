package sudo.utils.render;

import java.awt.Color;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import sudo.mixins.accessors.FrustramAccessor;
import sudo.mixins.accessors.WorldRendererAccessor;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class RenderUtils {
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	public static double getScaleFactor() {
        return mc.getWindow().getScaleFactor();
    }

    public static int getScaledWidth() {
        return mc.getWindow().getScaledWidth();
    }

    public static int getScaledHeight() {
        return mc.getWindow().getScaledHeight();
    }
 
    
	public static void setupRender() {
		RenderSystem.enableBlend();
		//RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
	
	public static void endRender() {
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
	}
	
    public static void shaderColor(int rgb) {
        float alpha = (rgb >> 24 & 0xFF) / 255.0F;
        float red = (rgb >> 16 & 0xFF) / 255.0F;
        float green = (rgb >> 8 & 0xFF) / 255.0F;
        float blue = (rgb & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }
    
	public static Vec3d center() {
		Vec3d pos = new Vec3d(0, 0, 1);
		
		return new Vec3d(pos.x, -pos.y, pos.z)
		.rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
		.rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
		.add(mc.gameRenderer.getCamera().getPos());
	}
	@SuppressWarnings("resource")
	public static Frustum getFrustum() {
		return ((WorldRendererAccessor) MinecraftClient.getInstance().worldRenderer).getFrustum();
	}
	
	public static boolean isOnScreen2d(Vec3d pos) {
        return pos != null && (pos.z > -1 && pos.z < 1);
    }
	
	public static double distanceTo(double x1, double x2) {
		return x2 - x1;
	}

	public static double slowDownTo(double x1, double x2, float smooth) {
		return (x2 - x1) / smooth;
	}
	

    public static void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, float x, float y, float scale, @Nullable String countLabel) {
        if (!stack.isEmpty()) {
            MatrixStack matrixStack = new MatrixStack();
            if (stack.getCount() != 1 || countLabel != null) {
                String string = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;
                matrixStack.translate(0.0D, 0.0D, (double)(mc.getItemRenderer().zOffset + 200.0F));
                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
                renderer.draw(string, (float)(x + 19 - 2 - renderer.getWidth(string)), (float)(y + 6 + 3), 16777215, true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
                immediate.draw();
            }

            if (stack.isItemBarVisible()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                int i = stack.getItemBarStep();
                int j = stack.getItemBarColor();
                fill(matrixStack, x + 2, y + 13, x + 2 + 13, y + 13 + 2, 0xff000000);
                fill(matrixStack, x + 2, y + 13, x + 2 + i, y + 13 + 1, new Color(j >> 16 & 255, j >> 8 & 255, j & 255, 255).getRGB());
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientPlayerEntity = mc.player;
            float f = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tessellator tessellator2 = Tessellator.getInstance();
                BufferBuilder bufferBuilder2 = tessellator2.getBuffer();
                renderGuiQuad(bufferBuilder2, x, y + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

        }
    }

    private static void renderGuiQuad(BufferBuilder buffer, float x, float y, float width, float height, int red, int green, int blue, int alpha) {
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();
    }
    
	public static void line(Vec3d start, Vec3d end, Color color, MatrixStack matrices) {
		float red = color.getRed() / 255f;
		float green = color.getGreen() / 255f;
		float blue = color.getBlue() / 255f;
		float alpha = color.getAlpha() / 255f;
		Camera c = mc.gameRenderer.getCamera();
		Vec3d camPos = c.getPos();
		start = start.subtract(camPos);
		end = end.subtract(camPos);
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		float x1 = (float) start.x;
		float y1 = (float) start.y;
		float z1 = (float) start.z;
		float x2 = (float) end.x;
		float y2 = (float) end.y;
		float z2 = (float) end.z;
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
		VertexFormats.POSITION_COLOR);
		buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
		
		BufferRenderer.drawWithShader(buffer.end());
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableBlend();
	}
	
	public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer(); 
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR); 
		 
		double toX1 = toX - rad; 
		double toY1 = toY - rad; 
		double fromX1 = fromX + rad; 
		double fromY1 = fromY + rad; 
		double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}}; 
		for (int i = 0; i < 4; i++) {
			double[] current = map[i]; 
			for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
				float rad1 = (float) Math.toRadians(r); 
				float sin = (float) (Math.sin(rad1) * rad); 
				float cos = (float) (Math.cos(rad1) * rad); 
				bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next(); 
			}
		} 
		BufferRenderer.drawWithShader(bufferBuilder.end()); 
	}
	
	public static void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double rad, double samples) {
		int color = c.getRGB(); 
		Matrix4f matrix = matrices.peek().getPositionMatrix(); 
		float f = (float) (color >> 24 & 255) / 255.0F; 
		float g = (float) (color >> 16 & 255) / 255.0F; 
		float h = (float) (color >> 8 & 255) / 255.0F; 
		float k = (float) (color & 255) / 255.0F; 
		RenderSystem.enableBlend(); 
		RenderSystem.disableTexture(); 
		RenderSystem.setShader(GameRenderer::getPositionColorShader); 
		 
		renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples); 
		 
		RenderSystem.enableTexture(); 
		RenderSystem.disableBlend(); 
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f); 
	}
	
	public static void setup2DRender(boolean disableDepth) {
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		if (disableDepth)
		RenderSystem.disableDepthTest();
	}
	
	public static void end2DRender() {
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		RenderSystem.enableDepthTest();
	}
	
	public static void setup3DRender(boolean disableDepth) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (disableDepth)
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		RenderSystem.enableCull();
	}
	
	public static void end3DRender() {
		RenderSystem.enableTexture();
		RenderSystem.disableCull();
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
	}
	
	public static Vec3d getRenderPosition(BlockPos blockPos) {
		double minX = blockPos.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
		double minY = blockPos.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
		double minZ = blockPos.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;
		return new Vec3d(minX, minY, minZ);
	}
	
	public static int getPercentColor(float percent) {
        if (percent <= 15)
            return new Color(255, 0, 0).getRGB();
        else if (percent <= 25)
            return new Color(255, 75, 92).getRGB();
        else if (percent <= 50)
            return new Color(255, 123, 17).getRGB();
        else if (percent <= 75)
            return new Color(255, 234, 0).getRGB();
        return new Color(0, 255, 0).getRGB();
    }
	
	public static void drawFace(MatrixStack matrixStack, float x, float y, int renderScale, Identifier id) {
        try {
            bindTexture(id);
            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 40 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
        }catch (Exception e){}
    }
	
	public static void fill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, int color) {
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		double j;
		if (x1 < x2) {
			j = x1;
			x1 = x2;
			x2 = j;
		}
		
		if (y1 < y2) {
			j = y1;
			y1 = y2;
			y2 = j;
		}
		
		float f = (float)(color >> 24 & 255) / 255.0F;
		float g = (float)(color >> 16 & 255) / 255.0F;
		float h = (float)(color >> 8 & 255) / 255.0F;
		float k = (float)(color & 255) / 255.0F;
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}
	
	@SuppressWarnings("resource")
	public static MatrixStack matrixFrom(double x, double y, double z) {
		MatrixStack matrices = new MatrixStack();
		
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));
		
		matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);
		
		return matrices;
	}
	public static void drawOutlineBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		Color color1 = color;
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		if (draw)
			bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
		
		VoxelShape shape = VoxelShapes.cuboid(bb);
		shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
			bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
			bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		});
		if (draw) {
			BufferRenderer.drawWithShader(bufferBuilder.end());
		}
	}
	public static void drawOutlineCircle(MatrixStack matrices, double xx, double yy, double radius, Color color) {
		RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		RenderUtils.bindTexture(new Identifier("sudo", "textures/outlinecircle.png"));
		RenderSystem.enableBlend();
		RenderUtils.drawTexture(matrices, (float) xx,(float) yy, (float)radius, (float)radius, 0, 0, (float)radius,(float) radius,(float) radius,(float) radius);
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
	
	public static void bindTexture(Identifier identifier) {
		RenderSystem.setShaderTexture(0, identifier);
	}
	
	public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, int textureWidth, int textureHeight) {
		RenderSystem.enableBlend();
		drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
		RenderSystem.disableBlend();
	}
	
	public static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight) {
		drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	}
	
	public static void drawTexture(MatrixStack matrices, float x0, float y0, float x1, float y1, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
		drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
	}
	
	public static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
		bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
		bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
		bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}
	
	public static void drawBottomFilledRect(Box box, QuadColor color, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}
		
		setup3DRender(true);
		
		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		// Fill
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		Vertexer1.vertexBottomFilledRect(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();
		
		end3DRender();
	}
	
	public static void drawBottomOutlineRect(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}
		
		setup3DRender(true);
		
		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		// Outline
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.lineWidth(lineWidth);
		
		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		Vertexer1.vertexBottomOutline(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();
		
		RenderSystem.enableCull();
		
		end3DRender();
	}
	
	public static void drawBoxFill(Box box, QuadColor color, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}
		
		setup3DRender(true);
		
		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		// Fill
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		Vertexer1.vertexBoxQuads(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();
		
		end3DRender();
	}
	
	public static void drawBoxFill(BlockPos blockPos, QuadColor color, Direction... excludeDirs) {
		drawBoxFill(new Box(blockPos), color, excludeDirs);
	}
	
	public static void drawBoxOutline(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}
		
		setup3DRender(true);
		
		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		
		// Outline
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.lineWidth(lineWidth);
		
		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		Vertexer1.vertexBoxLines(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();
		
		RenderSystem.enableCull();
		
		end3DRender();
	}
	
	public static void drawBoxOutline(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
		drawBoxOutline(new Box(blockPos), color, lineWidth, excludeDirs);
	}
	
	public static void drawFilledBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		Color color1 = color;
		setup3DRender(true);
		
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		if (draw)
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
		float minX = (float)bb.minX;
		float minY = (float)bb.minY;
		float minZ = (float)bb.minZ;
		float maxX = (float)bb.maxX;
		float maxY = (float)bb.maxY;
		float maxZ = (float)bb.maxZ;
		
		bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		
		bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		
		bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		
		bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		
		bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		
		bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		if (draw) {
			BufferRenderer.drawWithShader(bufferBuilder.end());
		}
		end3DRender();
	}
	
	public static Vec3d getEntityRenderPosition(Entity entity, double partial) {
		double x = entity.prevX + ((entity.getX() - entity.prevX) * partial) - mc.getEntityRenderDispatcher().camera.getPos().x;
		double y = entity.prevY + ((entity.getY() - entity.prevY) * partial) - mc.getEntityRenderDispatcher().camera.getPos().y;
		double z = entity.prevZ + ((entity.getZ() - entity.prevZ) * partial) - mc.getEntityRenderDispatcher().camera.getPos().z;
		return new Vec3d(x, y, z);
	}
	
	public static void drawEntityBox(MatrixStack matrixstack, Entity entity, double x, double y, double z, Color color) {
		setup3DRender(true);
		matrixstack.translate(x, y, z);
		matrixstack.multiply(new Quaternion(new Vec3f(0, -1, 0), 0, true));
		matrixstack.translate(-x, -y, -z);
		
		Box bb = new Box(x - entity.getWidth() + 0.25, y, z - entity.getWidth() + 0.25, x + entity.getWidth() - 0.25, y + entity.getHeight() + 0.1, z + entity.getWidth() - 0.25);
		if (entity instanceof ItemEntity)
		bb = new Box(x - 0.15, y + 0.1f, z - 0.15, x + 0.15, y + 0.5, z + 0.15);
		
		
		drawFilledBox(matrixstack, bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), 130), true);
		RenderSystem.lineWidth(1.5f);
		
		//drawOutlineBox(matrixstack, bb, color, true);
		
		end3DRender();
		matrixstack.translate(x, y, z);
		matrixstack.multiply(new Quaternion(new Vec3f(0, 1, 0), 0, true));
		matrixstack.translate(-x, -y, -z);
	}
	
	public static boolean isPointVisible(double x, double y, double z) {
		FrustramAccessor frustum = (FrustramAccessor) getFrustum();
		Vector4f[] frustumCoords = frustum.getHomogeneousCoordinates();
		Vector4f pos = new Vector4f((float) (x - frustum.getX()), (float) (y - frustum.getY()), (float) (z - frustum.getZ()), 1f);

		for (int i = 0; i < 6; ++i) {
			if (frustumCoords[i].dotProduct(pos) <= 0f) {
				return false;
			}
		}

		return true;
	}
	
	public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, LineColor color, float width) {
		if (!isPointVisible(x1, y1, z1) && !isPointVisible(x2, y2, z2)) {
			return;
		}

		setup3DRender(true);

		MatrixStack matrices = matrixFrom(x1, y1, z1);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		// Line
		RenderSystem.disableDepthTest();
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.lineWidth(width);

		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		Vertexer1.vertexLine(matrices, buffer, 0f, 0f, 0f, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), color);
		tessellator.draw();

		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		end3DRender();
	}
	
	public static void drawCircle(MatrixStack matrices, Vec3d pos, double rad, double height, int color) {
        double lastX = 0;
		double lastZ = rad;
		for (int angle = 0; angle <= 360; angle += 6) {
			float cos = MathHelper.cos((float) Math.toRadians(angle));
			float sin = MathHelper.sin((float) Math.toRadians(angle));

			double x = rad * sin;
			double z = rad * cos;
			drawLine(
					pos.x + lastX, pos.y, pos.z + lastZ,
					pos.x + x, pos.y, pos.z + z,
					LineColor.single(color), 2);

			lastX = x;
			lastZ = z;
		}
    }
	
	public static void drawText(Text text, double x, double y, double z, double scale, boolean fill) {
		drawText(text, x, y, z, 0, 0, scale, fill);
	}
	
	
	public static void drawWorldText(String string, double x, double y, double z, double scale, int color, boolean background) {
		drawWorldText(string, x, y, z, 0, 0, scale, false, color, background);
	}

	@SuppressWarnings("resource")
	public static void drawWorldText(String string, double x, double y, double z, double offX, double offY, double scale, boolean shadow, int color, boolean background) {
		MatrixStack matrices = matrixFrom(x, y, z);

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		matrices.translate(offX, offY, 0);
		matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);

		int halfWidth = (int) (textRend.getStringWidth(string) / 2);
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		if(shadow) {
			matrices.push();
			matrices.translate(1, 1, 0);
			textRend.drawString(matrices, string, -halfWidth, 0f, 0x202020, 1);
			immediate.draw();
			matrices.pop();
		}

		if(background) {
			float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
			int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

			int xF = (int) (-textRend.getStringWidth(string) / 2);
			DrawableHelper.fill(matrices, xF - 1, -2, (int) (textRend.getStringWidth(string) / 2 + 3), (int) (textRend.getFontHeight() + 1), backgroundColor);
		}
//		textRend.draw(matrices, Text.of(text), -halfWidth, 0f, 1);
        mc.textRenderer.draw(matrices, Text.of(string), -halfWidth, 0f, 1);
		immediate.draw();

		RenderSystem.disableBlend();
	}
	
	public static Vec3d getInterpolationOffset(Entity e) {
		if (MinecraftClient.getInstance().isPaused()) {
			return Vec3d.ZERO;
		}
		
		double tickDelta = MinecraftClient.getInstance().getTickDelta();
		return new Vec3d(
		e.getX() - MathHelper.lerp(tickDelta, e.lastRenderX, e.getX()),
		e.getY() - MathHelper.lerp(tickDelta, e.lastRenderY, e.getY()),
		e.getZ() - MathHelper.lerp(tickDelta, e.lastRenderZ, e.getZ()));
	}
	
	public static void drawText(Text text, double x, double y, double z, double offX, double offY, double scale, boolean fill) {
		MatrixStack matrices = matrixFrom(x, y, z);
		
		Camera camera = mc.gameRenderer.getCamera();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		matrices.translate(offX, offY, 0);
		matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
		
		int halfWidth = mc.textRenderer.getWidth(text) / 2;
		
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		
		if (fill) {
			@SuppressWarnings("resource")
			int opacity = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.6F) * 255.0F) << 24;
			mc.textRenderer.draw(text, -halfWidth, 0f, 553648127, false, matrices.peek().getPositionMatrix(), immediate, true, opacity, 0xf000f0);
			immediate.draw();
		} else {
			matrices.push();
			matrices.translate(1, 1, 0);
			immediate.draw();
			matrices.pop();
		}
		mc.textRenderer.draw(text, -halfWidth, 0f, -1, false, matrices.peek().getPositionMatrix(), immediate, true, 0, 0xf000f0);
		immediate.draw();
		
		RenderSystem.disableBlend();
	}
	
	public static void renderRoundedShadow(MatrixStack matrices, Color innerColor, double fromX, double fromY, double toX, double toY, double rad, double samples, double shadowWidth) {
		int color = innerColor.getRGB();
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		float f = (float) (color >> 24 & 255) / 255.0F;
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;
		setupRender();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		
		renderRoundedShadowInternal(matrix, g, h, k, transformColor(f), fromX, fromY, toX, toY, rad, samples, shadowWidth);
		endRender();
	}
	
	public static void renderRoundedShadowInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples, double wid) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
		
		double toX1 = toX - rad;
		double toY1 = toY - rad;
		double fromX1 = fromX + rad;
		double fromY1 = fromY + rad;
		double[][] map = new double[][] { new double[] { toX1, toY1 }, new double[] { toX1, fromY1 }, new double[] { fromX1, fromY1 },
		new double[] { fromX1, toY1 } };
		for (int i = 0; i < map.length; i++) {
			double[] current = map[i];
			for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
				float rad1 = (float) Math.toRadians(r);
				float sin = (float) (Math.sin(rad1) * rad);
				float cos = (float) (Math.cos(rad1) * rad);
				bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
				float sin1 = (float) (sin + Math.sin(rad1) * wid);
				float cos1 = (float) (cos + Math.cos(rad1) * wid);
				bufferBuilder.vertex(matrix, (float) current[0] + sin1, (float) current[1] + cos1, 0.0F).color(cr, cg, cb, 0f).next();
			}
		}
		{
			double[] current = map[0];
			float rad1 = (float) Math.toRadians(0);
			float sin = (float) (Math.sin(rad1) * rad);
			float cos = (float) (Math.cos(rad1) * rad);
			bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
			float sin1 = (float) (sin + Math.sin(rad1) * wid);
			float cos1 = (float) (cos + Math.cos(rad1) * wid);
			bufferBuilder.vertex(matrix, (float) current[0] + sin1, (float) current[1] + cos1, 0.0F).color(cr, cg, cb, 0f).next();
		}
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}
	
	public static float transformColor(float f) {
		return AlphaOverride.compute((int) (f * 255)) / 255f;
	}
	
	public static void renderOutlineRect(Entity e, Color color, MatrixStack stack) {
		float red = color.getRed() / 255f;
		float green = color.getGreen() / 255f;
		float blue = color.getBlue() / 255f;
		float alpha = color.getAlpha() / 255f;
		Camera c = mc.gameRenderer.getCamera();
		Vec3d camPos = c.getPos();
		Vec3d start = e.getPos().subtract(camPos);
		float x = (float) start.x;
		float y = (float) start.y;
		float z = (float) start.z;
		
		double r = Math.toRadians(-c.getYaw() + 90);
		float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
		float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
		stack.push();
		
		Matrix4f matrix = stack.peek().getPositionMatrix();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
		VertexFormats.POSITION_COLOR);
		
		buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
		
		BufferRenderer.drawWithShader(buffer.end());
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableBlend();
		stack.pop();
	}
	
	public static void renderOutlineRect(LivingEntity e, Color color, MatrixStack stack) {
		float red = color.getRed() / 255f;
		float green = color.getGreen() / 255f;
		float blue = color.getBlue() / 255f;
		float alpha = color.getAlpha() / 255f;
		Camera c = mc.gameRenderer.getCamera();
		Vec3d camPos = c.getPos();
		Vec3d start = e.getPos().subtract(camPos);
		float x = (float) start.x;
		float y = (float) start.y;
		float z = (float) start.z;
		
		double r = Math.toRadians(-c.getYaw() + 90);
		float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
		float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
		stack.push();
		
		Matrix4f matrix = stack.peek().getPositionMatrix();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
		VertexFormats.POSITION_COLOR);
		
		buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
		
		BufferRenderer.drawWithShader(buffer.end());
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableBlend();
		stack.pop();
	}
	
	public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
		if (framebuffer == null || framebuffer.viewportWidth != mc.getWindow().getFramebufferWidth() || framebuffer.viewportHeight != mc.getWindow().getFramebufferHeight()) {
			if (framebuffer != null) {
				framebuffer.delete();
			}
			return new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
		}
		return framebuffer;
	}
	
	public static void startScissor(int x, int y, int width, int height) {
		double factor = mc.getWindow().getScaleFactor();
		RenderSystem.enableScissor((int) (x * factor), (int) ((mc.getWindow().getHeight() - (y * factor) - height * factor)), (int) (width * factor), (int) (height * factor));
	}
	
	public static void endScissor() {
		RenderSystem.disableScissor();
	}
	
	public static ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier("sudo", "shaders/post/blur.json"),
			shader -> shader.setUniformValue("Radius", 8f));
	
	public static void blur(MatrixStack matrices, float fromX, float fromY, float toX, float toY, float Value) {
//		startScissor((int) fromX, (int) fromY, (int) toX, (int) toY);
		blur.setUniformValue("Radius", Value);
		blur.render(mc.getTickDelta());
		blur.render(mc.getTickDelta());
//		endScissor();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	
	private static int stencilBit = 0xff;
	
	public static void preStencil() {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.colorMask(false, false, false, false);
		RenderSystem.depthMask(false);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, stencilBit, stencilBit);
		RenderSystem.stencilMask(stencilBit);
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, false);
	}
	
	public static void postStencil() {
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.depthMask(true);
		RenderSystem.stencilMask(0x00);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilBit, stencilBit);
	}
	
	public static void disableStencil() {
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	
	public static void drawBlurredTexture(MatrixStack matrices, int x, int y, int z, float u, float v, int width, int height, int textureHeight, int textureWidth) {
		drawBlurredTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureHeight, textureWidth);
	}
	
	private static void drawBlurredTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
		drawBlurredTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z,
		(u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth,
		(v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
	}
	
	public static final ManagedCoreShader BLUR_SHADER = ShaderEffectManager.getInstance().manageCoreShader(new Identifier("blur"));
	
	public static void drawBlurredTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
		RenderSystem.setShader(BLUR_SHADER::getProgram);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
		bufferBuilder.vertex(matrix, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}
	
	public static void drawBoxBoth(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
		drawBoxBoth(new Box(blockPos), color, lineWidth, excludeDirs);
	}
	
	public static void drawBoxBoth(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
		QuadColor outlineColor = color.clone();
		outlineColor.overwriteAlpha(255);
		
		drawBoxBoth(box, color, outlineColor, lineWidth, excludeDirs);
	}
	
	public static void drawBoxBoth(BlockPos blockPos, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
		drawBoxBoth(new Box(blockPos), fillColor, outlineColor, lineWidth, excludeDirs);
	}
	
	public static void drawBoxBoth(Box box, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
		drawBoxFill(box, fillColor, excludeDirs);
		drawBoxOutline(box, outlineColor, lineWidth, excludeDirs);
	}
	
	@SuppressWarnings("deprecation")
	public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float g = (float)Math.atan(mouseY / 40.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 1050.0);
        matrixStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale(size, size, size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0f);
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = mouseY;
        entity.setYaw(mouseY);
        entity.setPitch(mouseX);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }
}