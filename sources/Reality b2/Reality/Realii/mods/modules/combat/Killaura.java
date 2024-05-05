package Reality.Realii.mods.modules.combat;

import Reality.Realii.event.events.world.*;
import Reality.Realii.utils.cheats.RenderUtills.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.player.Teams;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.mods.modules.world.Scaffold;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.player.RayCastShit;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.AnimationUtils;
import Reality.Realii.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {

	public static EntityLivingBase target;
	float[] roationscal;

	public float Fixyaw;
	public boolean SwingRange;
	private Option mob = new Option("Mob", false);
	private Option raycast = new Option("RayCast", false);
	private Option Delay = new Option("1.9", false);
	public static Option GayFix = new Option("Move Fix", false);
	private Option animals = new Option("Animals", false);
	private Option player = new Option("Player", true);
	private Option invisible = new Option("Invisible", false);
	private Option Rotaions = new Option("Rotations", true);
	private Option CombatDelay = new Option("1.9 Combat delay", false);
	private Option OnWorld = new Option("Disable On Worldchange", true);
	public static float yaw, pitch;
	public float[] rotse = new float[2];
	private Option smart = new Option("SmartSwitch", false);
	private Option lookAtTheClosestPoint = new Option("asdasd", true);
	private Option KeepSprint = new Option("KeepSprint", false);
	private Option Smart = new Option("Smart", false);
	private Option noSwing = new Option("NoSwing", false);
	private Option esp = new Option("Esp", true);
	private Option Chinaesp = new Option("ChinaEsp", true);
	private Option Autusim = new Option("Autism", true);
	private Option MMc = new Option("MMC Reach", true);
	private TimerUtil Sugi = new TimerUtil();
	private Mode Reachdisplay = new Mode("RangeArea", "RangeArea", new String[]{"On", "Off"}, "On");
	private Mode mode = new Mode("Mode", "Mode", new String[]{"Switch", "Single", "Multi"}, "Switch");

	private Mode priority = new Mode("Priority", "Priority", new String[]{"Distance", "Health", "Direction"}, "Distance");
	public static Mode blockhit = new Mode("autoblock", "autoblock", new String[]{"Vannila", "Vannila2","Intave", "Vannila3", "Uncp", "Taco", "Interact", "None", "Fake", "Watchdog", "Verus", "Sex", "Legit"}, "None");
	private Mode boxesp12 = new Mode("BoxEsp", "BoxEsp", new String[]{"On", "off",}, "off");
	private Mode Gofyyahh = new Mode("GofyyahhBy", "Gofyyahhh", new String[]{"On", "off", "Vulcan"}, "off");
	private Mode prepost = new Mode("KillauraHitEvent", "KillauraHitEvent", new String[]{"OnPre","Legit", "OnPost",}, "OnPre");
	private Mode BoxEspColor = new Mode("BoxEspColort", "BoxEspColort", new String[]{"Client", "White", "Black", "Green", "Rainbow"}, "Client");
	private Mode RnagerCircleColor = new Mode("CircleColor", "CircleColor", new String[]{"Client", "Rainbow"}, "Client");

	public static Numbers<Number> RainbowSpeed = new Numbers<>("RainbowSpeed", 45f, 5f, 255f, 100f);

	private float randomYaw, randomPitch;
	private Numbers<Number> switchDelay = new Numbers<>("SwitchDelay", 150, 10, 2000, 10);
	private Numbers<Number> rotSpeedMax = new Numbers<>("RotationSpeed", 80, 1, 100, 1);
	private Numbers<Number> rotSpeedMin = new Numbers<>("RotationSpeed", 80, 1, 100, 1);
	private Numbers<Number> Maxcps = new Numbers<>("MaxCPS", 11, 1, 20, 0.1);
	private Numbers<Number> minimumcps = new Numbers<>("MinimumCPS", 11, 1, 20, 0.1);
	private Numbers<Number> range = new Numbers<>("Range", 3.8, 1, 6, 0.1);
	//30
	private Numbers<Number> targets = new Numbers<>("Targets", 3, 1, 6, 1);

	static ArrayList<EntityLivingBase> curTargets = new ArrayList<>();

	private TimerUtil timer = new TimerUtil();
	private TimerUtil Startdelay = new TimerUtil();
	private TimerUtil cpsTimer = new TimerUtil();
	private AnimationUtils animationUtils1 = new AnimationUtils();
	private AnimationUtils animationUtils2 = new AnimationUtils();
	private AnimationUtils espAnimation = new AnimationUtils();

	private float yPos = 0;
	private boolean direction;
	private int cur = 0;
	private float KillCps = 0;
	private double y3 = 0;
	private Criticals crit;
	int Asiancolor;
	int rainbowTick = 0;

	public Killaura() {
		super("KillAura", ModuleType.Combat);
		addValues(priority, Chinaesp, GayFix, esp, Maxcps, minimumcps, lookAtTheClosestPoint, KeepSprint, range,
				Gofyyahh, targets, mob, animals, player, invisible, switchDelay, rotSpeedMax, rotSpeedMin, mode,
				smart, noSwing, Autusim, blockhit, prepost, boxesp12, BoxEspColor, Reachdisplay, RainbowSpeed,
				RnagerCircleColor, raycast, CombatDelay, Rotaions, OnWorld,MMc);
	}

	@Override
	public void onEnable() {
		SwingRange = false;
		this.rotse = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
		super.onEnable();
		Startdelay.reset();
		crit = (Criticals) ModuleManager.getModuleByClass(Criticals.class);
	}

	@EventHandler
	private void onRender3D(EventRender3D e) {

		if (target != null && ((boolean) esp.getValue())) {

			int i = 0;
			Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
			Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
			Pair<Color, Color> colors = Pair.of(startColor, endColor);
			Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
			Render2.drawCircle(target, e.getPartialTicks(), 0.65f, c.getRGB());

			//or
			//68
			//69
		}

		if (Reachdisplay.getValue().equals("On")) {
			this.drawRange(mc.thePlayer, e.getPartialTicks(), this.range.getValue().floatValue());
		}

	}

	@EventHandler
	public void onRenderChina(EventRender3D eventRender) {

		if (target != null && ((boolean) Chinaesp.getValue())) {

			if (!target.isInvisible()) {
				float x = (float) ((float) (target.lastTickPosX
						+ (target.posX - target.lastTickPosX) * eventRender.getPartialTicks())
						- mc.getRenderManager().renderPosX);
				float y = (float) (((float) (target.lastTickPosY
						+ (target.posY - target.lastTickPosY) * eventRender.getPartialTicks()) + 2.1
						- mc.getRenderManager().renderPosY));
				float z = (float) ((float) (target.lastTickPosZ
						+ (target.posZ - target.lastTickPosZ) * eventRender.getPartialTicks())
						- mc.getRenderManager().renderPosZ);
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glDepthMask(false);
				GL11.glLineWidth(0.6F);
				// GL11.glLineWidth(0.6F); size
				GL11.glBegin(1);
				Color color = null;

				int i3 = 0;
				Color startColor = new Color(ClientSettings.r.getValue().intValue(),
						ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
				Color endColor = new Color(ClientSettings.r2.getValue().intValue(),
						ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
				Pair<Color, Color> colors = Pair.of(startColor, endColor);
				Color color1 = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(),
						colors.getSecond(), false);

				for (double i = 0; i <= 360; i = i + 0.1) {
					GL11.glColor4f(color1.getRed() / 255.0F, color1.getGreen() / 255.0F, color1.getBlue() / 255.0F,
							0.2F);
					GL11.glVertex3d(x + Math.sin(i * Math.PI / 180.0D) * 0.7, y - 0.3D,
							z + Math.cos(i * Math.PI / 180.0D) * 0.7);
					GL11.glVertex3d(x, y + 0.05D, z);
				}

				GL11.glEnd();
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	private void drawRange(Entity entity, float partialTicks, double rad) {
		ScaledResolution sr = new ScaledResolution(mc);
		float x1 = sr.getScaledWidth(), y1 = 0;
		float arrayListY = y1;
		GL11.glPushMatrix();
		GL11.glDisable((int) 3553);
		RenderUtil.startSmooth();
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glLineWidth((float) 2);
		GL11.glBegin((int) 3);
		double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) partialTicks
				- mc.getRenderManager().viewerPosX;
		double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) partialTicks
				- mc.getRenderManager().viewerPosY;
		double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) partialTicks
				- mc.getRenderManager().viewerPosZ;

		double pix2 = Math.PI * 2;
		int tick = 0;

		for (int i = 0; i <= 45; ++i) {
			if (this.RnagerCircleColor.getValue().equals("Client")) {

				Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(),
						ClientSettings.b.getValue().intValue());
				GL11.glColor3f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f);
				GL11.glVertex3d((double) (x + rad * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y,
						(double) (z + rad * Math.sin((double) i * (Math.PI * 2) / 45.0)));
				tick += 1;
			}

			if (this.boxesp12.getValue().equals("Rainbow")) {

				Color c = new Color(Color.HSBtoRGB(
						(float) ((double) Client.instance.mc.thePlayer.ticksExisted / RainbowSpeed.getValue().intValue()
								+ Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f,
						1f, 1f));
				GL11.glColor3f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f);
				GL11.glVertex3d((double) (x + rad * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y,
						(double) (z + rad * Math.sin((double) i * (Math.PI * 2) / 45.0)));
				tick += 1;
			}
		}
		GL11.glEnd();
		GL11.glDepthMask((boolean) true);
		GL11.glEnable((int) 2929);
		RenderUtil.endSmooth();
		GL11.glEnable((int) 3553);
		GL11.glPopMatrix();
	}

	TimerUtil timerUtil = new TimerUtil();

	@EventHandler
	private void onRender2D(EventRender2D e) {
		if (timerUtil.delay(20)) {
			if (direction) {
				yPos += 0.03;
				if (2 - yPos < 0.02) {
					direction = false;
				}
			} else {
				yPos -= 0.03;
				if (yPos < 0.02) {
					direction = true;
				}
			}
			timerUtil.reset();
		}
	}


	@EventHandler
	public void onRender(EventRender3D event) {
		if (target != null && ((boolean) this.boxesp12.getValue().equals("On"))) {

			this.doBoxESP(event);

		}
		if (this.boxesp12.getValue().equals("Off")) {

		}

	}

	private void doBoxESP(EventRender3D event) {
		if (target != null && ((boolean) this.boxesp12.getValue().equals("On"))) {
			ScaledResolution sr = new ScaledResolution(mc);
			float x1 = sr.getScaledWidth(), y1 = 0;
			float arrayListY = y1;

			GL11.glBlendFunc(770, 771);
			GL11.glEnable(3042);
			GL11.glEnable(2848);
			GL11.glLineWidth(2.0f);
			GL11.glDisable(3553);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);

			if (this.BoxEspColor.getValue().equals("Client")) {
				int i3 = 0;
				Color startColor = new Color(ClientSettings.r.getValue().intValue(),
						ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), 70);
				Color endColor = new Color(ClientSettings.r2.getValue().intValue(),
						ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue(), 70);
				Pair<Color, Color> colors = Pair.of(startColor, endColor);
				entityESPBox2(Killaura.target, Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(),
						colors.getSecond(), false), event);
			}

			if (this.BoxEspColor.getValue().equals("Rainbow")) {
				Color c = new Color(Color.HSBtoRGB(
						(float) ((double) Client.instance.mc.thePlayer.ticksExisted / RainbowSpeed.getValue().intValue()
								+ Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f,
						1f, 1f));
				entityESPBox2(
						Killaura.target, new Color(ClientSettings.r.getValue().intValue(),
								ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), 70),
						event);
			}
			if (this.BoxEspColor.getValue().equals("Black")) {
				entityESPBox2(Killaura.target, new Color(000, 000, 000, 70), event);
			}

			if (this.BoxEspColor.getValue().equals("White")) {
				entityESPBox2(Killaura.target, new Color(255, 255, 255, 70), event);
			}
			if (this.BoxEspColor.getValue().equals("Green")) {
				entityESPBox2(Killaura.target, new Color(000, 255, 000, 70), event);
			}

			GL11.glDisable(2848);
			GL11.glEnable(3553);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			GL11.glDisable(3042);
		}
	}

	public static void entityESPBox2(Entity e, Color color, EventRender3D event) {

		double posX = Killaura.target.lastTickPosX + (e.posX - e.lastTickPosX) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double posY = Killaura.target.lastTickPosY + (e.posY - e.lastTickPosY) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double posZ = Killaura.target.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) event.getPartialTicks()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glColor4f((float) ((float) color.getRed() / 255.0f), (float) ((float) color.getGreen() / 255.0f),
				(float) ((float) color.getBlue() / 255.0f), color.getAlpha() / 255.0f);
		RenderUtil.drawBoundingBox(
				(AxisAlignedBB) new AxisAlignedBB(posX + 0.4, posY, posZ - 0.4, posX - 0.4, posY + 1.8, posZ + 0.4));
		RenderUtil.drawOutlinedBoundingBox(
				(AxisAlignedBB) new AxisAlignedBB(posX + 0.4, posY, posZ - 0.4, posX - 0.4, posY + 1.8, posZ + 0.4));
	}

	@Override
	public void onDisable() {
		super.onDisable();
		target = null;

		Startdelay.reset();

		curTargets.clear();
		mc.gameSettings.keyBindUseItem.pressed = false;
	}

	public void filter(List<? extends Entity> entities) {
		if(!prepost.getValue().equals("Legit")) {
			curTargets.clear();
			target = null;
			for (Entity entity : entities) {
				if (entity == mc.thePlayer)
					continue;
				if (!entity.isEntityAlive())
					continue;
				if (Teams.isOnSameTeam(entity))
					continue;
				if (AntiBots.isServerBot(entity))
					continue;
				if (curTargets.size() > targets.getValue().intValue())
					continue;

				if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue().doubleValue() + 1)
					continue;


				if (entity instanceof EntityMob && ((boolean) mob.getValue()))
					curTargets.add((EntityLivingBase) entity);

				if (entity instanceof EntityAnimal && ((boolean) animals.getValue()))
					curTargets.add((EntityLivingBase) entity);

				if (entity instanceof EntityPlayer && ((boolean) player.getValue())) {
					if (entity.isInvisible() && ((boolean) invisible.getValue()))
						curTargets.add((EntityLivingBase) entity);
					else if (!entity.isInvisible())
						curTargets.add((EntityLivingBase) entity);
				}

			}
		}
	}

	float cYaw, cPitch;


	@EventHandler
	public void onWorldChange(EventPacketRecieve event) {
		if (event.getPacket() instanceof S07PacketRespawn || event.getPacket() instanceof S01PacketJoinGame) {
			this.setEnabled(false);
		}
	}

	@EventHandler
	public void onPre(EventPreUpdate e) {
		if (this.prepost.getValue().equals("OnPre")) {
			if ((boolean) GayFix.getValue()) {
				//if(Killaura.target !=null) {
				//	mc.thePlayer.setSprinting(false);

				//} else{
				//	if(PlayerUtils.isMoving()) {
				//	mc.thePlayer.setSprinting(true);
				//}
				//}


			}

			// if(mc.thePlayer.isSwingInProgress) {
			// if (this.Gofyyahh.getValue().equals("Vulcan")) {

			// if (mc.thePlayer.ticksExisted % 1 == 0) {
			// mc.thePlayer.setSprinting(true);
			// }
			// if (mc.thePlayer.ticksExisted % 3 == 0) {
			// mc.thePlayer.setSprinting(false);
			// }
			// }

			// }
			if (target != null) {
				if (this.Gofyyahh.getValue().equals("On")) {
					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.thePlayer.setSprinting(false);
					}
				}

				if (this.Gofyyahh.getValue().equals("Vulcan")) {

					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 5 == 0) {
						mc.thePlayer.setSprinting(false);
					}

				}

			}

			if (ArrayList2.Sufix.getValue().equals("On")) {

				// this.setSuffix(this.mode.getModeAsString() + " " +
				// this.blockhit.getModeAsString());
				// OR

				//this.setSuffix(this.mode.getModeAsString() + " " + this.blockhit.getModeAsString() + " " +range.getModeAsString() + "r");
				this.setSuffix(this.mode.getModeAsString());


			}

			if (ArrayList2.Sufix.getValue().equals("Off")) {

				this.setSuffix("");
			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;
			filter(mc.theWorld.getLoadedEntityList());
			switchTarget();
			if (curTargets.size() != 0) {
				if (cur > curTargets.size() - 1) {
					cur = 0;
				}
				target = curTargets.get(cur);
				float[] rotations = PlayerUtils.getRotations(target);
				rotate(rotations, e);
			}

			if (this.blockhit.getValue().equals("None")) {

			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;
			// Random random = new Random();
			// double min = minimumcps.getValue().floatValue();
			// double max = Maxcps.getValue().floatValue();

			// double randomNumber = min + (max - min) * random.nextDouble();
			// double min = minimumcps.getValue().floatValue(); // minimum value
			// double max = Maxcps.getValue().floatValue(); // maximum value

			// double randomNumber = min + (max - min) * random.nextDouble();

			// OR

			Random random = new Random();
			int min13 = minimumcps.getValue().intValue();
			int max13 = Maxcps.getValue().intValue();
			// int randomNumbercps = random.nextInt(max13 - min13 +
			// minimumcps.getValue().intValue()) + min13;
			int randomNumbercps = random.nextInt(max13 - min13 + 6) + min13;
			// int randomNumbercps = random.nextInt(max13 - min13 + 1) + min13;

			// if (cpsTimer.delay(1000 / Maxcps.getValue().floatValue()) && target != null)
			// {
			// if (cpsTimer.delay((float) (800 / randomNumbercps)) && target != null) {

			if ((boolean) Delay.getValue()) {

			}


			if (((boolean) raycast.getValue()) && !RayCastShit.isMouseOver(e.getYaw(), e.getPitch(), target, range.getValue().floatValue())) {
				if (mc.thePlayer.isBlocking() && target == null) {
					//maybe remove this blockhit
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				}

				return;
			}
			if ((boolean) CombatDelay.getValue()) {
				KillCps = 1.4f;
			} else {
				KillCps = randomNumbercps;
			}

			if (cpsTimer.delay((int) (1000 / KillCps)) && target != null) {
				// USE THIS FOR HYPIXEL

				// if (Startdelay.hasReached(2000)) {
				// if (cpsTimer.delay((int) (1000 / 15)) && target != null) {
				// this nono

				//	if (cpsTimer.delay((int) (1000 / 1.4)) && target != null) {

				crit.doCrit(target);
				if ((boolean) noSwing.getValue()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				} else {
					mc.thePlayer.swingItem();
				}

				if ((boolean) KeepSprint.getValue()) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				} else {
					mc.playerController.attackEntity(mc.thePlayer, target);
				}

				if (this.blockhit.getValue().equals("Vannila")) {

					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.inventory.getCurrentItem() != null)
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getCurrentItem());

				}

				if (this.blockhit.getValue().equals("Vannila3")) {
					// if (this.mc.thePlayer.getCurrentEquippedItem() != null &&
					// this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
					// (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit !=
					// MovingObjectPosition.MovingObjectType.BLOCK)) {
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// }

					if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {

						PlayerUtils.sendClick(1, true);
					}


				}

				if (this.blockhit.getValue().equals("Intave")) {
					// if (this.mc.thePlayer.getCurrentEquippedItem() != null &&
					// this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
					// (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit !=
					// MovingObjectPosition.MovingObjectType.BLOCK)) {
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// }

					if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.ticksExisted % 8 == 0) {
							PlayerUtils.sendClick(1, true);
						} else {
							PlayerUtils.sendClick(1, false);
						}

					}


				}

				if (this.blockhit.getValue().equals("Vannila2")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					//mc.thePlayer.sendQueue
					//	.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					mc.gameSettings.keyBindUseItem.pressed = true;

				}

				if (this.blockhit.getValue().equals("Taco")) {

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.hurtTime > 1) {
							mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
							mc.gameSettings.keyBindUseItem.pressed = true;
							//if(Sugi.hasReached(80)) {
							//mc.gameSettings.keyBindUseItem.pressed = false;
							//Sugi.reset();
							//	}

						}
					}

				}


				if (this.blockhit.getValue().equals("Sex")) {
					mc.gameSettings.keyBindUseItem.pressed = true;

				}
				Random random2 = new Random();
				int min2 = 1; // minimum value
				int max2 = 6; // maximum value

				double randomNumber2 = random.nextInt(max2 - min2 + 1) + min;
				if (this.blockhit.getValue().equals("Uncp")) {
					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// if(mc.thePlayer.ticksExisted % 2 == 0) {
					// mc.gameSettings.keyBindUseItem.pressed = true;
					// }

				}
				if (this.blockhit.getValue().equals("Legit")) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));

					if (mc.thePlayer.ticksExisted % 3 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = true;
					}
				}
				if (this.blockhit.getValue().equals("Fake")) {

				}

				if (this.blockhit.getValue().equals("Interact")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

				}
				if (this.blockhit.getValue().equals("Verus")) {

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						mc.thePlayer.sendQueue.addToSendQueue(
								new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
					}

				}

				if (this.blockhit.getValue().equals("Watchdog")) {
					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.hurtTime > 1) {
							if (mc.thePlayer.ticksExisted % 1 == 0) {
								mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
								mc.gameSettings.keyBindUseItem.pressed = true;
							}
							if (mc.thePlayer.ticksExisted % 2 == 0) {
								mc.gameSettings.keyBindUseItem.pressed = false;
							}
						}

					}

				}

				cpsTimer.reset();

			}
			if (this.blockhit.getValue().equals("Verus")) {
				if (this.mc.thePlayer.getCurrentEquippedItem() != null
						&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
			// }
			else {
				if (mc.thePlayer.isBlocking() && target == null) {
					Startdelay.reset();
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				}
			}
		}

		}

	public void filter2(List<? extends Entity> entities) {
		if(prepost.getValue().equals("Legit")) {
			curTargets.clear();
			target = null;
			for (Entity entity : entities) {
				if (entity == mc.thePlayer)
					continue;
				if (!entity.isEntityAlive())
					continue;
				if (Teams.isOnSameTeam(entity))
					continue;
				if (AntiBots.isServerBot(entity))
					continue;
				if (curTargets.size() > targets.getValue().intValue())
					continue;

				if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue().doubleValue() + 1)
					continue;


				if (entity instanceof EntityMob && ((boolean) mob.getValue()))
					curTargets.add((EntityLivingBase) entity);

				if (entity instanceof EntityAnimal && ((boolean) animals.getValue()))
					curTargets.add((EntityLivingBase) entity);

				if (entity instanceof EntityPlayer && ((boolean) player.getValue())) {
					if (entity.isInvisible() && ((boolean) invisible.getValue()))
						curTargets.add((EntityLivingBase) entity);
					else if (!entity.isInvisible())
						curTargets.add((EntityLivingBase) entity);
				}

			}
		}
	}

	@EventHandler
	public void OnRotate(EventPreUpdate e) {
		if (this.prepost.getValue().equals("Legit")) {

			if (curTargets.size() != 0) {
				float[] rotations = RotationUtil.EveryRotation(target);
				//if (this.prepost.getValue().equals("Legit")) {
				e.setYaw(roationscal[0]);
				e.setPitch(roationscal[1]);
				//rotate(roationscal, e);
			}
			//}
			}
		}


	@EventHandler
	public void onTick(EventTick e) {
		if (this.prepost.getValue().equals("Legit")) {

			filter2(mc.theWorld.getLoadedEntityList());
			if (curTargets.size() != 0) {
				switchTarget();
				if (cur > curTargets.size() - 1) {
					cur = 0;
				}
				target = curTargets.get(cur);
				roationscal =  RotationUtil.EveryRotation(target);
				Fixyaw = roationscal[0];

			}

		}



	}
	@EventHandler
	public void onClick(EventClick e) {
		if (this.prepost.getValue().equals("Legit")) {

			if ((boolean) GayFix.getValue()) {
				//if(Killaura.target !=null) {
				//	mc.thePlayer.setSprinting(false);

				//} else{
				//	if(PlayerUtils.isMoving()) {
				//	mc.thePlayer.setSprinting(true);
				//}
				//}


			}

			// if(mc.thePlayer.isSwingInProgress) {
			// if (this.Gofyyahh.getValue().equals("Vulcan")) {

			// if (mc.thePlayer.ticksExisted % 1 == 0) {
			// mc.thePlayer.setSprinting(true);
			// }
			// if (mc.thePlayer.ticksExisted % 3 == 0) {
			// mc.thePlayer.setSprinting(false);
			// }
			// }

			// }
			if (target != null) {
				if (this.Gofyyahh.getValue().equals("On")) {
					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.thePlayer.setSprinting(false);
					}
				}

				if (this.Gofyyahh.getValue().equals("Vulcan")) {

					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 5 == 0) {
						mc.thePlayer.setSprinting(false);
					}

				}

			}

			if (ArrayList2.Sufix.getValue().equals("On")) {

				// this.setSuffix(this.mode.getModeAsString() + " " +
				// this.blockhit.getModeAsString());
				// OR

				//this.setSuffix(this.mode.getModeAsString() + " " + this.blockhit.getModeAsString() + " " +range.getModeAsString() + "r");
				this.setSuffix(this.mode.getModeAsString());


			}

			if (ArrayList2.Sufix.getValue().equals("Off")) {

				this.setSuffix("");
			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;




			if (this.blockhit.getValue().equals("None")) {

			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;
			// Random random = new Random();
			// double min = minimumcps.getValue().floatValue();
			// double max = Maxcps.getValue().floatValue();

			// double randomNumber = min + (max - min) * random.nextDouble();
			// double min = minimumcps.getValue().floatValue(); // minimum value
			// double max = Maxcps.getValue().floatValue(); // maximum value

			// double randomNumber = min + (max - min) * random.nextDouble();

			// OR

			Random random = new Random();
			int min13 = minimumcps.getValue().intValue();
			int max13 = Maxcps.getValue().intValue();
			// int randomNumbercps = random.nextInt(max13 - min13 +
			// minimumcps.getValue().intValue()) + min13;
			int randomNumbercps = random.nextInt(max13 - min13 + 6) + min13;
			// int randomNumbercps = random.nextInt(max13 - min13 + 1) + min13;

			// if (cpsTimer.delay(1000 / Maxcps.getValue().floatValue()) && target != null)
			// {
			// if (cpsTimer.delay((float) (800 / randomNumbercps)) && target != null) {

			if ((boolean) Delay.getValue()) {

			}

		MovingObjectPosition Niggafixed = RayCastShit.rayCast(1.0f,roationscal);
			

			if (((boolean) raycast.getValue()) && (Niggafixed == null || Niggafixed.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY  || Niggafixed .entityHit == null || Niggafixed .entityHit.getEntityId() != target.getEntityId())) {
				if (mc.thePlayer.isBlocking() && target == null) {
					//maybe remove this blockhit
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				}
				
				return;
			}
			if ((boolean) CombatDelay.getValue()) {
				KillCps = 1.4f;
			} else {
				KillCps = randomNumbercps;
			}

			if (cpsTimer.delay((int) (1000 / KillCps)) && target != null) {
				// USE THIS FOR HYPIXEL

				// if (Startdelay.hasReached(2000)) {
				// if (cpsTimer.delay((int) (1000 / 15)) && target != null) {
				// this nono

				//	if (cpsTimer.delay((int) (1000 / 1.4)) && target != null) {

				crit.doCrit(target);
				if ((boolean) noSwing.getValue()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				} else {
					mc.thePlayer.swingItem();
				}

				if ((boolean) KeepSprint.getValue()) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				} else {
					mc.playerController.attackEntity(mc.thePlayer, target);
				}

				if (this.blockhit.getValue().equals("Vannila")) {

					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.inventory.getCurrentItem() != null)
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getCurrentItem());

				}

				if (this.blockhit.getValue().equals("Vannila3")) {
					// if (this.mc.thePlayer.getCurrentEquippedItem() != null &&
					// this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
					// (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit !=
					// MovingObjectPosition.MovingObjectType.BLOCK)) {
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// }

					if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {

						PlayerUtils.sendClick(1, true);
					}


				}

				if (this.blockhit.getValue().equals("Intave")) {
					// if (this.mc.thePlayer.getCurrentEquippedItem() != null &&
					// this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
					// (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit !=
					// MovingObjectPosition.MovingObjectType.BLOCK)) {
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// }

					if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.ticksExisted % 8 == 0) {
							PlayerUtils.sendClick(1, true);
						} else {
							PlayerUtils.sendClick(1, false);
						}

					}


				}

				if (this.blockhit.getValue().equals("Vannila2")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					//mc.thePlayer.sendQueue
					//	.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					mc.gameSettings.keyBindUseItem.pressed = true;

				}

				if (this.blockhit.getValue().equals("Taco")) {

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.hurtTime > 1) {
							mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
							mc.gameSettings.keyBindUseItem.pressed = true;
							//if(Sugi.hasReached(80)) {
							//mc.gameSettings.keyBindUseItem.pressed = false;
							//Sugi.reset();
							//	}

						}
					}

				}


				if (this.blockhit.getValue().equals("Sex")) {
					mc.gameSettings.keyBindUseItem.pressed = true;

				}
				Random random2 = new Random();
				int min2 = 1; // minimum value
				int max2 = 6; // maximum value

				double randomNumber2 = random.nextInt(max2 - min2 + 1) + min;
				if (this.blockhit.getValue().equals("Uncp")) {
					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// if(mc.thePlayer.ticksExisted % 2 == 0) {
					// mc.gameSettings.keyBindUseItem.pressed = true;
					// }

				}
				if (this.blockhit.getValue().equals("Legit")) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));

					if (mc.thePlayer.ticksExisted % 3 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = true;
					}
				}
				if (this.blockhit.getValue().equals("Fake")) {

				}

				if (this.blockhit.getValue().equals("Interact")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

				}
				if (this.blockhit.getValue().equals("Verus")) {

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						mc.thePlayer.sendQueue.addToSendQueue(
								new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
					}

				}

				if (this.blockhit.getValue().equals("Watchdog")) {
					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						if (mc.thePlayer.hurtTime > 1) {
							if (mc.thePlayer.ticksExisted % 1 == 0) {
								mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
								mc.gameSettings.keyBindUseItem.pressed = true;
							}
							if (mc.thePlayer.ticksExisted % 2 == 0) {
								mc.gameSettings.keyBindUseItem.pressed = false;
							}
						}

					}

				}

				cpsTimer.reset();

			}
			if (this.blockhit.getValue().equals("Verus")) {
				if (this.mc.thePlayer.getCurrentEquippedItem() != null
						&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
			// }
			else {
				if (mc.thePlayer.isBlocking() && target == null) {
					Startdelay.reset();
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				}
			}
		}

	}






	@EventHandler
	public void onPost(EventPostUpdate e) {
		if (this.prepost.getValue().equals("OnPost")) {

			// if(mc.thePlayer.isSwingInProgress) {
			// if (this.Gofyyahh.getValue().equals("Vulcan")) {

			// if (mc.thePlayer.ticksExisted % 1 == 0) {
			// mc.thePlayer.setSprinting(true);
			// }
			// if (mc.thePlayer.ticksExisted % 3 == 0) {
			// mc.thePlayer.setSprinting(false);
			// }
			// }

			// }
			if (target != null) {
				if (this.Gofyyahh.getValue().equals("On")) {
					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.thePlayer.setSprinting(false);
					}
				}

				if (this.Gofyyahh.getValue().equals("Vulcan")) {

					if (mc.thePlayer.ticksExisted % 1 == 0) {
						mc.thePlayer.setSprinting(true);
					}
					if (mc.thePlayer.ticksExisted % 5 == 0) {
						mc.thePlayer.setSprinting(false);
					}

				}

			}

			if (ArrayList2.Sufix.getValue().equals("On")) {

				// this.setSuffix(this.mode.getModeAsString() + " " +
				// this.blockhit.getModeAsString());
				// OR
				//	this.setSuffix(this.mode.getModeAsString() + " " + this.blockhit.getModeAsString() + " "+ rot.getModeAsString());
				this.setSuffix(this.mode.getModeAsString());

			}

			if (ArrayList2.Sufix.getValue().equals("Off")) {

				this.setSuffix("");
			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;
			filter(mc.theWorld.getLoadedEntityList());
			switchTarget();
			if (curTargets.size() != 0) {
				if (cur > curTargets.size() - 1) {
					cur = 0;
				}
				target = curTargets.get(cur);
				float[] rotations = PlayerUtils.getRotations(target);
				rotatepost(rotations, e);
			}

			if (this.blockhit.getValue().equals("None")) {

			}

			if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
				return;
			// Random random = new Random();
			// double min = minimumcps.getValue().floatValue();
			// double max = Maxcps.getValue().floatValue();

			// double randomNumber = min + (max - min) * random.nextDouble();
			// double min = minimumcps.getValue().floatValue(); // minimum value
			// double max = Maxcps.getValue().floatValue(); // maximum value

			// double randomNumber = min + (max - min) * random.nextDouble();

			// OR

			Random random = new Random();
			int min13 = minimumcps.getValue().intValue();
			int max13 = Maxcps.getValue().intValue();
			// int randomNumbercps = random.nextInt(max13 - min13 +
			// minimumcps.getValue().intValue()) + min13;
			int randomNumbercps = random.nextInt(max13 - min13 + 6) + min13;
			// int randomNumbercps = random.nextInt(max13 - min13 + 1) + min13;

			// if (cpsTimer.delay(1000 / Maxcps.getValue().floatValue()) && target != null)
			// {
			// if (cpsTimer.delay((float) (800 / randomNumbercps)) && target != null) {

			if (cpsTimer.delay((int) (1000 / randomNumbercps)) && target != null) {

				crit.doCrit(target);
				if ((boolean) noSwing.getValue()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				} else {
					mc.thePlayer.swingItem();
				}

				if ((boolean) KeepSprint.getValue()) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				} else {
					mc.playerController.attackEntity(mc.thePlayer, target);
				}

				if (this.blockhit.getValue().equals("Vannila")) {

					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.inventory.getCurrentItem() != null)
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getCurrentItem());

				}


				if (this.blockhit.getValue().equals("Vannila3")) {
					// if (this.mc.thePlayer.getCurrentEquippedItem() != null &&
					// this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword &&
					// (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit !=
					// MovingObjectPosition.MovingObjectType.BLOCK)) {
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// }

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
							&& (mc.objectMouseOver == null
							|| mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)) {
						mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
						mc.thePlayer.sendQueue
								.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
						mc.gameSettings.keyBindUseItem.pressed = true;
					}

				}

				if (this.blockhit.getValue().equals("Vannila2")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					mc.gameSettings.keyBindUseItem.pressed = true;

				}

				if (this.blockhit.getValue().equals("Sex")) {
					mc.gameSettings.keyBindUseItem.pressed = true;

				}
				Random random2 = new Random();
				int min2 = 1; // minimum value
				int max2 = 6; // maximum value

				double randomNumber2 = random.nextInt(max2 - min2 + 1) + min;
				if (this.blockhit.getValue().equals("Uncp")) {
					mc.gameSettings.keyBindUseItem.pressed = true;
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					// mc.gameSettings.keyBindUseItem.pressed = false;
					// if(mc.thePlayer.ticksExisted % 2 == 0) {
					// mc.gameSettings.keyBindUseItem.pressed = true;
					// }

				}


				if (this.blockhit.getValue().equals("Legit")) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));

					if (mc.thePlayer.ticksExisted % 3 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.gameSettings.keyBindUseItem.pressed = true;
					}
				}
				if (this.blockhit.getValue().equals("Fake")) {

				}

				if (this.blockhit.getValue().equals("Interact")) {
					mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

				}
				if (this.blockhit.getValue().equals("Verus")) {

					if (this.mc.thePlayer.getCurrentEquippedItem() != null
							&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						mc.thePlayer.sendQueue.addToSendQueue(
								new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
					}

				}

				if (this.blockhit.getValue().equals("Watchdog")) {
					if (mc.thePlayer.hurtTime > -1) {
						// this.func_178105_d(f1);
						// this.transformFirstPersonItem(f, f1);

					}


					if (this.blockhit.getValue().equals("Watchdog")) {

						if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							mc.gameSettings.keyBindUseItem.pressed = true;
							if (mc.thePlayer.inventory.getCurrentItem() != null)
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.inventory.getCurrentItem());
						}
					}

					if (mc.thePlayer.hurtTime > 1) {

						mc.gameSettings.keyBindUseItem.pressed = true;
						if (mc.thePlayer.inventory.getCurrentItem() != null)
							mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getCurrentItem());

					}
				}

				cpsTimer.reset();

			}
			if (this.blockhit.getValue().equals("Verus")) {
				if (this.mc.thePlayer.getCurrentEquippedItem() != null
						&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			} else {
				if (mc.thePlayer.isBlocking() && target == null) {

					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				}
			}

		}
	}

	public float[] getRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				// deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),

				// deltaY = e.posY - e.posY + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw2 = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch2 = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		if (deltaX < 0 && deltaZ < 0) {
			yaw2 = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw2 = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[]{yaw2, pitch2};
	}


	public float[] getRotationstaco(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				deltaY = e.posY - 4 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				// deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),

				// deltaY = e.posY - e.posY + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw2 = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch2 = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		if (deltaX < 0 && deltaZ < 0) {
			yaw2 = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw2 = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[]{yaw2, pitch2};
	}


	public float[] getRotationsb(Entity e) {

		Random random = new Random();
		double min = 0.2; // minimum value
		// -0.2
		double max = 0.7; // maximum value
		double randomNumber = min + (max - min) * random.nextDouble();

		double minY = 4.2; // minimum value
		double maxY = 3.7; // maximum value
		double randomNumberY = min + (max - min) * random.nextDouble();
		double deltaX = e.posX + (e.posX - e.lastTickPosX) + randomNumber - mc.thePlayer.posX,
				deltaY = e.posY - 3.3 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				// deltaY = e.posY - 2.0 + e.getEyeHeight() - mc.thePlayer.posY,

				// deltaY = e.posY - 1.0 - mc.thePlayer.posY,
				// deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),

				// deltaY = e.posY - e.posY + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw2 = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch2 = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		if (deltaX < 0 && deltaZ < 0) {
			yaw2 = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw2 = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[]{yaw2, pitch2};
	}

	public float[] getRotationsbR(Entity e) {

		Random random = new Random();
		double min = 0.2; // minimum value
		double max = 0.7; // maximum value
		double randomNumber = min + (max - min) * random.nextDouble();

		double minY = 4.2; // minimum value
		double maxY = 3.7; // maximum value
		double randomNumberY = min + (max - min) * random.nextDouble();
		double deltaX = e.posX + (e.posX - e.lastTickPosX) + randomNumber - mc.thePlayer.posX,
				deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				// deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),

				// deltaY = e.posY - e.posY + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw2 = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch2 = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		if (deltaX < 0 && deltaZ < 0) {
			yaw2 = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw2 = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[]{yaw2, pitch2};
	}

	Random random = new Random();
	double min = 2.8; // minimum value
	double max = 3.9; // maximum value
	double randomNumber = min + (max - min) * random.nextDouble();

	public float[] getRotations3(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				deltaY = e.posY - randomNumber + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),

				// deltaY = e.posY - e.posY + e.getEyeHeight() - mc.thePlayer.posY +
				// mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw2 = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch2 = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		if (deltaX < 0 && deltaZ < 0) {
			yaw2 = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw2 = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[]{yaw2, pitch2};
	}


	public float[] getVulcanRotations(Entity entity) {
		float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
		// float f1 = mc.gameSettings.mouseSensitivity * 0.7F + 0.2F;
		float fac = f1 * f1 * f1 * 256.0F;

		double x = entity.posX - mc.thePlayer.posX;
		double z = entity.posZ - mc.thePlayer.posZ;

		double minus = (mc.thePlayer.posY - target.posY);


		final double y = (target.posY - (target.lastTickPosY - target.posY) * 0.3) + 0.4 + target.getEyeHeight() - 2.5 / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float) (-(MathHelper.atan2(y, 2) * 180.0 / Math.PI));
		yaw = smoothRotation(mc.thePlayer.prevRotationYawHead, yaw, fac * 1);
		pitch = smoothRotation(mc.thePlayer.prevRotationPitch, pitch, fac * 8);

		return new float[]{yaw, pitch};
	}


	public void setVisualRotations(float yaw, float pitch) {
		mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw;
		mc.thePlayer.rotationPitchHead = pitch;
	}


	public float[] getSmoothRotations(Entity entity) {
		float f1 = mc.gameSettings.mouseSensitivity * 0.7F + CustomRotations.Speed.getValue().floatValue();
		float fac = f1 * f1 * f1 * 256.0F;

		double x = entity.posX - mc.thePlayer.posX - MathHelper.getRandomDoubleInRange(new Random(), CustomRotations.YawRandom.getValue().doubleValue(), CustomRotations.YawRandom2.getValue().doubleValue());
		// double z = entity.posZ - mc.thePlayer.posZ - MathHelper.getRandomDoubleInRange(new Random(), 1, 2) ;
		double z = entity.posZ - mc.thePlayer.posZ;
		double minus = (mc.thePlayer.posY - target.posY);

		if ((boolean) CustomRotations.Vertical.getValue()) {
			y3 = (target.posY - (target.lastTickPosY - target.posY) * 0.3) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;
		} else {
			y3 = entity.posY + entity.getEyeHeight() - MathHelper.getRandomDoubleInRange(new Random(), CustomRotations.PitchRandom.getValue().doubleValue(), CustomRotations.PitchRandom2.getValue().doubleValue()) - (mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY) + 0.3);
		}

		double d3 = MathHelper.sqrt_double(x * x + z * z);
		//or use d3
		//2
		float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float) (-(MathHelper.atan2(y3, d3) * 180.0 / Math.PI));
		yaw = smoothRotation(mc.thePlayer.prevRotationYawHead, yaw, fac * 1);
		pitch = smoothRotation(mc.thePlayer.prevRotationPitch, pitch, fac * 8);


		return new float[]{yaw, pitch};
	}

	public float[] getSmoothRotations3(Entity entity) {
		float f1 = mc.gameSettings.mouseSensitivity * 0.7F + 0.3f;
		float fac = f1 * f1 * f1 * 256.0F;

		double x = entity.posX - mc.thePlayer.posX;
		// double z = entity.posZ - mc.thePlayer.posZ - MathHelper.getRandomDoubleInRange(new Random(), 1, 2) ;
		double z = entity.posZ - mc.thePlayer.posZ;
		double y = entity.posY + entity.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY) + 0.3);
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		//or use d3
		//2
		float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float) (-(MathHelper.atan2(y, d3) * 180.0 / Math.PI));
		yaw = smoothRotation(mc.thePlayer.prevRotationYawHead, yaw, fac * 1);
		pitch = smoothRotation(mc.thePlayer.prevRotationPitch, pitch, fac * 8);


		return new float[]{yaw, pitch};
	}


	public float[] getSmoothRotations2(Entity entity) {
		float f1 = mc.gameSettings.mouseSensitivity * 0.7F + 0.2f;
		float fac = f1 * f1 * f1 * 256.0F;

		double x = entity.posX - mc.thePlayer.posX;
		// double z = entity.posZ - mc.thePlayer.posZ - MathHelper.getRandomDoubleInRange(new Random(), 1, 2) ;
		double z = entity.posZ - mc.thePlayer.posZ;

		double minus = (mc.thePlayer.posY - target.posY);

		if (mc.thePlayer.onGround) {
			y3 = (target.posY - (target.lastTickPosY - target.posY) * 0.3) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;
		} else {
			y3 = entity.posY + entity.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY) + 0.3);
		}
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		//or use d3
		//2
		float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float) (-(MathHelper.atan2(y3, d3) * 180.0 / Math.PI));

		yaw = smoothRotation(mc.thePlayer.prevRotationYawHead, yaw, fac * 1);
		pitch = smoothRotation(mc.thePlayer.prevRotationPitch, pitch, fac * 8);


		return new float[]{yaw, pitch};
	}


	public static float smoothRotation(float from, float to, float speed) {
		float f = MathHelper.wrapAngleTo180_float(to - from);

		if (f > speed) {
			f = speed;
		}

		if (f < -speed) {
			f = -speed;
		}

		return from + f;
	}

	@EventHandler
	public void OnMoveFix(EventMoveFix e) {
		if((boolean)GayFix.getValue()&&  target !=null ) {
			e.setYaw(Fixyaw);
		}
	}
	//@EventHandler
	//public void OnMoveFix(EventSilentMove e) {
	//	if((boolean)GayFix.getValue() && target !=null) {
	//		e.setSilent(true);
	//	}
