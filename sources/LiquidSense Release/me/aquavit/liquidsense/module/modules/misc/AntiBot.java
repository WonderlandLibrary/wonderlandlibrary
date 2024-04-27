package me.aquavit.liquidsense.module.modules.misc;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.AttackEvent;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.event.events.WorldEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.timer.TimerOther;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ModuleInfo(name = "AntiBot", description = "Prevents KillAura from attacking AntiCheat bots.", category = ModuleCategory.MISC)
public class AntiBot extends Module {

    public static final List<String> playerName = new ArrayList<>();
    private static List<EntityPlayer> invalid = new ArrayList<>();
    private static List<EntityPlayer> removed = new ArrayList<>();

    private final IntegerValue livingTime = new IntegerValue("LivingTimeValue", 160, 0, 500);
    private final BoolValue killer = new BoolValue("BotKiller", true);
    private final BoolValue tabValue = new BoolValue("Tab", true);
    private final ListValue tabModeValue = new ListValue("TabMode", new String[]{"Equals", "Contains"}, "Contains");
    private final BoolValue entityIDValue = new BoolValue("EntityID", true);
    private final BoolValue colorValue = new BoolValue("Color", false);
    private final BoolValue livingTimeValue = new BoolValue("LivingTime", false);
    private final BoolValue groundValue = new BoolValue("Ground", true);
    private final BoolValue airValue = new BoolValue("Air", false);
    private final BoolValue invaildGroundValue = new BoolValue("InvaildGround", true);
    private final BoolValue swingValue = new BoolValue("Swing", false);
    private final BoolValue healthValue = new BoolValue("Health", false);
    private final BoolValue derpValue = new BoolValue("Derp", true);
    private final BoolValue wasInvisibleValue = new BoolValue("WasInvisible", false);
    private final BoolValue armorValue = new BoolValue("Armor", false);
    private final BoolValue pingValue = new BoolValue("Ping", false);
    private final BoolValue needHitValue = new BoolValue("NeedHit", false);
    private final BoolValue duplicateInWorldValue = new BoolValue("DuplicateInWorld", false);
    private final BoolValue duplicateInTabValue = new BoolValue("DuplicateInTab", false);
    private final BoolValue hytBedwarsValue = new BoolValue("HYTBedwars", false);

    private final List<Integer> ground = new ArrayList<>();
    private final List<Integer> air = new ArrayList<>();
    private final Map<Integer, Integer> invaildGround = new HashMap<>();
    private final List<Integer> swing = new ArrayList<>();
    private final List<Integer> invisible = new ArrayList<>();
    private final List<Integer> hitted = new ArrayList<>();
    TimerOther lastRemoved = new TimerOther();
    private TimerOther timer = new TimerOther();

