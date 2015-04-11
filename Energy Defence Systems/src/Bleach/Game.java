package Bleach;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import Bleach.InputManager.Receptionist;
import Bleach.InputManager.Receptionist.KeyBinding;
import Bleach.Loader.Discette;
import Bleach.PhysicsEngine.CollisionEngine.CollisionListener;
import Bleach.PhysicsEngine.Force.ExternalForce;

/*
 * This is for testing the game engine.
 * This is where the game developer resides.
 * 
 * */

public class Game {

	public static void main(String[] args) {

		Bleach myGame = new Bleach();

		myGame.loadImages("assets/images/assets.json");

		try {
			myGame.loadSounds("assets/sounds/assets.json");
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (UnsupportedAudioFileException e2) {
			e2.printStackTrace();
		}

		myGame.setFPS(60);

		myGame.setSize(800, 600);
		myGame.setTitle("Energy Defence Systems");

		Level firstLevel = new Level(2800, 1200, "Space");

		EntityBlob blobby = new EntityBlob(myGame.getSprite("blob"), 200, 264);
		Player player = new Player(myGame.getSprite("mushi"), 100, 100);
		firstLevel.addMobile(blobby);
		firstLevel.addPlayer(player);

		firstLevel.levelBuilder(myGame.loadLevel("assets/levels/level1.json"));

		myGame.addLevel(firstLevel);

		myGame.init();

		// Adding a hot receptionist
		Receptionist receptionist = new Receptionist() {

			@Override
			public void handleEvent(ActionEvent event) {
			}

			@Override
			public void handleEvent(MouseEvent event) {
			}
		};

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed A"), "pressed A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.getForce().setVectorAngle(Math.PI);
				player.isMoving(true);
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released A"), "released A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed D"), "pressed D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.getForce().setVectorAngle(0);
				player.isMoving(true);
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released D"), "released D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed SPACE"), "pressed SPACE", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (player.isLanded()) {
					player.addExternalForce(ExternalForce.ForceIdentifier.JUMP, new ExternalForce(Math.toRadians(270), 200));
					player.setLanded(false);

					try {
						Boom.playSound(Discette.getSound("drop"));
					} catch (LineUnavailableException e1) {
						e1.printStackTrace();
					}

				}
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("shift pressed SHIFT"), "shift pressed SHIFT", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ExternalForce thrust = new ExternalForce(Math.toRadians(270), 100);
				thrust.setOnCollision(new CollisionListener() {

					@Override
					public void onCollision(Entity collidedWith) {
						thrust.kill();
					}
				});

				player.addExternalForce("JETPACK", thrust);
				player.setLanded(false);

				try {
					Boom.playSound(Discette.getSound("explosion"));
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		}));

		((Entity) player).setOnCollision(new CollisionListener() {

			@Override
			public void onCollision(Entity collidedWith) {
			}

		});

		myGame.addReceptionist(receptionist);

		myGame.run();
	}

}
