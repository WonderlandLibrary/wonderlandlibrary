// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.GameType;
import java.util.Arrays;
import ru.tuskevich.commands.impl.StaffCommand;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.util.render.GlowUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.ui.newui.SmartScissor;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import ru.tuskevich.util.font.Fonts;
import java.util.Collection;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import java.util.ArrayList;
import ru.tuskevich.Minced;
import java.util.List;
import java.util.regex.Pattern;
import ru.tuskevich.util.drag.Dragging;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Staff List", desc = "", type = Type.PLAYER)
public class StaffInfo extends Module
{
    public final Dragging staffListDrag;
    private static final Pattern validUserPattern;
    List<String> players;
    List<String> notSpec;
    
    public StaffInfo() {
        this.staffListDrag = Minced.getInstance().createDrag(this, "staffListDrag", 5.0f, 60.0f);
        this.players = new ArrayList<String>();
        this.notSpec = new ArrayList<String>();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        final Minecraft mc = StaffInfo.mc;
        if (Minecraft.player.ticksExisted % 10 == 0) {
            this.players = this.getVanish();
            this.notSpec = getOnlinePlayerD();
            this.players.sort(String::compareTo);
            this.notSpec.sort(String::compareTo);
        }
    }
    
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        if (this.players.isEmpty() && this.notSpec.isEmpty()) {
            return;
        }
        Minced.instance.scaleMath.pushScale();
        final List<String> all = new ArrayList<String>();
        all.addAll(this.players);
        all.addAll(this.notSpec);
        final float width = all.stream().max(Comparator.comparingDouble((ToDoubleFunction<? super Object>)Fonts.Nunito12::getStringWidth)).map((Function<? super Object, ? extends Integer>)Fonts.Nunito12::getStringWidth).get() + 35.0f;
        final float height2 = (float)(10 + (this.notSpec.size() + this.players.size()) * 13);
        final float x = this.staffListDrag.getX();
        final float y = this.staffListDrag.getY();
        this.staffListDrag.setWidth(width);
        this.staffListDrag.setHeight(height2);
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates((int)x - 3, (int)y - 3, (int)width + 6, (int)height2 + 6);
        RenderUtility.drawRound(x, y, width, height2, 2.0f, new Color(21, 21, 21, 200));
        if (Hud.arrayListElements.get(0)) {
            GlowUtility.drawGlow(x, y, width, 10.0f, 15, Hud.getColor(280));
        }
        RenderUtility.drawGradientRound(x - 1.0f, y - 1.0f, width + 2.0f, 12.0f, 3.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
        RenderUtility.drawRound(x, y, width, 10.0f, 2.0f, new Color(25, 25, 25, 255));
        Fonts.Nunito14.drawCenteredString("Staff Info", x + width / 2.0f, y + 3.0f, ColorUtility.rgba(255, 255, 255, 255));
        Fonts.icon19.drawString("d", x + width / 43.0f, y + 3.0f, Hud.onecolor.getColorValue());
        if (height2 > 10.0f) {
            int staffY = 11;
            for (final String player : all) {
                Fonts.Nunito12.drawStringWithShadow(player.split(":")[0], x + 1.0f, y + 5.0f + staffY, -1);
                Fonts.Nunito12.drawStringWithShadow(player.split(":")[1].equalsIgnoreCase("vanish") ? (ChatFormatting.RED + "VANISH") : (player.split(":")[1].equalsIgnoreCase("gm3") ? (ChatFormatting.RED + "VANISH " + ChatFormatting.YELLOW + "(GM 3)") : (ChatFormatting.GREEN + "ACTIVE")), x + width - Fonts.Nunito12.getStringWidth(player.split(":")[1].equalsIgnoreCase("vanish") ? (ChatFormatting.RED + "VANISH") : (player.split(":")[1].equalsIgnoreCase("gm3") ? (ChatFormatting.RED + "VANISH " + ChatFormatting.YELLOW + "(GM 3)") : (ChatFormatting.GREEN + "ACTIVE"))) - 2.0f, y + staffY + 5.0f, ColorUtility.rgba(255, 255, 255, 255));
                staffY += 13;
            }
        }
        SmartScissor.unset();
        SmartScissor.pop();
        Minced.instance.scaleMath.popScale();
    }
    
    public static List<String> getOnlinePlayer() {
        final Minecraft mc = StaffInfo.mc;
        return Minecraft.player.connection.getPlayerInfoMap().stream().map((Function<? super NetworkPlayerInfo, ?>)NetworkPlayerInfo::getGameProfile).map((Function<? super Object, ?>)GameProfile::getName).filter(profileName -> StaffInfo.validUserPattern.matcher(profileName).matches()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
    }
    
    public static List<String> getOnlinePlayerD() {
        final List<String> S = new ArrayList<String>();
        final Minecraft mc = StaffInfo.mc;
        for (final NetworkPlayerInfo player : Minecraft.player.connection.getPlayerInfoMap()) {
            if (StaffInfo.mc.isSingleplayer()) {
                break;
            }
            if (player.getPlayerTeam() == null) {
                break;
            }
            final String prefix = player.getPlayerTeam().getColorPrefix();
            if (!check(ChatFormatting.stripFormatting(prefix).toLowerCase()) && !StaffCommand.staffNames.toString().toLowerCase().contains(player.getGameProfile().getName().toLowerCase()) && !player.getGameProfile().getName().toLowerCase().contains("1danil_mansoru1") && !player.getPlayerTeam().getColorPrefix().contains("YT")) {
                continue;
            }
            final String name = Arrays.asList(player.getPlayerTeam().getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");
            if (player.getGameType() == GameType.SPECTATOR) {
                S.add(player.getPlayerTeam().getColorPrefix() + name + ":gm3");
            }
            else {
                S.add(player.getPlayerTeam().getColorPrefix() + name + ":active");
            }
        }
        return S;
    }
    
    public List<String> getVanish() {
        final List<String> list = new ArrayList<String>();
        for (final ScorePlayerTeam s : StaffInfo.mc.world.getScoreboard().getTeams()) {
            if (s.getColorPrefix().length() != 0) {
                if (StaffInfo.mc.isSingleplayer()) {
                    continue;
                }
                final String name = Arrays.asList(s.getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");
                if (getOnlinePlayer().contains(name)) {
                    continue;
                }
                if (name.isEmpty()) {
                    continue;
                }
                if ((!StaffCommand.staffNames.toString().toLowerCase().contains(name.toLowerCase()) || !check(s.getColorPrefix().toLowerCase())) && !check(s.getColorPrefix().toLowerCase()) && !name.toLowerCase().contains("1danil_mansoru1") && !s.getColorPrefix().contains("YT")) {
                    continue;
                }
                list.add(s.getColorPrefix() + name + ":vanish");
            }
        }
        return list;
    }
    
    public static boolean check(final String name) {
        return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("\u0445\u0435\u043b\u043f\u0435\u0440") || name.contains("\u043c\u043e\u0434\u0435\u0440") || name.contains("\u0430\u0434\u043c\u0438\u043d") || name.contains("\u043a\u0443\u0440\u0430\u0442\u043e\u0440");
    }
    
    static {
        validUserPattern = Pattern.compile("^\\w{3,16}$");
    }
}
