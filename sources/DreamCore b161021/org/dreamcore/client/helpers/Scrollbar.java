package org.dreamcore.client.helpers;

public class Scrollbar {

    public float startX, startY, endY, currentScroll, maxScroll, minScroll;

    public void drawScrollBar() {
        maxScroll = maxScroll + currentScroll / 14;
    }

    public void setInformation(float startX, float startY, float endY, float currentScroll, float maxScroll, float minScroll) {
        this.startX = startX;
        this.startY = startY;
        this.endY = endY;
        this.currentScroll = currentScroll;
        this.maxScroll = maxScroll;
        this.minScroll = minScroll;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndY() {
        return endY;
    }

    public float getCurrentScroll() {
        return currentScroll;
    }

    public float getMaxScroll() {
        return maxScroll;
    }

    public float getMinScroll() {
        return minScroll;
    }

}
