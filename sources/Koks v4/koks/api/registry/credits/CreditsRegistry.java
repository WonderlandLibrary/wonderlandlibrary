package koks.api.registry.credits;

import koks.api.registry.Registry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class CreditsRegistry implements Registry {
    private static final List<Credits> CREDITS = new ArrayList<>();

    @Override
    public void initialize() {
        CREDITS.add(new DasNeueUpdate());
        CREDITS.add(new EnZaXD());
        CREDITS.add(new Sumandora());
        CREDITS.add(new Kadir());
        CREDITS.add(new Carl());
        CREDITS.add(new Ples12());
        CREDITS.add(new XButtonn());
        CREDITS.add(new OfficialMax());
        CREDITS.add(new W4z3d());
        CREDITS.add(new Igs());

        CREDITS.sort(Comparator.comparing(credits -> credits.getHelped().length));
    }

    @Credits.Info(discordTag = "DasNeueUpdate#2496", helped = {"Should Yaw", "Intave 13.0.0 Fly", "Physical Particles", "A3 Fix", "PathFinder", "Intave13 Spider", "CivBreak"})
    public static class DasNeueUpdate extends Credits { }

    @Credits.Info(discordTag = "EnZaXD#6257", helped = {"ViaForge"}, link = "https://www.github.com/FlorianMichael/ViaForge")
    public static class EnZaXD extends Credits { }

    @Credits.Info(discordTag = "Sumandora#6178", helped = {"BestVector in KillAura", "Expand Scaffold"})
    public static class Sumandora extends Credits { }

    @Credits.Info(discordTag = "Kadir#0069", helped = {"AntiVanish"})
    public static class Kadir extends Credits { }

    @Credits.Info(discordTag = "Carl.#9999", helped = {"CubeCraft Damage Method", "Verus1187 Damage Method", "Redesky Disabler"})
    public static class Carl extends Credits { }

    @Credits.Info(discordTag = "ples12#1337", helped = {"China Hat"})
    public static class Ples12 extends Credits { }

    @Credits.Info(discordTag = "XButtonn#7666", helped = {"Intave14 Gomme SkyWars LongJump"})
    public static class XButtonn extends Credits { }

    @Credits.Info(discordTag = "officialMax#1147", helped = {"Intave13 NoFall", "AAC4 NoSlowDown"})
    public static class OfficialMax extends Credits { }

    @Credits.Info(discordTag = "W4z3d#0911", helped = {"Mineplex Fast Speed"})
    public static class W4z3d extends Credits { }

    @Credits.Info(discordTag = "igs#9036", helped = {"Intave13 NoSlowDown Spoof"})
    public static class Igs extends Credits { }
}
