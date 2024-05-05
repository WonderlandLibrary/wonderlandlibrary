// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.player.EventAttackClient;
import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ListSetting;
import java.util.ArrayList;
import java.util.List;
import ru.fluger.client.feature.Feature;

public class AntiBot extends Feature
{
    public static List<vg> isBotPlayer;
    public static ArrayList<vg> isRealPlayer;
    public static ListSetting antiBotMode;
    public BooleanSetting removeWorld;
    
    public AntiBot() {
        super("AntiBot", "\u0423\u0434\u0430\u043b\u044f\u0435\u0442 \u0431\u043e\u0442\u043e\u0432 \u0441\u043e\u0437\u0434\u0430\u043d\u043d\u044b\u0445 \u0430\u043d\u0442\u0438-\u0447\u0438\u0442\u043e\u043c", Type.Combat);
        this.removeWorld = new BooleanSetting("Remove from World", true, () -> AntiBot.antiBotMode.currentMode.equalsIgnoreCase("Wellmore"));
        this.addSettings(AntiBot.antiBotMode, this.removeWorld);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        for (final vg entity : AntiBot.mc.f.i) {
            if (AntiBot.antiBotMode.currentMode.equalsIgnoreCase("Matrix")) {
                if (entity.bm().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.h_()).getBytes(StandardCharsets.UTF_8))) || !(entity instanceof bue)) {
                    continue;
                }
                AntiBot.isBotPlayer.add(entity);
            }
            else {
                if (!AntiBot.antiBotMode.currentMode.equalsIgnoreCase("Wellmore")) {
                    continue;
                }
                for (final vg entity2 : AntiBot.mc.f.e) {
                    final String Test = entity2.i_().d();
                    if (!entity2.aX() && entity2 != AntiBot.mc.h && entity2.T <= 30 && AntiBot.mc.h.g(entity2) <= 8.0f && !Test.startsWith("§7")) {
                        AntiBot.mc.f.e(entity2);
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void onMouse(final EventAttackClient event) {
        if (!this.getState()) {
            return;
        }
        if (AntiBot.antiBotMode.currentMode.equalsIgnoreCase("Need Hit")) {
            final aed entityPlayer = (aed)AntiBot.mc.s.d;
            final String name = entityPlayer.h_();
            if (entityPlayer == null) {
                return;
            }
            if (Fluger.instance.friendManager.getFriends().contains(entityPlayer.h_())) {
                return;
            }
            if (AntiBot.isRealPlayer.contains(entityPlayer)) {
                ChatHelper.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Already in AntiBot-List!");
            }
            else {
                ChatHelper.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Was added in AntiBot-List!");
            }
        }
    }
    
    static {
        AntiBot.isBotPlayer = new ArrayList<vg>();
        AntiBot.isRealPlayer = new ArrayList<vg>();
        AntiBot.antiBotMode = new ListSetting("AntiBot Mode", "Matrix", () -> true, new String[] { "Matrix", "Wellmore", "Need Hit" });
    }
}
