package cc.novoline.modules.misc;

import cc.novoline.Novoline;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.Channels;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import cc.novoline.utils.notifications.NotificationType;
import cc.novoline.utils.tasks.FutureTask;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.EnumChatFormatting;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.createString;

public class AutoHypixel extends AbstractModule {

    @Property("reconnect")
    private final BooleanProperty reconnect = PropertyFactory.booleanFalse();
    @Property("quick_math")
    private final BooleanProperty quick_math = PropertyFactory.booleanFalse();
    @Property("auto-play")
    private final BooleanProperty auto_play = PropertyFactory.booleanFalse();
    @Property("delay")
    private final IntProperty delay = PropertyFactory.createInt(3).minimum(1).maximum(10);
    @Property("auto-pit")
    private final BooleanProperty auto_pit = PropertyFactory.booleanFalse();
    @Property("auto-sw")
    private final BooleanProperty auto_sw = PropertyFactory.booleanFalse();
    @Property("auto-uhc")
    private final BooleanProperty auto_uhc = PropertyFactory.booleanFalse();
    @Property("craft-list")
    private final ListProperty<String> crafts = PropertyFactory.createList("Planks").acceptableValues("Planks");
    @Property("min-bounty")
    private final IntProperty min_bounty = PropertyFactory.createInt(500).minimum(50).maximum(5000);
    @Property("auto-tar")
    private final BooleanProperty auto_tar = PropertyFactory.booleanFalse();
    @Property("auto-gg")
    private final BooleanProperty auto_gg = PropertyFactory.booleanFalse();
    @Property("message")
    private final StringProperty message = createString("GG");
    @Property("death-tp")
    private final BooleanProperty death_tp = PropertyFactory.booleanFalse();
    @Property("auto-paper")
    private final BooleanProperty auto_paper = PropertyFactory.booleanFalse();

    private List<Entity> entities = new CopyOnWriteArrayList<>();
    private boolean send, queued;
    private int tpX, tpY, tpZ;
    private Timer timer = new Timer();

