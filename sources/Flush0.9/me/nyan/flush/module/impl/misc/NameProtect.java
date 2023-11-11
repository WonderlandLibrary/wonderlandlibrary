package me.nyan.flush.module.impl.misc;

import me.nyan.flush.module.Module;
import net.minecraft.util.EnumChatFormatting;

public class NameProtect extends Module {
    private String customName = EnumChatFormatting.GREEN + "Me";

    public NameProtect() {
        super("NameProtect", Category.MISC);
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
}
