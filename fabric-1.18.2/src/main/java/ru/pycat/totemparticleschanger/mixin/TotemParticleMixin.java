package ru.pycat.totemparticleschanger.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TotemParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.pycat.totemparticleschanger.config.TotemParticlesChangerConfig;

@Mixin(TotemParticle.class)
public class TotemParticleMixin extends SimpleAnimatedParticle {

    private TotemParticleMixin() {
        super(null, 0d, 0d, 0d, null, 0f);
    }

    @Inject(method = "<init>",
            at = @At(value = "RETURN"))
    private void onInit(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet, CallbackInfo ci) {
        if (TotemParticlesChangerConfig.enabled) {
            ((ParticleAccessor) this).setVelocityMultiplier(TotemParticlesChangerConfig.velocityMultiplier);
            this.scale(TotemParticlesChangerConfig.scale);

            float red, green, blue;

            if (TotemParticlesChangerConfig.randomColor) {
                red = this.random.nextFloat();
                green = this.random.nextFloat();
                blue = this.random.nextFloat();
            } else if (TotemParticlesChangerConfig.staticColor) {
                red = TotemParticlesChangerConfig.getRed();
                green = TotemParticlesChangerConfig.getGreen();
                blue = TotemParticlesChangerConfig.getBlue();
            } else {
                red = TotemParticlesChangerConfig.getRed() - this.random.nextFloat() / 200;
                green = TotemParticlesChangerConfig.getGreen() - this.random.nextFloat() / 200;
                blue = TotemParticlesChangerConfig.getBlue() - this.random.nextFloat() / 200;
            }

            setColor(red, green, blue);
        }
    }

}
