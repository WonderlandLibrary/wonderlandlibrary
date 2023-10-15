/*    */ package org.neverhook.client.ui.components.changelog;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.neverhook.client.NeverHook;
/*    */ 
/*    */ 
/*    */ public class ChangeManager
/*    */ {
/*  9 */   public static ArrayList<ChangeLog> changeLogs = new ArrayList<>();
/*    */   
/*    */   public ArrayList<ChangeLog> getChangeLogs() {
/* 12 */     return changeLogs;
/*    */   }
/*    */   
/*    */   public ChangeManager() {
/* 16 */     changeLogs.add(new ChangeLog("NeverHook build " + NeverHook.instance.build, ChangeType.NONE));
/* 17 */     changeLogs.add(new ChangeLog("Baritone", ChangeType.ADD));
/* 18 */     changeLogs.add(new ChangeLog("Trajectories", ChangeType.ADD));
/* 19 */     changeLogs.add(new ChangeLog("4 new capes", ChangeType.ADD));
/* 20 */     changeLogs.add(new ChangeLog("inventory and tab animations", ChangeType.ADD));
/* 21 */     changeLogs.add(new ChangeLog("many custom settings to AntiBot", ChangeType.ADD));
/* 22 */     changeLogs.add(new ChangeLog("new mode HighJump - MatrixDamage", ChangeType.ADD));
/* 23 */     changeLogs.add(new ChangeLog("checkbox and slider to Scaffold (Rotation Random, Rotation Pitch Random and Rotation Yaw Random)", ChangeType.ADD));
/* 24 */     changeLogs.add(new ChangeLog("2 check boxes to WaterMark (Shadow Effect and Glow Effect)", ChangeType.ADD));
/* 25 */     changeLogs.add(new ChangeLog("FontY to FeatureList", ChangeType.ADD));
/* 26 */     changeLogs.add(new ChangeLog("Speed modes (NCP LowHop, Matrix OnGround Latest)", ChangeType.ADD));
/* 27 */     changeLogs.add(new ChangeLog("HitColor", ChangeType.ADD));
/* 28 */     changeLogs.add(new ChangeLog("checkbox EnderPearlESP (Tracers)", ChangeType.ADD));
/* 29 */     changeLogs.add(new ChangeLog("checkbox Tracers (See Only)", ChangeType.ADD));
/* 30 */     changeLogs.add(new ChangeLog("AntiVoid mode (Invalid Position, Invalid Pitch and Flag)", ChangeType.ADD));
/* 31 */     changeLogs.add(new ChangeLog("mode NoFall (Hypixel)", ChangeType.ADD));
/* 32 */     changeLogs.add(new ChangeLog("checkbox ScoreBoard (Points)", ChangeType.ADD));
/* 33 */     changeLogs.add(new ChangeLog("PortalFeatures (Allows you to open a chat while in the portal)", ChangeType.ADD));
/* 34 */     changeLogs.add(new ChangeLog("custom setting AntiAFK", ChangeType.ADD));
/* 35 */     changeLogs.add(new ChangeLog("EntityESP mode (Glow)", ChangeType.FIXED));
/* 36 */     changeLogs.add(new ChangeLog("FPS drop when Trails is enabled", ChangeType.FIXED));
/* 37 */     changeLogs.add(new ChangeLog("Tracers", ChangeType.FIXED));
/* 38 */     changeLogs.add(new ChangeLog("AutoPotion", ChangeType.RECODE));
/* 39 */     changeLogs.add(new ChangeLog("AntiAFK", ChangeType.RECODE));
/* 40 */     changeLogs.add(new ChangeLog("Checkbox minecraft moved from HUD as FontList mode", ChangeType.MOVED));
/* 41 */     changeLogs.add(new ChangeLog("ChinaHat", ChangeType.IMPROVED));
/* 42 */     changeLogs.add(new ChangeLog("all WaterMarks", ChangeType.RECODE));
/* 43 */     changeLogs.add(new ChangeLog("Eagle", ChangeType.RECODE));
/* 44 */     changeLogs.add(new ChangeLog("TriangleESP", ChangeType.RECODE));
/* 45 */     changeLogs.add(new ChangeLog("HitBoxes (Sometimes not updated)", ChangeType.IMPROVED));
/* 46 */     changeLogs.add(new ChangeLog("If the NoClip speed is 0, then the player's normal speed is set", ChangeType.IMPROVED));
/* 47 */     changeLogs.add(new ChangeLog("FreeCam", ChangeType.RECODE));
/* 48 */     changeLogs.add(new ChangeLog("Secret feature", ChangeType.ADD));
/* 49 */     changeLogs.add(new ChangeLog("HurtClip", ChangeType.RECODE));
/* 50 */     changeLogs.add(new ChangeLog("HitMarker", ChangeType.DELETE));
/* 51 */     changeLogs.add(new ChangeLog("HitSounds", ChangeType.DELETE));
/* 52 */     changeLogs.add(new ChangeLog("SessionTime from HUD", ChangeType.DELETE));
/* 53 */     changeLogs.add(new ChangeLog("checkbox Tracers (Only Player)", ChangeType.ADD));
/* 54 */     changeLogs.add(new ChangeLog("KillAura modes [TargetESP] (Jello, Sims and Astolfo) and sliders (CircleRange and Points)", ChangeType.ADD));
/* 55 */     changeLogs.add(new ChangeLog("2 sliders to KillAura (Points, CircleRange)", ChangeType.ADD));
/* 56 */     changeLogs.add(new ChangeLog("many new colors to the WaterMark (Rainbow, Gradient, Default, Static)", ChangeType.ADD));
/* 57 */     changeLogs.add(new ChangeLog("now the name of the color picker setting is on the left", ChangeType.IMPROVED));
/* 58 */     changeLogs.add(new ChangeLog("checkbox antiaction to FreeCam", ChangeType.ADD));
/* 59 */     changeLogs.add(new ChangeLog("checkbox Matrix Destruction to Secret", ChangeType.ADD));
/* 60 */     changeLogs.add(new ChangeLog("AirStuck", ChangeType.RECODE));
/* 61 */     changeLogs.add(new ChangeLog("baritone crash due to Duplicate setting name", ChangeType.FIXED));
/* 62 */     changeLogs.add(new ChangeLog("big cpu load due to Discord Rpc", ChangeType.FIXED));
/* 63 */     changeLogs.add(new ChangeLog("custom setting to Animations (x, y, z, rotate, rotate2, rotate3, angle, scale)", ChangeType.ADD));
/* 64 */     changeLogs.add(new ChangeLog("Rotation strafe silent in KillAura", ChangeType.FIXED));
/* 65 */     changeLogs.add(new ChangeLog("now the head of the player on TargetHud when hitting it turns red", ChangeType.IMPROVED));
/* 66 */     changeLogs.add(new ChangeLog("the design of Cape Selector and Config Manager was slightly redesigned", ChangeType.IMPROVED));
/* 67 */     changeLogs.add(new ChangeLog("checkbox and slider Only Crits, Fall Distance to Trigger Bot", ChangeType.ADD));
/* 68 */     changeLogs.add(new ChangeLog("now if anti-bot detects a bot NameTags and EntityESP write [Bot] next to the nickname", ChangeType.IMPROVED));
/* 69 */     changeLogs.add(new ChangeLog("NoRotate to NoServerRotation", ChangeType.RENAMED));
/* 70 */     changeLogs.add(new ChangeLog("that only crits did not work in single player", ChangeType.FIXED));
/* 71 */     changeLogs.add(new ChangeLog("Bread Shield in KillAura", ChangeType.RECODE));
/* 72 */     changeLogs.add(new ChangeLog("now KillAura bypasses AAC", ChangeType.IMPROVED));
/* 73 */     changeLogs.add(new ChangeLog("NoSlowDown", ChangeType.IMPROVED));
/* 74 */     changeLogs.add(new ChangeLog("now NameTags renders what is in the left hand", ChangeType.IMPROVED));
/* 75 */     changeLogs.add(new ChangeLog("checkbox off hand in NameTags", ChangeType.DELETE));
/* 76 */     changeLogs.add(new ChangeLog("EntityESP 2D", ChangeType.RECODE));
/* 77 */     changeLogs.add(new ChangeLog("GuiButton", ChangeType.IMPROVED));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\changelog\ChangeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */