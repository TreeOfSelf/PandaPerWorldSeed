package me.sebastian420.PandaPerWorldSeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PandaPerWorldSeed implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("panda-per-world-seed");
	private static final File CONFIG_FILE = new File("./config/per_world_seed.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static long OVERWORLD_SEED;
	public static long THE_NETHER_SEED;
	public static long THE_END_SEED;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing PerWorldSeed Mod");
		loadOrGenerateSeeds();
	}

	private void loadOrGenerateSeeds() {
		if (CONFIG_FILE.exists()) {
			try (FileReader reader = new FileReader(CONFIG_FILE)) {
				JsonObject json = GSON.fromJson(reader, JsonObject.class);
				OVERWORLD_SEED = json.get("OVERWORLD").getAsLong();
				THE_NETHER_SEED = json.get("THE_NETHER").getAsLong();
				THE_END_SEED = json.get("THE_END").getAsLong();
				LOGGER.info("Loaded seeds from configuration file.");
			} catch (IOException e) {
				LOGGER.error("Failed to read the configuration file.", e);
				generateAndSaveSeeds();
			}
		} else {
			generateAndSaveSeeds();
		}
	}

	private void generateAndSaveSeeds() {
		Random random = new Random();
		OVERWORLD_SEED = random.nextLong();
		THE_NETHER_SEED = random.nextLong();
		THE_END_SEED = random.nextLong();

		JsonObject json = new JsonObject();
		json.addProperty("OVERWORLD", OVERWORLD_SEED);
		json.addProperty("THE_NETHER", THE_NETHER_SEED);
		json.addProperty("THE_END", THE_END_SEED);

		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			GSON.toJson(json, writer);
			LOGGER.info("Generated new seeds and saved to configuration file.");
		} catch (IOException e) {
			LOGGER.error("Failed to write the configuration file.", e);
		}
	}
}
