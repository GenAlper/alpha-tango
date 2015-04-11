package Bleach;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Sprite {

	protected BufferedImage image;
	protected int width, height;
	protected int originx, originy;

	public Sprite(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		originx = width / 2;
		originy = height / 2;
		this.image = image;
	}

	public Sprite(BufferedImage image, Integer width, Integer height, Integer originx, Integer originy) {
		/*
		 * Make a new image with the specified width and height. width and
		 * height could be null as we can get data from a JSON parser.
		 */
		this.width = width == null ? image.getWidth() : width;
		this.height = height == null ? image.getHeight() : height;
		this.originx = originx == null ? this.width / 2 : originx;
		this.originy = originy == null ? this.height / 2 : originy;

		this.image = new BufferedImage(this.width, this.height, image.getType());
		Graphics g = this.image.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	public BufferedImage getFrame() {
		return image;
	}

	public int getHeight() {
		return height;
	}

	public Point getOrigin() {
		return new Point(originx, originy);
	}

	public int getWidth() {
		return width;
	}
}
