package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import today.flux.event.RespawnEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.ModeValue;
import today.flux.utility.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AntiBots extends Module {
	public static ModeValue mode = new ModeValue("AntiBot", "Mode", "Basic", "Basic", "Advanced", "Hypixel", "BrokenID", "Invisible", "Enclose");
	private HashMap<String, Integer> TabTicks = new HashMap<>();
	private HashMap<Integer, Integer> InvisTicks = new HashMap<>();
	private List<Integer> Grounded = new ArrayList<>();

	private List<Integer> blacklisted = new ArrayList<>();

	public AntiBots() {
		super("AntiBot", Category.Combat, mode);
		this.setEnabled(true);
	}

	public static boolean isInTablist(EntityLivingBase entity) {
		for (Object item : mc.getNetHandler().getPlayerInfoMap()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) item;

			if (playerInfo != null && playerInfo.getGameProfile() != null
					&& playerInfo.getGameProfile().getName().contains(entity.getName())) {
				return true;
			}
		}

		return false;
	}

	private boolean hasBadlionBots(final EntityPlayer parent) {
		if (parent.isInvisible())
			return false;

		for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
			if (player != parent && player.isInvisible() && parent.getDistanceToEntity(player) < 3.0)
				return true;
		}

		return false;
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
			final String name = EnumChatFormatting.getTextWithoutFormattingCodes(player.getName());

			if (!TabTicks.containsKey(name)) {
				TabTicks.put(name, 0);
			}

			if (isInTablist(player)) {
				int before = TabTicks.get(name);
				TabTicks.remove(name);
				TabTicks.put(name, before + 1);
			}
		}
	}

	@EventTarget
	public void onRespawn(RespawnEvent event) {
		this.TabTicks.clear();
		this.Grounded.clear();
		this.InvisTicks.clear();

		this.blacklisted.clear();
	}

	public boolean isNPC(EntityLivingBase entity) {
		if (entity == null) {
			return true;
		}

		if (entity == mc.thePlayer) {
			return true;
		}

		if (!(entity instanceof EntityPlayer)) {
			return false; // ALLOW ALL MOBS
		}

		if (ServerUtils.INSTANCE.isOnHypixel() && entity.ticksExisted <= 10 * 20)
			return false;

		if (entity.isPlayerSleeping()) {
			return true;
		}

		if (mode.getValue().equals("BrokenID") && entity.getEntityId() > 1000000) {
			return true;
		}

		if ((mode.getValue().equals("Invisible") || mode.getValue().equals("Basic")) && !isInTablist(entity)) {
			return true;
		}

		if ((mode.getValue().equals("Basic") || mode.getValue().equals("Advanced"))
				&& ((EntityPlayer) entity).ticksExisted <= 80) {
			return true;
		}

		if (mode.getValue().equals("Advanced") && !this.Grounded.contains(entity.getEntityId())) {
			return true;
		}

		if (mode.getValue().equals("Enclose") && hasBadlionBots((EntityPlayer) entity)) {
			return true;
		}

		if (mode.getValue().equals("Invisible") && this.blacklisted.contains(entity.getEntityId())) {
			return true;
		}

		return false;
	}

	public static List<EntityPlayer> getTabPlayerList() {
		NetHandlerPlayClient nhpc = mc.thePlayer.sendQueue;
		List<EntityPlayer> list = new ArrayList<>();
		List players = new GuiPlayerTabOverlay(mc,
				mc.ingameGUI).field_175252_a.sortedCopy(nhpc.getPlayerInfoMap());
		for (final Object o : players) {
			final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
			if (info == null) {
				continue;
			}
			list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}

	public static boolean isHypixelNPC(Entity entity) {
		if (!ModuleManager.antiBotsMod.isEnabled() || !mode.isCurrentMode("Hypixel")) return false;

		String formattedName = entity.getDisplayName().getFormattedText();
		String customName = entity.getCustomNameTag();

		if (!formattedName.startsWith("\247") && formattedName.endsWith("\247r")) {
			return true;
		}

		if (formattedName.contains("[NPC]")) {
			return true;
		}
		return false;
	}

	@EventTarget
	public void onTickon(TickEvent event) {
		if (mode.getValue().equals("Invisible")) {
			for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
				if (this.InvisTicks.containsKey(player.getEntityId()) && this.InvisTicks.get(player.getEntityId()) > 40
						&& EntityUtils.hasFakeInvisible(player)) {
					this.blacklisted.add(player.getEntityId());

					ChatUtils.debug("removed Invisible bot (name:" + player.getDisplayName().getFormattedText() + ")");
				}
			}
		}

		if (mode.getValue().equals("Hypixel")) {
			for (EntityPlayer entity : mc.theWorld.playerEntities) {
				if (entity != mc.thePlayer && entity != null) {
					if (!this.getTabPlayerList().contains(entity)
							&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")
							&& entity.getDisplayName().getFormattedText().startsWith("\u00a7")
							&& entity.isEntityAlive()) {
						if (!isHypixelNPC(entity) && entity.isInvisible()) {
							ChatUtils.debug(entity);
							mc.theWorld.removeEntity(entity);
						}
					}
				}
			}
		}

		// grounded
		this.Grounded.addAll(WorldUtil.getLivingPlayers().stream()
				.filter(entityPlayer -> entityPlayer.onGround && !this.Grounded.contains(entityPlayer.getEntityId()))
				.map(EntityPlayer::getEntityId).collect(Collectors.toList()));

		// custom ticks
		for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
			if (!this.InvisTicks.containsKey(player.getEntityId()))
				this.InvisTicks.put(player.getEntityId(), 0);

			if (player.isInvisible() && EntityUtils.hasFakeInvisible(player)) {
				this.InvisTicks.put(player.getEntityId(), this.InvisTicks.get(player.getEntityId()) + 1);
			} else {
				this.InvisTicks.put(player.getEntityId(), 0);
			}
		}
	}

	private boolean isWatchdoger(EntityPlayer entity) {
		Location myLoc = new Location(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		Location targetLoc = new Location(entity.posX, entity.posY, entity.posZ);

		if (entity.ticksExisted <= 20 && (entity.posY - this.mc.thePlayer.posY) > 8.0
				&& myLoc.distanceToXZ(targetLoc) < 3.5)
			return true;

		return false;
	}
}