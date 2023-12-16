// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.server;

import moonsense.enums.ModuleCategory;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import moonsense.config.ModuleConfig;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.JSONArray;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.util.UUID;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class NickHiderModule extends SCModule
{
    public static NickHiderModule INSTANCE;
    public final Setting hideNick;
    public final Setting nickname;
    public final Setting hideRealName;
    public final Setting hideOtherPlayersNames;
    
    public NickHiderModule() {
        super("Nick Hider", "No description set!", 16);
        NickHiderModule.INSTANCE = this;
        new Setting(this, "Name Options");
        this.hideNick = new Setting(this, "Hide Your Nickname").setDefault(false);
        this.nickname = new Setting(this, "Nickname").setDefault("You");
        this.hideRealName = new Setting(this, "Hide Real Name").setDefault(true);
        this.hideOtherPlayersNames = new Setting(this, "Hide Other Players' Names").setDefault(false);
    }
    
    public String getName(final UUID uuid) {
        final String url = "https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names";
        try {
            final String nameJson = IOUtils.toString(new URL(url));
            final JSONArray nameValue = (JSONArray)JSONValue.parseWithException(nameJson);
            final String playerSlot = nameValue.get(nameValue.length() - 1).toString();
            final JSONObject nameObject = (JSONObject)JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        }
        catch (IOException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return "error";
        }
    }
    
    public String apply(final String input) {
        if (!ModuleConfig.INSTANCE.isEnabled(this) || this.hideNick.getBoolean()) {
            return input;
        }
        if (!ModuleConfig.INSTANCE.isEnabled(this) || !this.hideRealName.getBoolean()) {
            return input;
        }
        return input.replaceAll(this.mc.session.getUsername(), this.nickname.getString());
    }
    
    public String[] tabComplete(final String[] in, final String soFar) {
        if (!this.hideRealName.getBoolean()) {
            return in;
        }
        if (this.hideNick.getBoolean()) {
            return in;
        }
        for (int i = 0; i < in.length; ++i) {
            in[i] = this.apply(in[i]);
        }
        final String[] split = soFar.split(" ");
        final String tmp = split[split.length - 1];
        final List<String> newNames = new ArrayList<String>();
        for (final String string : in) {
            newNames.add(string);
        }
        final Set<String> nameSet = (Set<String>)Sets.newHashSet((Object[])in);
        nameSet.addAll(newNames);
        return nameSet.toArray(new String[0]);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
    
    @Override
    public String getAuthor() {
        return "Sk1er";
    }
}
