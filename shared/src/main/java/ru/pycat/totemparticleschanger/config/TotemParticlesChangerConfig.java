package ru.pycat.totemparticleschanger.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TotemParticlesChangerConfig {
    private static final Logger LOG = LogManager.getLogger("TotemParticlesChanger");
    private static final Gson GSON = new Gson();

    public static boolean enabled = true;
    public static float scale = 1F;
    public static float velocityMultiplier = 0.7F;
    public static boolean staticColor = false;
    public static boolean randomColor = false;
    public static int red = 172;
    public static int green = 248;
    public static int blue = 252;

    public static void loadConfig(Path directory) {
        try {
            Path path = directory.resolve("totemparticleschanger.json");
            if (!Files.isRegularFile(path)) return;
            JsonObject json = GSON.fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), JsonObject.class);
            enabled = json.get("enabled").getAsBoolean();
            scale = json.get("scale").getAsFloat();
            velocityMultiplier = json.get("velocityMultiplier").getAsFloat();
            staticColor = json.get("colorStatic").getAsBoolean();
            randomColor = json.get("randomColor").getAsBoolean();
            red = json.get("red").getAsInt();
            green = json.get("green").getAsInt();
            blue = json.get("blue").getAsInt();
            LOG.info("TotemParticlesChanger config loaded.");
        } catch (Exception e) {
            LOG.warn("Unable to load TotemParticlesChanger config.", e);
        }
    }

    public static void saveConfig(Path directory) {
        try {
            Path path = directory.resolve("totemparticleschanger.json");
            Files.createDirectories(directory);
            JsonObject json = new JsonObject();
            json.addProperty("enabled", enabled);
            json.addProperty("scale", scale);
            json.addProperty("velocityMultiplier", velocityMultiplier);
            json.addProperty("colorStatic", staticColor);
            json.addProperty("randomColor", randomColor);
            json.addProperty("red", red);
            json.addProperty("green", green);
            json.addProperty("blue", blue);
            Files.write(path, GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
            LOG.info("TotemParticlesChanger config saved.");
        } catch (Exception e) {
            LOG.warn("Unable to save TotemParticlesChanger config.", e);
        }
    }

    public static float getRed() {
        return 0.003921568627451F * red;
    }

    public static float getGreen() {
        return 0.003921568627451F * green;
    }

    public static float getBlue() {
        return 0.003921568627451F * blue;
    }

}