    @Override
    public void onDisable() {
        mc.ingameGUI.getChatGUI().addToSentMessages("test");
        clearAll();
        super.onDisable();
    }
    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (killer.get()) {
            if (!removed.isEmpty()) {
                if (lastRemoved.delay(1000)) {
                    if (removed.size() == 1) {
                        mc.thePlayer.addChatMessage(new ChatComponentText("§b"+removed.size() + " bot has been removed"));
                    } else {
                        mc.thePlayer.addChatMessage(new ChatComponentText("§f"+removed.size() + " bots has been removed"));
                    }
                    lastRemoved.reset();
                    removed.clear();
                }
            }
        }
        if (!invalid.isEmpty() && timer.delay(1000)) {
            invalid.clear();
            timer.reset();
        }
        // Loop through entity list
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer) o;
                //Make sure it's not the local player + they are in a worrying distance. Ignore them if they're already invalid.
                if (ent != mc.thePlayer && !invalid.contains(ent)) {

                    String formated = ent.getDisplayName().getFormattedText();
                    String custom = ent.getCustomNameTag();
                    String name = ent.getName();

                    if (ent.isInvisible() && !formated.startsWith("§c") && formated.endsWith("§r") && custom.equals(name)) {
                        double diffX = Math.abs(ent.posX - mc.thePlayer.posX);
                        double diffY = Math.abs(ent.posY - mc.thePlayer.posY);
                        double diffZ = Math.abs(ent.posZ - mc.thePlayer.posZ);
                        double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
                        if (diffY < 13 && diffY > 10 && diffH < 3) {
                            List<EntityPlayer> list = getTabPlayerList();
                            if (!list.contains(ent)) {
                                if (killer.get() != null) {
                                    lastRemoved.reset();
                                    removed.add(ent);
                                    mc.theWorld.removeEntity(ent);
                                }
                                invalid.add(ent);
                            }

                        }

                    }
                    //SHOP BEDWARS
                    if (!formated.startsWith("§") && formated.endsWith("§r")) {
                        invalid.add(ent);
                    }
                    if (ent.isInvisible()) {
                        //BOT INVISIBLES IN GAME
                        if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("§c§c") && name.contains("§c")) {
                            if (killer.get() != null) {
                                lastRemoved.reset();
                                removed.add(ent);
                                mc.theWorld.removeEntity(ent);
                            }
                            invalid.add(ent);
                        }
                    }
                    //WATCHDOG BOT
                    if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("§c") && custom.toLowerCase().contains("§r") || custom.endsWith("§c")) {
                        if (killer.get() != null) {
                            lastRemoved.reset();
                            removed.add(ent);
                            mc.theWorld.removeEntity(ent);
                        }
                        invalid.add(ent);
                    }

                    //BOT LOBBY
                    if (formated.contains("§8[NPC]")) {
                        invalid.add(ent);
                    }
                    if (!formated.contains("§c") && !custom.equalsIgnoreCase("")) {

                        invalid.add(ent);
                    }
                }
            }
        }
    }

    public static List<EntityPlayer> getTabPlayerList() {
        Minecraft mc = AntiBot.mc;
        NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
        ArrayList list = new ArrayList();
        List players = Ordering.from(new PlayerComparator()).sortedCopy((Iterable) var4.getPlayerInfoMap());
        for (Object o : players) {
            NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            ArrayList list2 = list;
            list2.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return (List<EntityPlayer>) list;
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null)
            return;

        final Packet<?> packet = event.getPacket();


        if (packet instanceof S02PacketChat) {
            if (hytBedwarsValue.get()) {
                S02PacketChat chatMessage = (S02PacketChat) packet;

                Matcher matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());
                Matcher matcher2 = Pattern.compile("> (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());
                if (matcher.find()) {
                    String name = matcher.group(1);
                    if (!name.equals("")) {
                        if (!AntiBot.playerName.contains(name)) {
                            AntiBot.playerName.add(name);
//                        mc.thePlayer.addChatMessage(new ChatComponentText("已加入列表: " + name));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(6000);
                                        playerName.remove(name);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
                if (matcher2.find()) {
                    String name = matcher2.group(1);
                    if (!name.equals("")) {
                        if (!AntiBot.playerName.contains(name)) {
                            AntiBot.playerName.add(name);
//                        mc.thePlayer.addChatMessage(new ChatComponentText("已加入列表: " + name));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(6000);
                                        playerName.remove(name);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
            }
        }

        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity packetEntity = (S14PacketEntity) event.getPacket();
            final Entity entity = packetEntity.getEntity(mc.theWorld);

            if (entity instanceof EntityPlayer) {
                if (packetEntity.getOnGround() && !ground.contains(entity.getEntityId()))
                    ground.add(entity.getEntityId());

                if (!packetEntity.getOnGround() && !air.contains(entity.getEntityId()))
                    air.add(entity.getEntityId());

                if (packetEntity.getOnGround()) {
                    if (entity.prevPosY != entity.posY)
                        invaildGround.put(entity.getEntityId(), invaildGround.getOrDefault(entity.getEntityId(), 0) + 1);
                } else {
                    final int currentVL = invaildGround.getOrDefault(entity.getEntityId(), 0) / 2;

                    if (currentVL <= 0)
                        invaildGround.remove(entity.getEntityId());
                    else
                        invaildGround.put(entity.getEntityId(), currentVL);
                }

                if (entity.isInvisible() && !invisible.contains(entity.getEntityId()))
                    invisible.add(entity.getEntityId());
            }
        }

        if (packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation packetAnimation = (S0BPacketAnimation) event.getPacket();
            final Entity entity = mc.theWorld.getEntityByID(packetAnimation.getEntityID());

            if (entity instanceof EntityLivingBase && packetAnimation.getAnimationType() == 0 && !swing.contains(entity.getEntityId()))
                swing.add(entity.getEntityId());
        }
    }

    @EventTarget
    public void onAttack(final AttackEvent e) {
        final Entity entity = e.getTargetEntity();

        if (entity instanceof EntityLivingBase && !hitted.contains(entity.getEntityId()))
            hitted.add(entity.getEntityId());
    }

    @EventTarget
    public void onWorld(final WorldEvent event) {
        clearAll();
    }

    private void clearAll() {
        hitted.clear();
        swing.clear();
        ground.clear();
        invaildGround.clear();
        invisible.clear();
        playerName.clear();
    }

    public static boolean isBot(final EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer))
            return false;

        final AntiBot antiBot = (AntiBot) LiquidSense.moduleManager.getModule(AntiBot.class);

//        mc.ingameGUI.getChatGUI().addToSentMessages("tick: " + entity.ticksExisted);

        if (antiBot == null || !antiBot.getState())
            return false;
        if (antiBot.colorValue.get() && !entity.getDisplayName().getFormattedText()
                .replace("§r", "").contains("§"))
            return true;

//        mc.thePlayer.addChatMessage(new ChatComponentText(entity.getName()
//                + " " + playerName.contains(entity.getName())
//                + " " + playerName.size()));
        if (antiBot.hytBedwarsValue.get() && playerName.contains(entity.getName()))
            return true;

        if (antiBot.livingTimeValue.get() && entity.ticksExisted < antiBot.livingTime.get())
            return true;

        if (antiBot.groundValue.get() && !antiBot.ground.contains(entity.getEntityId()))
            return true;

        if (antiBot.airValue.get() && !antiBot.air.contains(entity.getEntityId()))
            return true;

        if (antiBot.swingValue.get() && !antiBot.swing.contains(entity.getEntityId()))
            return true;

        if (antiBot.healthValue.get() && entity.getHealth() > 20F)
            return true;

        if (antiBot.entityIDValue.get() && (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= -1))
            return true;

        if (antiBot.derpValue.get() && (entity.rotationPitch > 90F || entity.rotationPitch < -90F))
            return true;

        if (antiBot.wasInvisibleValue.get() && antiBot.invisible.contains(entity.getEntityId()))
            return true;

        if (antiBot.armorValue.get()) {
            final EntityPlayer player = (EntityPlayer) entity;

            if (player.inventory.armorInventory[0] == null && player.inventory.armorInventory[1] == null &&
                    player.inventory.armorInventory[2] == null && player.inventory.armorInventory[3] == null)
                return true;
        }

        if (antiBot.pingValue.get()) {
            EntityPlayer player = (EntityPlayer) entity;

            if (mc.getNetHandler().getPlayerInfo(player.getUniqueID()).getResponseTime() == 0)
                return true;
        }

        if (antiBot.needHitValue.get() && !antiBot.hitted.contains(entity.getEntityId()))
            return true;

        if (antiBot.invaildGroundValue.get() && antiBot.invaildGround.getOrDefault(entity.getEntityId(), 0) >= 10)
            return true;

        if (antiBot.tabValue.get()) {
            final boolean equals = antiBot.tabModeValue.get().equalsIgnoreCase("Equals");
            final String targetName = ColorUtils.stripColor(entity.getDisplayName().getFormattedText());

            if (targetName != null) {
                for (final NetworkPlayerInfo networkPlayerInfo : mc.getNetHandler().getPlayerInfoMap()) {
                    final String networkName = ColorUtils.stripColor(EntityUtils.getName(networkPlayerInfo));

                    if (networkName == null)
                        continue;

                    if (equals ? targetName.equals(networkName) : targetName.contains(networkName))
                        return false;
                }

                return true;
            }
        }

        if (antiBot.duplicateInWorldValue.get()) {
            if (mc.theWorld.loadedEntityList.stream()
                    .filter(currEntity -> currEntity instanceof EntityPlayer && ((EntityPlayer) currEntity)
                            .getDisplayNameString().equals(((EntityPlayer) currEntity).getDisplayNameString()))
                    .count() > 1)
                return true;
        }

        if (antiBot.duplicateInTabValue.get()) {
            if (mc.getNetHandler().getPlayerInfoMap().stream()
                    .filter(networkPlayer -> entity.getName().equals(ColorUtils.stripColor(EntityUtils.getName(networkPlayer))))
                    .count() > 1)
                return true;
        }

        return entity.getName().isEmpty() || entity.getName().equals(mc.thePlayer.getName());
    }

    @SideOnly(Side.CLIENT)
    public static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start()
                    .compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR,
                            p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR)
                    .compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "",
                            scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "")
                    .compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
