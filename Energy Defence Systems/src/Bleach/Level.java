package Bleach;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Bleach.Loader.Discette;

public class Level implements LevelInteractable {

	private List<EntityTranslatable> mobiles;
	private List<EntityTranslatable> loots;
	private List<EntityTranslatable> players;
	private List<EntityTranslatable> projectiles;
	private List<TerrainBlock> terrains;
	private List<BufferedImage> backgrounds; // A list of textures that are to
												// be parallaxed in the
												// background.
	private Point2D.Double viewport; // Offset for scrolling. This points at the
										// middle of the viewport.
	private int width, height;
	private int screenWidth, screenHeight;
	private int parallaxDistance; // How far away the background layers are.
									// Used for the parallaxing of backgrounds.
	private String key; // Identifier for this level.

	private boolean isScrolling; // Does the level auto-scroll.
	private double scrollVelocity; // Auto-scroll speed, pixels per second.
	private double scrollAngle; // Auto-scroll: scroll towards this angle.
	private long timePreviousScroll; // Time since last scroll happened. Used to
										// calculate delta-time.

	public Level() {
		this(800, 600, "Level" + System.currentTimeMillis());
	}

	public Level(Discette.JsonObjectLevel levelData) {
		this();
		levelBuilder(levelData);
	}

	public Level(int width, int height, String key) {
		this.width = screenWidth = width;
		this.height = screenHeight = height;
		this.key = key;
		parallaxDistance = 10;
		isScrolling = false;
		scrollVelocity = 0;
		scrollAngle = 0;
		timePreviousScroll = System.currentTimeMillis();

		mobiles = new ArrayList<>();
		loots = new ArrayList<>();
		players = new ArrayList<>();
		projectiles = new ArrayList<>();
		backgrounds = new ArrayList<>();
		terrains = new ArrayList<TerrainBlock>();

		viewport = new Point2D.Double(0, 0);
		screenWidth = screenHeight = 1000;
	}

	public Level(String key) {
		this(800, 600, key);
	}

	public void addBackground(BufferedImage img) {
		/*
		 * Add a background image to scroll (parallax), add it behind others if
		 * some exists already.
		 */
		if (img != null)
			backgrounds.add(img);
	}

	public void addLoot(EntityTranslatable loot) {
		if (loot != null)
			loots.add(loot);
	}

	public void addMobile(EntityTranslatable mob) {
		if (mob != null)
			mobiles.add(mob);
	}

	public void addPlayer(EntityTranslatable player) {
		if (player != null)
			players.add(player);
	}

	public void addProjectile(EntityTranslatable proj) {
		if (proj != null)
			projectiles.add(proj);
	}

	public void addTerrainBlock(TerrainBlock terrain) {
		terrains.add(terrain);
	}

	public void clearBackgrounds() {
		/* Removes all backgrounds */
		backgrounds.clear();
	}

	public void doAutoScroll(boolean doScroll) {
		isScrolling = doScroll;
		timePreviousScroll = System.nanoTime();
	}

	public void focusEntity(Entity entity, boolean center) {
		int padding = 150;

		if (center) {
			viewport.x = entity.x;
			viewport.y = entity.y;
		} else {
			if (entity.x > viewport.x - screenWidth / 2.0 + screenWidth - padding) {
				viewport.x = (int) entity.x - screenWidth / 2.0 + padding;
			}
			if (entity.x < viewport.x - screenWidth / 2.0 + padding) {
				viewport.x = (int) entity.x + screenWidth / 2.0 - padding;
			}
			if (entity.y > viewport.y - screenHeight / 2.0 + screenHeight - padding) {
				viewport.y = (int) entity.y - screenHeight / 2.0 + padding;
			}
			if (entity.y < viewport.y - screenHeight / 2.0 + padding) {
				viewport.y = (int) entity.y + screenHeight / 2.0 - padding;
			}
		}

		// Limit viewport to screen
		viewport.x = viewport.x - screenWidth / 2.0 < 0 ? 0 + screenWidth / 2.0 : viewport.x;
		viewport.y = viewport.y - screenHeight / 2.0 < 0 ? 0 + screenHeight / 2.0 : viewport.y;
		viewport.x = viewport.x + screenWidth / 2.0 > width ? width - screenWidth / 2.0 : viewport.x;
		viewport.y = viewport.y + screenHeight / 2.0 > height ? height - screenHeight / 2.0 : viewport.y;
	}

