package tech.drainwalk.client.theme;

import lombok.Getter;
import tech.drainwalk.utility.color.ColorUtility;

public enum Theme {
    WHITE(
            ColorUtility.rgb(240, 240, 240),
            ColorUtility.rgb(255, 255, 255),
            ColorUtility.rgb(217, 217, 217),

            ColorUtility.rgb(255, 255, 255),

            ColorUtility.rgb(0, 0, 0),
            ColorUtility.rgb(122, 122, 122),

            ColorUtility.rgb(3, 168, 245),
            ColorUtility.rgb(104, 197, 255),
            ColorUtility.rgb(217, 217, 217),

            ColorUtility.rgb(230, 230, 230),
            ColorUtility.rgb(154, 154, 154)
    ),
    BLACK(
            ColorUtility.rgb(8, 8, 8),
            ColorUtility.rgb(12, 12, 12),
            ColorUtility.rgb(25, 26, 26),

            ColorUtility.rgb(12, 12, 12),

            ColorUtility.rgb(226, 226, 226),
            ColorUtility.rgb(153, 176, 189),

            ColorUtility.rgb(3, 168, 245),
            ColorUtility.rgb(0, 88, 129),
            ColorUtility.rgb(44, 44, 44),


            ColorUtility.rgb(24, 25, 25),
            ColorUtility.rgb(75, 88, 98)
    ),
    BLUE(
            ColorUtility.rgb(27, 28, 44),
            ColorUtility.rgb(30, 31, 48),
            ColorUtility.rgb(38, 39, 59),

            ColorUtility.rgb(30, 31, 48),

            ColorUtility.rgb(222, 221, 255),
            ColorUtility.rgb(133, 138, 185),

            ColorUtility.rgb(55, 100, 255),
            ColorUtility.rgb(36, 63, 158),
            ColorUtility.rgb(53, 55, 86),

            ColorUtility.rgb(51, 51, 81),
            ColorUtility.rgb(133, 138, 185)
    ),
    FATALITY(
            ColorUtility.rgb(25, 20, 50),
            ColorUtility.rgb(30, 20, 55),
            ColorUtility.rgb(60, 55, 95),

            ColorUtility.rgb(35, 25, 65),

            ColorUtility.rgb(232, 231, 243),
            ColorUtility.rgb(161, 145, 200),

            ColorUtility.rgb(215, 10, 90),
            ColorUtility.rgb(139, 21, 78),
            ColorUtility.rgb(92, 17, 67),

            ColorUtility.rgb(30 - 2, 25 - 2, 65 - 2),
            ColorUtility.rgb(45, 40, 100)

    );
    @Getter
    private final int panelMain;
    @Getter
    private final int panel;
    @Getter
    private final int panelLines;

    @Getter
    private final int object;

    @Getter
    private final int textMain;
    @Getter
    private final int textStay;

    @Getter
    private final int main;
    @Getter
    private final int mainStay;

    @Getter
    private final int category;

    @Getter
    private final int checkBoxStayBG;
    @Getter
    private final int checkBoxStay;
    Theme(
            int panelMain,
            int panel,
            int panelLines,

            int object,

            int textMain,
            int textStay,

            int main,
            int mainStay,
            int category,
            int checkBoxStayBG,
            int checkBoxStay
    ) {
        this.panelMain = panelMain;
        this.panel = panel;
        this.panelLines = panelLines;

        this.object = object;

        this.textMain = textMain;
        this.textStay = textStay;

        this.main = main;
        this.mainStay = mainStay;

        this.category = category;
        this.checkBoxStayBG = checkBoxStayBG;
        this.checkBoxStay = checkBoxStay;

    }
}
