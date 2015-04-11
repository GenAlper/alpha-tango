package Bleach;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public interface LevelInteractable {
	public int getBackgroundParallaxDistance();

	public List<BufferedImage> getBackgrounds();

	public List<EntityTranslatable> getLoots();

	public List<EntityTranslatable> getMobiles();

	public List<EntityTranslatable> getPlayers();

	public List<EntityTranslatable> getProjectiles();

	public List<TerrainBlock> getTerrains();

	public Point2D.Double getViewport();

	public void removeLoot(EntityTranslatable loot);

	public void removeMobile(EntityTranslatable mobile);

	public void removePlayer(EntityTranslatable player);

	public void removeProjectile(EntityTranslatable projectile);

	public void removeTerrain(EntityTranslatable terrain);
}
