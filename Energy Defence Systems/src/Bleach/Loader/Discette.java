package Bleach.Loader;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Bleach.Sprite;
import Bleach.SpriteAnimated;
import Bleach.SoundEngine.Sound;

public class Discette {
	public static class JsonObjectLevel {
		public class JsonObjectBacks {
			public String texturekey;
		}

		public class JsonObjectTiles {
			public String texturekey;
			public Integer absolutex;
			public Integer absolutey;
			public Integer gridx;
			public Integer gridy;
			public Integer gridwidth;
			public Integer gridheight;
		}

		public String key;
		public Integer width;
		public Integer height;

		public JsonObjectBacks[] backgrounds;

		public JsonObjectTiles[] tiles;
	}

	private static class JsonObject {
		private String key;
		private String filename;
		private Integer width;
		private Integer height;
		private Integer frametime;
		private Integer originx;
		private Integer originy;
	}

	private static Map<String, Sprite> images = new HashMap<String, Sprite>();

	private static Map<String, Sound> sounds = new HashMap<>();

	public static Sprite getImage(String imageID) {
		return images.get(imageID);
	}

	public static Sound getSound(String soundID) {
		return sounds.get(soundID);
	}

	public static void loadImages(String assetJsonpath) {
		/*
		 * Loop through the results of the parsed JSON data and load in graphics
		 * files.
		 */
		JsonObject[] sprites = parseJsonFile(assetJsonpath);
		String path = new File(assetJsonpath).getParent() + File.separator;

		for (JsonObject sprite : sprites) {
			if (sprite.key != null && sprite.filename != null) {
				if (sprite.frametime != null && sprite.frametime > 0) {
					images.put(sprite.key, new SpriteAnimated(imgLoader(path + sprite.filename), sprite.width, sprite.height, sprite.originx, sprite.originy, sprite.frametime));
				} else {
					images.put(sprite.key, new Sprite(imgLoader(path + sprite.filename), sprite.width, sprite.height, sprite.originx, sprite.originy));
				}
			}
		}
	}

	public static JsonObjectLevel loadLevel(String assetJsonPath) {
		return parseJsonFileLevel(assetJsonPath);
	}

	public static void loadSound(String assetJsonpath) throws IOException, UnsupportedAudioFileException {
		JsonObject[] audios = parseJsonFile(assetJsonpath);
		String path = new File(assetJsonpath).getParent() + File.separator;

		for (JsonObject audio : audios) {
			if (audio.key != null && audio.filename != null) {
				sounds.put(audio.key, soundLoader(path + audio.filename));
			}
		}
	}

	public static JsonObjectLevel parseJsonFileLevel(String pathToJSON) {
		/**/
		Gson json = new Gson();
		JsonObjectLevel level = null;

		try {
			level = json.fromJson(readFile(pathToJSON), JsonObjectLevel.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return level;
	}

	public static BufferedImage toCompatibleImage(BufferedImage image) {
		/*
		 * Optimizes (set image color model) image so that it's compatible with
		 * the current system. This is an absolute must to get java 2D graphics
		 * to run smoothly.
		 */

		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image;
	}

	private static BufferedImage imgLoader(String filename) {
		/*
		 * Loads an image file and return it as a "BufferedImage". Optionally
		 * optimize it (which might mess up alpha channel on some systems).
		 */

		BufferedImage compatImg = null;

		try {
			return toCompatibleImage(ImageIO.read(new File(filename)));
		} catch (IOException e) {
			System.err.println("[IMAGE] Error loading file: \"" + filename + "\" " + e);
		}

		return compatImg;
	}

	private static JsonObject[] parseJsonFile(String pathToJSON) {
		/* Parses a JSON file and returns a list of entries */
		Gson json = new Gson();
		JsonObject[] entries = null;

		try {
			entries = json.fromJson(readFile(pathToJSON), JsonObject[].class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entries;
	}

	private static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, "UTF-8");
	}

	private static Sound soundLoader(String filename) throws IOException, UnsupportedAudioFileException {

		InputStream soundDataStream = new FileInputStream(new File(filename));

		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		byte data[] = new byte[1024];
		for (int i = soundDataStream.read(data); i != -1; i = soundDataStream.read(data))
			bytearrayoutputstream.write(data, 0, i);

		soundDataStream.close();
		bytearrayoutputstream.close();
		data = bytearrayoutputstream.toByteArray();

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
		AudioFormat af = audioInputStream.getFormat();
		int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
		byte[] audioData = new byte[size];
		DataLine.Info info = new DataLine.Info(Clip.class, af, size);
		audioInputStream.read(audioData, 0, size);

		return new Sound(audioInputStream, size, info, audioData);
	}
}
