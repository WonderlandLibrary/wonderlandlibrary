// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module;

import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.EntityUtil;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.guirewrite.elements.Target;
import com.elementars.eclient.guirewrite.elements.Logo;
import com.elementars.eclient.guirewrite.elements.Watermark;
import com.elementars.eclient.guirewrite.elements.GodInfo;
import com.elementars.eclient.guirewrite.elements.Armor;
import com.elementars.eclient.guirewrite.elements.Info;
import com.elementars.eclient.guirewrite.elements.HoleHud;
import com.elementars.eclient.guirewrite.elements.StickyNotes;
import com.elementars.eclient.guirewrite.elements.Potions;
import com.elementars.eclient.guirewrite.elements.TheGoons;
import com.elementars.eclient.guirewrite.elements.OldName;
import com.elementars.eclient.guirewrite.elements.Welcome;
import com.elementars.eclient.guirewrite.elements.Player;
import com.elementars.eclient.guirewrite.elements.FeatureList;
import com.elementars.eclient.guirewrite.elements.TextRadar;
import com.elementars.eclient.guirewrite.elements.CraftingPreview;
import com.elementars.eclient.guirewrite.elements.InvPreview;
import com.elementars.eclient.guirewrite.elements.Exp;
import com.elementars.eclient.guirewrite.elements.Gapples;
import com.elementars.eclient.guirewrite.elements.Crystals;
import com.elementars.eclient.guirewrite.elements.Obsidian;
import com.elementars.eclient.guirewrite.elements.Totems;
import com.elementars.eclient.guirewrite.HudEditor;
import com.elementars.eclient.module.render.Xray;
import com.elementars.eclient.module.render.Waypoints;
import com.elementars.eclient.module.render.VoidESP;
import com.elementars.eclient.module.render.ViewmodelChanger;
import com.elementars.eclient.module.render.Trajectories;
import com.elementars.eclient.module.render.Tracers;
import com.elementars.eclient.module.render.ToolTips;
import com.elementars.eclient.module.render.StorageESP;
import com.elementars.eclient.module.render.Skeleton;
import com.elementars.eclient.module.render.ShulkerPreview;
import com.elementars.eclient.guirewrite.elements.PvPInfo;
import com.elementars.eclient.module.render.SecretShaders;
import com.elementars.eclient.module.render.Search;
import com.elementars.eclient.module.render.Pathfind;
import com.elementars.eclient.module.render.OutlineESP;
import com.elementars.eclient.module.render.OffhandSwing;
import com.elementars.eclient.module.render.NoRender;
import com.elementars.eclient.module.render.NoHurtCam;
import com.elementars.eclient.module.render.Nametags;
import com.elementars.eclient.module.render.LogoutSpots;
import com.elementars.eclient.module.render.ItemESP;
import com.elementars.eclient.module.render.ImageESP;
import com.elementars.eclient.module.render.HoleESP;
import com.elementars.eclient.module.render.HellenKeller;
import com.elementars.eclient.module.render.FullBright;
import com.elementars.eclient.module.render.ExtraTab;
import com.elementars.eclient.module.render.ExeterGui;
import com.elementars.eclient.module.render.ESP;
import com.elementars.eclient.module.render.NewGui;
import com.elementars.eclient.module.render.EnchantColor;
import com.elementars.eclient.module.render.Compass;
import com.elementars.eclient.module.render.ChunkFinder;
import com.elementars.eclient.module.render.Chat;
import com.elementars.eclient.module.render.Chams;
import com.elementars.eclient.module.render.Cape;
import com.elementars.eclient.module.render.BreakESP;
import com.elementars.eclient.module.render.BossStack;
import com.elementars.eclient.module.render.BlockHighlight;
import com.elementars.eclient.module.render.Arrows;
import com.elementars.eclient.module.render.AntiFog;
import com.elementars.eclient.module.player.XCarry;
import com.elementars.eclient.module.player.TpsSync;
import com.elementars.eclient.module.player.Speedmine;
import com.elementars.eclient.module.player.SelfLogoutSpot;
import com.elementars.eclient.module.player.PacketSwing;
import com.elementars.eclient.module.player.NoBreakAnimation;
import com.elementars.eclient.module.player.Multitask;
import com.elementars.eclient.module.player.ItemSpoof;
import com.elementars.eclient.module.player.Freecam;
import com.elementars.eclient.module.player.FastFall;
import com.elementars.eclient.module.player.Blink;
import com.elementars.eclient.module.player.AutoWalk;
import com.elementars.eclient.module.player.AutoReplenish;
import com.elementars.eclient.module.player.AntiVoid;
import com.elementars.eclient.module.movement.Velocity;
import com.elementars.eclient.module.movement.Strafe;
import com.elementars.eclient.module.movement.Step;
import com.elementars.eclient.module.movement.Sprint;
import com.elementars.eclient.module.movement.NoSlowDown;
import com.elementars.eclient.module.movement.LongJump;
import com.elementars.eclient.module.movement.Jesus;
import com.elementars.eclient.module.movement.HoleTP;
import com.elementars.eclient.module.movement.GuiMove;
import com.elementars.eclient.module.movement.ForgeFly;
import com.elementars.eclient.module.movement.Flight;
import com.elementars.eclient.module.movement.FastSwim;
import com.elementars.eclient.module.movement.ElytraFly;
import com.elementars.eclient.module.misc.VisualRange;
import com.elementars.eclient.module.misc.Timer;
import com.elementars.eclient.module.misc.Time;
import com.elementars.eclient.module.misc.SkinFlicker;
import com.elementars.eclient.module.misc.PortalChat;
import com.elementars.eclient.module.misc.NoPacketKick;
import com.elementars.eclient.module.misc.NoEntityTrace;
import com.elementars.eclient.module.misc.MobOwner;
import com.elementars.eclient.module.misc.MCF;
import com.elementars.eclient.module.misc.LiquidInteract;
import com.elementars.eclient.module.misc.HopperNuker;
import com.elementars.eclient.module.misc.FovSlider;
import com.elementars.eclient.module.misc.FakeVanilla;
import com.elementars.eclient.module.misc.FakePlayer;
import com.elementars.eclient.module.misc.DonkeyAlert;
import com.elementars.eclient.module.misc.CustomChat;
import com.elementars.eclient.module.misc.CrashExploit;
import com.elementars.eclient.module.misc.CoordLogger;
import com.elementars.eclient.module.misc.ColorSign;
import com.elementars.eclient.module.misc.CameraClip;
import com.elementars.eclient.module.misc.Avoid;
import com.elementars.eclient.module.misc.AutoWither;
import com.elementars.eclient.module.misc.AutoQMain;
import com.elementars.eclient.module.misc.Australia;
import com.elementars.eclient.module.misc.AntiSpam;
import com.elementars.eclient.module.misc.AntiSound;
import com.elementars.eclient.module.misc.AntiDeathScreen;
import com.elementars.eclient.module.misc.Announcer;
import com.elementars.eclient.module.combat.Surround;
import com.elementars.eclient.module.combat.StrengthDetect;
import com.elementars.eclient.module.combat.Sharp32kDetect;
import com.elementars.eclient.module.combat.SelfWeb;
import com.elementars.eclient.module.combat.PopCounter;
import com.elementars.eclient.module.combat.PearlAlert;
import com.elementars.eclient.module.combat.Offhand;
import com.elementars.eclient.module.combat.MiddleClickPearl;
import com.elementars.eclient.module.combat.HoleFill;
import com.elementars.eclient.module.combat.HoleBlocker;
import com.elementars.eclient.module.combat.FuckedDetect;
import com.elementars.eclient.module.combat.FastBow;
import com.elementars.eclient.module.combat.EXPFast;
import com.elementars.eclient.module.combat.DurabilityAlert;
import com.elementars.eclient.module.combat.Criticals;
import com.elementars.eclient.module.combat.CityBlocker;
import com.elementars.eclient.module.combat.BreakAlert;
import com.elementars.eclient.module.combat.AutoWeb;
import com.elementars.eclient.module.combat.AutoTrap;
import com.elementars.eclient.module.combat.AutoTotem;
import com.elementars.eclient.module.combat.AutoRepair;
import com.elementars.eclient.module.combat.AutoFeetBlock;
import com.elementars.eclient.module.combat.AutoEz;
import com.elementars.eclient.module.combat.AutoCrystal;
import com.elementars.eclient.module.combat.AutoArmor;
import com.elementars.eclient.module.combat.Aura;
import com.elementars.eclient.module.combat.AntiChainPop;
import com.elementars.eclient.module.core.TitleScreenShader;
import com.elementars.eclient.module.core.CustomFont;
import com.elementars.eclient.module.core.Global;
import java.util.ArrayList;

