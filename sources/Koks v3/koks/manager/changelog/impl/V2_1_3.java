package koks.manager.changelog.impl;

import koks.manager.changelog.Changelog;
import koks.manager.changelog.ChangelogInfo;

/**
 * @author kroko
 * @created on 06.11.2020 : 15:59
 */

@ChangelogInfo(version = "2.1.3")
public class V2_1_3 extends Changelog {

    @Override
    public void changes() {
        added("NameProtect");
        added("CivBreak");
        added("IgnoreDeath Setting in KillAura");
        added("Roles for Users");
        added("Application Icon");
        added("Shader Background");
        added("Friends");
        added("Configs");
        added("Altening Support");
        added("Alt Login");
        added("Critical's (W.I.P)");
        added("TNTBlock");
        added("Teams");
        added("New Velocity (Custom, IntaveKeepLow)");
        added("Switched to Client Launcher (by Haze)");
        added("Rcon Login");
        added("Tired Speed");
        added("Intave Speed");
        added("AAC3.3.12 Speed");
        added("Intave Speed");
        added("new MainMenu Design");
        added("Mineplex Speed");
        added("Mineplex Step");
        added("Teleport (Vanilla atm)");
        added("ItemESP Glow Mode");
        added("PlayerESP Glow Mode");
        added("New Scaffold Type");
        added("Settings now Save");
        removed("SuperHit (W.I.P)");
        fixed("Can't Attack With Hand (now fixed)");
        fixed("Minecraft Start Title");
        fixed("NameTags NaN Display");
        fixed("Mouse Sensitivity Fix");
    }
}
