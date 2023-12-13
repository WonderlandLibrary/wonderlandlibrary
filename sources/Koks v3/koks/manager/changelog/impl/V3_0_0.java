package koks.manager.changelog.impl;

import koks.manager.changelog.Changelog;
import koks.manager.changelog.ChangelogInfo;

/**
 * @author kroko
 * @created on 01.11.2020 : 05:17
 */

@ChangelogInfo(version = "3.0.0")
public class V3_0_0 extends Changelog{

    @Override
    public void changes() {
        added("ShaderESP to all ESP's");
        added("Pictures to the Main Menu");
        added("DormentESP");
        added("Tracers");
        added("ChestESP Cycle Mode");
        added("Swing");
        added("MotionGraph");
        added("ArrayList Mode");
        added("FakeBlock");
        added("Blink");
        added("Online Configs");
        added("AAC4 Speed");
        added("Intave GodMode for DBD (Dead By Daylight)");
        added("Discord Rich Presents");
        added("FastBow Timer Mode");
        added("Added new ClickGUI (Periodic Table)");
        added("WallSpeed");
        added("Step Modes");
        added("Friends");
        added("Safewalk");
        added("GommeMode");
        added("Xray");
        added("BedFucker");
        added("TrueSight");
        added("AntiVoid");
        added("FastBreak");
        added("Fullbright");
        added("ShopSaver");
        added("Changelog");
        added("NoRotate");
        added("IceSpeed");
        added("NoPitchLimit");
        added("AimBot");
        added("AntiAim");
        added("SystemTray");
        added("ItemTP (WIP)");
        added("Ambiance");
        added("DoubleJump");
        added("Hypixel AntiVoid");
        added("Hypixel Jump Fly");
        added("Disabler");
        added("Moved all KillAura Bot settings to AntiBot");
        fixed("ChestStealer");
        fixed("Intave Scaffold");
        fixed("Block Bug");
        removed("ClickGUI Settings");
    }

}
