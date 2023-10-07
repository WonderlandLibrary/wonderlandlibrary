package cc.novoline.gui.screen.test;

import cc.novoline.Novoline;
import cc.novoline.gui.NovoGuiScreen;
import cc.novoline.gui.button.FunctionalButton;
import cc.novoline.gui.group2.BasicRoundedGroupWithTitle;
import cc.novoline.gui.label.BasicLabel;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_16.SFBOLD_16;
import cc.novoline.utils.notifications.NotificationType;

/**
 * @author xDelsy
 */
public class TestScreen extends NovoGuiScreen {

    @Override
    protected void onInitialize() {
        register(new FunctionalButton(new BasicLabel("Test label", 0xFF00FF00, SFBOLD_16), 10, 10, a -> {
            Novoline.getInstance().getNotificationManager().pop("Clicked " + a, NotificationType.SUCCESS);
        }));
            register(new BasicRoundedGroupWithTitle(new BasicLabel("", 0xFF00FF00, SFBOLD_16), 8, 10, 40, 400, 200));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
