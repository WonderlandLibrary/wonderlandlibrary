// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.server.hypixel;

import java.awt.event.ActionEvent;
import moonsense.enums.ModuleCategory;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;
import moonsense.event.impl.SCSoundEvent;
import moonsense.event.SubscribeEvent;
import java.util.regex.Matcher;
import javax.swing.Timer;
import java.util.Arrays;
import moonsense.event.impl.SCChatEvent;
import net.minecraft.util.EnumChatFormatting;
import java.util.Random;
import java.util.regex.Pattern;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class HypixelModsModule extends SCModule
{
    private final Setting autoGLHFGroup;
    private final Setting autoGLHF;
    private final Setting autoGLHFUseCustom;
    private final Setting autoGLHFCustomMessage;
    private final Setting autoGGGroup;
    private final Setting autoGG;
    private final Setting autoGGUseCustom;
    private final Setting autoGGCustomMessage;
    private final Setting joinMsgGroup;
    private final Setting joinMsgEnabled;
    private final Setting joinMsg;
    private final Setting autoFriend;
    private final Setting friendMessages;
    private final Setting lobbySoundsSettingGroup;
    private final Setting lobbySounds;
    private final Setting restrictToHypixel;
    private final Setting soundSlime;
    private final Setting soundDragon;
    private final Setting soundWither;
    private final Setting soundItemPickup;
    private final Setting soundXPOrb;
    private final Setting soundNotes;
    private final Setting soundTNT;
    private final Setting soundExplosion;
    private final Setting soundDeliveryMan;
    private final Setting soundMysteryBox;
    private final Setting soundFirework;
    private final Setting soundLevelup;
    private final Setting soundStepping;
    private final Setting soundArrow;
    private final Setting soundBat;
    private final Setting soundFire;
    private final Setting soundEnderman;
    private boolean shouldSendGLHF;
    private long lastTriggerGLHF;
    private boolean shouldSendGG;
    private long lastTriggerGG;
    private static final String COMPASS_NAME;
    private static final String COMPASS_LORE;
    private boolean inHypixelLobby;
    private boolean onHypixel;
    private final Pattern autoFriendPattern;
    private Random random;
    
    static {
        COMPASS_NAME = EnumChatFormatting.GREEN + "Game Menu " + EnumChatFormatting.GRAY + "(Right Click)";
        COMPASS_LORE = String.valueOf(EnumChatFormatting.DARK_PURPLE.toString()) + EnumChatFormatting.ITALIC + EnumChatFormatting.GRAY + "Right Click to bring up the Game Menu!";
    }
    
    public HypixelModsModule() {
        super("Hypixel Mods", "A collection of mods that integrates with the Hypixel network.");
        this.shouldSendGLHF = true;
        this.lastTriggerGLHF = 0L;
        this.shouldSendGG = true;
        this.lastTriggerGG = 0L;
        this.random = new Random();
        this.autoFriendPattern = Pattern.compile("\u00c2§m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");
        new Setting(this, "Chat Mods");
        this.autoGLHFGroup = new Setting(this, "AutoGLHF", "autoglhf.group").setDefault(new Setting.CompoundSettingGroup("AutoGLHF", new Setting[] { this.autoGLHF = new Setting(null, "AutoGLHF", "autoglhf.enabled").setDefault(true).compound(true), this.autoGLHFUseCustom = new Setting(null, "Custom Message", "autoglhf.custommessage").setDefault(false).compound(true), this.autoGLHFCustomMessage = new Setting(null, "Message", "autoglhf.message").setDefault("").compound(true) }));
        this.autoGGGroup = new Setting(this, "AutoGG", "autogg.group").setDefault(new Setting.CompoundSettingGroup("AutoGG", new Setting[] { this.autoGG = new Setting(null, "AutoGG", "autogg.group").setDefault(true).compound(true), this.autoGGUseCustom = new Setting(null, "Custom Message", "autogg.custommessage").setDefault(false).compound(true), this.autoGGCustomMessage = new Setting(null, "Message", "autogg.message").setDefault("").compound(true) }));
        this.joinMsgGroup = new Setting(this, "Join Message", "joinmsg.group").setDefault(new Setting.CompoundSettingGroup("Join Message", new Setting[] { this.joinMsgEnabled = new Setting(null, "Join Message", "joinmsg.enabled").setDefault(false).compound(true), this.joinMsg = new Setting(null, "Message", "joinmsg.message").setDefault("Hello!").compound(true) }));
        this.autoFriend = new Setting(this, "Auto Friend").setDefault(true);
        this.friendMessages = new Setting(this, "Friend Messages").setDefault(false);
        new Setting(this, "Misc Mods");
        this.lobbySoundsSettingGroup = new Setting(this, "Hypixel Lobby Sounds").setDefault(new Setting.CompoundSettingGroup("Lobby Sounds", new Setting[] { new Setting(null, "Lobby Sounds").compound(true), this.lobbySounds = new Setting(null, "Modify Lobby Sounds").setDefault(false).compound(true), this.restrictToHypixel = new Setting(null, "Restrict To Hypixel").setDefault(false).compound(true), new Setting(null, "Mob Sounds").compound(true), this.soundBat = new Setting(null, "Disable Bat Sounds").setDefault(false).compound(true), this.soundDragon = new Setting(null, "Disable Dragon Sounds").setDefault(false).compound(true), this.soundEnderman = new Setting(null, "Disable Enderman Sounds").setDefault(false).compound(true), this.soundSlime = new Setting(null, "Disable Slime Sounds").setDefault(false).compound(true), this.soundWither = new Setting(null, "Disable Wither Sounds").setDefault(false).compound(true), new Setting(null, "Misc. Entity Sounds").compound(true), this.soundNotes = new Setting(null, "Disable Noteblock Sounds").setDefault(false).compound(true), this.soundTNT = new Setting(null, "Disable TNT Sounds").setDefault(false).compound(true), this.soundExplosion = new Setting(null, "Disable Explosion Sounds").setDefault(false).compound(true), this.soundItemPickup = new Setting(null, "Disable Item Pickup Sounds").setDefault(false).compound(true), this.soundXPOrb = new Setting(null, "Disable Item Pickup Sounds").setDefault(false).compound(true), this.soundFirework = new Setting(null, "Disable Firework Sounds").setDefault(false).compound(true), this.soundArrow = new Setting(null, "Disable Arrow Sounds").setDefault(false).compound(true), new Setting(null, "Hypixel Exclusive Sounds").compound(true), this.soundMysteryBox = new Setting(null, "Disable Mystery Box Sounds").setDefault(false).compound(true), this.soundDeliveryMan = new Setting(null, "Disable Delivery Man Sounds").setDefault(false).compound(true), new Setting(null, "Other Sounds").compound(true), this.soundFire = new Setting(null, "Disable Fire Sounds").setDefault(false).compound(true), this.soundLevelup = new Setting(null, "Disable Levelup Sounds").setDefault(false).compound(true), this.soundStepping = new Setting(null, "Disable Stepping Sounds").setDefault(false).compound(true) }));
    }
    
    @Override
    public void initMod() {
        this.runLobbyCheckerTimer();
    }
    
    @SubscribeEvent
    public void onChat(final SCChatEvent e) {
        if (this.autoGLHF.getBoolean() && this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().serverIP != null && System.currentTimeMillis() > this.lastTriggerGLHF + 1000L && Arrays.asList(getHypixelTriggerGLHF().split("\n")).stream().anyMatch(trigger -> e.component.getUnformattedText().contains(trigger)) && !e.component.getUnformattedText().contains(":") && this.shouldSendGLHF) {
            final int timeleft = Integer.parseInt(e.component.getUnformattedText().replaceAll("\\D+", ""));
            if (timeleft <= 1) {
                this.shouldSendGLHF = false;
                String message;
                final Timer t = new Timer((timeleft + 1) * 1000, event -> {
                    if (this.autoGLHFUseCustom.getBoolean()) {
                        message = this.autoGLHFCustomMessage.getString();
                    }
                    else {
                        message = "GL HF!";
                    }
                    if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().serverIP.contains("hypixel")) {
                        this.mc.thePlayer.sendChatMessage("/achat " + message);
                        this.mc.ingameGUI.getChatGUI().addToSentMessages("/achat " + message);
                    }
                    else {
                        this.mc.thePlayer.sendChatMessage(message);
                        this.mc.ingameGUI.getChatGUI().addToSentMessages(message);
                    }
                    this.lastTriggerGLHF = System.currentTimeMillis();
                    this.shouldSendGLHF = true;
                    return;
                });
                t.setRepeats(false);
                t.start();
            }
        }
        if (this.autoGG.getBoolean() && System.currentTimeMillis() > this.lastTriggerGG + 1000L && Arrays.asList(getHypixelTriggerGG().split("\n")).stream().anyMatch(trigger -> e.component.getUnformattedText().contains(trigger)) && !e.component.getUnformattedText().contains(":") && this.shouldSendGG && System.currentTimeMillis() > this.lastTriggerGG + 1000L && Arrays.asList(getHypixelTriggerGG().split("\n")).stream().anyMatch(trigger -> e.component.getUnformattedText().contains(trigger))) {
            String message2;
            if (this.autoGGUseCustom.getBoolean()) {
                message2 = this.autoGGCustomMessage.getString();
            }
            else {
                message2 = "gg";
            }
            if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().serverIP.contains("hypixel")) {
                this.mc.thePlayer.sendChatMessage("/achat " + message2);
                this.mc.ingameGUI.getChatGUI().addToSentMessages("/achat " + message2);
            }
            else {
                this.mc.thePlayer.sendChatMessage(message2);
                this.mc.ingameGUI.getChatGUI().addToSentMessages(message2);
            }
            this.lastTriggerGG = System.currentTimeMillis();
        }
        if (this.autoFriend.getBoolean()) {
            String msg = e.component.getUnformattedText();
            msg = msg.replace("\n", "");
            final Matcher friendMatcher = this.autoFriendPattern.matcher(msg);
            if (friendMatcher.matches()) {
                String name = friendMatcher.group("name");
                if (name.startsWith("[")) {
                    name = name.substring(name.indexOf("] ") + 2);
                }
                System.out.println("Friending " + name);
                this.mc.thePlayer.sendChatMessage("/friend accept " + name);
                if (!this.friendMessages.getBoolean()) {
                    e.setCancelled(true);
                }
            }
            if (!this.friendMessages.getBoolean() && msg.endsWith(" removed you from their friends list!")) {
                e.setCancelled(true);
            }
            if (!this.friendMessages.getBoolean() && msg.startsWith("You are now friends with ")) {
                e.setCancelled(true);
            }
            if (!this.friendMessages.getBoolean() && msg.equals("-----------------------------------------------------")) {
                e.setCancelled(true);
            }
        }
        if (this.joinMsgEnabled.getBoolean()) {
            final String msg = e.component.getUnformattedText();
            if (msg.contains(String.valueOf(this.mc.session.getUsername()) + " has joined")) {
                this.mc.thePlayer.sendChatMessage(this.joinMsg.getString());
            }
        }
    }
    
    @SubscribeEvent
    public void onSoundPlay(final SCSoundEvent event) {
        final String soundName = event.soundLocation;
        if ((this.onHypixel && this.inHypixelLobby && this.restrictToHypixel.getBoolean()) || !this.restrictToHypixel.getBoolean()) {
            if (soundName.startsWith("mob.slime") && this.soundSlime.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("mob.enderdragon") && this.soundDragon.getBoolean()) {
                event.setCancelled(true);
            }
            if ((soundName.startsWith("mob.wither") || soundName.startsWith("mob.skeleton")) && this.soundWither.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("random.orb") && this.soundXPOrb.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("random.pop") && this.soundItemPickup.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("game.tnt.primed") && this.soundTNT.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("random.explode") && (this.soundExplosion.getBoolean() || this.soundMysteryBox.getBoolean())) {
                event.setCancelled(true);
            }
            if (soundName.equals("mob.chicken.plop") && this.soundDeliveryMan.getBoolean()) {
                event.setCancelled(true);
            }
            if ((soundName.startsWith("note.") || soundName.equals("random.click")) && (this.soundNotes.getBoolean() || this.soundMysteryBox.getBoolean())) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("fireworks") && this.soundFirework.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("random.levelup") && this.soundLevelup.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("step.") && this.soundStepping.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("mob.bat") && this.soundBat.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.equals("fire.fire") && this.soundFire.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("mob.endermen") && this.soundEnderman.getBoolean()) {
                event.setCancelled(true);
            }
            if (soundName.startsWith("random.bow") && this.soundArrow.getBoolean()) {
                event.setCancelled(true);
            }
        }
    }
    
    public static String getHypixelTriggerGLHF() {
        return "The game starts in \nStarting in \nBeginning in \nThe game is going to start in \n";
    }
    
    public static String getHypixelTriggerGG() {
        return "Winner #1 (\nTop Survivors\nWinners - \nWinners: \nWinning Team: \n won the game!\nTop Seeker: \nLast team standing!\n1st Place: \n1st Place - \nWinner: \n - Damage Dealt - \nWinning Team -\n1st - \n Duel - \n";
    }
    
    public static String[] getPossibleGLHFs() {
        return new String[] { "GLHF!", "GLHF", "GL HF!", "GL HF", "gl hf!", "gl hf", "glhf!", "glhf", "good luck!", "good luck", "good luck have fun!", "good luck have fun", "have fun!", "have fun" };
    }
    
    public static String[] getPossibleGGs() {
        return new String[] { "GG!", "GG", "gg!", "gg", "Good Game!", "Good Game", "good game!", "good game", "nice game!", "nice game" };
    }
    
    private void runLobbyCheckerTimer() {
        final Minecraft mc = Minecraft.getMinecraft();
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                HypixelModsModule.this.checkForItem();
                HypixelModsModule.this.checkIp();
            }
        }, 1000L, 1000L);
    }
    
    private void checkIp() {
        if (this.mc.getCurrentServerData() != null) {
            final String ip = this.mc.getCurrentServerData().serverIP.toLowerCase();
            if (ip.equals("hypixel.net") || ip.endsWith(".hypixel.net") || ip.contains(".hypixel.net:") || ip.startsWith("hypixel.net:")) {
                this.onHypixel = true;
                return;
            }
        }
        this.onHypixel = false;
    }
    
    private void checkForItem() {
        if (this.mc != null && this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.getStackInSlot(0);
            if (itemStack != null && itemStack.hasDisplayName() && itemStack.getDisplayName().equals(HypixelModsModule.COMPASS_NAME)) {
                final List<String> tooltip = (List<String>)itemStack.getTooltip(this.mc.thePlayer, false);
                if (tooltip.get(1).equals(HypixelModsModule.COMPASS_LORE)) {
                    this.inHypixelLobby = true;
                    return;
                }
            }
        }
        this.inHypixelLobby = false;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
    
    @Override
    public boolean isNewModule() {
        return true;
    }
}
