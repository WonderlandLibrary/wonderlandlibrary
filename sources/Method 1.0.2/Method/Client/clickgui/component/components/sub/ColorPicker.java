package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;


public class ColorPicker extends Component {

    private final Setting set;
    private boolean dragging = false;
    private String localname;
    private int indexer; // 0 color 1 saturation 2 brightness 3 Alpha
    public double renderWidth = 0;

    public ColorPicker(Setting value, Button button, int offset) {
        this.indexer = 0;
        this.set = value;
        this.parent = button;
        this.offset = offset;
        this.localname = set.getName();
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, 0xA6222222);
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, this.set.getcolor());
        Gui.drawRect((int) (parent.parent.getX() + renderWidth), parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth + 6, parent.parent.getY() + offset + 12, 0xBF444444);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1f, 1f, 0.5f);
        Button.fontSelectButton(this.localname, (float) ((parent.parent.getX() + 8)), (float) ((parent.parent.getY() + offset + 2)), -1);
        GlStateManager.popMatrix();
    }

    @Override
    public String getName() {
        return this.set.getName();
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        double diff = Math.min(this.parent.parent.getWidth(), Math.max(0, mouseX - this.parent.parent.getX()));
        double min = set.getMin();
        double max = set.getMax();
        switch (this.indexer) {
            case 0:
                renderWidth = (88) * (set.getValDouble() - min) / (max - min);
                this.localname = (set.getName() + " Color");
                break;
            case 1:
                renderWidth = (88) * (set.getSat() - min) / (max - min);
                this.localname = (set.getName() + " Saturation");
                break;
            case 2:
                this.localname = (set.getName() + " Brightness");
                renderWidth = (88) * (set.getBri() - min) / (max - min);
                break;
            case 3:
                this.localname = (set.getName() + " Alpha");
                renderWidth = (88) * (set.getAlpha() - min) / (max - min);
                break;
        }

        if (dragging) {
            final double value = (diff / 88) * (max - min) + min;
            switch (this.indexer) {
                case 0:
                    set.setValDouble(diff == 0 ? set.getMin() : roundToPlace(value));
                    break;
                case 1:
                    set.setsaturation((float) (diff == 0 ? set.getMin() : roundToPlace(value)));
                    break;
                case 2:
                    set.setbrightness((float) (diff == 0 ? set.getMin() : roundToPlace(value)));
                    break;
                case 3:
                    set.setAlpha((float) (diff == 0 ? set.getMin() : roundToPlace(value)));
                    break;
            }
        }
        super.updateComponent(mouseX, mouseY);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0 && this.parent.open) {
            this.parent.mod.settingChanged(ClickType.Color);
            this.dragging = true;
        }
        if ((this.hovered && button == 1 && this.parent.open)) {
            this.indexer = this.indexer == 3 ? 0 : this.indexer + 1;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
