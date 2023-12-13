package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ScreenShotHelper;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.TimeHelper;

public class AutoGG extends Module {
    public BooleanValue gg = new BooleanValue("AutoGG", "GG", true);
    public BooleanValue ad = new BooleanValue("AutoGG", "AD", true);
    public BooleanValue screenshot = new BooleanValue("AutoGG", "ScreenShot", false);
    public BooleanValue autoplay = new BooleanValue("AutoGG", "AutoPlay", true);
    public FloatValue autoPlayDelay = new FloatValue("AutoGG", "AutoPlay Delay", 3000f, 500f, 10000f, 100f);
    public FloatValue delay = new FloatValue("AutoGG", "AutoGG Delay", 100f, 100f, 3000f, 100f);

    public boolean needSpeak = false;
    public boolean speaked = false;

    public TimeHelper timer = new TimeHelper();

    public String playCommand = "";
    public String lastTitle = "";

    public int win = 0;
    public int total = 0;

    public AutoGG() {
        super("AutoGG", Category.World, false);
    }

    public String content = "";
    public float animationY = 0;
    public boolean display = false;

    @Override
    public void onEnable() {
        playCommand = "";
        setTag("Empty");
    }

    @Override
    public void onDisable() {
        this.display = false;
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        if (mc.thePlayer != null) {
            if (needSpeak) {
                if (!speaked && timer.isDelayComplete(delay.getValueState())) {
                    speaked = true;
                    if (this.isEnabled()) {
                        if (gg.getValueState() && !mc.thePlayer.capabilities.isFlying) {
                            mc.thePlayer.sendChatMessage("/ac GG" + (ad.getValueState() ? " Buy Flux --> flux dot today" : ""));
                        }

                        if (screenshot.getValueState()) {
                            mc.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer()));
                        }
                    }
                }

                if (speaked) {
                    if (timer.isDelayComplete(autoPlayDelay.getValueState() + delay.getValueState())) {
                        speaked = false;
                        needSpeak = false;
                        this.display = false;
                        if (autoplay.getValueState() && this.isEnabled()) mc.thePlayer.sendChatMessage(playCommand);
                    } else {
                        this.display = autoplay.getValueState();
                    }
                }
            }
        }
    }

    @EventTarget
    public void onPacketR(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) e.getPacket();
            String title = packet.getMessage().getFormattedText();

            if ((title.startsWith("\2476\247l") && title.endsWith("\247r")) || (title.startsWith("\247c\247lYOU") && title.endsWith("\247r")) || (title.startsWith("\247c\247lGame") && title.endsWith("\247r")) || (title.startsWith("\247c\247lWITH") && title.endsWith("\247r")) || (title.startsWith("\247c\247lYARR") && title.endsWith("\247r"))) {
                total++;
            }

            if (title.startsWith("\2476\247l") && title.endsWith("\247r")) {
                win++;
            }

            if (title.startsWith("\2476\247l") && title.endsWith("\247r") || title.startsWith("\247c\247lY") && title.endsWith("\247r")) {
                timer.reset();
                needSpeak = true;
            }

            lastTitle = title;
        }
    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if (playCommand.startsWith("/play ")) {
            String display = playCommand.replace("/play ", "").replace("_", " ");
            boolean nextUp = true;
            String result = "";
            for (char c : display.toCharArray()) {
                if (c == ' ') {
                    nextUp = true;
                    result += " ";
                    continue;
                }
                if (nextUp) {
                    nextUp = false;
                    result += Character.toUpperCase(c);
                } else {
                    result += c;
                }
            }
            this.setTag(result);
        } else {
            this.setTag("Empty");
        }

        if (e.getPacket() instanceof C0EPacketClickWindow) {
            C0EPacketClickWindow packet = (C0EPacketClickWindow) e.getPacket();
            String itemname = packet.getClickedItem().getDisplayName();
            if (packet.getClickedItem().getDisplayName().startsWith("\247a")) {
                int itemID = Item.getIdFromItem(packet.getClickedItem().getItem());
                if (itemID == 381 || itemID == 368) {
                    if (itemname.contains("空岛战争") || itemname.contains("SkyWars")) {
                        if (itemname.contains("双人") || itemname.contains("Doubles")) {
                            if (itemname.contains("普通") || itemname.contains("Normal")) {
                                playCommand = "/play teams_normal";
                            } else if (itemname.contains("疯狂") || itemname.contains("Insane")) {
                                playCommand = "/play teams_insane";
                            }
                        } else if (itemname.contains("单挑") || itemname.contains("Solo")) {
                            if (itemname.contains("普通") || itemname.contains("Normal")) {
                                playCommand = "/play solo_normal";
                            } else if (itemname.contains("疯狂") || itemname.contains("Insane")) {
                                playCommand = "/play solo_insane";
                            }
                        }
                    }
                } else if (itemID == 355) {
                    if (itemname.contains("起床战争") || itemname.contains("Bed Wars")) {
                        if (itemname.contains("4v4")) {
                            playCommand = "/play bedwars_four_four";
                        } else if (itemname.contains("3v3")) {
                            playCommand = "/play bedwars_four_three";
                        } else if (itemname.contains("双人模式") || itemname.contains("Doubles")) {
                            playCommand = "/play bedwars_eight_two";
                        } else if (itemname.contains("单挑") || itemname.contains("Solo")) {
                            playCommand = "/play bedwars_eight_one";
                        }
                    }
                }
            }
        } else if (e.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packet = (C01PacketChatMessage) e.getPacket();
            if (packet.getMessage().startsWith("/play")) {
                playCommand = packet.getMessage();
            }
        }
    }

}