    public AutoHypixel(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MISC, "AutoHypixel", "Auto Hypixel");
        Manager.put(new Setting("AH_CRAFTS", "Auto Crafts", SettingType.SELECTBOX, this, crafts));
        Manager.put(new Setting("AH_PIT_DEATH_TP", "Death TP", SettingType.CHECKBOX, this, death_tp));
        Manager.put(new Setting("AH_UHC_UHC", "Auto UHC", SettingType.CHECKBOX, this, auto_uhc));
        Manager.put(new Setting("AH_PIT", "Auto Pit", SettingType.CHECKBOX, this, auto_pit));
        Manager.put(new Setting("AH_UHC_QM", "Quick Math", SettingType.CHECKBOX, this, quick_math, auto_pit::get));
        Manager.put(new Setting("AH_UHC_AT", "Target Bounty", SettingType.CHECKBOX, this, auto_tar, auto_pit::get));
        Manager.put(new Setting("AH_BOUNTY", "Minimal Bounty", SettingType.SLIDER, this, min_bounty, 50, () -> auto_pit.get() && auto_tar.get()));
        Manager.put(new Setting("AH_UHC_REC", "Reconnect", SettingType.CHECKBOX, this, reconnect));
        Manager.put(new Setting("AH_UHC_AA", "Auto Play", SettingType.CHECKBOX, this, auto_play));
        Manager.put(new Setting("AP_DELAY", "Delay (Seconds)", SettingType.SLIDER, this, this.delay, 1, auto_play::get));
        Manager.put(new Setting("AH_AUTO_GG", "Auto GG", SettingType.CHECKBOX, this, auto_gg));
        Manager.put(new Setting("AH_AUTO_GG_TEXT", "Message", SettingType.TEXTBOX, this, "Message text", this.message, auto_gg::get));
        Manager.put(new Setting("AH_SW", "Auto SkyWars", SettingType.CHECKBOX, this, auto_sw));
        Manager.put(new Setting("AH_SW_APG", "Paper Challenge", SettingType.CHECKBOX, this, auto_paper, auto_sw::get));
    }

    @EventTarget
    public void onPreUpdate(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            for (Entity playerEntity : entities) {
                if (!mc.world.getPlayerEntities().contains(playerEntity)) {
                    entities.remove(playerEntity);
                    novoline.getPlayerManager().removeType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);
                }
            }

            if (crafts.contains("Planks")) {
                for (int slotIndex = 0; slotIndex < 36; slotIndex++) {
                    ItemStack stack = mc.player.inventory.getStackInSlot(slotIndex);

                    if (stack != null) {
                        if (Item.getIdFromItem(stack.getItem()) == 17 || Item.getIdFromItem(stack.getItem()) == 162) {
                            int slot = slotIndex < 9 ? slotIndex + 36 : slotIndex;
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, 0, mc.player);
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 3, 0, 0, mc.player); //крафт слот 1-4
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 0, 0, 1, mc.player);
                        }
                    }
                }
            }

            if (auto_pit.get()) {
                if (auto_tar.get()) {
                    for (EntityPlayer playerEntity : mc.world.getPlayerEntities()) {
                        if (!playerEntity.equals(mc.player)) {
                            String displayName = playerEntity.getDisplayName().getUnformattedText();
                            String name = playerEntity.getName();

                            if (displayName.contains("\u00A76\u00A7l")) {
                                String[] split = displayName.split(" ");
                                if (split.length > 2) {
                                    int bounty = Integer.parseInt(
                                            split[split.length - 1].
                                                    replace("\u00A76\u00A7l", "").
                                                    replace("g", ""));
                                    if (bounty >= min_bounty.get()) {
                                        if (!entities.contains(playerEntity) && !novoline.getPlayerManager().hasType(playerEntity.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
                                            entities.add(playerEntity);
                                            novoline.getNotificationManager().pop("Found a bountied player", playerEntity.getName() + " has " + EnumChatFormatting.GOLD + bounty + "g" + EnumChatFormatting.RESET + " on him!", 7000, NotificationType.INFO);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (Entity playerEntity : entities) {
                        if (novoline.getPlayerManager().hasType(playerEntity.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
                            entities.remove(playerEntity);
                            novoline.getPlayerManager().removeType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);
                            break;
                        }
                        String displayName = playerEntity.getDisplayName().getUnformattedText();
                        String name = playerEntity.getName();

                        novoline.getPlayerManager().setType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);

                        if (displayName.contains("\u00A76\u00A7l")) {
                            String[] split = displayName.split(" ");
                            if (split.length < 3) {
                                entities.remove(playerEntity);
                                novoline.getPlayerManager().removeType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);
                            } else {
                                int bounty = Integer.parseInt(
                                        split[split.length - 1].
                                                replace("\u00A76\u00A7l", "").
                                                replace("g", ""));
                                if (bounty < min_bounty.get()) {
                                    entities.remove(playerEntity);
                                    novoline.getPlayerManager().removeType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);
                                }
                            }
                        } else {
                            entities.remove(playerEntity);
                            novoline.getPlayerManager().removeType(playerEntity.getName(), PlayerManager.EnumPlayerType.TARGET);
                        }

                    }
                }
            }
        }
    }

    @EventTarget
    public void features(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (reconnect.get()) {
                reconnect(event);
            }

            if (auto_play.get()) {
                autoplay(event);
            }

            if (auto_pit.get() && quick_math.get()) {
                quickmath(event);
            }

            if (death_tp.get()) {
                deathtp(event);
            }

            if (auto_gg.get()) {
                autogg(event);
            }
        }
    }

    private void reconnect(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            String message = packet.getChatComponent().getUnformattedText();

            if (message.contains("Flying or related")) {
                sendPacketNoEvent(new C01PacketChatMessage("/back"));
            }
        }
    }

    private void autoplay(PacketEvent packetEventReceive) {
        if (packetEventReceive.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat) packetEventReceive.getPacket();
            final String command = packet.getChatComponent().toString().split("action=RUN_COMMAND, value='")[1];

            if (command.startsWith("/play ")) {
                final String split = command.split("'}")[0];
                Novoline.getInstance().getNotificationManager().pop("Sending you to the next game in ", delay.get() * 1000, NotificationType.INFO);

                Novoline.getInstance().getTaskManager().queue(new FutureTask(this.delay.get() * 1_000) {

                    @Override
                    public void execute() {
                        sendPacket(new C01PacketChatMessage(split));
                    }

                    @Override
                    public void run() {
                    }
                });
            }
        }
    }

    private void autogg(PacketEvent e) {
        if (e.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) e.getPacket();

            if (!isEnabled(Spammer.class) && packet.getMessage().getUnformattedText().equals("VICTORY!")) {
                if (message.get().startsWith("/")) {
                    sendPacketNoEvent(new C01PacketChatMessage(message.get()));
                } else if (!ServerUtils.isHypixel() || !ServerUtils.channelIs(Channels.PM)) {
                    if (message.get().toLowerCase().startsWith("gg")) {
                        sendPacketNoEvent(new C01PacketChatMessage(message.get()));
                    } else {
                        sendPacket(new C01PacketChatMessage(message.get()));
                    }
                }
            }
        }
    }

    private void quickmath(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packetChat = (S02PacketChat) event.getPacket();
            String text = packetChat.getChatComponent().getUnformattedText();

            if (text.contains("QUICK MATHS! Solve:")) {
                String[] eArray = text.split("Solve: ");
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");

                try {
                    sendPacketNoEvent(new C01PacketChatMessage(engine.eval(eArray[1].replace("x", "*")).toString()));
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deathtp(PacketEvent event) {
        if (ServerUtils.isHypixel()) {
            if (event.getState().equals(PacketEvent.State.INCOMING)) {
                if (ServerUtils.serverIs(Servers.PIT)) {
                    if (event.getPacket() instanceof S45PacketTitle) {
                        S45PacketTitle title = (S45PacketTitle) event.getPacket();

                        if (title.getMessage().getFormattedText().contains("YOU DIED") || title.getMessage().getFormattedText().contains("VOUS ETES MORT")) {
                            tpX = (int) mc.player.posX;
                            tpY = (int) mc.player.posY;
                            tpZ = (int) mc.player.posZ;
                            mc.player.sendChatMessage(".tp " + tpX + " " + tpY + " " + tpZ + " " + "goteleportnigga");
                        }
                    }
                } else if (ServerUtils.serverIs(Servers.BW)) {
                    if (event.getPacket() instanceof S39PacketPlayerAbilities) {
                        S39PacketPlayerAbilities packet = (S39PacketPlayerAbilities) event.getPacket();

                        if (packet.isFlying()) {
                            timer.reset();
                        }
                    }

                    if (event.getPacket() instanceof S45PacketTitle && !queued) {
                        S45PacketTitle title = (S45PacketTitle) event.getPacket();

                        if (title.getMessage().getFormattedText().contains("YOU DIED")) {
                            send = true;
                            queued = true;
                        }
                    }


                    if (send) {
                        novoline.getTaskManager().queue(new FutureTask(5100) {
                            @Override
                            public void execute() {
                                mc.player.sendChatMessage(".tp " + tpX + " " + tpY + " " + tpZ + " " + "goteleportnigga");
                                queued = false;
                            }

                            @Override
                            public void run() {
                            }
                        });
                        send = false;
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (death_tp.get() && ServerUtils.serverIs(Servers.BW)) {
            if (mc.player.onGround && mc.player.posY % 0.125 == 0) {
                tpX = (int) mc.player.posX;
                tpY = (int) mc.player.posY;
                tpZ = (int) mc.player.posZ;
            }
        }
    }

    private int getAndurilInHotbar() {
        int slot = -1;

        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i) != null && mc.player.inventory.getStackInSlot(i).stackSize != 0) {
                ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getDisplayName().equalsIgnoreCase("\u00a7aAnd\u00faril") && itemStack != null && itemStack.stackSize != 0) {
                    slot = i;
                }
            }
        }

        return slot;
    }

    @Override
    public void onDisable() {
        if (!entities.isEmpty()) {
            entities.clear();
        }
    }

    public boolean isAutoSW() {
        return auto_sw.get();
    }

    public boolean isAutoPaper() {
        return auto_paper.get();
    }

    public BooleanProperty getDeath_tp() {
        return death_tp;
    }
}
