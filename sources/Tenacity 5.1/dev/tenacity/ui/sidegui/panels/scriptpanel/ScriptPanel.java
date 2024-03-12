package dev.tenacity.ui.sidegui.panels.scriptpanel;

import dev.tenacity.Tenacity;
import dev.tenacity.intent.cloud.data.CloudScript;
import dev.tenacity.intent.cloud.data.Votes;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.scripting.api.Script;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.ui.sidegui.panels.Panel;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.ui.sidegui.utils.CarouselButtons;
import dev.tenacity.ui.sidegui.utils.DropdownObject;
import dev.tenacity.ui.sidegui.utils.ToggleButton;
import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.utils.objects.Scroll;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.time.TimerUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScriptPanel extends Panel {

    private final DropdownObject sorting = new DropdownObject("Sort by", "Relevance", "Alphabetical", "Top all time", "Recently updated");

    private final CarouselButtons carouselButtons = new CarouselButtons("Cloud", "Local");

    private final ToggleButton scriptFilter = new ToggleButton("Only show scripts made by you");
    private final ToggleButton compactMode = new ToggleButton("Compact Mode");

    @Getter
    private final List<CloudScriptRect> cloudScriptRects = new ArrayList<>();
    private final List<LocalScriptRect> localScriptRects = new ArrayList<>();
    private final Scroll cloudScriptScroll = new Scroll();
    private final Scroll localScriptScroll = new Scroll();

    private final List<ActionButton> actionButtons;

    @Setter
    private boolean refresh;
    private boolean firstRefresh = true;

    public ScriptPanel() {
        actionButtons = new ArrayList<>();
        actionButtons.add(new ActionButton("Open documentation"));
        actionButtons.add(new ActionButton("Open folder"));
        Multithreading.runAsync(() -> {
            refresh();
            firstRefresh = false;
        });
    }

    private String sortingSelection = "Relevance";

    @Override
    public void initGui() {
        sortingSelection = sorting.getSelection();
        sortScripts();
        cloudScriptScroll.setRawScroll(0);
        localScriptScroll.setRawScroll(0);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    private final TimerUtil voteRefreshTimer = new TimerUtil();

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        boolean isCloud = carouselButtons.getCurrentButton().equals("Cloud");
        //Every 30 seconds we update the votes count
        if (isCloud && voteRefreshTimer.hasTimeElapsed(30000)) {
            Multithreading.runAsync(() -> Tenacity.INSTANCE.getCloudDataManager().refreshVotes());
            voteRefreshTimer.reset();
        }

        Tenacity.INSTANCE.getCloudDataManager().applyVotes();

        tenacityBoldFont40.drawString("Scripts", getX() + 8, getY() + 8, getTextColor());


        carouselButtons.setAlpha(getAlpha());
        carouselButtons.setRectWidth(50);
        carouselButtons.setRectHeight(18);
        carouselButtons.setBackgroundColor(ColorUtil.tripleColor(55));
        carouselButtons.setY(getY() + 72 - carouselButtons.getRectHeight());
        carouselButtons.setX(getX() + getWidth() / 2f - carouselButtons.getTotalWidth() / 2f);
        carouselButtons.drawScreen(mouseX, mouseY);

        float spacing = 8;

        float backgroundY = carouselButtons.getY() + carouselButtons.getRectHeight() + spacing;
        float backgroundX = getX() + spacing;
        float backgroundWidth = getWidth() - (spacing * 2);
        float backgroundHeight = getHeight() - ((backgroundY - getY()) + 1 + spacing);

        int additionalSeparation = 0;
        for (ActionButton button : actionButtons) {
            button.setX(getX() + 10 + additionalSeparation);
            button.setWidth(additionalSeparation == 0 ? 90 : 70);
            button.setHeight(15);
            button.setY(carouselButtons.getY() + carouselButtons.getRectHeight() / 2f - button.getHeight() / 2f);
            button.setAlpha(getAlpha());

            button.setClickAction(() -> {
                //TODO: Add clickaction for the forms
                switch (button.getName()) {
                    case "Open documentation":
                        IOUtils.openLink("https://scripting.tenacity.dev");
                        break;
                    case "Open folder":
                        IOUtils.openFolder(Tenacity.INSTANCE.getScriptManager().getScriptDirectory());
                        break;
                }
            });

            button.drawScreen(mouseX, mouseY);

            additionalSeparation += button.getWidth() + spacing;
        }

        boolean hovering = SideGUI.isHovering(getX() + spacing, backgroundY, backgroundWidth, backgroundHeight, mouseX, mouseY);
        if (hovering) {
            if (carouselButtons.getCurrentButton().equals("Cloud")) {
                cloudScriptScroll.onScroll(35);
            } else {
                localScriptScroll.onScroll(35);
            }
        }


        if (isCloud) {
            scriptFilter.setX(getX() + getWidth() - (scriptFilter.getWH() + 12));
            scriptFilter.setY(backgroundY - (scriptFilter.getWH() + spacing));
            scriptFilter.setAlpha(getAlpha());
            scriptFilter.drawScreen(mouseX, mouseY);
        }

        compactMode.setX(getX() + 69);
        compactMode.setY(getY() + 33);
        compactMode.setAlpha(getAlpha());
        compactMode.drawScreen(mouseX, mouseY);


        RoundedUtil.drawRound(backgroundX, backgroundY, backgroundWidth, backgroundHeight, 5, ColorUtil.tripleColor(27, getAlpha()));

        if (firstRefresh) return;

        if (refresh) {
            refresh();
            return;
        }

        if (Tenacity.INSTANCE.getCloudDataManager().isRefreshing())
            return;

        if (isCloud) {


            if (!sortingSelection.equals(sorting.getSelection())) {
                Multithreading.runAsync(() -> Tenacity.INSTANCE.getCloudDataManager().refreshData());
                sortingSelection = sorting.getSelection();
            }

            boolean filter = scriptFilter.isEnabled();
            //6 spacing on left and right = 12
            //12 + ((12 spacing between configs) * 2 large spaces because we want 3 configs on the top)
            // This equals 36, so we deduct that from the background width and then divide by 3 to get the width of each config
            float scriptWidth = (backgroundWidth - 36) / 3f;

            float scriptHeight = compactMode.isEnabled() ? 38 : 90;
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(getX() + spacing, backgroundY, backgroundWidth, backgroundHeight, 5, ColorUtil.tripleColor(27, getAlpha()));
            StencilUtil.readStencilBuffer(1);


            int count = 0;
            int rectXSeparation = 0;
            int rectYSeparation = 0;
            for (CloudScriptRect cloudScriptRect : cloudScriptRects) {

                if (filter) {
                    String configAuthor = cloudScriptRect.getScript().getAuthor();
                    if (!Tenacity.INSTANCE.getIntentAccount().username.equals(configAuthor)) continue;
                }


                cloudScriptRect.setAlpha(getAlpha());
                cloudScriptRect.setAccentColor(getAccentColor());
                //This changes the x and y position for showing 3 configs per line
                if (count > 2) {
                    rectXSeparation = 0;
                    rectYSeparation += scriptHeight + 12;
                    count = 0;
                }

                cloudScriptRect.setX(backgroundX + 6 + rectXSeparation);
                cloudScriptRect.setY(backgroundY + 6 + rectYSeparation + cloudScriptScroll.getScroll());
                cloudScriptRect.setWidth(scriptWidth);
                cloudScriptRect.setHeight(scriptHeight);
                cloudScriptRect.setCompact(compactMode.isEnabled());

                if (cloudScriptRect.getY() + cloudScriptRect.getHeight() > backgroundY && cloudScriptRect.getY() < backgroundY + backgroundHeight) {
                    cloudScriptRect.setClickable(true);
                    cloudScriptRect.drawScreen(mouseX, mouseY);
                } else {
                    cloudScriptRect.setClickable(false);
                }


                rectXSeparation += cloudScriptRect.getWidth() + 12;
                count++;
            }

            cloudScriptScroll.setMaxScroll(rectYSeparation);

            StencilUtil.uninitStencilBuffer();

            sorting.setWidth(120);
            sorting.setHeight(15);
            sorting.setY(getY() + 5);
            sorting.setX(getX() + getWidth() - (sorting.getWidth() + 8));
            sorting.setAccentColor(HUDMod.getClientColors().getFirst());
            sorting.setAlpha(getAlpha());
            sorting.drawScreen(mouseX, mouseY);

        } else {

            //6 spacing on left and right = 12
            //12 + ((12 spacing between configs) * 2 large spaces because we want 3 configs on the top)
            // This equals 36, so we deduct that from the background width and then divide by 3 to get the width of each config
            float localScriptWidth = (backgroundWidth - 36) / 3f;

            float localScriptHeight = compactMode.isEnabled() ? 38 : 90;
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(getX() + spacing, backgroundY, backgroundWidth, backgroundHeight, 5, ColorUtil.tripleColor(27, getAlpha()));
            StencilUtil.readStencilBuffer(1);


            int count2 = 0;
            int rectXSeparation2 = 0;
            int rectYSeparation2 = 0;
            for (LocalScriptRect localScriptRect : localScriptRects) {


                localScriptRect.setAlpha(getAlpha());
                localScriptRect.setAccentColor(getAccentColor());
                //This changes the x and y position for showing 3 configs per line
                if (count2 > 2) {
                    rectXSeparation2 = 0;
                    rectYSeparation2 += localScriptHeight + 12;
                    count2 = 0;
                }

                localScriptRect.setX(backgroundX + 6 + rectXSeparation2);
                localScriptRect.setY(backgroundY + 6 + rectYSeparation2 + localScriptScroll.getScroll());
                localScriptRect.setWidth(localScriptWidth);
                localScriptRect.setHeight(localScriptHeight);
                localScriptRect.setCompact(compactMode.isEnabled());

                if (localScriptRect.getY() + localScriptRect.getHeight() > backgroundY && localScriptRect.getY() < backgroundY + backgroundHeight) {
                    localScriptRect.setClickable(true);
                    localScriptRect.drawScreen(mouseX, mouseY);
                } else {
                    localScriptRect.setClickable(false);
                }


                rectXSeparation2 += localScriptRect.getWidth() + 12;
                count2++;
            }

            localScriptScroll.setMaxScroll(rectYSeparation2);

            StencilUtil.uninitStencilBuffer();
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        carouselButtons.mouseClicked(mouseX, mouseY, button);

        boolean isCloud = carouselButtons.getCurrentButton().equals("Cloud");
        if (isCloud) {
            sorting.mouseClicked(mouseX, mouseY, button);
            if (sorting.isClosed()) {
                scriptFilter.mouseClicked(mouseX, mouseY, button);
            }

            cloudScriptRects.forEach(cloudScriptRect -> cloudScriptRect.mouseClicked(mouseX, mouseY, button));
        } else {
            localScriptRects.forEach(localScriptRect -> localScriptRect.mouseClicked(mouseX, mouseY, button));
        }

        compactMode.mouseClicked(mouseX, mouseY, button);
        actionButtons.forEach(actionButton -> actionButton.mouseClicked(mouseX, mouseY, button));

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void refresh() {
        cloudScriptRects.clear();
        for (CloudScript cloudScript : Tenacity.INSTANCE.getCloudDataManager().getCloudScripts()) {
            cloudScriptRects.add(new CloudScriptRect(cloudScript));
        }

        Tenacity.INSTANCE.getScriptManager().reloadScripts();


        localScriptRects.clear();
        for (Script script : Tenacity.INSTANCE.getScriptManager().getScripts()) {
            localScriptRects.add(new LocalScriptRect(script));
        }


        initGui();
        refresh = false;
    }

    public void sortScripts() {
        switch (sorting.getSelection()) {
            case "Relevance":
                cloudScriptRects.sort(relevanceSorting());
                break;
            case "Alphabetical":
                cloudScriptRects.sort(alphabeticalSorting());
                break;
            case "Top all time":
                cloudScriptRects.sort(allTimeSorting().reversed());
                break;
            case "Recently updated":
                cloudScriptRects.sort(recentlyUpdatedSorting());
                break;
        }
    }


    public Comparator<CloudScriptRect> recentlyUpdatedSorting() {
        return Comparator.comparingInt(configRect -> configRect.getScript().minutesSinceLastUpdate());
    }

    public Comparator<CloudScriptRect> allTimeSorting() {
        return Comparator.comparingInt(configRect -> configRect.getScript().getVotes().getTotalVotes());
    }

    public Comparator<CloudScriptRect> alphabeticalSorting() {
        return Comparator.comparing(cloudConfigRect -> cloudConfigRect.getScript().getName().toLowerCase());
    }


    public Comparator<CloudScriptRect> relevanceSorting() {
        return Comparator.<CloudScriptRect>comparingInt(configRect -> {
            CloudScript cloudConfig = configRect.getScript();
            //The algorithm scores configs based on the scores of these following weights
            int recentlyUploadedWeight = 20, voteRatioWeight = 80;
            int recentlyUploadedScore, voteRatioScore;

            // We then have an exponential decay for the score of the config based on the time since it was uploaded
            // Decay ends at 15 days since upload at which the score will be 0
            int daysSinceLastUpload = cloudConfig.daysSinceLastUpdate();
            recentlyUploadedScore = Math.max(0, 225 - (daysSinceLastUpload * daysSinceLastUpload));

            //Then we get the ratio of upvotes to total votes
            Votes votes = cloudConfig.getVotes();
            if(votes != null) {
                int totalVotes = votes.getUpvotes() + votes.getDownvotes();
                voteRatioScore = (votes.getUpvotes() / Math.max(1, totalVotes)) * 100;

                if (totalVotes < 15) {
                    voteRatioScore *= .75;
                }
            }else {
                voteRatioScore = 0;
            }


            //Weighted Grade Formula = (w1 x g1 + w2 x g2 + w3 x g3 + …) / (w1 + w2 + w3 + …)

            int totalWeight = recentlyUploadedWeight + voteRatioWeight;

            int score = (recentlyUploadedScore * recentlyUploadedWeight) + (voteRatioScore * voteRatioWeight);

            return score / totalWeight;
        }).reversed();
    }


}
