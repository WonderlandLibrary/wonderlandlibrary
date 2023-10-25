package intent.AquaDev.aqua.modules.combat;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventClick;
import events.listeners.EventFakePreMotion;
import events.listeners.EventKillaura;
import events.listeners.EventPacket;
import events.listeners.EventPostRender2D;
import events.listeners.EventPreMotion;
import events.listeners.EventRender3D;
import events.listeners.EventSilentMove;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.RotationUtil;
import intent.AquaDev.aqua.utils.TimeUtil;
import intent.AquaDev.aqua.utils.Translate;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Killaura
extends Module {
    public static EntityPlayer target = null;
    public static ArrayList<Entity> bots = new ArrayList();
    private final Translate translate = new Translate(0.0f, 0.0f);
    private final double scale = 1.0;
    TimeUtil timeUtil = new TimeUtil();
    TimeUtil timer = new TimeUtil();
    public double yaw;
    float lostHealthPercentage = 0.0f;
    float lastHealthPercentage = 0.0f;
    private EntityLivingBase lastTarget;
    private float displayHealth;
    private float health;

    public Killaura() {
        super("Killaura", "Killaura", 0, Category.Combat);
    }

    public void setup() {
        Aqua.setmgr.register(new Setting("Range", (Module)this, 6.0, 3.0, 6.0, false));
        Aqua.setmgr.register(new Setting("minCPS", (Module)this, 17.0, 1.0, 20.0, false));
        Aqua.setmgr.register(new Setting("maxCPS", (Module)this, 19.0, 1.0, 20.0, false));
        Aqua.setmgr.register(new Setting("Autoblock", (Module)this, true));
        Aqua.setmgr.register(new Setting("JumpFix", (Module)this, false));
        Aqua.setmgr.register(new Setting("MouseSensiFix", (Module)this, false));
        Aqua.setmgr.register(new Setting("MouseDelayFix", (Module)this, false));
        Aqua.setmgr.register(new Setting("RangeBlock", (Module)this, false));
        Aqua.setmgr.register(new Setting("CPSFix", (Module)this, false));
        Aqua.setmgr.register(new Setting("Wtap", (Module)this, false));
        Aqua.setmgr.register(new Setting("KeepSprint", (Module)this, true));
        Aqua.setmgr.register(new Setting("AntiBots", (Module)this, true));
        Aqua.setmgr.register(new Setting("Rotations", (Module)this, false));
        Aqua.setmgr.register(new Setting("1.9", (Module)this, false));
        Aqua.setmgr.register(new Setting("Cubecraft", (Module)this, false));
        Aqua.setmgr.register(new Setting("CorrectMovement", (Module)this, false));
        Aqua.setmgr.register(new Setting("SilentMoveFix", (Module)this, false));
        Aqua.setmgr.register(new Setting("Random", (Module)this, false));
        Aqua.setmgr.register(new Setting("LegitAttack", (Module)this, false));
        Aqua.setmgr.register(new Setting("ThroughWalls", (Module)this, false));
    }

    public void onEnable() {
        if (Killaura.mc.thePlayer != null) {
            Killaura.mc.thePlayer.sprintReset = false;
        }
        super.onEnable();
    }

    public void onDisable() {
        target = null;
        bots.clear();
        super.onDisable();
    }

    public void onEvent(Event event) {
        float[] rota;
        S02PacketChat s02PacketChat;
        String cp21;
        Packet packet;
        if (event instanceof EventPacket && (packet = EventPacket.getPacket()) instanceof S02PacketChat && (cp21 = (s02PacketChat = (S02PacketChat)packet).getChatComponent().getUnformattedText()).contains((CharSequence)"Cages opened! FIGHT!")) {
            bots.clear();
        }
        if (event instanceof EventPostRender2D && Aqua.setmgr.getSetting("KillauraCPSFix").isState() && !Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
            float minCPS = (float)Aqua.setmgr.getSetting("KillauraminCPS").getCurrentNumber();
            float maxCPS = (float)Aqua.setmgr.getSetting("KillauramaxCPS").getCurrentNumber();
            float CPS = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)minCPS, (double)maxCPS);
            target = this.searchTargets();
            if (target != null) {
                this.lastTarget = target;
            }
            if ((!Aqua.setmgr.getSetting("Killaura1.9").isState() || Aqua.setmgr.getSetting("KillauraLegitAttack").isState()) && this.timeUtil.hasReached((long)(1000.0f / CPS))) {
                if (target != null) {
                    if (Aqua.setmgr.getSetting("KillauraLegitAttack").isState()) {
                        mc.clickMouse();
                    } else {
                        Killaura.mc.thePlayer.swingItem();
                        Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                    }
                }
                this.timeUtil.reset();
            }
            if (Aqua.setmgr.getSetting("KillauraCubecraft").isState()) {
                if (Aqua.setmgr.getSetting("Killaura1.9").isState() && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    if (Killaura.mc.thePlayer.ticksExisted % 12 == 0) {
                        if (target != null) {
                            Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                            Killaura.mc.thePlayer.swingItem();
                        }
                        this.timeUtil.reset();
                    }
                } else if (Aqua.setmgr.getSetting("Killaura1.9").isState() && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe) {
                    if (Killaura.mc.thePlayer.ticksExisted % 22 == 0) {
                        if (target != null) {
                            Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                            Killaura.mc.thePlayer.swingItem();
                        }
                        this.timeUtil.reset();
                    }
                } else if (Killaura.mc.thePlayer.ticksExisted % 12 == 0) {
                    if (target != null) {
                        Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                        Killaura.mc.thePlayer.swingItem();
                    }
                    this.timeUtil.reset();
                }
            }
        }
        if (event instanceof EventSilentMove && Aqua.setmgr.getSetting("KillauraSilentMoveFix").isState() && target != null && Aqua.setmgr.getSetting("KillauraCorrectMovement").isState()) {
            ((EventSilentMove)event).setSilent(true);
        }
        if (event instanceof EventRender3D && target != null) {
            GL11.glPushMatrix();
            double x = Killaura.target.lastTickPosX + (Killaura.target.posX - Killaura.target.lastTickPosX) * (double)Killaura.mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosX();
            double y = Killaura.target.lastTickPosY + (Killaura.target.posY - Killaura.target.lastTickPosY) * (double)Killaura.mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosY();
            double z = Killaura.target.lastTickPosZ + (Killaura.target.posZ - Killaura.target.lastTickPosZ) * (double)Killaura.mc.timer.renderPartialTicks - mc.getRenderManager().getRenderPosZ();
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glRotatef((float)(-Killaura.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)Killaura.mc.getRenderManager().playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glPopMatrix();
        }
        if (event instanceof EventKillaura) {
            if (target != null) {
                // empty if block
            }
            if (Aqua.setmgr.getSetting("KillauraAntiBots").isState()) {
                if (Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("jartexnetwork.com") || Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("pika.host") || Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc.hydracraft.es") || Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("gamster.org") || Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("play.pika.network.com") || Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("jartex.fun")) {
                    if (Killaura.mc.thePlayer.ticksExisted > 110) {
                        for (Entity entity : Killaura.mc.theWorld.loadedEntityList) {
                            if (!(entity instanceof EntityPlayer) || entity == Killaura.mc.thePlayer || entity.getCustomNameTag() != "" || bots.contains((Object)((EntityPlayer)entity))) continue;
                            bots.add((Object)entity);
                        }
                    } else {
                        bots = new ArrayList();
                    }
                }
                for (Entity entity : Killaura.mc.theWorld.getLoadedEntityList()) {
                    if (!(entity instanceof EntityPlayer) || !this.isBot((EntityPlayer)entity) && !entity.isInvisible() || entity == Killaura.mc.thePlayer) continue;
                    bots.add((Object)entity);
                    if (!Killaura.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) continue;
                    for (NetworkPlayerInfo playerInfo : Killaura.mc.thePlayer.sendQueue.getPlayerInfoMap()) {
                        if (!playerInfo.getGameProfile().getId().equals((Object)entity.getUniqueID()) || playerInfo.getResponseTime() == 1) continue;
                        bots.add((Object)entity);
                    }
                    Killaura.mc.theWorld.removeEntity(entity);
                }
            }
            if (Aqua.setmgr.getSetting("KillauraAutoblock").isState() && Killaura.mc.thePlayer.isSwingInProgress && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target != null) {
                Killaura.mc.gameSettings.keyBindUseItem.pressed = true;
            }
        }
        if (event instanceof EventClick && Aqua.setmgr.getSetting("KillauraWtap").isState() && Killaura.target.hurtTime == 9) {
            Killaura.mc.thePlayer.sprintReset = true;
        }
        if (event instanceof EventFakePreMotion) {
            rota = RotationUtil.Intavee((EntityPlayerSP)Killaura.mc.thePlayer, (EntityLivingBase)target);
            if (!Aqua.setmgr.getSetting("KillauraRotations").isState()) {
                ((EventFakePreMotion)event).setPitch(RotationUtil.pitch);
                ((EventFakePreMotion)event).setYaw(RotationUtil.yaw);
                RotationUtil.setYaw((float)rota[0], (float)180.0f);
                RotationUtil.setPitch((float)rota[1], (float)8.0f);
            }
        }
        if (event instanceof EventPreMotion) {
            if (Aqua.setmgr.getSetting("KillauraAutoblock").isState() && !Killaura.mc.thePlayer.isSwingInProgress && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target == null) {
                Killaura.mc.gameSettings.keyBindUseItem.pressed = false;
            }
            rota = RotationUtil.Intavee((EntityPlayerSP)Killaura.mc.thePlayer, (EntityLivingBase)target);
            if (Aqua.setmgr.getSetting("KillauraRotations").isState()) {
                ((EventPreMotion)event).setPitch(RotationUtil.pitch);
                ((EventPreMotion)event).setYaw(RotationUtil.yaw);
                RotationUtil.setYaw((float)rota[0], (float)180.0f);
                RotationUtil.setPitch((float)rota[1], (float)180.0f);
            }
        }
        if (event instanceof EventUpdate) {
            if (Killaura.mc.thePlayer.ticksExisted % 5 == 0) {
                // empty if block
            }
            event.setCancelled(true);
            if (Aqua.setmgr.getSetting("KillauraRangeBlock").isState()) {
                if (target != null) {
                    if (this.timer.hasReached(700L)) {
                        Killaura.mc.gameSettings.keyBindUseItem.pressed = true;
                        this.timer.reset();
                    } else {
                        Killaura.mc.gameSettings.keyBindUseItem.pressed = false;
                    }
                } else {
                    Killaura.mc.gameSettings.keyBindUseItem.pressed = false;
                }
            }
            if (!Aqua.setmgr.getSetting("KillauraCPSFix").isState()) {
                float minCPS = (float)Aqua.setmgr.getSetting("KillauraminCPS").getCurrentNumber();
                float maxCPS = (float)Aqua.setmgr.getSetting("KillauramaxCPS").getCurrentNumber();
                float CPS = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)minCPS, (double)maxCPS);
                target = this.searchTargets();
                if (target != null) {
                    this.lastTarget = target;
                }
                if ((!Aqua.setmgr.getSetting("Killaura1.9").isState() || Aqua.setmgr.getSetting("KillauraLegitAttack").isState()) && this.timeUtil.hasReached((long)(1000.0f / CPS))) {
                    if (target != null) {
                        if (Aqua.setmgr.getSetting("KillauraLegitAttack").isState()) {
                            mc.clickMouse();
                        } else {
                            Killaura.mc.thePlayer.swingItem();
                            Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                        }
                    }
                    this.timeUtil.reset();
                }
            }
            if (Aqua.setmgr.getSetting("KillauraCubecraft").isState()) {
                if (Aqua.setmgr.getSetting("Killaura1.9").isState() && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    if (Killaura.mc.thePlayer.ticksExisted % 12 == 0) {
                        if (target != null) {
                            Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                            Killaura.mc.thePlayer.swingItem();
                        }
                        this.timeUtil.reset();
                    }
                } else if (Aqua.setmgr.getSetting("Killaura1.9").isState() && Killaura.mc.thePlayer.getHeldItem() != null && Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe) {
                    if (Killaura.mc.thePlayer.ticksExisted % 22 == 0) {
                        if (target != null) {
                            Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                            Killaura.mc.thePlayer.swingItem();
                        }
                        this.timeUtil.reset();
                    }
                } else if (Killaura.mc.thePlayer.ticksExisted % 12 == 0) {
                    if (target != null) {
                        Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.thePlayer, (Entity)target);
                        Killaura.mc.thePlayer.swingItem();
                    }
                    this.timeUtil.reset();
                }
            }
        }
    }

    public EntityPlayer searchTargets() {
        float range = (float)Aqua.setmgr.getSetting("KillauraRange").getCurrentNumber();
        EntityPlayer player = null;
        double closestDist = 100000.0;
        for (Entity o : Killaura.mc.theWorld.loadedEntityList) {
            double dist;
            if (o.getName().equals((Object)Killaura.mc.thePlayer.getName()) || !(o instanceof EntityPlayer) || bots.contains((Object)o) && !Killaura.mc.session.getUsername().equalsIgnoreCase("Administradora") || !(Killaura.mc.thePlayer.getDistanceToEntity(o) < range) || !((dist = (double)Killaura.mc.thePlayer.getDistanceToEntity(o)) < closestDist)) continue;
            closestDist = dist;
            player = (EntityPlayer)o;
        }
        return player;
    }

    boolean isBot(EntityPlayer player) {
        if (!this.isInTablist(player)) {
            return true;
        }
        return this.invalidName((Entity)player);
    }

    boolean isInTablist(EntityPlayer player) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return false;
        }
        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) continue;
            return true;
        }
        return false;
    }

    boolean invalidName(Entity e) {
        if (e.getName().contains((CharSequence)"-")) {
            return true;
        }
        if (e.getName().contains((CharSequence)"/")) {
            return true;
        }
        if (e.getName().contains((CharSequence)"|")) {
            return true;
        }
        if (e.getName().contains((CharSequence)"<")) {
            return true;
        }
        if (e.getName().contains((CharSequence)">")) {
            return true;
        }
        return e.getName().contains((CharSequence)"\u0e22\u0e07");
    }

    public static void renderPlayerModelTexture(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        ResourceLocation skin = target.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        GL11.glEnable((int)3042);
        Gui.drawScaledCustomSizeModalRect((int)((int)x), (int)((int)y), (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
        GL11.glDisable((int)3042);
    }
}
