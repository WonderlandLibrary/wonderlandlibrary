/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;

public class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(BaseBounds baseBounds) {
        this.setBounds(baseBounds);
    }

    public Rectangle(Rectangle rectangle) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }

    public Rectangle(int n, int n2) {
        this(0, 0, n, n2);
    }

    public void setBounds(Rectangle rectangle) {
        this.setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        this.reshape(n, n2, n3, n4);
    }

    public void setBounds(BaseBounds baseBounds) {
        this.x = (int)Math.floor(baseBounds.getMinX());
        this.y = (int)Math.floor(baseBounds.getMinY());
        int n = (int)Math.ceil(baseBounds.getMaxX());
        int n2 = (int)Math.ceil(baseBounds.getMaxY());
        this.width = n - this.x;
        this.height = n2 - this.y;
    }

    public boolean contains(int n, int n2) {
        int n3 = this.width;
        int n4 = this.height;
        if ((n3 | n4) < 0) {
            return false;
        }
        int n5 = this.x;
        int n6 = this.y;
        if (n < n5 || n2 < n6) {
            return false;
        }
        n4 += n6;
        return !((n3 += n5) >= n5 && n3 <= n || n4 >= n6 && n4 <= n2);
    }

    public boolean contains(Rectangle rectangle) {
        return this.contains(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public boolean contains(int n, int n2, int n3, int n4) {
        int n5 = this.width;
        int n6 = this.height;
        if ((n5 | n6 | n3 | n4) < 0) {
            return false;
        }
        int n7 = this.x;
        int n8 = this.y;
        if (n < n7 || n2 < n8) {
            return false;
        }
        n5 += n7;
        if ((n3 += n) <= n ? n5 >= n7 || n3 > n5 : n5 >= n7 && n3 > n5) {
            return false;
        }
        n6 += n8;
        return !((n4 += n2) <= n2 ? n6 >= n8 || n4 > n6 : n6 >= n8 && n4 > n6);
    }

    public Rectangle intersection(Rectangle rectangle) {
        Rectangle rectangle2 = new Rectangle(this);
        rectangle2.intersectWith(rectangle);
        return rectangle2;
    }

    public void intersectWith(Rectangle rectangle) {
        if (rectangle == null) {
            return;
        }
        int n = this.x;
        int n2 = this.y;
        int n3 = rectangle.x;
        int n4 = rectangle.y;
        long l = n;
        l += (long)this.width;
        long l2 = n2;
        l2 += (long)this.height;
        long l3 = n3;
        l3 += (long)rectangle.width;
        long l4 = n4;
        l4 += (long)rectangle.height;
        if (n < n3) {
            n = n3;
        }
        if (n2 < n4) {
            n2 = n4;
        }
        if (l > l3) {
            l = l3;
        }
        if (l2 > l4) {
            l2 = l4;
        }
        l2 -= (long)n2;
        if ((l -= (long)n) < Integer.MIN_VALUE) {
            l = Integer.MIN_VALUE;
        }
        if (l2 < Integer.MIN_VALUE) {
            l2 = Integer.MIN_VALUE;
        }
        this.setBounds(n, n2, (int)l, (int)l2);
    }

    public void translate(int n, int n2) {
        int n3 = this.x;
        int n4 = n3 + n;
        if (n < 0) {
            if (n4 > n3) {
                if (this.width >= 0) {
                    this.width += n4 - Integer.MIN_VALUE;
                }
                n4 = Integer.MIN_VALUE;
            }
        } else if (n4 < n3) {
            if (this.width >= 0) {
                this.width += n4 - Integer.MAX_VALUE;
                if (this.width < 0) {
                    this.width = Integer.MAX_VALUE;
                }
            }
            n4 = Integer.MAX_VALUE;
        }
        this.x = n4;
        n3 = this.y;
        n4 = n3 + n2;
        if (n2 < 0) {
            if (n4 > n3) {
                if (this.height >= 0) {
                    this.height += n4 - Integer.MIN_VALUE;
                }
                n4 = Integer.MIN_VALUE;
            }
        } else if (n4 < n3) {
            if (this.height >= 0) {
                this.height += n4 - Integer.MAX_VALUE;
                if (this.height < 0) {
                    this.height = Integer.MAX_VALUE;
                }
            }
            n4 = Integer.MAX_VALUE;
        }
        this.y = n4;
    }

    public RectBounds toRectBounds() {
        return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    public void add(int n, int n2) {
        if ((this.width | this.height) < 0) {
            this.x = n;
            this.y = n2;
            this.height = 0;
            this.width = 0;
            return;
        }
        int n3 = this.x;
        int n4 = this.y;
        long l = this.width;
        long l2 = this.height;
        l += (long)n3;
        l2 += (long)n4;
        if (n3 > n) {
            n3 = n;
        }
        if (n4 > n2) {
            n4 = n2;
        }
        if (l < (long)n) {
            l = n;
        }
        if (l2 < (long)n2) {
            l2 = n2;
        }
        l2 -= (long)n4;
        if ((l -= (long)n3) > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        if (l2 > Integer.MAX_VALUE) {
            l2 = Integer.MAX_VALUE;
        }
        this.reshape(n3, n4, (int)l, (int)l2);
    }

    public void add(Rectangle rectangle) {
        long l;
        long l2;
        long l3 = this.width;
        long l4 = this.height;
        if ((l3 | l4) < 0L) {
            this.reshape(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        if (((l2 = (long)rectangle.width) | (l = (long)rectangle.height)) < 0L) {
            return;
        }
        int n = this.x;
        int n2 = this.y;
        l3 += (long)n;
        l4 += (long)n2;
        int n3 = rectangle.x;
        int n4 = rectangle.y;
        l2 += (long)n3;
        l += (long)n4;
        if (n > n3) {
            n = n3;
        }
        if (n2 > n4) {
            n2 = n4;
        }
        if (l3 < l2) {
            l3 = l2;
        }
        if (l4 < l) {
            l4 = l;
        }
        l4 -= (long)n2;
        if ((l3 -= (long)n) > Integer.MAX_VALUE) {
            l3 = Integer.MAX_VALUE;
        }
        if (l4 > Integer.MAX_VALUE) {
            l4 = Integer.MAX_VALUE;
        }
        this.reshape(n, n2, (int)l3, (int)l4);
    }

    public void grow(int n, int n2) {
        long l = this.x;
        long l2 = this.y;
        long l3 = this.width;
        long l4 = this.height;
        l3 += l;
        l4 += l2;
        l2 -= (long)n2;
        l4 += (long)n2;
        if ((l3 += (long)n) < (l -= (long)n)) {
            if ((l3 -= l) < Integer.MIN_VALUE) {
                l3 = Integer.MIN_VALUE;
            }
            if (l < Integer.MIN_VALUE) {
                l = Integer.MIN_VALUE;
            } else if (l > Integer.MAX_VALUE) {
                l = Integer.MAX_VALUE;
            }
        } else {
            if (l < Integer.MIN_VALUE) {
                l = Integer.MIN_VALUE;
            } else if (l > Integer.MAX_VALUE) {
                l = Integer.MAX_VALUE;
            }
            if ((l3 -= l) < Integer.MIN_VALUE) {
                l3 = Integer.MIN_VALUE;
            } else if (l3 > Integer.MAX_VALUE) {
                l3 = Integer.MAX_VALUE;
            }
        }
        if (l4 < l2) {
            if ((l4 -= l2) < Integer.MIN_VALUE) {
                l4 = Integer.MIN_VALUE;
            }
            if (l2 < Integer.MIN_VALUE) {
                l2 = Integer.MIN_VALUE;
            } else if (l2 > Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
        } else {
            if (l2 < Integer.MIN_VALUE) {
                l2 = Integer.MIN_VALUE;
            } else if (l2 > Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
            if ((l4 -= l2) < Integer.MIN_VALUE) {
                l4 = Integer.MIN_VALUE;
            } else if (l4 > Integer.MAX_VALUE) {
                l4 = Integer.MAX_VALUE;
            }
        }
        this.reshape((int)l, (int)l2, (int)l3, (int)l4);
    }

    private void reshape(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public boolean equals(Object object) {
        if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)object;
            return this.x == rectangle.x && this.y == rectangle.y && this.width == rectangle.width && this.height == rectangle.height;
        }
        return super.equals(object);
    }

    public int hashCode() {
        int n = Float.floatToIntBits(this.x);
        n += Float.floatToIntBits(this.y) * 37;
        n += Float.floatToIntBits(this.width) * 43;
        return n += Float.floatToIntBits(this.height) * 47;
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}

