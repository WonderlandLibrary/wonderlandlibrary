// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.server.hypixel;

import java.util.Collection;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.scoreboard.Score;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumChatFormatting;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import moonsense.enums.ModuleCategory;
import moonsense.event.SubscribeEvent;
import net.minecraft.util.IChatComponent;
import moonsense.notifications.NotificationColor;
import moonsense.notifications.NotificationType;
import moonsense.MoonsenseClient;
import java.util.Date;
import java.text.SimpleDateFormat;
import moonsense.utils.StringUtils;
import moonsense.event.impl.SCChatEvent;
import moonsense.features.SCModule;

public class HypixelReplayLoggerModule extends SCModule
{
    public static HypixelReplayLoggerModule INSTANCE;
    
    public HypixelReplayLoggerModule() {
        super("Replay Logger", "Saves game replays locally so you can view them easily!");
        HypixelReplayLoggerModule.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onChat(final SCChatEvent event) {
        final IChatComponent comp = event.component;
        if (comp.getUnformattedText().equals("\u00c2§aThis game has been recorded. \u00c2§6Click here to watch the Replay!")) {
            System.out.println("=====> FOUND A REPLAY! NOW SAVING! <=====");
            try {
                final String cmd = comp.getChatStyle().getChatClickEvent().getValue();
                final String map = this.getMapName();
                String mode = this.formatEntry(this.getMode());
                mode = StringUtils.title(mode);
                final String pattern = "MM-dd-yyyy HH:mm:ss";
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                date = String.valueOf(date) + " on" + this.formatEntry(map) + " (" + mode + ")";
                MoonsenseClient.INSTANCE.getNotificationManager().createNotification("Hypixel Replay Logger", "Saved replay!", true, 2500L, NotificationType.INFO, NotificationColor.GREEN);
                MoonsenseClient.INSTANCE.getReplayManager().put(date, cmd);
                MoonsenseClient.INSTANCE.getReplayManager().save();
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
    
    private String getMapName() {
        final Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective objective = null;
        final ScorePlayerTeam team = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
        if (team != null) {
            final int var16 = team.func_178775_l().func_175746_b();
            if (var16 >= 0) {
                objective = scoreboard.getObjectiveInDisplaySlot(3 + var16);
            }
        }
        final ScoreObjective finalObjective = (objective != null) ? objective : scoreboard.getObjectiveInDisplaySlot(1);
        final ArrayList<String> entries = this.getSBEntries(finalObjective);
        for (final String s : entries) {
            if (s.startsWith("Map:")) {
                return s;
            }
        }
        return "";
    }
    
    private String getMode() {
        final Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective objective = null;
        final ScorePlayerTeam team = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
        if (team != null) {
            final int var16 = team.func_178775_l().func_175746_b();
            if (var16 >= 0) {
                objective = scoreboard.getObjectiveInDisplaySlot(3 + var16);
            }
        }
        final ScoreObjective finalObjective = (objective != null) ? objective : scoreboard.getObjectiveInDisplaySlot(1);
        final ArrayList<String> entries = this.getSBEntries(finalObjective);
        return entries.get(entries.size() - 1);
    }
    
    private String formatEntry(final String s) {
        System.out.println(s);
        String ns = "";
        char[] charArray;
        for (int length = (charArray = s.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            System.out.print(c);
            if (Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ') {
                ns = String.valueOf(ns) + c;
            }
        }
        System.out.println();
        int count = 0;
        EnumChatFormatting[] values;
        for (int length2 = (values = EnumChatFormatting.values()).length, j = 0; j < length2; ++j) {
            final EnumChatFormatting f = values[j];
            if (count > 2) {
                break;
            }
            ns.replace(f.toString(), "");
            ++count;
        }
        return ns.substring(3);
    }
    
    private ArrayList<String> getSBEntries(final ScoreObjective obj) {
        final Scoreboard var3 = obj.getScoreboard();
        final Collection var4 = var3.getSortedScores(obj);
        final ArrayList var5 = Lists.newArrayList(Iterables.filter((Iterable)var4, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001958";
            
            public boolean func_178903_a(final Score p_178903_1_) {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList var6;
        if (var5.size() > 15) {
            var6 = Lists.newArrayList(Iterables.skip((Iterable)var5, var4.size() - 15));
        }
        else {
            var6 = var5;
        }
        int var7 = this.mc.ingameGUI.func_175179_f().getStringWidth(obj.getDisplayName());
        for (final Score var9 : var6) {
            final ScorePlayerTeam var10 = var3.getPlayersTeam(var9.getPlayerName());
            final String var11 = String.valueOf(ScorePlayerTeam.formatPlayerName(var10, var9.getPlayerName())) + ": " + EnumChatFormatting.RED + var9.getScorePoints();
            var7 = Math.max(var7, this.mc.ingameGUI.func_175179_f().getStringWidth(var11));
        }
        final byte var12 = 3;
        int var13 = 0;
        final Iterator var14 = var6.iterator();
        final ArrayList<String> entries = new ArrayList<String>();
        while (var14.hasNext()) {
            final Score var15 = var14.next();
            ++var13;
            final ScorePlayerTeam var16 = var3.getPlayersTeam(var15.getPlayerName());
            final String var17 = ScorePlayerTeam.formatPlayerName(var16, var15.getPlayerName());
            entries.add(var17);
        }
        entries.add(obj.getDisplayName());
        return entries;
    }
}
