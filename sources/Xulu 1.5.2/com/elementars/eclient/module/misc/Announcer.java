// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.command.Command;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.event.events.AnnouncerRegistry;
import com.elementars.eclient.module.Module;

public class Announcer extends Module
{
    private AnnouncerRegistry announcerRegistry;
    public static int delayy;
    public static Value<String> mode;
    public static Value<Integer> delay;
    public static Value<Boolean> walk;
    public static Value<Boolean> craftedItem;
    public static Value<Boolean> pickUpItem;
    public static Value<Boolean> smeltedItem;
    public static Value<Boolean> respawn;
    public static Value<Boolean> blockPlaced;
    public static Value<Boolean> blockBroke;
    public static Value<Boolean> itemDroped;
    public static Value<Boolean> openChat;
    public static Value<Boolean> pickBlock;
    public static Value<Boolean> command;
    public static Value<Boolean> fullScreen;
    public static Value<Boolean> pauseGame;
    public static Value<Boolean> openInv;
    public static Value<Boolean> playerList;
    public static Value<Boolean> screenShot;
    public static Value<Boolean> swapHand;
    public static Value<Boolean> sneak;
    public static Value<Boolean> Perspective;
    public static Value<Boolean> jump;
    public static Value<Boolean> attack;
    public static Value<Boolean> eatting;
    
    public Announcer() {
        super("Announcer", "Announce EVERYTHING in chat", 0, Category.MISC, true);
        this.announcerRegistry = new AnnouncerRegistry();
        Announcer.mode = this.register(new Value<String>("Mode", this, "English", new ArrayList<String>(Arrays.asList("English", "Hebrew"))));
        Announcer.delay = this.register(new Value<Integer>("Delay", this, 10, 0, 60));
        Announcer.walk = this.register(new Value<Boolean>("Walk", this, true));
        Announcer.craftedItem = this.register(new Value<Boolean>("CraftedItem", this, true));
        Announcer.pickUpItem = this.register(new Value<Boolean>("PickUpItem", this, true));
        Announcer.smeltedItem = this.register(new Value<Boolean>("SmeltedItem", this, true));
        Announcer.respawn = this.register(new Value<Boolean>("Respawn", this, true));
        Announcer.blockPlaced = this.register(new Value<Boolean>("BlockPlaced", this, true));
        Announcer.blockBroke = this.register(new Value<Boolean>("BlockBroke", this, true));
        Announcer.itemDroped = this.register(new Value<Boolean>("ItemDropped", this, true));
        Announcer.openChat = this.register(new Value<Boolean>("OpenChat", this, true));
        Announcer.pickBlock = this.register(new Value<Boolean>("PickBlock", this, true));
        Announcer.command = this.register(new Value<Boolean>("Command", this, true));
        Announcer.fullScreen = this.register(new Value<Boolean>("FullScreen", this, true));
        Announcer.pauseGame = this.register(new Value<Boolean>("PauseGame", this, true));
        Announcer.openInv = this.register(new Value<Boolean>("OpenInv", this, true));
        Announcer.playerList = this.register(new Value<Boolean>("PlayerList", this, true));
        Announcer.screenShot = this.register(new Value<Boolean>("ScreenShot", this, true));
        Announcer.swapHand = this.register(new Value<Boolean>("SwapHand", this, true));
        Announcer.sneak = this.register(new Value<Boolean>("Sneak", this, true));
        Announcer.Perspective = this.register(new Value<Boolean>("Perspective", this, true));
        Announcer.jump = this.register(new Value<Boolean>("Jump", this, true));
        Announcer.attack = this.register(new Value<Boolean>("Attack", this, true));
        Announcer.eatting = this.register(new Value<Boolean>("Eating", this, true));
    }
    
    @Override
    public void onUpdate() {
        if (Announcer.delayy > 0) {
            --Announcer.delayy;
        }
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this.announcerRegistry);
        Command.sendChatMessage("Announcer ON");
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this.announcerRegistry);
        Command.sendChatMessage("Announcer OFF");
    }
}
