package dev.rise.util.render.theme;

import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public enum ThemeType {
    ARRAYLIST,
    LOGO,
    FLAT_COLOR,
    GENERAL,
}
