package me.sound;

public enum SoundPlayer$SoundType {
    Enter("enter.wav"),
    Notification("notification.wav"),
    Startup("startup.wav"),
    ClickGuiOpen("clickguiopen.wav"),
    Ding("dingsound.wav"),
    Crack("cracksound.wav"),
    EDITION("ingame.wav"),
    VICTORY("victory.wav"),
    BACKDOOL("back.wav"),
    SKEET("skeet.wav"),
    NEKO("neko.wav"),
    SPECIAL("spec.wav");

    final String name;

    private SoundPlayer$SoundType(String fileName) {
        this.name = fileName;
    }

    String getName() {
        return this.name;
    }
}
