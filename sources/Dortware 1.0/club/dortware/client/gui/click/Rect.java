package club.dortware.client.gui.click;

public class Rect {
	public float x;
	public float y;
	public float w;
	public float h;

	public Rect(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
        float var5;

        if (this.x < this.w)
        {
            var5 = this.x;
            this.x = this.w;
            this.w = var5;
        }

        if (this.y < this.h)
        {
            var5 = this.y;
            this.y = this.h;
            this.h = var5;
        }
	}

	public Rect(double d, double e, double f, double g) {
		this((float)d, (float)e, (float)f, (float)g);
	}

	public boolean checkCollision(int pointX, int pointY) {
		return !(pointX < x || pointY < y || pointX > (x + w) || pointY > (y + h));
	}

	public boolean isMouseHovering(int mouseX, int mouseY) {
		return mouseX >= this.w && mouseY >= this.h && mouseX <= this.x && mouseY <= this.y;
	}
}
