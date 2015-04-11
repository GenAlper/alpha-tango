package Bleach;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpriteAnimated extends Sprite {

	private long timeAnim, timeStart;
	private int frameCount;

	public SpriteAnimated(BufferedImage image, Integer frameWidth, Integer frameHeight, Integer originx, Integer originy, long timeAnim) {
		super(image);
		this.width = frameWidth;
		this.height = frameHeight;
		this.originx = originx == null ? width / 2 : originx;
		this.originy = originy == null ? height / 2 : originy;
		this.timeAnim = timeAnim * 1000000; // millis to nanos
		timeStart = System.nanoTime();
		frameCount = Math.max(1, image.getWidth() / frameWidth);
	}

	@Override
	public BufferedImage getFrame() {
		/*
		 * Calculate the index based on the current time and use it for
		 * getFrame(index)
		 */
		return getFrame((int) ((System.nanoTime() - timeStart) / timeAnim) % frameCount);
	}

	public BufferedImage getFrame(int index) {
		BufferedImage bi = new BufferedImage(width, height, image.getType());
		Graphics g = bi.getGraphics();
		int sourceX = (index % frameCount) * width;

		g.drawImage(image, 0, 0, width, height, sourceX, 0, sourceX + width, height, null);
		g.dispose();

		return bi;
	}
}
