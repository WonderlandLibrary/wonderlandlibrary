package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class Projectiles extends Module {

	private final Tessellator tessellator = Tessellator.getInstance();

	private final Cylinder cylinder = new Cylinder();

	private final float landingSize = 0.5F;

	private boolean landing = true;

	private boolean arrows = true;

	private boolean lines = true;

	public Projectiles() {
		super("Projectiles", Keyboard.KEY_NONE, Category.RENDER, "Shows a line of where arrows are going to land.");
	}

	@Override
	public void onRender() {
		ItemStack item = getApplicableItem();
		if (item != null) {
			int mode = 0;
			float velocity;

			if (item.getItem() instanceof ItemBow) {
				mode = 1;
			}
			else if (item.getItem() instanceof ItemPotion && ((ItemPotion)item.getItem()).isSplash(((ItemPotion)item.getItem()).getMaxDamage())) {
				mode = 2;
			}
			
			float rotationYaw = mc.thePlayer.rotationYaw;
			float rotationPitch = mc.thePlayer.rotationPitch;
			
			double posX = mc.getRenderManager().viewerPosX
					- (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F),
					posY = (mc.getRenderManager().viewerPosY + (double) mc.thePlayer.getEyeHeight())
							- 0.10000000149011612D,
					posZ = mc.getRenderManager().viewerPosZ
							- (double) (MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F),
					motionX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
							* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI))
							* (mode == 1 ? 1.0 : 0.4),
					motionY = (double) (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI))
							* (mode == 1 ? 1.0 : 0.4),
					motionZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
							* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI))
							* (mode == 1 ? 1.0 : 0.4);
			if (mc.thePlayer.getItemInUseCount() <= 0 && mode == 1)
				velocity = 1F;
			else
				velocity = ItemBow.getArrowVelocity((Jigsaw.getModuleByName("FastBow").isToggled() ? mc.thePlayer.getItemInUseCount() : 72000 - mc.thePlayer.getItemInUseCount()));
			renderProjectile(mode, velocity, posX, posY, posZ, motionX, motionY, motionZ, null);
		}
		if (arrows) {
			for (Object o : mc.theWorld.loadedEntityList) {
				if (o instanceof EntityArrow) {
					EntityArrow entity = (EntityArrow) o;
					if (entity.isDead || entity.ticksInGround > 10)
						continue;
					renderProjectile(1, -1, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY,
							entity.motionZ, entity.shootingEntity != null ? entity.shootingEntity.getName() : null);
				}
			}
		}
	}

	private void renderProjectile(int mode, float velocity, double x, double y, double z, double motionX,
			double motionY, double motionZ, String text) {
		if (velocity != -1) {
			float theta = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
			motionX /= (double) theta;
			motionY /= (double) theta;
			motionZ /= (double) theta;
			motionX *= (mode == 1 ? (velocity * 2) : 1) * getMult(mode);
			motionY *= (mode == 1 ? (velocity * 2) : 1) * getMult(mode);
			motionZ *= (mode == 1 ? (velocity * 2) : 1) * getMult(mode);
		}
		boolean hasLanded = false, isEntity = false;
		MovingObjectPosition collision = null;
		float size = mode == 1 ? 0.3F : 0.25F;
		float gravity = getGravity(mode);

//		if (text != null) {
//			GlStateManager.pushMatrix();
//			RenderTools.prepareBillboarding((float) (x - mc.getRenderManager().viewerPosX),
//					(float) (y - mc.getRenderManager().viewerPosY), (float) (z - mc.getRenderManager().viewerPosZ),
//					true);
//			GlStateManager.enableTexture2D();
//			mc.fontRendererObj.drawStringWithShadow(text, -mc.fontRendererObj.getStringWidth(text) / 2, 1, 0xFFFFFFF);
//			GlStateManager.disableTexture2D();
//			GlStateManager.popMatrix();
//		}
		
		ArrayList<Vec3> positions = new ArrayList<Vec3>();
		
		Entity hitEntity = null;
		


		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);

		if(hitEntity != null) {
			RenderTools.color4f(1f, 0.3f, 0.3f, 0.7f);
		}
		else {
			RenderTools.color4f(0.3f, 1f, 1f, 0.7f);
		}
		
		RenderTools.lineWidth(2);
		RenderTools.glBegin(3);
		
		for (; !hasLanded && y > 0;) {
			Vec3 present = new Vec3(x, y, z);
			Vec3 future = new Vec3(x + motionX, y + motionY, z + motionZ);
			MovingObjectPosition possibleCollision = mc.theWorld.rayTraceBlocks(present, future, false, true, false);
			if (possibleCollision != null) {
				hasLanded = true;
				collision = possibleCollision;
				if(hitEntity == null && possibleCollision.entityHit != null) {
					hitEntity = possibleCollision.entityHit;
				}
			}
			AxisAlignedBB boundingBox = new AxisAlignedBB(x - size, y - size, z - size, x + size, y + size, z + size);

			List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer,
					boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			for (int index = 0; index < entities.size(); ++index) {
				Entity entity = entities.get(index);

				if (entity.canBeCollidedWith() && entity != mc.thePlayer) {
					AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D);
					MovingObjectPosition entityCollision = entityBoundingBox.calculateIntercept(present, future);
					if (entityCollision != null) {
						hasLanded = true;
						isEntity = true;
						collision = entityCollision;
						if(hitEntity == null && entityCollision.entityHit != null) {
							hitEntity = entityCollision.entityHit;
						}
					}
				}
			}

			x += motionX;
			y += motionY;
			z += motionZ;
			float motionAdjustment = 0.99F;
			boolean inWater = isInMaterial(boundingBox, Material.water);
			if (inWater) {
				motionAdjustment = 0.6F;
			}

			motionX *= motionAdjustment;
			motionY *= motionAdjustment;
			motionZ *= motionAdjustment;
			motionY -= gravity;
			if (lines) {
				if(inWater) {
					RenderTools.color4f(1f, 0.3f, 0.3f, 0.7f);
				}
				else {
					RenderTools.color4f(0.3f, 1f, 1f, 0.7f);
				}
				RenderTools.putVertex3d(new Vec3(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY,
						z - mc.getRenderManager().viewerPosZ));
			}
		}
		
		for(Vec3 vec : positions) {
			RenderTools.putVertex3d(vec);
		}

		RenderTools.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		
		if (landing) {
			GlStateManager.pushMatrix();
			if (collision != null) {
				if(hitEntity != null) {
					Vec3 vec = RenderTools.getRenderPos(hitEntity.posX, hitEntity.posY, hitEntity.posZ);
					RenderTools.drawSolidEntityESP(vec.xCoord, vec.yCoord, vec.zCoord, hitEntity.width / 2, hitEntity.height, 1f, 1f, 1f, 1f);
				}
				if(collision.hitVec != null) {
					Block block = Utils.getBlock(collision.getBlockPos());
					
					if(block instanceof BlockAir) {
						GlStateManager.popMatrix();
						return;
					}
					
					Vec3 vec = RenderTools.getRenderPos(collision.getBlockPos().getX(), collision.getBlockPos().getY(), collision.getBlockPos().getZ());
					RenderTools.drawSolidBlockESP(vec.xCoord, vec.yCoord, vec.zCoord, 0.3f, 1f, 1f, 0.2f);
				}
			}
			GlStateManager.popMatrix();
		}
	}

	/**
	 * Finds an item, checking both hands
	 *
	 * todo - maybe have a check if the player has arrows? if no arrows, ignore
	 * bow
	 */
	private ItemStack getApplicableItem() {
		if(Jigsaw.getModuleByName("FastBow").isToggled()) {
			return mc.thePlayer.getHeldItem();
		}
		else {
			if (mc.thePlayer.getItemInUse() != null && isThrowable(mc.thePlayer.getItemInUse().getItem()))
				return mc.thePlayer.getItemInUse();
			ItemStack main = mc.thePlayer.getItemInUse();
			if (main != null && isThrowable(main.getItem()))
				return main;
		}
		return null;
	}

	private boolean isThrowable(Item item) {
		return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEnderPearl
				|| item instanceof ItemEgg ||  (item instanceof ItemPotion && ((ItemPotion)item).isSplash(((ItemPotion)item).getMaxDamage()));
	}

	private float getMult(int mode) {
		return mode == 2 ? 0.5F : 1.5F;
	}

	private float getGravity(int mode) {
		return mode >= 1 ? 0.05F : 0.03F;
	}

	private void renderPoint() {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		renderer.pos(-landingSize, 0, 0).endVertex();
		renderer.pos(0, 0, 0).endVertex();
		renderer.pos(0, 0, -landingSize).endVertex();
		renderer.pos(0, 0, 0).endVertex();

		renderer.pos(landingSize, 0, 0).endVertex();
		renderer.pos(0, 0, 0).endVertex();
		renderer.pos(0, 0, landingSize).endVertex();
		renderer.pos(0, 0, 0).endVertex();
		tessellator.draw();
	}

	private boolean isInMaterial(AxisAlignedBB axisalignedBB, Material material) {
		int chunkMinX = MathHelper.floor_double(axisalignedBB.minX);
		int chunkMaxX = MathHelper.floor_double(axisalignedBB.maxX + 1.0D);
		int chunkMinY = MathHelper.floor_double(axisalignedBB.minY);
		int chunkMaxY = MathHelper.floor_double(axisalignedBB.maxY + 1.0D);
		int chunkMinZ = MathHelper.floor_double(axisalignedBB.minZ);
		int chunkMaxZ = MathHelper.floor_double(axisalignedBB.maxZ + 1.0D);

		StructureBoundingBox structureBoundingBox = new StructureBoundingBox(chunkMinX, chunkMinY, chunkMinZ, chunkMaxX,
				chunkMaxY, chunkMaxZ);
		if (!mc.theWorld.isAreaLoaded(structureBoundingBox)) {
			return false;
		} else {
			boolean isWithin = false;
			for (int x = chunkMinX; x < chunkMaxX; ++x) {
				for (int y = chunkMinY; y < chunkMaxY; ++y) {
					for (int z = chunkMinZ; z < chunkMaxZ; ++z) {
						IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(x, y, z));
						Block block = blockState.getBlock();
						if (block != null && block.getMaterial() == material) {
							double liquidHeight = (double) ((float) (y + 1) - BlockLiquid
									.getLiquidHeightPercent((Integer) blockState.getValue(BlockLiquid.LEVEL)));
							if ((double) chunkMaxY >= liquidHeight)
								isWithin = true;
						}
					}
				}
			}
			return isWithin;
		}
	}

}
