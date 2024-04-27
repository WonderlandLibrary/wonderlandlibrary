package me.aquavit.liquidsense.command;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class Command extends MinecraftInstance {
    private final String command;
    private final String[] alias;

    public Command(String command, String ... alias) {
        this.command = command;
        this.alias = alias;
    }

    public abstract void execute(String[] args);

    public List<String> tabComplete(String[] args) {
        return new ArrayList<>();
    }

    protected void chat(String msg) {
        ClientUtils.displayChatMessage("§8[§9§l" + LiquidSense.CLIENT_NAME + "§8] §3" + msg);
    }

    protected void chatSyntax(String syntax) {
        ClientUtils.displayChatMessage("§8[§9§l" + LiquidSense.CLIENT_NAME + "§8] §3Syntax: §7" + LiquidSense.commandManager.getPrefix() + syntax);
    }

    protected void chatSyntax(String[] syntaxes) {
        ClientUtils.displayChatMessage("§8[§9§l" + LiquidSense.CLIENT_NAME + "§8] §3Syntax:");
        for (String syntax : syntaxes)
            ClientUtils.displayChatMessage("§8> §7" + LiquidSense.commandManager.getPrefix() + command + " " + syntax.toLowerCase());
    }

    public final String getCommand() {
        return this.command;
    }

    public final String[] getAlias() {
        return this.alias;
    }

    protected void chatSyntaxError() {
        ClientUtils.displayChatMessage("§8[§9§l" + LiquidSense.CLIENT_NAME + "§8] §3Syntax error");
    }

    protected void playEdit() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
    }
}
