package wtf.monsoon.api.module;

public enum Category {
    COMBAT("Combat", 10),
    MOVEMENT("Movement", 120),
    PLAYER("Player", 230),
    RENDER("Visual", 340),
    EXPLOIT("Exploit", 450),
    MISC("Miscellaneous", 560),
    HIDDEN("", 0);

    public String name;
    public int moduleIndex;
    public int pos;
    private int x;
    private int y;
    private boolean open;
    Category(String name, int pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName()
    {
        return name;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }
}