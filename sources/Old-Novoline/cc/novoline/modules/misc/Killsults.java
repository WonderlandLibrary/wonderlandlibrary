package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.LoadWorldEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import cc.novoline.utils.tasks.FutureTask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static cc.novoline.gui.screen.setting.SettingType.CHECKBOX;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanFalse;

public final class Killsults extends AbstractModule {

    /* fields */
    private List<EntityPlayer> lastTargets = new CopyOnWriteArrayList<>();
    private List<String> killsults = new CopyOnWriteArrayList<>();
    private final Timer timer = new Timer();
    private Path path = Paths.get(novoline.getPathString() + "Killsults.novo");
    private int order;
    private boolean spectator;

    /* properties @off */
    @Property("random-order")
    private final BooleanProperty randomOrder = booleanFalse();

    /* constructors @on */
    public Killsults(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Killsults", "Killsults", EnumModuleType.MISC, "Insults your opponents");
        Manager.put(new Setting("KS_RANDOM", "Random Order", CHECKBOX, this, this.randomOrder));
    }

    /* methods */
    @Override
    public void onDisable() {
        if (!lastTargets.isEmpty()) {
            lastTargets.clear();
        }

        spectator = false;
        order = 0;
    }

    @Override
    public void onEnable() {
        loadSults();
    }

    public void loadSults() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
            List<String> list = reader.lines().collect(Collectors.toList());

            if (!list.isEmpty()) {
                for (String str : list) {
                    if (!killsults.contains(str)) {
                        killsults.add(str);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private int delay() {
        String name = mc.player.getDisplayName().getFormattedText();
        return name.contains("VIP") || name.contains("MVP") ? 1100 : 3100;
    }

    public int neededDelay() {
        return (int) (delay() - MathHelper.clamp_float(timer.getCurrentMS() - timer.getLastMS(), 0, (float) delay()));
    }

    private void sendSultMessage(EntityPlayer player) {
        if (randomOrder.get()) {
            order = ThreadLocalRandom.current().nextInt(0, killsults.size());
        } else {
            order++;

            if (order >= killsults.size()) {
                order = 0;
            }
        }

        String fromList = killsults.get(order);
        String string = fromList.replace("\u00A7", "").replace("%s", player.getName());
        char[] chars = string.toCharArray();
        StringBuilder sult = new StringBuilder();

        for (char aChar : chars) {
            sult.append(aChar);

            for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, 3); i++) {
                sult.append('\u05fc');
            }
        }

        sendPacketNoEvent(new C01PacketChatMessage(sult.toString()));
        lastTargets.remove(player);
        timer.reset();
    }

    /* events */
    @EventTarget
    public void onMessage(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S2FPacketSetSlot) {
                S2FPacketSetSlot packet = (S2FPacketSetSlot) event.getPacket();

                if (packet.getItem().getDisplayName().contains("\u00A7") && packet.getItem().getDisplayName().contains("Spectator")) {
                    spectator = true;
                }
            }

            if (!ServerUtils.serverIs(Servers.PRE) && !ServerUtils.serverIs(Servers.LOBBY)) {
                if (!spectator && event.getPacket() instanceof S38PacketPlayerListItem) {
                    S38PacketPlayerListItem packet = (S38PacketPlayerListItem) event.getPacket();

                    for (S38PacketPlayerListItem.AddPlayerData playerData : packet.playersDataList()) {
                        EntityPlayer player = mc.world.getPlayerEntityByUUID(playerData.getProfile().getId());

                        if (packet.getAction().equals(S38PacketPlayerListItem.Action.REMOVE_PLAYER) && !player.equals(mc.player)) {
                            if (playerData.getProfile().getName() == null && player.getHealth() < player.getMaxHealth() && lastTargets.contains(player)) {
                                if (timer.delay(delay())) {
                                    sendSultMessage(player);
                                } else {
                                    novoline.getTaskManager().queue(new FutureTask(neededDelay()) {
                                        @Override
                                        public void execute() {
                                            sendSultMessage(player);
                                        }

                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }

            } else if (spectator) {
                spectator = false;
            }
        }
    }

    @EventTarget
    public void onLoadWorld(LoadWorldEvent event) {
        if (!lastTargets.isEmpty()) {
            lastTargets.clear();
        }

        spectator = false;
        order = 0;
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (isEnabled(KillAura.class) && getModule(KillAura.class).shouldAttack()) {
            KillAura killAura = getModule(KillAura.class);

            if (killAura.getTarget() != null && killAura.getTarget() instanceof EntityPlayer) {
                EntityPlayer target = (EntityPlayer) killAura.getTarget();

                if (!lastTargets.contains(target)) {
                    lastTargets.add(target);
                }
            }
        }
    }

    public List<String> getKillsults() {
        return killsults;
    }

    public Path getPath() {
        return path;
    }
}
