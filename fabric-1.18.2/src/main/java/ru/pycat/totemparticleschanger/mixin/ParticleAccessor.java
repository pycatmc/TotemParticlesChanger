package ru.pycat.totemparticleschanger.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {

    @Accessor("friction")
    public void setVelocityMultiplier(float velocityMultiplier);

}
