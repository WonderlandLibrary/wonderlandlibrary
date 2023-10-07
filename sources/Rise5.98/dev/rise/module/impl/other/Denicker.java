package dev.rise.module.impl.other;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.rise.Rise;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.render.InGameBlurUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;

@ModuleInfo(name = "Denicker", description = "Denicks any nicked players in your game", category = Category.OTHER)
public class Denicker extends Module {

    private final BooleanSetting overlaySupport = new BooleanSetting("Overlay Support", this, true);

    private ArrayList<String> denickQueue = new ArrayList<String>();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem s38 = (S38PacketPlayerListItem) event.getPacket();

            if (s38.func_179768_b().equals(S38PacketPlayerListItem.Action.ADD_PLAYER) && (s38.func_179767_a().get(0) != null)) {
                try {
                    S38PacketPlayerListItem.AddPlayerData addPlayerData = s38.func_179767_a().get(0);
                    GameProfile gameProfile = addPlayerData.getProfile();
                    PropertyMap propertyMap = gameProfile.getProperties();
                    String nickedName = gameProfile.getName();

                    if (Objects.equals(nickedName, mc.thePlayer.getName())) return;

                    // get base64 skin data from s38
                    Collection<Property> values = propertyMap.get("textures");
                    if (values.isEmpty()) return;

                    String skinDataRaw = ((Property) values.toArray()[0]).getValue();
                    String skinData = new String(Base64.getDecoder().decode(skinDataRaw)); // decode base 64

                    // json decoding
                    JsonObject jsonObject = new JsonParser().parse(skinData).getAsJsonObject();
                    String realName = jsonObject.get("profileName").getAsString();

                    if (!Objects.equals(realName, nickedName) && !denickQueue.contains(nickedName)) {
                        denickQueue.add(realName + "=" + nickedName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) event.getPacket()).chatComponent.getUnformattedText();

            if (message.contains("has joined") && !denickQueue.isEmpty()) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (String s : denickQueue) {

                            String realName = s.split("=")[0];
                            String nickedName = s.split("=")[1];

                            if (message.contains(s.split("=")[1])) {
                                Rise.INSTANCE.getNotificationManager().registerNotification(realName + " is nicked as " + nickedName, 7500, NotificationType.NOTIFICATION);

                                if (overlaySupport.isEnabled()) {
                                    logger.info("[CHAT] " + StringUtils.stripControlCodes(message.replace(nickedName, realName)));
                                }
                            }
                        }
                    }
                });

                thread.start();
            }
        }
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        denickQueue.clear();
    }
}
