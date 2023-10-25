package net.minecraft.client.renderer.entity;

import net.minecraft.scoreboard.Team;

static class RendererLivingEntity.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$scoreboard$Team$EnumVisible;

    static {
        $SwitchMap$net$minecraft$scoreboard$Team$EnumVisible = new int[Team.EnumVisible.values().length];
        try {
            RendererLivingEntity.1.$SwitchMap$net$minecraft$scoreboard$Team$EnumVisible[Team.EnumVisible.ALWAYS.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            RendererLivingEntity.1.$SwitchMap$net$minecraft$scoreboard$Team$EnumVisible[Team.EnumVisible.NEVER.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            RendererLivingEntity.1.$SwitchMap$net$minecraft$scoreboard$Team$EnumVisible[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            RendererLivingEntity.1.$SwitchMap$net$minecraft$scoreboard$Team$EnumVisible[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
