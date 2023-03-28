package ru.pycat.totemparticleschanger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import ru.pycat.totemparticleschanger.config.TotemParticlesChangerConfig;

public class TotemParticlesChanger implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TotemParticlesChangerConfig.loadConfig(FabricLoader.getInstance().getConfigDir());
    }
}
