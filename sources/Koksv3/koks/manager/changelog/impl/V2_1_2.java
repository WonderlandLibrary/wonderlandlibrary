package koks.manager.changelog.impl;

import koks.manager.changelog.Changelog;
import koks.manager.changelog.ChangelogInfo;

/**
 * @author kroko
 * @created on 06.11.2020 : 16:24
 */

@ChangelogInfo(version = "2.1.2")
public class V2_1_2 extends Changelog {

    @Override
    public void changes() {
        added("Recoded the Client!");
        added("AutoBlock for KillAura");
        added("Rotations Module (for KillAura Settings)");
        added("SuperHit");
        added("CubeCraft Fly");
        added("FastBow");
        added("FakeRotations");
        added("InvMove");
        added("Fly (AAC3.3.12, MCCentral)");
        added("Nametags");
        added("NoFov");
        added("NoBob");
        added("NoHurtCam");
        added("CustomItem");
        added("Step (Vanilla, Intave)");
        added("NoFall (Mineplex, Intave, AAC4)");
        added("SetBack");
        added("Phase (Intave, Hive)");
        added("Velocity (AAC3, AAC4)");
        added("MemoryCleaner");
        added("VClip");
        fixed("Intave Velocity");
        fixed("Intave Jesus");
    }
}
