package tech.dort.dortware.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.RandomUtils;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.networking.PacketUtil;
import tech.dort.dortware.impl.utils.player.ChatUtil;
import tech.dort.dortware.impl.utils.time.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutoHypixel extends Module {

    private final List<EntityPlayer> reportedPlayers = new ArrayList<>();
    private final String[] cheats = {"flight", "killaura", "autoclicker", "speed", "antikb", "reach", "jesus"};

    private final Stopwatch stopwatch = new Stopwatch();

    public static String ggMsg = "GG";

    private boolean sendPlay;
    public boolean enabledPaper;
    private int prevSlot;

    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, AutoHypixel.Mode.values());
    private final NumberValue delay = new NumberValue("Report Delay", this, 3, 1, 10, true);
    private final BooleanValue autoGG = new BooleanValue("Auto GG", this, true);
    private final BooleanValue autoPlay = new BooleanValue("Auto Play", this, true);
    private final BooleanValue antiAtlas = new BooleanValue("Report All", this, true);
    private final BooleanValue paperChallenge = new BooleanValue("Paper Challenge", this, true);

    public AutoHypixel(ModuleData moduleData) {
        super(moduleData);
        register(mode, delay, autoGG, autoPlay, antiAtlas, paperChallenge);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (sendPlay) {
            PacketUtil.sendPacketNoEvent(new C01PacketChatMessage(String.format("/play %s", mode.getValue().name().toLowerCase())));
            sendPlay = false;
        }

        if (mc.thePlayer.ticksExisted < 5) {
            enabledPaper = false;
        }

        if (antiAtlas.getValue()) {
            for (EntityPlayer player : mc.theWorld.playerEntities.stream().filter(player -> player != mc.thePlayer && !reportedPlayers.contains(player)).collect(Collectors.toList())) {
                if (stopwatch.timeElapsed(delay.getValue().longValue() * 1000L) && player.isValid()) {
                    mc.thePlayer.sendChatMessage("/wdr " + player.getName() + " " + cheats[RandomUtils.nextInt(0, cheats.length - 1)]);
                    stopwatch.resetTime();
                    reportedPlayers.add(player);
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer != null && event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packetChat = event.getPacket();

            switch (packetChat.getChatComponent().getUnformattedText()) {
                case "You won! Want to play again? Click here! ":
                    sendPlay = autoPlay.getValue();

                    // TODO save gg message like module settings
                    if (autoGG.getValue()) {
                        mc.thePlayer.sendChatMessage(ggMsg);
                    }

                    stopwatch.resetTime();
                    break;

                case "You died! Want to play again? Click here! ":
                    sendPlay = autoPlay.getValue();
                    break;

                case "Thanks for your Cheating report. We understand your concerns and it will be reviewed as soon as possible.":
                    event.forceCancel(antiAtlas.getValue());
                    break;

                case "The game starts in 5 seconds!":
                    if (paperChallenge.getValue()) {
                        prevSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.closeScreen();
                        PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(7));
                        PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    }
                    break;

                case "The game starts in 4 seconds!":
                    if (paperChallenge.getValue()) {
                        if (mc.thePlayer.openContainer instanceof ContainerChest) {
                            final ContainerChest containerChest = (ContainerChest) mc.thePlayer.openContainer;

                            if (containerChest.getLowerChestInventory().getStackInSlot(7) != null) {
                                mc.playerController.windowClick(containerChest.windowId, 7, 0, 3, mc.thePlayer);
                            }

                            mc.thePlayer.closeScreen();
                        }

                        PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(prevSlot));
                        mc.thePlayer.closeScreen();
                        ChatUtil.displayChatMessage("Attempted to enable Paper Challenge.");
                    }
                    break;

                case "You have activated the Paper Challenge!":
                    mc.thePlayer.closeScreen();
                    enabledPaper = true;
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        sendPlay = false;
        stopwatch.resetTime();
        reportedPlayers.clear();
    }

    private enum Mode implements INameable {
        SOLO_INSANE("Solo Insane"), TEAMS_INSANE("Teams Insane"), SOLO_NORMAL("Solo Normal"), TEAMS_NORMAL("Teams Normal");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return this.name;
        }
    }
}
