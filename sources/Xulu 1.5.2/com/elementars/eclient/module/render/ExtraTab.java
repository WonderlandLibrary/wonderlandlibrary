// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.friend.Friends;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ExtraTab extends Module
{
    public final Value<Integer> tabSize;
    public final Value<String> color;
    public final Value<String> ecolor;
    public static ExtraTab INSTANCE;
    
    public ExtraTab() {
        super("ExtraTab", "Expands tab menu", 0, Category.RENDER, true);
        this.tabSize = this.register(new Value<Integer>("Players", this, 80, 1, 1000));
        this.color = this.register(new Value<String>("Friend Color", this, "LightGreen", ColorTextUtils.colors));
        this.ecolor = this.register(new Value<String>("Enemy Color", this, "LightRed", ColorTextUtils.colors));
        ExtraTab.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%s" + ColorTextUtils.getColor(ExtraTab.INSTANCE.color.getValue()).substring(1) + "%s", Command.SECTIONSIGN(), dname);
        }
        if (Enemies.isEnemy(dname)) {
            return String.format("%s" + ColorTextUtils.getColor(ExtraTab.INSTANCE.ecolor.getValue()).substring(1) + "%s", Command.SECTIONSIGN(), dname);
        }
        return dname;
    }
}