public class ModuleManager
{
    private static ArrayList<Module> modules;
    
    public void init() {
        ModuleManager.modules.add(new Global());
        ModuleManager.modules.add(new CustomFont());
        ModuleManager.modules.add(new TitleScreenShader());
        ModuleManager.modules.add(new AntiChainPop());
        ModuleManager.modules.add(new Aura());
        ModuleManager.modules.add(new AutoArmor());
        ModuleManager.modules.add(new AutoCrystal());
        ModuleManager.modules.add(new AutoEz());
        ModuleManager.modules.add(new AutoFeetBlock());
        ModuleManager.modules.add(new AutoRepair());
        ModuleManager.modules.add(new AutoTotem());
        ModuleManager.modules.add(new AutoTrap());
        ModuleManager.modules.add(new AutoWeb());
        ModuleManager.modules.add(new BreakAlert());
        ModuleManager.modules.add(new CityBlocker());
        ModuleManager.modules.add(new Criticals());
        ModuleManager.modules.add(new DurabilityAlert());
        ModuleManager.modules.add(new EXPFast());
        ModuleManager.modules.add(new FastBow());
        ModuleManager.modules.add(new FuckedDetect());
        ModuleManager.modules.add(new HoleBlocker());
        ModuleManager.modules.add(new HoleFill());
        ModuleManager.modules.add(new MiddleClickPearl());
        ModuleManager.modules.add(new Offhand());
        ModuleManager.modules.add(new PearlAlert());
        ModuleManager.modules.add(new PopCounter());
        ModuleManager.modules.add(new SelfWeb());
        ModuleManager.modules.add(new Sharp32kDetect());
        ModuleManager.modules.add(new StrengthDetect());
        ModuleManager.modules.add(new Surround());
        ModuleManager.modules.add(new Announcer());
        ModuleManager.modules.add(new AntiDeathScreen());
        ModuleManager.modules.add(new AntiSound());
        ModuleManager.modules.add(new AntiSpam());
        ModuleManager.modules.add(new Australia());
        ModuleManager.modules.add(new AutoQMain());
        ModuleManager.modules.add(new AutoWither());
        ModuleManager.modules.add(new Avoid());
        ModuleManager.modules.add(new CameraClip());
        ModuleManager.modules.add(new ColorSign());
        ModuleManager.modules.add(new CoordLogger());
        ModuleManager.modules.add(new CrashExploit());
        ModuleManager.modules.add(new CustomChat());
        ModuleManager.modules.add(new DonkeyAlert());
        ModuleManager.modules.add(new FakePlayer());
        ModuleManager.modules.add(new FakeVanilla());
        ModuleManager.modules.add(new FovSlider());
        ModuleManager.modules.add(new HopperNuker());
        ModuleManager.modules.add(new LiquidInteract());
        ModuleManager.modules.add(new MCF());
        ModuleManager.modules.add(new MobOwner());
        ModuleManager.modules.add(new NoEntityTrace());
        ModuleManager.modules.add(new NoPacketKick());
        ModuleManager.modules.add(new PortalChat());
        ModuleManager.modules.add(new SkinFlicker());
        ModuleManager.modules.add(new Time());
        ModuleManager.modules.add(new Timer());
        ModuleManager.modules.add(new VisualRange());
        ModuleManager.modules.add(new ElytraFly());
        ModuleManager.modules.add(new FastSwim());
        ModuleManager.modules.add(new Flight());
        ModuleManager.modules.add(new ForgeFly());
        ModuleManager.modules.add(new GuiMove());
        ModuleManager.modules.add(new HoleTP());
        ModuleManager.modules.add(new Jesus());
        ModuleManager.modules.add(new LongJump());
        ModuleManager.modules.add(new NoSlowDown());
        ModuleManager.modules.add(new Sprint());
        ModuleManager.modules.add(new Step());
        ModuleManager.modules.add(new Strafe());
        ModuleManager.modules.add(new Velocity());
        ModuleManager.modules.add(new AntiVoid());
        ModuleManager.modules.add(new AutoReplenish());
        ModuleManager.modules.add(new AutoWalk());
        ModuleManager.modules.add(new Blink());
        ModuleManager.modules.add(new FastFall());
        ModuleManager.modules.add(new Freecam());
        ModuleManager.modules.add(new ItemSpoof());
        ModuleManager.modules.add(new Multitask());
        ModuleManager.modules.add(new NoBreakAnimation());
        ModuleManager.modules.add(new PacketSwing());
        ModuleManager.modules.add(new SelfLogoutSpot());
        ModuleManager.modules.add(new Speedmine());
        ModuleManager.modules.add(new TpsSync());
        ModuleManager.modules.add(new XCarry());
        ModuleManager.modules.add(new AntiFog());
        ModuleManager.modules.add(new Arrows());
        ModuleManager.modules.add(new BlockHighlight());
        ModuleManager.modules.add(new BossStack());
        ModuleManager.modules.add(new BreakESP());
        ModuleManager.modules.add(new Cape());
        ModuleManager.modules.add(new Chams());
        ModuleManager.modules.add(new Chat());
        ModuleManager.modules.add(new ChunkFinder());
        ModuleManager.modules.add(new Compass());
        ModuleManager.modules.add(new EnchantColor());
        ModuleManager.modules.add(new NewGui());
        ModuleManager.modules.add(new ESP());
        ModuleManager.modules.add(new ExeterGui());
        ModuleManager.modules.add(new ExtraTab());
        ModuleManager.modules.add(new FullBright());
        ModuleManager.modules.add(new HellenKeller());
        ModuleManager.modules.add(new HoleESP());
        ModuleManager.modules.add(new ImageESP());
        ModuleManager.modules.add(new ItemESP());
        ModuleManager.modules.add(new LogoutSpots());
        ModuleManager.modules.add(new Nametags());
        ModuleManager.modules.add(new NoHurtCam());
        ModuleManager.modules.add(new NoRender());
        ModuleManager.modules.add(new OffhandSwing());
        ModuleManager.modules.add(new OutlineESP());
        ModuleManager.modules.add(new Pathfind());
        ModuleManager.modules.add(new Search());
        ModuleManager.modules.add(new SecretShaders());
        ModuleManager.modules.add(new PvPInfo());
        ModuleManager.modules.add(new ShulkerPreview());
        ModuleManager.modules.add(new Skeleton());
        ModuleManager.modules.add(new StorageESP());
        ModuleManager.modules.add(new ToolTips());
        ModuleManager.modules.add(new Tracers());
        ModuleManager.modules.add(new Trajectories());
        ModuleManager.modules.add(new ViewmodelChanger());
        ModuleManager.modules.add(new VoidESP());
        ModuleManager.modules.add(new Waypoints());
        ModuleManager.modules.add(new Xray());
        ModuleManager.modules.add(new HudEditor());
        ModuleManager.modules.add(new Totems());
        ModuleManager.modules.add(new Obsidian());
        ModuleManager.modules.add(new Crystals());
        ModuleManager.modules.add(new Gapples());
        ModuleManager.modules.add(new Exp());
        ModuleManager.modules.add(new InvPreview());
        ModuleManager.modules.add(new CraftingPreview());
        ModuleManager.modules.add(new TextRadar());
        ModuleManager.modules.add(new FeatureList());
        ModuleManager.modules.add(new Player());
        ModuleManager.modules.add(new Welcome());
        ModuleManager.modules.add(new OldName());
        ModuleManager.modules.add(new TheGoons());
        ModuleManager.modules.add(new Potions());
        ModuleManager.modules.add(new StickyNotes());
        ModuleManager.modules.add(new HoleHud());
        ModuleManager.modules.add(new Info());
        ModuleManager.modules.add(new Armor());
        ModuleManager.modules.add(new GodInfo());
        ModuleManager.modules.add(new Watermark());
        ModuleManager.modules.add(new Logo());
        ModuleManager.modules.add(new Target());
    }
    