//	}

	@EventHandler
	public void OnJumpFix(EventJumpFix e) {
		if((boolean)GayFix.getValue() && target !=null) {
			e.setYaw(Fixyaw);

		}
	}


	
	private void rotate(float[] rotations, EventPreUpdate e) {


			if((boolean)Rotaions.getValue()) {
			
			if(Client.instance.getModuleManager().getModuleByClass(CustomRotations.class).isEnabled()) {

					cYaw =  rotse[0];
					cPitch = rotse[1];

		//	mc.thePlayer.rotationYaw = cYaw;
			//mc.thePlayer.rotationPitch = cPitch;
		//	mc.thePlayer.rotationYawHead = cYaw;
			//mc.thePlayer.renderYawOffset = cYaw;
		//	mc.thePlayer.rotationsPitchHead = cPitch;
			//setVisualRotations(e.getYaw(), e.getPitch());
				if(!(boolean)CustomRotations.AimLock.getValue()) {
					e.setYaw(cYaw);
					e.setPitch(cPitch);

				} else{
					mc.thePlayer.rotationYaw = cYaw;
					mc.thePlayer.rotationPitch = cPitch;
				}



			} else {
				cYaw = getSmoothRotations3(target)[0];
				cPitch = getSmoothRotations3(target)[1];
//				/cYaw = getSmoothRotations2(target)[0];
				//cPitch = getSmoothRotations2(target)[1];
			//	mc.thePlayer.rotationYaw = cYaw;
				//mc.thePlayer.rotationPitch = cPitch;
			//	mc.thePlayer.rotationYawHead = cYaw;
				//mc.thePlayer.renderYawOffset = cYaw;
			//	mc.thePlayer.rotationsPitchHead = cPitch;
				//setVisualRotations(e.getYaw(), e.getPitch());

				if(!(boolean)CustomRotations.AimLock.getValue()) {
					e.setYaw(cYaw);
					e.setPitch(cPitch);
				} else{
					mc.thePlayer.rotationYaw = cYaw;
					mc.thePlayer.rotationPitch = cPitch;
				}
			}
			}
		Fixyaw = e.getYaw();

		
	}

	private void rotatepost(float[] rotations, EventPostUpdate e) {
	

			// if (mc.thePlayer.ticksExisted % 5 == 0) {
			// cYaw = getRotations2(target)[0];
			// if (mc.thePlayer.ticksExisted % 5 == 0) {
			// cPitch = getRotations2(target)[1];
			// }
			// e.setRotationYawHead(cYaw);
		if((boolean)Rotaions.getValue()) {
			// e.setRotationYawHead(cPitch);
		if(Client.instance.getModuleManager().getModuleByClass(CustomRotations.class).isEnabled()) {
			cYaw = getSmoothRotations(target)[0];
			
			cPitch = getSmoothRotations(target)[1];
			PlayerUtils.rotate(cYaw, cPitch);
			if(!(boolean)CustomRotations.AimLock.getValue()) {
				e.setYaw(cYaw);
				e.setPitch(cPitch);
			} else{
				mc.thePlayer.rotationYaw = cYaw;
				mc.thePlayer.rotationPitch = cPitch;
			}
		} else {
			cYaw = getSmoothRotations3(target)[0];
			cPitch = getSmoothRotations3(target)[1];
		//	mc.thePlayer.rotationYaw = cYaw;
			//mc.thePlayer.rotationPitch = cPitch;
		//	mc.thePlayer.rotationYawHead = cYaw;
			//mc.thePlayer.renderYawOffset = cYaw;
		//	mc.thePlayer.rotationsPitchHead = cPitch;
			//setVisualRotations(e.getYaw(), e.getPitch());

			if(!(boolean)CustomRotations.AimLock.getValue()) {
				e.setYaw(cYaw);
				e.setPitch(cPitch);
			} else{
				mc.thePlayer.rotationYaw = cYaw;
				mc.thePlayer.rotationPitch = cPitch;
			}
		}
		}
		

			
	}

	public float getRotationDis(EntityLivingBase entity) {
		float pitch = PlayerUtils.getRotations(entity)[1];
		return mc.thePlayer.rotationPitch - pitch;
	}

	private void switchTarget() {
		if (mode.isValid("Switch")) {

			if (timer.delay(switchDelay.getValue().intValue())) {
				if (cur < curTargets.size() - 1) {
					if (((boolean) smart.getValue() && target != null && target.getHealth() < 5)) {
						timer.reset();
						return;
					}
					cur++;
				} else {
					cur = 0;
				}
				timer.reset();
			}
		} else if (mode.isValid("Single")) {
			switch (priority.getValue().toString()) {
			case "Distance":

				curTargets.sort(((o1,
						o2) -> (int) (o2.getDistanceToEntity(mc.thePlayer) - o1.getDistanceToEntity(mc.thePlayer))));
				break;
			case "Health":

				curTargets.sort(((o1, o2) -> (int) (o2.getHealth() - o1.getHealth())));
				break;
			case "Direction":

				curTargets.sort(((o1, o2) -> (int) (getRotationDis(o2) - getRotationDis(o1))));
				break;
			}
			cur = 0;
		} else if (mode.isValid("Multi")) {
			if (timer.delay(80)) {
				if (cur < curTargets.size() - 1) {
					if (((boolean) smart.getValue() && target != null && target.getHealth() < 5)) {
						timer.reset();
						return;
					}
					cur++;
				} else {
					cur = 0;
				}
				timer.reset();
			}
		}
	}
}
