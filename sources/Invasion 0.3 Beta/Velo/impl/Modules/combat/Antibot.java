package Velo.impl.Modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;

public class Antibot extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Watchdog", "Watchdog2", "Mineplex");
	
	
	
	public static ArrayList Bots = new ArrayList();
	int bots1;
	private static List invalid = new ArrayList();
	private List<Packet> packetList = new CopyOnWriteArrayList();
	public static ArrayList bot = new ArrayList();
	private static List removed = new ArrayList();
	public Timer lastRemoved = new Timer();

	public static List onAirInvalid = new ArrayList();
	
    public static List<EntityLivingBase> bots = new ArrayList<>();
    int currentEntity;
    int playerList;
    int index;
    int next;
    
	public Antibot() {
		super("Antibot", "Antibot", Keyboard.KEY_NONE, Category.COMBAT);
		
		this.bots1 = 0;
		this.loadSettings(mode);
		
	}
	
	public void onEnable() {
		Bots.clear();
	}
	
	public void onDisable() {
		Bots.clear();
	}
	
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Antibot " + mode.modes.get(mode.index));
		if(mode.equalsIgnorecase("Watchdog")) {
			for(Object entity : mc.theWorld.loadedEntityList) {
				if(((Entity)entity).isInvisible() && ((Entity) entity) != mc.thePlayer && !((Entity)entity).onGround) {
					mc.theWorld.removeEntity(((Entity)entity));
				}
				if(((Entity)entity) instanceof EntityPlayer && ((Entity)entity) != mc.thePlayer) {
					EntityPlayer entity1 = (EntityPlayer) entity;
					if(Float.isNaN(entity1.getHealth())) {
						mc.theWorld.removeEntity(entity1);
					}
				} else {
					
				}
				if(mc.thePlayer == null || mc.theWorld == null) {
					return;
				}
			}
		}
		
		
		if(mode.equalsIgnorecase("Watchdog2")) {
			Iterator var3 = mc.theWorld.playerEntities.iterator();
			while (var3.hasNext()) {
				EntityPlayer entity = (EntityPlayer) var3.next();
				if (entity != mc.thePlayer && entity != null && entity instanceof EntityLivingBase) {
					if (entity != mc.thePlayer && entity instanceof EntityPlayer && !isInTablist(entity)
							&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")
							&& entity.getDisplayName().getFormattedText().startsWith("\247")) {
						mc.theWorld.removeEntity(entity);
					}
				}
			}

			if (!removed.isEmpty() && this.lastRemoved.elapsed(1000L)) {
				this.lastRemoved.reset();
				removed.clear();
			}
			var3 = mc.theWorld.getLoadedEntityList().iterator();

			while (var3.hasNext()) {
				Object o = var3.next();
				if (o instanceof EntityPlayer) {
					EntityPlayer ent = (EntityPlayer) o;
					if (ent != mc.thePlayer && !invalid.contains(ent)) {
						String formated = ent.getDisplayName().getFormattedText();
						String custom = ent.getCustomNameTag();
						String name = ent.getName();
						if (ent.isInvisible() && !formated.startsWith("\u00a7c") && formated.endsWith("\u00a7r")
								&& custom.equals(name)) {
							double diffX = Math.abs(ent.posX - mc.thePlayer.posX);
							double diffY = Math.abs(ent.posY - mc.thePlayer.posY);
							double diffZ = Math.abs(ent.posZ - mc.thePlayer.posZ);
							double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
							if (diffY < 13.0D && diffY > 10.0D && diffH < 3.0D) {
								List list = getPlayerList();
								if (!list.contains(ent)) {
									this.lastRemoved.reset();
									removed.add(ent);
									mc.theWorld.removeEntity(ent);

									invalid.add(ent);
								}
							}
						}

						if (!formated.startsWith("\247") && formated.endsWith("\u00a7r")) {
							invalid.add(ent);
						}

						if (!isInTablist(ent)) {
							invalid.add(ent);
						}

						if (ent.isInvisible() && !custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\u00a7c\u00a7c")
								&& name.contains("\u00a7c")) {
							this.lastRemoved.reset();
							removed.add(ent);
							mc.theWorld.removeEntity(ent);

							invalid.add(ent);
						}

						if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\u00a7c")
								&& custom.toLowerCase().contains("\u00a7r")) {
							this.lastRemoved.reset();
							removed.add(ent);
							mc.theWorld.removeEntity(ent);

							invalid.add(ent);
						}

						if (formated.contains("\u00a78[NPC]")) {
							invalid.add(ent);
						}

						if (!formated.contains("\u00a7c") && !custom.equalsIgnoreCase("")) {
							invalid.add(ent);
						}
					}
				}
			}
		}
		if(mode.equalsIgnorecase("Mineplex")) {
			for(Object theObject : mc.theWorld.loadedEntityList) {
				if(((Entity)theObject) instanceof EntityPlayer && ((Entity)theObject) != mc.thePlayer) {
					EntityPlayer entity = (EntityPlayer) theObject;
					if(entity.isInvisible()) {
						mc.theWorld.removeEntity(entity);
					}
				}
			}
		}
	}
	
	
	
	
	public static boolean isBot(Entity entity) {
		if (Main.moduleManager.get("Antibot").isToggled()) {
			if (Antibot.mode.equalsIgnorecase("Watchdog2")) {
				return invalid.contains(entity) || !onAirInvalid.contains(entity.getEntityId());
			} 
			if (Main.moduleManager.get("Antibot").isToggled()) {
				if (Antibot.mode.equalsIgnorecase("Watchdog")) {
					if (entity.getDisplayName().getFormattedText().startsWith("\u00a7")
							&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")) {
						return false;
					}
					return true;
				}
				return false;
			}


		}

		return false;
	}
	
	
	
	
	
	
	public static boolean isInTablist(EntityPlayer player) {
		if (Minecraft.getMinecraft().isSingleplayer()) {
			return true;
		}
		for (NetworkPlayerInfo o : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
			NetworkPlayerInfo playerInfo = o;
			if (!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName()))
				continue;
			return true;
		}
		return false;
	}
	
	
	public List<EntityPlayer> getPlayerList() {
		Minecraft.getMinecraft();
		Collection<NetworkPlayerInfo> playerInfoMap = mc.thePlayer.sendQueue.getPlayerInfoMap();
		ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
		for (NetworkPlayerInfo networkPlayerInfo : playerInfoMap) {
			list.add(Minecraft.getMinecraft().theWorld
					.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
		}
		return list;
	}
	
	
    public  boolean isWatchdogBot(EntityPlayer e) {
        if (e.getGameProfile() == null) {
            return true;
        }
        NetworkPlayerInfo npi = mc.getNetHandler().getPlayerInfo(e.getGameProfile().getId());
        return (npi == null || npi.getGameProfile() == null || e.ticksExisted < 20 || npi.getResponseTime() != 1);
    }
}
