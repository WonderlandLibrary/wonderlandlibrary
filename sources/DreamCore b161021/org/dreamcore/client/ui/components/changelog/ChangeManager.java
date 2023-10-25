package org.dreamcore.client.ui.components.changelog;

import org.dreamcore.client.dreamcore;

import java.util.ArrayList;

public class ChangeManager {

    public static ArrayList<ChangeLog> changeLogs = new ArrayList<>();

    public ArrayList<ChangeLog> getChangeLogs() {
        return changeLogs;
    }

    public ChangeManager() {
        changeLogs.add(new ChangeLog("dreamcore build " + dreamcore.instance.build, ChangeType.NONE));
        changeLogs.add(new ChangeLog("Baritone", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Trajectories", ChangeType.ADD));
        changeLogs.add(new ChangeLog("4 new capes", ChangeType.ADD));
        changeLogs.add(new ChangeLog("inventory and tab animations", ChangeType.ADD));
        changeLogs.add(new ChangeLog("many custom settings to AntiBot", ChangeType.ADD));
        changeLogs.add(new ChangeLog("new mode HighJump - MatrixDamage", ChangeType.ADD));
        changeLogs.add(new ChangeLog("checkbox and slider to Scaffold (Rotation Random, Rotation Pitch Random and Rotation Yaw Random)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("2 check boxes to WaterMark (Shadow Effect and Glow Effect)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("FontY to FeatureList", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Speed modes (NCP LowHop, Matrix OnGround Latest)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("HitColor", ChangeType.ADD));
        changeLogs.add(new ChangeLog("checkbox EnderPearlESP (Tracers)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("checkbox Tracers (See Only)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("AntiVoid mode (Invalid Position, Invalid Pitch and Flag)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("mode NoFall (Hypixel)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("checkbox ScoreBoard (Points)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("PortalFeatures (Allows you to open a chat while in the portal)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("custom setting AntiAFK", ChangeType.ADD));
        changeLogs.add(new ChangeLog("EntityESP mode (Glow)", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("FPS drop when Trails is enabled", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("Tracers", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("AutoPotion", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("AntiAFK", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("Checkbox minecraft moved from HUD as FontList mode", ChangeType.MOVED));
        changeLogs.add(new ChangeLog("ChinaHat", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("all WaterMarks", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("Eagle", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("TriangleESP", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("HitBoxes (Sometimes not updated)", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("If the NoClip speed is 0, then the player's normal speed is set", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("FreeCam", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("Secret feature", ChangeType.ADD));
        changeLogs.add(new ChangeLog("HurtClip", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("HitMarker", ChangeType.DELETE));
        changeLogs.add(new ChangeLog("HitSounds", ChangeType.DELETE));
        changeLogs.add(new ChangeLog("SessionTime from HUD", ChangeType.DELETE));
        changeLogs.add(new ChangeLog("checkbox Tracers (Only Player)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("KillAura modes [TargetESP] (Jello, Sims and Astolfo) and sliders (CircleRange and Points)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("2 sliders to KillAura (Points, CircleRange)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("many new colors to the WaterMark (Rainbow, Gradient, Default, Static)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("now the name of the color picker setting is on the left", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("checkbox antiaction to FreeCam", ChangeType.ADD));
        changeLogs.add(new ChangeLog("checkbox Matrix Destruction to Secret", ChangeType.ADD));
        changeLogs.add(new ChangeLog("AirStuck", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("baritone crash due to Duplicate setting name", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("big cpu load due to Discord Rpc", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("custom setting to Animations (x, y, z, rotate, rotate2, rotate3, angle, scale)", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Rotation strafe silent in KillAura", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("now the head of the player on TargetHud when hitting it turns red", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("the design of Cape Selector and Config Manager was slightly redesigned", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("checkbox and slider Only Crits, Fall Distance to Trigger Bot", ChangeType.ADD));
        changeLogs.add(new ChangeLog("now if anti-bot detects a bot NameTags and EntityESP write [Bot] next to the nickname", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("NoRotate to NoServerRotation", ChangeType.RENAMED));
        changeLogs.add(new ChangeLog("that only crits did not work in single player", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("Bread Shield in KillAura", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("now KillAura bypasses AAC", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("NoSlowDown", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("now NameTags renders what is in the left hand", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("checkbox off hand in NameTags", ChangeType.DELETE));
        changeLogs.add(new ChangeLog("EntityESP 2D", ChangeType.RECODE));
        changeLogs.add(new ChangeLog("GuiButton", ChangeType.IMPROVED));
    }
}