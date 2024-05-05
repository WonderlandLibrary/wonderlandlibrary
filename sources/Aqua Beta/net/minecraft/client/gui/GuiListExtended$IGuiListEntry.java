package net.minecraft.client.gui;

public static interface GuiListExtended.IGuiListEntry {
    public void setSelected(int var1, int var2, int var3);

    public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8);

    public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

    public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
}
