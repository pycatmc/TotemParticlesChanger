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
    public static int profile = 1;

    public static void setProfile(Path directory, int profile) {
        try {
            saveConfig(directory);

            TotemParticlesChangerConfig.profile = profile;

            Path pathConfig = directory.resolve("TotemParticlesChanger.json");
            Files.createDirectories(directory);
            JsonObject jsonConfig = new JsonObject();
            jsonConfig.addProperty("profile", TotemParticlesChangerConfig.profile);
            Files.write(pathConfig, GSON.toJson(jsonConfig).getBytes(StandardCharsets.UTF_8));

            loadConfig(directory);
        } catch (Exception e) {
            LOG.warn("Unable to save TotemParticlesChanger config.", e);
        }
    }

    public static void loadConfig(Path directory) {
        try {
            Path pathConfig = directory.resolve("TotemParticlesChanger.json");
            if (!Files.isRegularFile(pathConfig)) return;
            JsonObject jsonConfig = GSON.fromJson(new String(Files.readAllBytes(pathConfig), StandardCharsets.UTF_8), JsonObject.class);
            profile = jsonConfig.get("profile").getAsInt();

            Path pathProfile = directory.resolve("TotemParticlesChanger-Profile"+profile+".json");
            if (!Files.isRegularFile(pathProfile)) return;
            JsonObject jsonProfile = GSON.fromJson(new String(Files.readAllBytes(pathProfile), StandardCharsets.UTF_8), JsonObject.class);
            enabled = jsonProfile.get("enabled").getAsBoolean();
            scale = jsonProfile.get("scale").getAsFloat();
            velocityMultiplier = jsonProfile.get("velocityMultiplier").getAsFloat();
            staticColor = jsonProfile.get("colorStatic").getAsBoolean();
            randomColor = jsonProfile.get("randomColor").getAsBoolean();
            red = jsonProfile.get("red").getAsInt();
            green = jsonProfile.get("green").getAsInt();
            blue = jsonProfile.get("blue").getAsInt();

            LOG.info("TotemParticlesChanger config loaded.");
        } catch (Exception e) {
            LOG.warn("Unable to load TotemParticlesChanger config.", e);
        }
    }

    public static void saveConfig(Path directory) {
        try {
            Path pathConfig = directory.resolve("TotemParticlesChanger.json");
            Files.createDirectories(directory);
            JsonObject jsonConfig = new JsonObject();
            jsonConfig.addProperty("profile", profile);
            Files.write(pathConfig, GSON.toJson(jsonConfig).getBytes(StandardCharsets.UTF_8));

            Path pathProfile = directory.resolve("TotemParticlesChanger-Profile"+profile+".json");
            Files.createDirectories(directory);
            JsonObject jsonProfile = new JsonObject();
            jsonProfile.addProperty("enabled", enabled);
            jsonProfile.addProperty("scale", scale);
            jsonProfile.addProperty("velocityMultiplier", velocityMultiplier);
            jsonProfile.addProperty("colorStatic", staticColor);
            jsonProfile.addProperty("randomColor", randomColor);
            jsonProfile.addProperty("red", red);
            jsonProfile.addProperty("green", green);
            jsonProfile.addProperty("blue", blue);
            Files.write(pathProfile, GSON.toJson(jsonProfile).getBytes(StandardCharsets.UTF_8));

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
