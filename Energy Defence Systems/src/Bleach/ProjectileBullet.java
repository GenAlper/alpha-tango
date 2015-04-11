package Bleach;

import java.util.List;
import java.util.ArrayList;

import Bleach.Loader.Discette;
import Bleach.PhysicsEngine.CollisionEngine.Impact;

public class ProjectileBullet extends Projectile {

	public ProjectileBullet(double x, double y, double angle, EntityLiving owner) {
		super(Discette.getImage("heart"), x, y, 4, angle, owner);
	}

	@Override
	public void tick(LevelInteractable activeLevel) {
		// Assemble a list of all Entities that we want this projectile to
		// interact with.
		List<EntityLiving> interactors = new ArrayList<EntityLiving>();
		for (EntityTranslatable entity : activeLevel.getMobiles()) {
			interactors.add((EntityLiving) entity);
		}

		/*
		 * // Should we include playes here? I'm not sure atm. for
		 * (EntityTranslatable entity : activeLevel.getPlayers()) {
		 * interactors.add((EntityLiving)entity); }
		 */

		for (EntityLiving entity : interactors) {
			if (Impact.collides(this, entity)) {
				entity.takeDamage(dealDamage());
				// sound engine play sound!
				activeLevel.removeProjectile(this); // This projectile should
													// die now.
				System.out.println("proj death entity");
				break;
			}
		}

		// Check if hits the terrain
		List<TerrainBlock> terrains = new ArrayList<TerrainBlock>();
		for (EntityTranslatable terrain : activeLevel.getTerrains()) {
			terrains.add((TerrainBlock) terrain);
		}
		for (TerrainBlock terrain : terrains) {
			if (Impact.collides(this, terrain)) {
				activeLevel.removeTerrain(this);
				System.out.println("proj death terrain");
				break;
			}
		}

		// Check if outside of map
		if (isOutsideoflevel(activeLevel)) {
			activeLevel.removeTerrain(this);
		}
	}

	@Override
	double dealDamage() {
		/*
		 * Calculate the amount of damage this projectile does. owner could be
		 * used to modify the damage (buffs etc)
		 */

		return 5;
	}
}
