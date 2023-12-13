package de.tired.base.guis.newclickgui.section;

import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SectionRenderer {
    private int posX, posY, mouseX, mouseY;

    private ArrayList<Section> sections = new ArrayList<>();

    public SectionRenderer(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        final AtomicInteger xAdditional = new AtomicInteger(0);

        for (SectionType sectionType : SectionType.values())
            sections.add(new Section(sectionType, posX + xAdditional.addAndGet(20), posY));
    }

    public void drawSections(float posX, float posY, int mouseX, int mouseY) {
        final AtomicInteger xAdditional = new AtomicInteger(0);

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(posX + 6, posY - 10, 100, 20, 3, Color.RED);

        for (Section section : sections) {
            section.render(posX + xAdditional.addAndGet(30), posY, mouseX, mouseY);
        }
    }

}
