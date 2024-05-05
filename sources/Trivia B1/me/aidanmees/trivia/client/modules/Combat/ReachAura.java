package me.aidanmees.trivia.client.modules.Combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.target.AuraUtils;
import me.aidanmees.trivia.client.tools.RenderTools;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.module.Module;
import me.aidanmees.trivia.pathfinding.Node;
import me.aidanmees.trivia.pathfinding.NodeProcessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class ReachAura extends Module {
	WaitTimer timerInf = new WaitTimer();
	private static EntityLivingBase en = null;
	private boolean HasTPED = false;
	public static final double maxTP = 9.9;
	private PathFinder pathFinder = new PathFinder(new WalkNodeProcessor());
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	ArrayList<Node> triedPaths = new ArrayList<Node>();
	
	private WaitTimer timer = new WaitTimer();

	public ReachAura() {
		super("ReachAura", Keyboard.KEY_NONE, Category.COMBAT, "Dank pathfind tpaura");
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onToggle() {
		
		AuraUtils.targets.clear();
		positions.clear();
		positionsBack.clear();

		super.onToggle();
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
		if ((trivia.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if(timer.hasTimeElapsed(2500, true)){
            
            
		
		                timer.reset();
		            }
	
		AuraUtils.targets.clear();
		en = Utils.getClosestEntity(200);
		if (en == null) {
			return;
		}
		if (!KillauraBETA.IsntMineplexBot(en)) {
			return;
		}
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		

		if (!timerInf.hasTimeElapsed(1000 / 10, true)) {
			return;
		}

		for (Vec3 vec : positions) {
HasTPED = true;
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(vec.xCoord, vec.yCoord,
					vec.zCoord, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
			
		}
		if (HasTPED) {
			attack(en);
			HasTPED = false;
		}

		
			
		
		for (Vec3 vec : positionsBack) {

			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(vec.xCoord, vec.yCoord,
					vec.zCoord, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));

		

		}
		updateStages();
		// event.cancel();
		super.onUpdate();
	}

	public void updateStages() {
		
		if (AuraUtils.hasEntity(en)) {
		
		
		NodeProcessor processor = new NodeProcessor();
		positions.clear();
		positionsBack.clear();
		processor.getPath(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new BlockPos(en.posX, en.posY, en.posZ));
		triedPaths = processor.triedPaths;
		for (Node node : processor.path) {
			BlockPos pos = node.getBlockpos();
			// sendPacket(new
			// C03PacketPlayer.C04PacketPlayerPosition(node.getBlockpos().getX() + 0.5,
			// node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
			
			positions.add(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
			AuraUtils.targets.clear();
		}
		// sendPacket(new C02PacketUseEntity(en, C02PacketUseEntity.Action.ATTACK));
		// Go back!
		for (int i = positions.size() - 1; i > -1; i--) {
			// sendPacket(new
			// C03PacketPlayer.C04PacketPlayerPosition(positions.get(i).xCoord,
			// positions.get(i).yCoord, positions.get(i).zCoord, true));
			positionsBack.add(positions.get(i));
		}
		}
		
	}

	public static boolean doBlock() {
		return en != null;
	}

	@Override
	public void onRender() {
		
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(GL11.GL_BLEND);
			RenderTools.lineWidth(2);
			RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
			RenderTools.glBegin(3);
			int i = 0;
			for (Vec3 vec : positions) {
				RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
				i++;
			}
			RenderTools.glEnd();
			RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
			RenderTools.glBegin(3);
			i = 0;
			for (Vec3 vec : positionsBack) {
				RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
				i++;
			}
			RenderTools.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
			RenderTools.lineWidth(3);
			for (Vec3 vec : positions) {
				drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
			}
			RenderTools.lineWidth(1.5f);
			for (Vec3 vec : positionsBack) {
				drawESP(0.3f, 1f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);

				super.onRender();
			}
		
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}
	@Override
	public ModSetting[] getModSettings() {

		
		
		

		CheckBtnSetting players = new CheckBtnSetting("Team", "TEAMMM");
		
	     
		
		
		return new ModSetting[] { players};
	}


}
