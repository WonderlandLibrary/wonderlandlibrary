package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

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
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	ArrayList<AbstractPacket> packets = new ArrayList<AbstractPacket>();
	ArrayList<EntityLivingBase> hit = new ArrayList<EntityLivingBase>();
	public static final double maxTP = 9.5;
	public static final int maxYTP = 9;

	@Override
	public Component[] getModSettings() {
		final BasicCheckButton box1 = new BasicCheckButton("Infinite Mode Multi-Target");
		box1.setSelected(ClientSettings.TpAuramulti);
		box1.addButtonListener(new ButtonListener() {

			@Override
			public void onRightButtonPress(Button button) {

			}

			@Override
			public void onButtonPress(Button button) {
				ClientSettings.TpAuramulti = box1.isSelected();
			}
		});
		BasicSlider slider1 = new BasicSlider("Multi-Target Max Targets", ClientSettings.TpAuramaxTargets, 1, 50, 0,
				ValueDisplay.INTEGER) {
			@Override
			public void update() {

				super.update();
				this.setVisible(box1.isSelected());
			}
		};
		slider1.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {
				ClientSettings.TpAuramaxTargets = (int) Math.round(slider.getValue());
			}
		});
		BasicSlider slider2 = new BasicSlider("Infinite Mode Hit Range", ClientSettings.TpAurarange, 1, 200, 0,
				ValueDisplay.DECIMAL);
		slider2.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {
				ClientSettings.TpAurarange = (float) slider.getValue();
			}
		});
		BasicSlider slider3 = new BasicSlider("Infinite Mode APS", ClientSettings.TpAuraAPS, 1, 20, 0,
				ValueDisplay.DECIMAL);
		slider3.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {
				ClientSettings.TpAuraAPS = slider.getValue();
			}
		});
		return new Component[] { slider2, slider3, box1, slider1 };
	}

	public TpAura() {
		super("TpAura", Keyboard.KEY_NONE, Category.COMBAT,
				"The home of infinite reach, also various modes that teleport you and attack entities");
	}

	@Override
	public void onToggle() {
		en = null;
		hit.clear();
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
		hit.clear();

		if (this.currentMode.equals("Infinite")) {
			if (!timer.hasTimeElapsed((int) Math.round((1000 / ClientSettings.TpAuraAPS)), true)) {
				return;
			}
		}
		AuraUtils.targets.clear();
		if (this.currentMode.equals("Infinite")) {
			en = Utils.getClosestEntity((float) ClientSettings.TpAurarange);
		} else {
			en = Utils.getClosestEntity((float) AuraUtils.getRange());
		}
		if (en == null) {
			return;
		}
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		if (currentMode.equalsIgnoreCase("Circle")) {
			mc.thePlayer.setPosition(en.posX + rand.nextInt(4) - 2, en.posY, en.posZ + rand.nextInt(4) - 2);
		}
		if (currentMode.equalsIgnoreCase("Behind")) {
			double angleA = Math.toRadians(Utils.normalizeAngle(en.rotationYawHead - 90));
			mc.thePlayer.setPosition(en.posX + Math.cos(angleA) * 2, en.posY, en.posZ + Math.sin(angleA) * 2);
			if (!mc.thePlayer.onGround && mc.thePlayer.getDistanceToEntity(en) <= AuraUtils.getRange()) {
				mc.thePlayer.motionY = 0;
			}
		}
		if (this.currentMode.equals("Infinite")) {
			updateStages();
		}
		super.onUpdate();
	}

	public void updateStages() {
		hit.clear();
		if (ClientSettings.TpAuramulti) {
			positions.clear();
			positionsBack.clear();
			int targets = 0;
			ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
			if (Jigsaw.java8) {
				list.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.thePlayer.getDistanceToEntity(o1) > mc.thePlayer.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) < mc.thePlayer.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) == mc.thePlayer.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			for (EntityLivingBase en : list) {
				for (EntityLivingBase en1 : hit) {
					if (en.isEntityEqual(en1)) {
						continue;
					}
				}
				AuraUtils.targets.add(en);
				boolean up = false;
				positions.clear();
				positionsBack.clear();
				this.en = en;
				doReach(mc.thePlayer.getDistanceToEntity(this.en), up, list);
				stage = 0;
				targets++;
				if (targets >= ClientSettings.TpAuramaxTargets) {
					// Jigsaw.chatMessage(targets);
					break;
				}
			}
		} else {

			positions.clear();
			positionsBack.clear();
			if (en == null) {
				return;
			}
			boolean up = false;
			doReach(mc.thePlayer.getDistanceToEntity(this.en), up, new ArrayList<EntityLivingBase>());
			stage = 0;

		}

	}

	public void doReach(double range, boolean up, ArrayList<EntityLivingBase> list) {
		if (mc.thePlayer.getDistanceToEntity(en) <= 4) {
			attack(en);
			return;
		}
		attack = true;
		packets.clear();
		boolean tpUpOneBlock = false;
		double step = maxTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxTP * steps);
			if (maxTP * steps > range) {
				break;
			}
		}
		int ind = 0;
		xPreEn = en.posX;
		yPreEn = en.posY;
		zPreEn = en.posZ;
		xPre = mc.thePlayer.posX;
		yPre = mc.thePlayer.posY;
		zPre = mc.thePlayer.posZ;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		boolean sneaking = mc.thePlayer.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		MovingObjectPosition rayTrace = null;
		MovingObjectPosition rayTrace1 = null;
		MovingObjectPosition rayTraceCarpet = null;
		if ((rayTrace(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(en.posX, en.posY, en.posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
					new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ),
							new Vec3(en.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
							true)) != null) {
				MovingObjectPosition trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.thePlayer.posY;
					// yPreEn = mc.thePlayer.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							MovingObjectPosition tr = rayTracePos(
									new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
									new Vec3(en.posX, target.getY() + i, en.posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							Block block = mc.theWorld.getBlockState(blockPos).getBlock();
							if (block.getMaterial() != Material.air) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.thePlayer.posX - xPreEn;
						double difZ = mc.thePlayer.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return;
						}
					} else {
						attack = false;
						return;
					}
				}
			} else {
				MovingObjectPosition ent = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY, en.posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.thePlayer.posY;
					yPreEn = mc.thePlayer.posY;
				} else {
					y = mc.thePlayer.posY;
					yPreEn = en.posY;
				}

			}
		}
		if (!attack) {
			return;
		}
		if (sneaking) {
			sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.thePlayer.posX;
				y = yPreEn;
				z = mc.thePlayer.posZ;
				sendPacket(false);
			}
			if (i != steps - 1) {
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false);
			} else {
				// if last teleport
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - en.posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false);
				}
				if (Math.abs(yDist) < maxTP && mc.thePlayer.getDistanceToEntity(en) >= 4) {
					x = xPreEn;
					y = en.posY;
					z = zPreEn;
					sendPacket(false);
					if (Jigsaw.getModuleByName("MegaKnockback").isToggled() && en.onGround) {
						for (int ii = 0; ii < 300; ii++) {
							sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
						}
					}
					attackInf(en);
					if (!list.isEmpty()) {
						for (EntityLivingBase en : list) {
							float f = (float) (this.x - en.posX);
							float f1 = (float) (this.y - en.posY);
							float f2 = (float) (this.z - en.posZ);
							float distance = MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
							if (distance <= 4) {
								attackInf(en);
							}
						}
					}
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(true);
		}
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		sendPacket(true);
		if (!attack) {
			if (sneaking) {
				sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			packets.clear();
			return;
		}
		for (int i = 0; i < packets.size() - 1; i++) {
			// AbstractPacket packet = packets.get(i);
			// if(packet instanceof C02PacketUseEntity) {
			// AutoBlock.stopBlock();
			// mc.thePlayer.swingItem();
			// //Jigsaw.chatMessage(en.hurtResistantTime);
			// if(!(en.hurtResistantTime > 12) || en.hurtResistantTime == 0) {
			// Criticals.crit(x, y, z);
			// }
			// Criticals.disable = true;
			// sendPacketFinal(packet);
			// Criticals.disable = false;
			// AutoBlock.startBlock();
			// }
			// else {
			// Jigsaw.chatMessage(((C06PacketPlayerPosLook)packet).y);
			// sendPacketFinal(packet);
			// }
		}
		packets.clear();
		if (sneaking) {
			sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
	}

	@Override
	public void onLateUpdate() {
		if (!attack) {
			return;
		}
		attack = false;
		if (currentMode.equalsIgnoreCase("Circle")) {
			if (!mc.thePlayer.onGround && mc.thePlayer.getDistanceToEntity(en) <= AuraUtils.getRange()) {
				mc.thePlayer.motionY = 0;
			}
			attack(en);
		}
		if (currentMode.equalsIgnoreCase("Behind")) {
			attack(en);
		}
		super.onLateUpdate();
	}

	@Override
	public void onRender() {
		if (currentMode.equals("Infinite")) {

			// GL11.glPushMatrix();
			// GL11.glEnable(GL11.GL_BLEND);
			// GL11.glEnable(GL11.GL_LINE_SMOOTH);
			// GL11.glDisable(GL11.GL_TEXTURE_2D);
			// GL11.glDisable(GL11.GL_DEPTH_TEST);
			// GL11.glBlendFunc(770, 771);
			// GL11.glEnable(GL11.GL_BLEND);
			// RenderTools.lineWidth(2);
			// RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
			// RenderTools.glBegin(3);
			// int i = 0;
			// for (Vec3 vec : positions) {
			// RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord,
			// vec.yCoord, vec.zCoord));
			// i++;
			// }
			// RenderTools.glEnd();
			// RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
			// RenderTools.glBegin(3);
			// i = 0;
			// for (Vec3 vec : positionsBack) {
			// RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord,
			// vec.yCoord, vec.zCoord));
			// i++;
			// }
			// RenderTools.glEnd();
			// GL11.glDisable(GL11.GL_BLEND);
			// GL11.glEnable(GL11.GL_TEXTURE_2D);
			// GL11.glDisable(GL11.GL_LINE_SMOOTH);
			// GL11.glDisable(GL11.GL_BLEND);
			// GL11.glEnable(GL11.GL_DEPTH_TEST);
			// GL11.glPopMatrix();
			// RenderTools.lineWidth(3);
			// for (Vec3 vec : positions) {
			// drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
			// }
			// RenderTools.lineWidth(1.5f);
			// for (Vec3 vec : positionsBack) {
			// drawESP(0.3f, 0.3f, 1f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
			// }
		}
		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}

	public static boolean doBlock() {
		return en != null;
	}

	public boolean rayTrace(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox,
			boolean returnLastUncollidableBlock) {
		float[] rot = Utils.getFacePosEntity(en);
		double angleA = Math.toRadians(Utils.normalizeAngle(rot[0]));
		double angleB = Math.toRadians(Utils.normalizeAngle(rot[0]) + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (returnLastUncollidableBlock) {
			return (trace1 != null && Utils.getBlock(trace1.getBlockPos()).getMaterial() != Material.air)
					|| (trace2 != null && Utils.getBlock(trace2.getBlockPos()).getMaterial() != Material.air)
					|| (trace3 != null && Utils.getBlock(trace3.getBlockPos()).getMaterial() != Material.air)
					|| (trace4 != null && Utils.getBlock(trace4.getBlockPos()).getMaterial() != Material.air)
					|| (trace5 != null && Utils.getBlock(trace5.getBlockPos()).getMaterial() != Material.air);
		} else {
			return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
		}

	}

	@SuppressWarnings("unused")
	public MovingObjectPosition rayTracePos(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		float[] rot = Utils.getFacePosEntity(en);
		double angleA = Math.toRadians(Utils.normalizeAngle(rot[0]));
		double angleB = Math.toRadians(Utils.normalizeAngle(rot[0]) + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		if (false) {
			MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
					ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
			return trace2;
		}
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		// positionsBack.add(rightA);
		// positionsBack.add(right2A);
		// positionsBack.add(leftA);
		// positionsBack.add(left2A);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace5 != null) {
			if (returnLastUncollidableBlock) {
				if (trace5 != null && (Utils.getBlock(trace5.getBlockPos()).getMaterial() != Material.air
						|| trace5.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace5;
				}
				if (trace4 != null && (Utils.getBlock(trace4.getBlockPos()).getMaterial() != Material.air
						|| trace4.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace4;
				}
				if (trace3 != null && (Utils.getBlock(trace3.getBlockPos()).getMaterial() != Material.air
						|| trace3.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null && (Utils.getBlock(trace1.getBlockPos()).getMaterial() != Material.air
						|| trace1.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null && (Utils.getBlock(trace2.getBlockPos()).getMaterial() != Material.air
						|| trace2.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			} else {
				if (trace5 != null) {
					return trace5;
				}
				if (trace4 != null) {
					return trace4;
				}
				if (trace3 != null) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			}
		}
		if (trace2 == null) {
			if (trace3 == null) {
				if (trace1 == null) {
					if (trace5 == null) {
						if (trace4 == null) {
							return null;
						}
						return trace4;
					}
					return trace5;
				}
				return trace1;
			}
			return trace3;
		}
		return trace2;
	}

	public void sendPacket(boolean goingBack) {
		C04PacketPlayerPosition playerPacket = new C04PacketPlayerPosition(x, y, z, true);
		sendPacketFinal(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3(x, y, z));
			return;
		}
		positions.add(new Vec3(x, y, z));
	}

	private void attackInf(EntityLivingBase en) {
		hit.add(en);
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		sendPacketFinal(new C02PacketUseEntity(en, Action.ATTACK));
		Criticals.disable = false;
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}

	private void attack(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}

	@Override
	public String[] getModes() {
		return new String[] { "Circle", "Behind", "Infinite" };
	}
}