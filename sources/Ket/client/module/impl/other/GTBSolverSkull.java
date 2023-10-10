package client.module.impl.other;

import client.Client;
import client.bot.BotManager;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.TickEvent;
import client.event.impl.other.UpdateEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.ModuleManager;
import client.module.impl.combat.Friends;
import client.util.ChatUtil;
import client.value.impl.NumberValue;
import client.value.impl.StringValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@ModuleInfo(name = "GTBSolver", description = "assy", category = Category.OTHER)
public class GTBSolverSkull extends Module {

    private final StringValue word = new StringValue("Word", this, "sn__");

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
            final AtomicBoolean found = new AtomicBoolean();
            mc.theWorld.loadedEntityList.stream().filter(entity -> {
                final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
                final BotManager botManager = Client.INSTANCE.getBotManager();
                return entity != mc.thePlayer && entity instanceof EntityPlayer && (!moduleManager.get(AntiBot.class).isEnabled() || !botManager.contains(entity)) && (!moduleManager.get(Friends.class).isEnabled() || !botManager.getFriends().contains(entity.getName()));
            }).forEach(entity -> {
                mc.thePlayer.sendChatMessage("/msg " + entity.getName() + " Drillinz");
                found.set(true);
            });
    };
}
