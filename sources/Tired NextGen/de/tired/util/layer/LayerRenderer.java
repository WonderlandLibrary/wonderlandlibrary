package de.tired.util.layer;


import de.tired.base.module.implementation.visual.*;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.Translate;
import de.tired.base.interfaces.IHook;
import de.tired.util.render.notification.NotifyManager;
import de.tired.util.render.shaderloader.ShaderRenderer;
import de.tired.Tired;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class LayerRenderer extends ShaderRenderLayer implements IHook {
    public float float2;
    private float animationX;
    private final Translate translate;


    public LayerRenderer() {
        this.translate = new Translate(0, 0);
    }

    @Override
    public void renderLayerWBlur() {
        if (Shader.getInstance().isState()) {
            ShaderRenderer.startBlur();

            if (HudModule.getInstance().isState())
                HudModule.getInstance().drawArraylist(true);

            if (Tired.INSTANCE.moduleManager.moduleBy(Notifications.class).isState())
                NotifyManager.drawNotifications(true, true);

            if (NameTags.getInstance().isState())
                NameTags.getInstance().doRenderFinal(true);

            if (Shader.getInstance().chatBlur.getValue())
                MC.ingameGUI.renderChatFinal(true);


            final Scoreboard scoreboard = Tired.INSTANCE.moduleManager.moduleBy(Scoreboard.class);


            if (HotBar.getInstance().state)
                RenderUtil.instance.renderHotbar(HotBar.getInstance().smooth.getValue(), true);
            ShaderRenderer.stopBlur(12);


            if (Tired.INSTANCE.moduleManager.moduleBy(Notifications.class).isState()) {
                NotifyManager.drawNotifications(true, true);
            }


        } else {
            if (Tired.INSTANCE.moduleManager.moduleBy(Notifications.class).isState()) {
                NotifyManager.drawNotifications(true, true);
            }
        }
    }


    @Override
    public void renderNormalLayer() {


        if (Tired.INSTANCE.moduleManager.findModuleByClass(HotBar.class).isState()) {
            RenderUtil.instance.renderHotbar(HotBar.getInstance().smooth.getValue(), true);
        }

        if (NameTags.getInstance().isState()) {
            NameTags.getInstance().doRenderFinal(false);
        }

        final Scoreboard scoreboard = Tired.INSTANCE.moduleManager.moduleBy(Scoreboard.class);
        if (scoreboard.state) {
            scoreboard.draggable.setObjectWidth(MC.ingameGUI.scoreboardWidth);
            scoreboard.draggable.setObjectHeight(MC.ingameGUI.scoreboardHeight);
            renderScoreBoard(false, (int) scoreboard.draggable.getXPosition(), (int) scoreboard.draggable.getYPosition());
            renderScoreBoard(true, (int) scoreboard.draggable.getXPosition(), (int) scoreboard.draggable.getYPosition());
        }

        if (HudModule.getInstance().isState()) {
            HudModule.getInstance().drawArraylist(true);
            HudModule.getInstance().drawArraylist(false);
        }
        if (Tired.INSTANCE.moduleManager.moduleBy(Notifications.class).isState()) {
            NotifyManager.drawNotifications(false, false);
        }
    }

    public void renderScoreBoard(boolean rect, int x, int y) {
        net.minecraft.scoreboard.Scoreboard scoreboard = MC.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(MC.thePlayer.getName());

        if (scoreplayerteam != null) {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        final ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(MC);

            MC.ingameGUI.renderScoreboard(scoreobjective1, scaledresolution, rect, x, y);



        }
    }

}
