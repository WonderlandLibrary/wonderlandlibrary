package io.github.nevalackin.client.ui.nl.components;

public enum Theme {
    BLUE(0xFF00A7F2,
         0xAA030D1A, 0xFF04334D, 0xFF08080D, 0xFF061925, 0xFF484848,
         0xFF08080D, 0xFF061925,
         0xFFFFFFFF,
         0xFF000B16, 0xFF001A2C,
         0xFF9AACB2, 0xFFFFFFFF,
         0xFF08080D, 0xFF222222,
         0xFF00A7F2, 0xFF7B8B9C, 0xFF00030B, 0xFF09172D,
         0xFF011B2B
    );
//    LIGHT(),
//    DARK();

    private final int mainColour,
        pageSelectorBackgroundColour, pageSelectorSelectedPageColour, pageBackgroundColour, pageSelectorPageSeparatorColour, pageSelectorLabelColour,
        headerBackgroundColour, headerPageSeparatorColour,
        watermarkTextColour,
        groupBoxBackgroundColour, groupBoxHeaderSeparatorColour,
        textColour, highlightedTextColour,
        componentBackgroundColour, componentOutlineColour,
        checkBoxEnabledColour, checkBoxDisabledColour, checkBoxBackgroundDisabledColour, checkBoxBackgroundEnabledColour,
        sliderBackgroundColour;

    Theme(int mainColour, int pageSelectorBackgroundColour, int pageSelectorSelectedPageColour, int pageBackgroundColour, int pageSelectorPageSeparatorColour, int pageSelectorLabelColour, int headerBackgroundColour, int headerPageSeparatorColour, int watermarkTextColour, int groupBoxBackgroundColour, int groupBoxHeaderSeparatorColour, int textColour, int highlightedTextColour, int componentBackgroundColour, int componentOutlineColour, int checkBoxEnabledColour, int checkBoxDisabledColour, int checkBoxBackgroundDisabledColour, int checkBoxBackgroundEnabledColour, int sliderBackgroundColour) {
        this.mainColour = mainColour;
        this.pageSelectorBackgroundColour = pageSelectorBackgroundColour;
        this.pageSelectorSelectedPageColour = pageSelectorSelectedPageColour;
        this.pageBackgroundColour = pageBackgroundColour;
        this.pageSelectorPageSeparatorColour = pageSelectorPageSeparatorColour;
        this.pageSelectorLabelColour = pageSelectorLabelColour;
        this.headerBackgroundColour = headerBackgroundColour;
        this.headerPageSeparatorColour = headerPageSeparatorColour;
        this.watermarkTextColour = watermarkTextColour;
        this.groupBoxBackgroundColour = groupBoxBackgroundColour;
        this.groupBoxHeaderSeparatorColour = groupBoxHeaderSeparatorColour;
        this.textColour = textColour;
        this.highlightedTextColour = highlightedTextColour;
        this.componentBackgroundColour = componentBackgroundColour;
        this.componentOutlineColour = componentOutlineColour;
        this.checkBoxEnabledColour = checkBoxEnabledColour;
        this.checkBoxDisabledColour = checkBoxDisabledColour;
        this.checkBoxBackgroundDisabledColour = checkBoxBackgroundDisabledColour;
        this.checkBoxBackgroundEnabledColour = checkBoxBackgroundEnabledColour;
        this.sliderBackgroundColour = sliderBackgroundColour;
    }

    public int getSliderBackgroundColour() {
        return sliderBackgroundColour;
    }

    public int getMainColour() {
        return mainColour;
    }

    public int getPageSelectorBackgroundColour() {
        return pageSelectorBackgroundColour;
    }

    public int getPageSelectorSelectedPageColour() {
        return pageSelectorSelectedPageColour;
    }

    public int getPageBackgroundColour() {
        return pageBackgroundColour;
    }

    public int getPageSelectorLabelColour() {
        return pageSelectorLabelColour;
    }

    public int getPageSelectorPageSeparatorColour() {
        return pageSelectorPageSeparatorColour;
    }

    public int getHeaderBackgroundColour() {
        return headerBackgroundColour;
    }

    public int getHeaderPageSeparatorColour() {
        return headerPageSeparatorColour;
    }

    public int getWatermarkTextColour() {
        return watermarkTextColour;
    }

    public int getGroupBoxBackgroundColour() {
        return groupBoxBackgroundColour;
    }

    public int getGroupBoxHeaderSeparatorColour() {
        return groupBoxHeaderSeparatorColour;
    }

    public int getTextColour() {
        return textColour;
    }

    public int getHighlightedTextColour() {
        return highlightedTextColour;
    }

    public int getComponentBackgroundColour() {
        return componentBackgroundColour;
    }

    public int getComponentOutlineColour() {
        return componentOutlineColour;
    }

    public int getCheckBoxEnabledColour() {
        return checkBoxEnabledColour;
    }

    public int getCheckBoxDisabledColour() {
        return checkBoxDisabledColour;
    }

    public int getCheckBoxBackgroundDisabledColour() {
        return checkBoxBackgroundDisabledColour;
    }

    public int getCheckBoxBackgroundEnabledColour() {
        return checkBoxBackgroundEnabledColour;
    }
}