	@Override
	public int getBackgroundParallaxDistance() {
		return parallaxDistance;
	}

	@Override
	public List<BufferedImage> getBackgrounds() {
		return backgrounds;
	}

	public int getHeight() {
		return height;
	}

	public String getKey() {
		/* Returns the identifier of this level. */
		return key;
	}

	@Override
	public List<EntityTranslatable> getLoots() {
		return loots;
	}

	@Override
	public List<EntityTranslatable> getMobiles() {
		return mobiles;
	}

	@Override
	public List<EntityTranslatable> getPlayers() {
		return players;
	}

	@Override
	public List<EntityTranslatable> getProjectiles() {
		return projectiles;
	}

	@Override
	public List<TerrainBlock> getTerrains() {
		return terrains;
	}

	@Override
	public Point2D.Double getViewport() {
		if (isScrolling) {
			/*
			 * Viewport is set to auto-scroll. Let's calculate the new position
			 * based on the delta-time.
			 */
			viewport.x += Math.cos(scrollAngle) * (scrollVelocity * (System.currentTimeMillis() - timePreviousScroll));
			viewport.y += Math.sin(scrollAngle) * (scrollVelocity * (System.currentTimeMillis() - timePreviousScroll));
			timePreviousScroll = System.currentTimeMillis();
		}

		return viewport;
	}

	public int getWidth() {
		return width;
	}

	public void levelBuilder(Discette.JsonObjectLevel levelObject) {
		width = levelObject.width == null ? width : levelObject.width;
		height = levelObject.height == null ? height : levelObject.height;
		key = levelObject.key == null ? key : levelObject.key;

		for (Discette.JsonObjectLevel.JsonObjectBacks background : levelObject.backgrounds) {
			Sprite sprite = null;
			sprite = Discette.getImage(background.texturekey);
			if (sprite != null)
				addBackground(sprite.getFrame());
		}

		for (Discette.JsonObjectLevel.JsonObjectTiles tile : levelObject.tiles) {
			if (tile == null)
				break;

			Sprite sprite = null;
			sprite = Discette.getImage(tile.texturekey);

			if (sprite != null) {
				addTerrainBlock(new TerrainBlock(sprite, tile.gridx, tile.gridy, tile.gridwidth, tile.gridheight, tile.absolutex, tile.absolutey));
			}
		}
	}

	@Override
	public void removeLoot(EntityTranslatable loot) {
		loots.remove(loot);
	}

	@Override
	public void removeMobile(EntityTranslatable mobile) {
		mobiles.remove(mobile);
	}

	@Override
	public void removePlayer(EntityTranslatable player) {
		players.remove(player);
	}

	@Override
	public void removeProjectile(EntityTranslatable projectile) {
		projectiles.remove(projectile);
	}

	@Override
	public void removeTerrain(EntityTranslatable terrain) {
		terrains.remove(terrain);
	}

	public int setBackgroundParallaxDistance(int dist) {
		/* Sets the parallax distance for backgrounds. Returns the old distance. */
		int retval = parallaxDistance;
		parallaxDistance = dist;

		return retval;
	}

	public void setScreenSize(int width, int height) {
		/*
		 * Tells the level the dimensions of the screen.
		 */
		screenWidth = width;
		screenHeight = height;
	}

	public void setScrollAngle(double angleRad) {
		scrollAngle = angleRad;
	}

	public void setScrollSpeed(double speedPPS) {
		scrollVelocity = speedPPS;
	}

	public void setViewport(Point2D.Double offset) {
		viewport = offset;
	}
}