    public static void onUpdate() {
        Xulu.MODULE_MANAGER.getModules().stream().filter(Module::isToggled).forEach(Module::onUpdate);
        NewGui.resetGui();
        Xulu.TASK_SCHEDULER.onUpdate();
    }
    
    public static void onRender() {
        Xulu.MODULE_MANAGER.getModules().stream().filter(Module::isToggled).forEach(Module::onRender);
    }
    
    public static void onWorldRender(final RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("eclient");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        final Vec3d renderPos = EntityUtil.getInterpolatedPos((Entity)Wrapper.getPlayer(), event.getPartialTicks());
        final RenderEvent e = new RenderEvent(XuluTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        final RenderEvent event2;
        Xulu.MODULE_MANAGER.getModules().stream().filter(Module::isToggled).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.onWorldRender(event2);
            Minecraft.getMinecraft().profiler.endSection();
            return;
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        XuluTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
        Minecraft.getMinecraft().profiler.endSection();
    }
    
    public ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }
    
    @Deprecated
    public Module getModuleByName(final String name) {
        return ModuleManager.modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public Module getModule(final Class<? extends Module> clazz) {
        return ModuleManager.modules.stream().filter(module -> module.getClass() == clazz).findFirst().orElse(null);
    }
    
    public <T extends Module> T getModuleT(final Class<T> clazz) {
        return ModuleManager.modules.stream().filter(module -> module.getClass() == clazz).map(module -> module).findFirst().orElse(null);
    }
    
    public static boolean isModuleEnabled(final String name) {
        final Module m = ModuleManager.modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return m != null && m.isToggled();
    }
    
    static {
        ModuleManager.modules = new ArrayList<Module>();
    }
}
