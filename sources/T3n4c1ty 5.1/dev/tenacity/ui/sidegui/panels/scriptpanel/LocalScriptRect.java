package dev.tenacity.ui.sidegui.panels.scriptpanel;

import dev.tenacity.Tenacity;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.scripting.api.Script;
import dev.tenacity.ui.Screen;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.ui.sidegui.forms.Form;
import dev.tenacity.ui.sidegui.utils.CloudDataUtils;
import dev.tenacity.ui.sidegui.utils.IconButton;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RoundedUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LocalScriptRect implements Screen {
    private float x, y, width, height, alpha;
    private Color accentColor;
    private boolean clickable = true, compact = false;

    private BasicFileAttributes bfa = null;
    private final TooltipObject hoverInformation = new TooltipObject();


    private final List<IconButton> buttons = new ArrayList<>();

    private final Script script;
    private final Animation hoverAnimation = new DecelerateAnimation(250, 1);

    public LocalScriptRect(Script script) {
        this.script = script;
        Tenacity.INSTANCE.getSideGui().addTooltip(hoverInformation);
        buttons.add(new IconButton(FontUtil.UPLOAD, "Upload this script"));
        buttons.add(new IconButton(FontUtil.TRASH, "Delete this script"));

        try {
            bfa = Files.readAttributes(script.getFile().toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initGui() {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        Color textColor = ColorUtil.applyOpacity(Color.WHITE, alpha);
        RoundedUtil.drawRound(x, y, width, height, 5, ColorUtil.tripleColor(37, alpha));

        tenacityBoldFont26.drawString(script.getName(), x + 3, y + 3, textColor);

        float yOffset = compact ? 2.5f : 2;
        tenacityFont16.drawString(script.getAuthor(), x + 3, y + yOffset + tenacityBoldFont32.getHeight(), accentColor);

        if (bfa != null) {
            tenacityFont16.drawString(CloudDataUtils.getLastEditedTime(String.valueOf(bfa.lastModifiedTime().toMillis() / 1000)),
                    x + 5 + tenacityFont16.getStringWidth(script.getAuthor()), y + yOffset + tenacityBoldFont32.getHeight(),
                    ColorUtil.applyOpacity(textColor, .5f));
        }

        boolean hovering = SideGUI.isHovering(x, y, width, height, mouseX, mouseY);
        hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        hoverAnimation.setDuration(hovering ? 150 : 300);

        if (!compact) {
            tenacityFont16.drawWrappedText(script.getDescription(), x + 3,
                    y + 6 + tenacityBoldFont32.getHeight() + tenacityFont16.getHeight(),
                    ColorUtil.applyOpacity(textColor.getRGB(), .5f + (.5f * hoverAnimation.getOutput().floatValue())), width - 12, 3);
        }

        int seperationX = 0;
        for (IconButton button : buttons) {
            button.setX(x + width - (button.getWidth() + 4 + seperationX));
            button.setY(y + height - (button.getHeight() + 4));
            button.setAlpha(alpha);
            button.setIconFont(iconFont20);
            if (FontUtil.TRASH.equals(button.getIcon())) {
                button.setAccentColor(new Color(209, 56, 56));
            } else {
                button.setAccentColor(accentColor);
            }

            button.setClickAction(() -> {

                switch (button.getIcon()) {
                    case FontUtil.UPLOAD:
                        Form uploadForm = Tenacity.INSTANCE.getSideGui().displayForm("Upload Script");
                        uploadForm.setUploadAction((name, description) -> {
                            Multithreading.runAsync(() -> {
                                boolean validScript = Tenacity.INSTANCE.getScriptManager().processScriptData(script.getFile());
                                if (validScript) {
                                    String fileData = FileUtils.readFile(script.getFile());
                                    System.out.println(fileData);
                                    CloudUtils.postOnlineScript(name, description, fileData);
                                    Tenacity.INSTANCE.getCloudDataManager().refreshData();
                                }
                            });
                        });
                        break;
                    case FontUtil.TRASH:
                        IOUtils.deleteFile(script.getFile());
                        Tenacity.INSTANCE.getSideGui().getTooltips().clear();
                        Tenacity.INSTANCE.getSideGui().getScriptPanel().setRefresh(true);
                        break;
                }

            });


            button.drawScreen(mouseX, mouseY);
            seperationX += 8 + button.getWidth();
        }


        if (bfa != null) {
            String formatCode = "§a";
            hoverInformation.setTip(formatCode + "Last Modified§r: " + getCurrentTimeStamp(new Date(bfa.lastModifiedTime().toMillis())) + "\n");
            hoverInformation.setAdditionalInformation(compact ? (formatCode + "Description§r: " + script.getDescription()) : null);
        }


        boolean hoveringInfo = SideGUI.isHovering(getX() + 3, getY() + getHeight() - (tenacityFont14.getHeight() + 3),
                iconFont20.getStringWidth(FontUtil.INFO) + 2 + tenacityFont14.getStringWidth("Hover for more information"),
                tenacityFont14.getHeight() + 3, mouseX, mouseY);

        hoverInformation.setHovering(hoveringInfo);


        Animation hoverAnim = hoverInformation.getFadeInAnimation();
        float additionalAlpha = .65f * hoverAnim.getOutput().floatValue();

        iconFont16.drawString(FontUtil.INFO, getX() + 3, getY() + getHeight() - (iconFont16.getHeight() + 3), ColorUtil.applyOpacity(textColor, .35f + additionalAlpha));


        tenacityFont14.drawString("Hover for more information", getX() + 5 + iconFont16.getStringWidth(FontUtil.INFO),
                getY() + getHeight() - (tenacityFont14.getHeight() + 3), ColorUtil.applyOpacity(textColor, .35f + additionalAlpha));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (clickable) {
            buttons.forEach(button1 -> button1.mouseClicked(mouseX, mouseY, button));
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public String getCurrentTimeStamp(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        return sdfDate.format(date);
    }

}
