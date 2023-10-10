package today.flux.module;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.gui.FontRenderer;
import sun.misc.Unsafe;
import today.flux.event.RespawnEvent;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.irc.IRCClient;
import today.flux.module.implement.Combat.*;
import today.flux.module.implement.Ghost.*;
import today.flux.module.implement.Misc.*;
import today.flux.module.implement.Movement.*;
import today.flux.module.implement.Player.*;
import today.flux.module.implement.Render.*;
import today.flux.module.implement.World.*;
import today.flux.utility.ChatUtils;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public static List<Module> apiModules = new ArrayList<>();
    private static final ArrayList<Module> mL = new ArrayList<>();

    public static AntiPrick antiPrick = new AntiPrick();
    public static KillAura killAuraMod = new KillAura();
    public static NoRender noRenderMod = new NoRender();
    public static XRay xRayMod = new XRay();
    public static FullBright fullBrightMod = new FullBright();
    public static Phase phaseMod = new Phase();
    public static Chams chamsMod = new Chams();
    public static Ambience ambienceMod = new Ambience();
    public static AntiBots antiBotsMod = new AntiBots();
    public static MurderMystery murderMysteryMod = new MurderMystery();
    public static Scaffold scaffoldMod = new Scaffold();
    public static NoSlow noSlowMod = new NoSlow();
    public static ItemESP itemESPMod = new ItemESP();
    public static BetterSword betterSwordMod = new BetterSword();
    public static InvCleaner invCleanerMod = new InvCleaner();
    public static Hud hudMod = new Hud();
    public static FastUse fastUseMod = new FastUse();
    public static Criticals criticalsMod = new Criticals();
    public static AutoArmor autoArmorMod = new AutoArmor();
    public static Regen regenMod = new Regen();
    public static AutoPot autoPotMod = new AutoPot();
    public static Blink blinkMod = new Blink();
    public static NameTags nameTagsMod = new NameTags();
    public static LongJump longJumpMod = new LongJump();
    public static AutoClicker autoClickerMod = new AutoClicker();
    public static FastPlace fastPlaceMod = new FastPlace();
    public static Fly flyMod = new Fly();
    public static Freecam freecamMod = new Freecam();
    public static InvMove invMoveMod = new InvMove();
    public static Jesus jesusMod = new Jesus();
    public static NoRotate noRotateMod = new NoRotate();
    public static ESP espMod = new ESP();
    public static Speed speedMod = new Speed();
    public static Sprint sprintMod = new Sprint();
    public static Step stepMod = new Step();
    public static ChestESP chestESPMod = new ChestESP();
    public static Tracers tracersMod = new Tracers();
    public static Velocity velocityMod = new Velocity();
    public static Derp derpMod = new Derp();
    public static ChestStealer chestStealerMod = new ChestStealer();
    public static FastBreak fastBreakMod = new FastBreak();
    public static AirJump airJumpMod = new AirJump();
    public static NoFov noFovMod = new NoFov();
    public static NoFall noFallMod = new NoFall();
    public static Zoot zootMod = new Zoot();
    public static SafeWalk safeWalkMod = new SafeWalk();
    public static ViewClip viewClipMod = new ViewClip();
    public static NoHurtCam noHurtCamMod = new NoHurtCam();
    public static Strafe strafeMod = new Strafe();
    public static Nuker nukerMod = new Nuker();
    public static AntiVoid antiVoidMod = new AntiVoid();
    public static Sneak sneakMod = new Sneak();
    public static NameProtect nameProtectMod = new NameProtect();
    public static Ghost ghostMod = new Ghost();
    public static Reach reachMod = new Reach();
    public static Panic panicMod = new Panic();
    public static BreakAura breakAuraMod = new BreakAura();
    public static BowAimbot bowAimbotMod = new BowAimbot();
    public static CGUI clickGUIMod = new CGUI();
    public static FPSHurtCam fpsHurtCamMod = new FPSHurtCam();
    public static PullBack pullBackMod = new PullBack();
    public static MiddleClickFriend middleClickFriendMod = new MiddleClickFriend();
    public static AimAssist AimAssistMod = new AimAssist();
    public static AntiInvis antiInvisMod = new AntiInvis();
    public static TriggerBot triggerBotMod = new TriggerBot();
    public static HitBox hitBoxMod = new HitBox();
    public static AutoEat autoEatMod = new AutoEat();
    public static Aimbot aimbotMod = new Aimbot();
    public static DamageParticle damageParticleMod = new DamageParticle();
    public static ItemPhysics itemPhysicsMod = new ItemPhysics();
    public static Fastbow fastbowMod = new Fastbow();
    public static MotionBlur motionBlurMod = new MotionBlur();
    public static Spider spiderMod = new Spider();
    public static Projectile projectileMod = new Projectile();
    public static PortalMenu portalmenuMod = new PortalMenu();
    public static AutoL autoLMod = new AutoL();
    public static Wings wingMod = new Wings();
    public static AutoGG autoGGMod = new AutoGG();
    public static Eagle eagleMod = new Eagle();
    public static Module debugMod = new Module("Debug", Category.Misc, false);
    public static TargetStrafe targetStrafeModule = new TargetStrafe();
    public static HackerDetector hackdetectionModule = new HackerDetector();
    public static ZombieCrisis zombiecrisismodule = new ZombieCrisis();
    public static AutoTools autoToolsMod = new AutoTools();
    public static ChatBypass chatBypassMod = new ChatBypass();
    public static BlockAnimation animationMod = new BlockAnimation();
    public static Skeletal skeletalMod = new Skeletal();
    public static LightningLocator lightningLocatorMod = new LightningLocator();
    public static ObsidianRemover obsidianRemoverMod = new ObsidianRemover();
    public static Pendant pendantMod = new Pendant();
    public static BlockESP blockEspMod = new BlockESP();
    public static RotationAnimation rotationAnimationMod = new RotationAnimation();
    public static ChestAura chestAuraMod = new ChestAura();
    public static AutoHead autoHeadMod = new AutoHead();
    public static StaffAnalyser staffAnalyserMod = new StaffAnalyser();
    public static AutoRod autoRodMod = new AutoRod();
    public static Refill refillMod = new Refill();
    public static Timer timerMod = new Timer();
    public static BowLongJump bowLongJumpMod = new BowLongJump();
    public static RacistHat racistHatMod = new RacistHat();
    public static HudWindowMod hudWindowMod = new HudWindowMod();
    public static EnchantEffect enchantEffectMod = new EnchantEffect();
    public static Module ircMod = new Module("IRC", Category.World, false);
    public static Disabler disabler = new Disabler();
    public static AntiExploit antiExploitMod = new AntiExploit();

    public ModuleManager() {
        registerMod();
        mL.sort(Comparator.comparing(Module::getName));
    }

    public List<Module> getModulesRender(Object font) {
        List<Module> modules = null;
        modules = getModList().stream().filter(module -> !module.isHide() && module.isEnabled() && (!Hud.renderRenderCategory.getValue() || module.getCategory() != Category.Render)).collect(Collectors.toList());

        if (font instanceof FontUtils) {
            FontUtils nFont = (FontUtils) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return Float.compare(width1, width2);
            });
        } else if (font instanceof FontRenderer) {
            FontRenderer nFont = (FontRenderer) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return Float.compare(width1, width2);
            });
        }

        Collections.reverse(modules);
        return modules;
    }

    public List<Module> getModulesRenderWithAnimation(Object font) {
        List<Module> modules = null;
        modules = new ArrayList<>(getModList());

        if (font instanceof FontUtils) {
            FontUtils nFont = (FontUtils) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return -Float.compare(width1, width2);
            });
        } else if (font instanceof FontRenderer) {
            FontRenderer nFont = (FontRenderer) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return -Float.compare(width1, width2);
            });
        }

        return modules;
    }

    @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void registerMod() {
        mL.add(ircMod);
        mL.add(antiPrick);
        mL.add(noRenderMod);
        mL.add(fastUseMod);
        mL.add(speedMod);
        mL.add(sprintMod);
        mL.add(stepMod);
        mL.add(chestESPMod);
        mL.add(pendantMod);
        mL.add(tracersMod);
        // mL.add(voidJumpMod);
        mL.add(velocityMod);
        mL.add(autoHeadMod);
        mL.add(derpMod);
        mL.add(autoToolsMod);
        mL.add(chatBypassMod);
        mL.add(staffAnalyserMod);
        mL.add(autoRodMod);
        mL.add(refillMod);
        mL.add(bowLongJumpMod);
        mL.add(enchantEffectMod);
        // Client Check (Way 3)
        try {
            int offset = (int) System.currentTimeMillis() / 1000 / 60;
            Field strChar = String.class.getDeclaredField("value");
            strChar.setAccessible(true);
            char[] aArray = (char[]) strChar.get(IRCClient.loggedPacket.getVerifySign());
            char[] pre = new char[] {'3', 'I', '4', 'C', 'V', 'X', ')', 'Q', '9', '2', '~', 'n', '~', 'X', '!', 'k', '!', 'z', '%', 'T', '!', 'I', 'g', '+', 'v', 'g', 'H', 'q', 'B', 'S', '*', 'Y', 'G', '%', 's', '9', '1', 'Q', 's', 'd', 'i', '*', 'B', 'V', 'q', 'W', 'U', '#', 'M', 'j', 'r', 'b', '*', '#', 'F', '3', 'n', 'y', '8', 'C', '5', 'x', 'y', 'm', 'T', 'q', 'a', 'D', 'x', 'y', 'V', '(', '0', 'M', ')', 'h', 'a', 'K', 'L', 'N', 'K', 'c', 'P', 'I', 'i', '@', 'z', 'Q', 'S', 'f', '4', 'H', 'X', 'g', 'd', 'S', '8', 'i', '%', 'C', 'O', '*', 'N', '5', '(', '3', '7', '*', 'K', 'b', 'c', '0', 'd', 'k', '#', '!', '0', 's', 'O', 'O', 'i', 'B', 'K', '4', 'o'};
            char[] bArray = (char[]) strChar.get(IRCClient.loggedPacket.getI());
            char[] post = new char[] {'b', 't', 'J', '(', 'X', 'I', 'e', 'a', 'C', '8', 'e', 'k', '@', 'l', 'x', '!', 'K', '~', 'C', '0', '5', 'F', 'B', 'M', '0', 'q', 'T', 'r', 'I', 'g', 'L', '*', 'C', 'l', 'S', 'c', 'z', 'l', '9', '^', 'Y', '6', 't', 'q', 'N', 'N', 'u', 'W', '_', 'J', 'l', '!', 'L', 'F', 'F', 'o', '8', 'Z', '8', 'S', '~', 'A', 'L', 's', '7', 'N', 'E', '#', 'y', 'F', '8', 'S', '4', '4', 'd', 'F', 'G', '4', '*', 'n', 'J', '9', 'Q', 'Z', 'J', 'R', 'M', 'F', 'c', 'p', 'c', 'b', '1', '0', 'n', '6', 'i', 'm', 'e', 'Y', 'C', 'y', '5', 'W', '1', 'X', ')', '5', 'A', 'Z', '^', 's'};
            char[] c1 = new char[pre.length + bArray.length + post.length];
            int index = 0;

            for (char c : pre)
                c1[index++] = c;
            for (char c : bArray)
                c1[index++] = c;
            for (char c : post)
                c1[index++] = c;

            int a = 0;
            do {
                if (c1.length >= (1 + a * 10)) { c1[a * 10] = (char) (((c1[a * 10] & 0x0D28469A | 0x7987DEC2 ^ 0x31DBD21E) ^ 0x016CF772 & 0xA187AB4C | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (2 + a * 10)) { c1[1 + a * 10] = (char) (((c1[1 + a * 10] & 0xE4ABDAE7 | 0xCA8A1BEA ^ 0x49FCDD8A) ^ 0x406A0ED1 & 0xE04B1232 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (3 + a * 10)) { c1[2 + a * 10] = (char) (((c1[2 + a * 10] & 0x3C77E0C2 | 0xE1BAAB15 ^ 0x5043FEF7) ^ 0x0DB8F4B3 & 0x26B79531 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (4 + a * 10)) { c1[3 + a * 10] = (char) (((c1[3 + a * 10] & 0x9C8DE373 | 0x6AC3E8F1 ^ 0x9CCF4BC2) ^ 0xF78DA7D2 & 0x20F98FDC | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (5 + a * 10)) { c1[4 + a * 10] = (char) (((c1[4 + a * 10] & 0x988035C4 | 0xA7DC44EF ^ 0x5B51D984) ^ 0x287347FD & 0x463AA1AF | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (6 + a * 10)) { c1[5 + a * 10] = (char) (((c1[5 + a * 10] & 0xEE5CD652 | 0xFC26C20A ^ 0x07F68B19) ^ 0xAF656F86 & 0xF0606138 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (7 + a * 10)) { c1[6 + a * 10] = (char) (((c1[6 + a * 10] & 0xF87F4368 | 0x708FBFB6 ^ 0xEEEBFE36) ^ 0xC83E71D0 & 0x3A71737D | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (8 + a * 10)) { c1[7 + a * 10] = (char) (((c1[7 + a * 10] & 0x73D37DA5 | 0x9C506569 ^ 0xD46A6D9E) ^ 0x25B3152E & 0x9C508CD9 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (9 + a * 10)) { c1[8 + a * 10] = (char) (((c1[8 + a * 10] & 0x5E020069 | 0x9FD921C6 ^ 0xF741771B) ^ 0xEF79EA28 & 0x42242C3F | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (10 + a * 10)) { c1[9 + a * 10] = (char) (((c1[9 + a * 10] & 0x4BDFE9E4 | 0x4B73B73C ^ 0x615D6664) ^ 0x81832764 & 0x59E84A88 | (offset / 153) + (offset / 325)) % 57);}
            } while (c1.length > ++a*10);

            for (int i = 0; i < c1.length; i++) {
                c1[i] += (i % 4) | 0x0A8E2251 ^ 0xD67C88CC;
                c1[i] *= i % 2 == 0 ? 0xAB175676 : 0xBC920323;
                c1[i] %= 57;
                c1[i] = (char) (c1[i] > 0 ? c1[i] : -c1[i]);
                c1[i] += 65;
            }

            for (int i = 0; i < aArray.length; i++) {
                if (aArray[i] - c1[i] != 0) {
                    EventCancellable.a--;
                    Field field = Unsafe.class.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    Unsafe unsafe = (Unsafe) field.get(null);
                    Class<?> cacheClass = Class.forName("java.lang.Integer$IntegerCache");
                    Field cache = cacheClass.getDeclaredField("cache");

                    unsafe.putObject(Integer.getInteger(System.currentTimeMillis() / 223 + "B"), unsafe.staticFieldOffset(cache), null);
                    return;
                }
            }

            mL.add(criticalsMod);
            mL.add(autoArmorMod);
            mL.add(chestAuraMod);
            mL.add(regenMod);
            mL.add(autoPotMod);
            mL.add(obsidianRemoverMod);
            mL.add(hudMod);
            mL.add(blinkMod);
            mL.add(nameTagsMod);
            mL.add(lightningLocatorMod);
            mL.add(longJumpMod);
            mL.add(autoClickerMod);
            mL.add(fastPlaceMod);
            mL.add(flyMod);
            mL.add(freecamMod);
            mL.add(fullBrightMod);
            mL.add(invMoveMod);
            mL.add(jesusMod);
            mL.add(noRotateMod);
            mL.add(noSlowMod);
            mL.add(espMod);
            mL.add(killAuraMod);
            mL.add(chestStealerMod);
            mL.add(rotationAnimationMod);
            mL.add(antiBotsMod);
            mL.add(scaffoldMod);
            mL.add(invCleanerMod);
            mL.add(fastBreakMod);
            mL.add(airJumpMod);
            mL.add(noFovMod);
        } catch (Throwable e) {
            new Color(25123, 12351, 61231);
        }
        mL.add(noFallMod);
        mL.add(zootMod);
        mL.add(safeWalkMod);
        mL.add(viewClipMod);
        mL.add(noHurtCamMod);
        mL.add(strafeMod);
        mL.add(nukerMod);
        mL.add(antiVoidMod);
        mL.add(sneakMod);
        mL.add(nameProtectMod);
        mL.add(ghostMod);
        mL.add(reachMod);
        mL.add(panicMod);
        mL.add(breakAuraMod);
        mL.add(bowAimbotMod);
        mL.add(clickGUIMod);
        mL.add(fpsHurtCamMod);
        mL.add(pullBackMod);
        mL.add(middleClickFriendMod);
        mL.add(AimAssistMod);
        mL.add(antiInvisMod);
        mL.add(triggerBotMod);
        mL.add(xRayMod);
        mL.add(murderMysteryMod);
        mL.add(phaseMod);
        mL.add(chamsMod);
        mL.add(ambienceMod);
        mL.add(itemESPMod);
        mL.add(betterSwordMod);
        mL.add(hitBoxMod);
        mL.add(autoEatMod);
        mL.add(aimbotMod);
        mL.add(damageParticleMod);
        mL.add(itemPhysicsMod);
        mL.add(fastbowMod);
        mL.add(motionBlurMod);
        mL.add(spiderMod);
        mL.add(projectileMod);
        mL.add(portalmenuMod);
        mL.add(wingMod);
        mL.add(autoLMod);
        mL.add(autoGGMod);
        mL.add(eagleMod);
        mL.add(targetStrafeModule);
        mL.add(hackdetectionModule);
        mL.add(zombiecrisismodule);
        mL.add(animationMod);
        mL.add(skeletalMod);
        mL.add(blockEspMod);
        mL.add(timerMod);
        mL.add(hudWindowMod);
        mL.add(racistHatMod);
        mL.add(disabler);
        mL.add(antiExploitMod);
    }

    public Module getModuleByName(String theMod) {
        for (Module mod : getModList()) {
            if (mod.getName().equalsIgnoreCase(theMod)) {
                return mod;
            }
        }
        return null;
    }

    public Module getModuleByClass(Class<? extends Module> theMod) {
        for (Module mod : getModList()) {
            if (mod.getClass().equals(theMod)) {
                return mod;
            }
        }
        return null;
    }

    public List<Module> getModulesByCategory(Category category) {
        return getModList().stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }

    public static void registerModule(Module module) {
        mL.add(module);
    }

    public static ArrayList<Module> getModList() {
        ArrayList<Module> allModules = new ArrayList<>(mL);
        allModules.addAll(apiModules);
        return allModules;
    }

    @EventTarget
    public void onRespawn(RespawnEvent e) {
        if (Module.a == 1) {
            ChatUtils.debug("Reloading Modules");
            for (Module module : getModList()) {
                if (module == ModuleManager.antiBotsMod || module == ModuleManager.autoGGMod) continue;
                module.update();
            }
        }
    }
}
