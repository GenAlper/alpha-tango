package Bleach.Renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;
import Bleach.Sprite;
import Bleach.TerrainBlock;
import Bleach.Loader.Discette;

public class Picasso {
	private int width, height; // Screen width and height.
	private List<String> debug; // Debug data to be printed on the screen.
	private boolean doDebug; // Whether to display the debug data or not.
	BufferedImage canvas;

	public Picasso(int width, int height) {
		this.width = width;
		this.height = height;
		debug = new ArrayList<String>();
		doDebug = true;
		canvas = Discette.toCompatibleImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
	}

	public void addDebugLine(String line) {
		debug.add(line);
	}

	public void clearDebugLines() {
		debug.clear();
	}

	public void render(Graphics panelGraphics, LevelInteractable currentLevelSetting) {

		if (currentLevelSetting == null)
			return;

		double offsetX = currentLevelSetting.getViewport().x - width / 2.0;
		double offsetY = currentLevelSetting.getViewport().y - height / 2.0;

		Graphics graphics = canvas.getGraphics();

		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, width, height);

		// Render level backgrounds using the parallax effect.
		int currentBackgroundNumber = 1;
		List<BufferedImage> backgrounds = currentLevelSetting.getBackgrounds();

		for (BufferedImage background : backgrounds) {
			// Calculate the position of this background and tile it if needed.
			int parallaxDistance = currentLevelSetting.getBackgroundParallaxDistance();
			double parallaxModifier = currentBackgroundNumber / (parallaxDistance / 1.5); // Do
																							// some
																							// math
																							// to
																							// get
																							// a
																							// modifier
																							// that
																							// seems
																							// to
																							// be
																							// Ok.
																							// This
																							// modifier
																							// alters
																							// the
																							// position
																							// of
																							// the
																							// current
																							// background
																							// in
																							// order
																							// to
																							// create
																							// the
																							// parallax
																							// effect.
			double scrollX = (currentLevelSetting.getViewport().getX() - width / 2.0) / width * -1;
			double scrollY = (currentLevelSetting.getViewport().getY() - height / 2.0) / height * -1;
			int tileCountX = (int) Math.ceil((double) width / background.getWidth() + 1);
			int tileCountY = (int) Math.ceil((double) height / background.getHeight() + 1);

			int startX = (int) ((width * scrollX * parallaxModifier) % background.getWidth());
			int startY = (int) ((height * scrollY * parallaxModifier) % background.getHeight());

			for (int i = 0; i < tileCountX; i++) {
				for (int j = 0; j < tileCountY; j++) {
					int x = startX + i * background.getWidth();
					int y = startY + j * background.getHeight();

					graphics.drawImage(background, x, y, background.getWidth(), background.getHeight(), null);
				}
			}
			currentBackgroundNumber++;
		}

		// Render TerrainBlocks
		for (EntityTranslatable block : currentLevelSetting.getTerrains()) {
			TerrainBlock tb = ((TerrainBlock) block);
			Sprite sprite = tb.getSprite();

			double x = tb.getPosition().x - offsetX;
			double y = tb.getPosition().y - offsetY;

			graphics.drawImage(sprite.getFrame(), (int) x, (int) y, sprite.getWidth(), sprite.getHeight(), null);
			if (doDebug) {
				graphics.setColor(Color.red);
				graphics.drawRect((int) (tb.getPosition().x - offsetX), (int) (tb.getPosition().y - offsetY), 1, 1);
				graphics.drawRect((int) (tb.getBoundary().x - offsetX), (int) (tb.getBoundary().y - offsetY), (int) tb.getBoundary().width, (int) tb.getBoundary().height);
			}
		}

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());
		entities.addAll(currentLevelSetting.getProjectiles());

		// Iterate over objects and render them
		for (EntityTranslatable entityTranslatable : entities) {
			Entity entity = (Entity) entityTranslatable;
			if (entity == null || entity.getSprite() == null)
				break; // If the entity or sprite is null then it's pointless to
						// try to draw anything.
			Point spriteOrigin = entity.getSprite().getOrigin();

			graphics.drawImage(entity.getSprite().getFrame(), (int) (entity.getPosition().x - spriteOrigin.x - offsetX), (int) (entity.getPosition().y - spriteOrigin.y - offsetY), entity.getSprite().getWidth(), entity.getSprite().getHeight(), null);
			if (doDebug) {
				graphics.setColor(Color.red);
				graphics.drawRect((int) (entity.getPosition().x - offsetX), (int) (entity.getPosition().y - offsetY), 1, 1);
				graphics.drawRect((int) (entity.getBoundary().x - offsetX), (int) (entity.getBoundary().y - offsetY), (int) entity.getBoundary().width, (int) entity.getBoundary().height);
			}
		}

		// Handle debug data
		if (doDebug) {

			int lineNumber = 0;
			for (String line : debug) {
				graphics.setColor(Color.black);
				graphics.drawString(line, 6, 16 + 10 * lineNumber);
				graphics.setColor(Color.white);
				graphics.drawString(line, 5, 15 + 10 * lineNumber);
				lineNumber++;
			}
		}

		graphics.dispose();
		panelGraphics.drawImage(canvas, 0, 0, null);
	}

	public void setDebug(boolean doDebug) {
		this.doDebug = doDebug;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
