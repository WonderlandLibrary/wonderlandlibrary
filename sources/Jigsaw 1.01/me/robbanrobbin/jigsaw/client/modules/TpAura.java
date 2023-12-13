package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.TeleportResult;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnModLinker;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import me.robbanrobbin.jigsaw.pathfinding.Node;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TpAura extends Module {
	WaitTimer timer = new WaitTimer();
	private static EntityLivingBase en = null;
	boolean attack = false;
	double x;
	double y;
	double z;
	double xPreEn;
	double yPreEn;
	double zPreEn;
	double xPre;
	double yPre;
	double zPre;
	int stage = 0;
	
	ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
	ArrayList<Vec3d> positionsBack = new ArrayList<Vec3d>();
	ArrayList<Node> triedPaths = new ArrayList<Node>();
	
	public static final double maxXZTP = 9.8;
	public static final int maxYTP = 9;

	@Override
	public ModSetting[] getModSettings() {
		ModSetting slider2 = new SliderSetting("Max Targets", "TpAuramaxTargets", 1.0, 50.0, ValueFormat.INT);
		ModSetting slider3 = new SliderSetting("Hit Range", "TpAurarange", 6, 200, 0.0, ValueFormat.DECIMAL);
		ModSetting slider1 = new SliderSetting("APS", "TpAuraAPS", 1.0, 20.0, ValueFormat.DECIMAL);
		return new ModSetting[] {
				new CheckBtnModLinker("AutoBlock"),
				new CheckBtnModLinker("Criticals"),
				new CheckBtnModLinker("NoSwing"),
				slider2, slider3, slider1 };
	}

	public TpAura() {
		super("TpAura", Keyboard.KEY_NONE, Category.COMBAT,
				"The original infinite reach :)");
	}

	@Override
	public void onToggle() {
		en = null;
		AuraUtils.targets.clear();
		this.stage = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.xPreEn = 0;
		this.yPreEn = 0;
		this.zPreEn = 0;
		this.attack = false;
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(1000 / ClientSettings.TpAuraAPS, true)) {
			return;
		}
		AuraUtils.targets.clear();
		en = Utils.getClosestEntity((float) ClientSettings.TpAurarange);
		if (en == null) {
			return;
		}
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		updateStages();
		super.onUpdate();
	}

	public void updateStages() {
		
		if(currentMode.equals("Improved")) {
			ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
			if (Jigsaw.java8) {
				list.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.player.getDistanceToEntity(o1) > mc.player.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.player.getDistanceToEntity(o1) < mc.player.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.player.getDistanceToEntity(o1) == mc.player.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			int i = 0;
			for(EntityLivingBase en : list) {
				if(i >= ClientSettings.TpAuramaxTargets) {
					break;
				}
				positions.clear();
				positionsBack.clear();
				triedPaths.clear();
//				Jigsaw.chatMessage(en.posX);
//				Jigsaw.chatMessage(en.posY);
//				Jigsaw.chatMessage(en.posZ);
//				System.out.println(en.posX + "," + en.posY + "," + en.posZ);
				
				TeleportResult result = Utils.pathFinderTeleportTo(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(en.posX, en.posY, en.posZ));
				
				triedPaths = result.triedPaths;
				
				if(!result.foundPath) {
					return;
				}
				
				Vec3d lastPos = result.lastPos;

				Criticals.disable = true;
				Criticals.crit(lastPos.x, lastPos.y, lastPos.z);
				sendPacket(new CPacketUseEntity(en));
				Criticals.disable = false;
				
				mc.player.swingArm(EnumHand.MAIN_HAND);
				
				positions = result.positions;
//				
				TeleportResult resultBack = Utils.pathFinderTeleportBack(positions);
				
				positionsBack = resultBack.positionsBack;
				i++;
			}
		}
		if(currentMode.equals("Old")) {
			positions.clear();
			positionsBack.clear();
			int targets = 0;
			ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
			if (Jigsaw.java8) {
				list.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.player.getDistanceToEntity(o1) > mc.player.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.player.getDistanceToEntity(o1) < mc.player.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.player.getDistanceToEntity(o1) == mc.player.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			for (EntityLivingBase en : list) {
				AuraUtils.targets.add(en);
				boolean up = false;
				positions.clear();
				positionsBack.clear();
				this.en = en;
				doReach(mc.player.getDistanceToEntity(this.en), up, list);
				stage = 0;
				targets++;
				if (targets >= ClientSettings.TpAuramaxTargets) {
					// Jigsaw.chatMessage(targets);
					break;
				}
			}
		}
		if(currentMode.equals("Mineplex Old")) {
			ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
			if (Jigsaw.java8) {
				list.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.player.getDistanceToEntity(o1) > mc.player.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.player.getDistanceToEntity(o1) < mc.player.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.player.getDistanceToEntity(o1) == mc.player.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			int i = 0;
			for(EntityLivingBase en : list) {
				if(i >= ClientSettings.TpAuramaxTargets) {
					break;
				}
				positions.clear();
				BlockPos blockPos = en.getPosition().add(-0.5, 0, -0.5);
				
				if(blockPos.getY() != mc.player.posY) {
					continue;
				}
				double distance = mc.player.getDistance(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
				double stepSize = 0.20;
				double step = stepSize / distance;
				double steps = 0;
				for (int j = 0; j < distance; j++) {
					steps += 1 / stepSize;
					if (stepSize * steps > distance) {
						break;
					}
				}
				
				double targetX = blockPos.getX() + 0.5;
				double targetY = blockPos.getY();
				double targetZ = blockPos.getZ() + 0.5;
				
				for(int j = 0; j < steps; j++) {
					
					double difX = mc.player.posX - targetX;
					double difY = mc.player.posY - targetY;
					double difZ = mc.player.posZ - targetZ;
					
					double divider = step * j;
					
					double posX = mc.player.posX - (difX * divider);
					double posY = mc.player.posY;
					double posZ = mc.player.posZ - (difZ * divider);
					
					Vec3d position = new Vec3d(posX, posY, posZ);
					
					positions.add(position);
					
//					List<AxisAlignedBB> lst = mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.contract(0.01).set(posX, posY, posZ));
//					
//					if(lst.size() > 1) {
////						Jigsaw.chatMessage("Collision! Size: " + lst.size() + ", Index: " + i);
////						return;
//					}
					
				}
				
				for(Vec3d vec : positions) {
					
					double posX = vec.x;
					double posY = vec.y;
					double posZ = vec.z;
					
					sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(posX, posY - 1, posZ), EnumFacing.DOWN, EnumHand.OFF_HAND, 0f, 1f, 0f));
					sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
					
				}
				
				sendPacket(new CPacketHeldItemChange(0));
				
				Vec3d lastPos = positions.get(positions.size() - 1);
				
				Criticals.disable = true;
				Criticals.crit(lastPos.x, lastPos.y, lastPos.z);
				sendPacket(new CPacketUseEntity(en));
				Criticals.disable = false;
				
				mc.player.swingArm(EnumHand.MAIN_HAND);
				
				sendPacket(new CPacketHeldItemChange(7));
				
				Collections.reverse(positions);
				
				for(int j = 0; j < positions.size(); j++) {
					
					Vec3d position = positions.get(j);
					
					double posX = position.x;
					double posY = position.y;
					double posZ = position.z;

					sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(posX, posY - 1, posZ), EnumFacing.DOWN, EnumHand.OFF_HAND, 0f, 1f, 0f));
					sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
					
				}
				i++;
			}
			
		}
		if(currentMode.equals("Mineplex Improved")) {
			ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
			if (Jigsaw.java8) {
				list.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.player.getDistanceToEntity(o1) > mc.player.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.player.getDistanceToEntity(o1) < mc.player.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.player.getDistanceToEntity(o1) == mc.player.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			int i = 0;
			for(EntityLivingBase en : list) {
				if(i >= ClientSettings.TpAuramaxTargets) {
					break;
				}
				if(Math.abs(mc.player.posY - en.posY) > 2) {
					continue;
				}
				positions.clear();
				positionsBack.clear();
				triedPaths.clear();
//				Jigsaw.chatMessage(en.posX);
//				Jigsaw.chatMessage(en.posY);
//				Jigsaw.chatMessage(en.posZ);
//				System.out.println(en.posX + "," + en.posY + "," + en.posZ);
				
				TeleportResult result = Utils.pathFinderTeleportTo_MINEPLEX(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(en.posX, en.posY, en.posZ));
				
				triedPaths = result.triedPaths;
				
				if(!result.foundPath) {
					continue;
				}
				
				Vec3d lastPos = result.lastPos;

//				Criticals.disable = true;
//				Criticals.crit(lastPos.x, lastPos.y, lastPos.z);
//				sendPacket(new CPacketUseEntity(en));
//				Criticals.disable = false;
				
//				mc.player.swingArm(EnumHand.MAIN_HAND);
				
				positions = result.positions;
//				
				TeleportResult resultBack = Utils.pathFinderTeleportBack_MINEPLEX(positions);
				
				positionsBack = resultBack.positionsBack;
				i++;
			}
			
		}
		

	}

	public void doReach(double range, boolean up, ArrayList<EntityLivingBase> list) {
		if (mc.player.getDistanceToEntity(en) <= 4) {
			attack(en);
			return;
		}
		attack = Utils.infiniteReach(range, maxXZTP, maxYTP, positionsBack, positions, en);
	}

	@Override
	public void onLateUpdate() {
		if (!attack) {
			return;
		}
		attack = false;
		super.onLateUpdate();
	}

	@Override
	public void onRender() {

//		GL11.glPushMatrix();
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glBlendFunc(770, 771);
//		GL11.glEnable(GL11.GL_BLEND);
//		RenderTools.lineWidth(2);
//		RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
//		RenderTools.glBegin(3);
//		int i = 0;
//		for (Vec3d vec : positions) {
//			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.x, vec.y, vec.z));
//			i++;
//		}
//		RenderTools.glEnd();
//		RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
//		RenderTools.glBegin(3);
//		i = 0;
//		for (Vec3d vec : positionsBack) {
//			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.x, vec.y, vec.z));
//			i++;
//		}
//		RenderTools.glEnd();
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_LINE_SMOOTH);
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glPopMatrix();
		
//		for(Node node : triedPaths) {
//			BlockPos pos = node.getBlockpos();
//			Vec3d vec = RenderTools.getRenderPos(pos.getX(), pos.getY(), pos.getZ());
//			
//			RenderTools.drawBlockESP(vec.x, vec.y, vec.z, 1f, 1f, 1f, 0.1f, 1f, 1f, 1f, 0.1f, 3f);
//		}
		
		RenderTools.lineWidth(3);
		for (Vec3d vec : positions) {
			drawESP(1f, 0.3f, 0.3f, 1f, vec.x, vec.y, vec.z);
		}
		RenderTools.lineWidth(1.5f);
		for (Vec3d vec : positionsBack) {
			drawESP(0.3f, 0.3f, 1f, 1f, vec.x, vec.y, vec.z);
		}
		
//		for(Node node : triedPaths) {
//			BlockPos pos = node.getBlockpos();
//			Vec3d vec = RenderTools.getRenderPos(pos.getX(), pos.getY(), pos.getZ());
//			EntityRenderer.drawNameplate(mc.fontRenderer, "" + Math.round(node.gCost * 10d) / 10d, (float)vec.x + 0.2f, (float)vec.y + 1f, (float)vec.z + 0.8f, i, mc.player.rotationYaw, mc.player.rotationPitch, false, false);
//			EntityRenderer.drawNameplate(mc.fontRenderer, "" + Math.round(node.hCost * 10d) / 10d, (float)vec.x + 0.2f, (float)vec.y + 1f, (float)vec.z + 0.2f, i, mc.player.rotationYaw, mc.player.rotationPitch, false, false);
//			EntityRenderer.drawNameplate(mc.fontRenderer, "" + Math.round(node.fCost * 10d) / 10d, (float)vec.x + 0.5f, (float)vec.y + 1.3f, (float)vec.z + 0.5f, i, mc.player.rotationYaw, mc.player.rotationPitch, false, false);
//		}
		
		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.player.width / 2, mc.player.height, red, green,
				blue, alpha);
	}

	public static boolean doBlock() {
		return en != null;
	}

	public void sendPacket(boolean goingBack) {
		CPacketPlayer.Position playerPacket = new CPacketPlayer.Position(x, y, z, true);
		sendPacketFinal(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3d(x, y, z));
			return;
		}
		positions.add(new Vec3d(x, y, z));
	}

	private void attackInf(EntityLivingBase en) {
		AutoBlock.stopBlock();
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		sendPacketFinal(new CPacketUseEntity(en));
		Criticals.disable = false;
		mc.player.swingArm(EnumHand.MAIN_HAND);
		AutoBlock.startBlock();

		Utils.createHitParticles(Jigsaw.getModuleByName("Criticals").isToggled(), en);
	}

	private void attack(EntityLivingBase en) {
		AutoBlock.stopBlock();
		sendPacket(new CPacketUseEntity(en));
		mc.player.swingArm(EnumHand.MAIN_HAND);
		AutoBlock.startBlock();

		Utils.createHitParticles(Jigsaw.getModuleByName("Criticals").isToggled(), en);
	}
	
	@Override
	public String[] getModes() {
		return new String[]{"Old", "Improved", "Mineplex Improved", "Mineplex Old"};
	}
	
}