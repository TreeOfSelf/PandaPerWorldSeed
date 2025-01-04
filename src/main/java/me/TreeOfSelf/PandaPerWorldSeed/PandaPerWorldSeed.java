package me.TreeOfSelf.PandaPerWorldSeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PandaPerWorldSeed implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("panda-per-world-seed");
	private static final File CONFIG_FILE = new File("./config/per_world_seed.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final Map<String, Long> DIMENSION_SEEDS = new HashMap<>();

	private static final Map<String, String> LEGACY_KEY_MAPPING = new HashMap<>() {{
		put("OVERWORLD", "overworld");
		put("THE_NETHER", "the_nether");
		put("THE_END", "the_end");
	}};

	@Override
	public void onInitialize() {
		LOGGER.info("PerWorldSeed Started!");

		loadSeedsFromFile();

		for (Map.Entry<String, String> entry : LEGACY_KEY_MAPPING.entrySet()) {
			if (DIMENSION_SEEDS.containsKey(entry.getKey())) {
				long seed = DIMENSION_SEEDS.remove(entry.getKey());
				DIMENSION_SEEDS.put(entry.getValue(), seed);
				LOGGER.info("Migrated seed for dimension {} to {}", entry.getKey(), entry.getValue());
			}
		}

		saveSeedsToFile();

		ServerWorldEvents.LOAD.register(this::onWorldLoaded);
	}

	private void loadSeedsFromFile() {
		if (CONFIG_FILE.exists()) {
			try (FileReader reader = new FileReader(CONFIG_FILE)) {
				JsonObject json = GSON.fromJson(reader, JsonObject.class);
				json.entrySet().forEach(entry -> {
					DIMENSION_SEEDS.put(entry.getKey(), entry.getValue().getAsLong());
				});
			} catch (IOException e) {
				LOGGER.error("Failed to read the configuration file.", e);
			}
		} else {
			try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
				GSON.toJson(new JsonObject(), writer);
			} catch (IOException e) {
				LOGGER.error("Failed to create the configuration file.", e);
			}
		}
	}

	private void onWorldLoaded(MinecraftServer minecraftServer, ServerWorld serverWorld) {
		String dimension = serverWorld.getRegistryKey().getValue().toString().replaceFirst("minecraft:", "");
		if (!DIMENSION_SEEDS.containsKey(dimension)) {
			long seed = new Random().nextLong();
			DIMENSION_SEEDS.put(dimension, seed);
			saveSeedToFile(dimension, seed);
		}
	}

	private void saveSeedsToFile() {
		JsonObject json = new JsonObject();
		DIMENSION_SEEDS.forEach(json::addProperty);
		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			GSON.toJson(json, writer);
		} catch (IOException e) {
			LOGGER.error("Failed to write the configuration file.", e);
		}
	}

	private void saveSeedToFile(String dimension, long seed) {
		JsonObject json = new JsonObject();
		json.addProperty(dimension, seed);
		saveSeedsToFile();
	}

	public static Long getSeed(String dimension) {
		return DIMENSION_SEEDS.get(dimension);
	}
}
