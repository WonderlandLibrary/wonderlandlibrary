// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay;

import net.minecraft.item.ItemStack;
import moonsense.features.modules.QuickplayModule;
import moonsense.features.quickplay.ui.QuickplayMenu;
import com.google.gson.JsonObject;
import moonsense.features.quickplay.ui.QuickplayOption;

public class QuickplayGameMode implements QuickplayOption
{
    private QuickplayGame parent;
    private String name;
    private String command;
    
    public QuickplayGameMode(final QuickplayGame parent, final JsonObject object) {
        this.parent = parent;
        this.name = object.get("name").getAsString();
        this.command = object.get("command").getAsString();
        if (this.command.equals("/quickplay limbo")) {
            this.command = "/achat §";
        }
        else if (this.command.equals("/quickplay delivery")) {
            this.command = "/delivery";
        }
    }
    
    public String getFullId() {
        return String.valueOf(this.parent.getId()) + "." + this.command;
    }
    
    @Override
    public String getText() {
        if (this.parent.getModes().size() == 1) {
            return this.parent.getName();
        }
        return String.valueOf(this.parent.getName()) + " - " + this.name;
    }
    
    @Override
    public void onClick(final QuickplayMenu palette, final QuickplayModule mod) {
        mod.playGame(this);
    }
    
    @Override
    public ItemStack getIcon() {
        return this.parent.getIcon();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCommand() {
        return this.command;
    }
}
