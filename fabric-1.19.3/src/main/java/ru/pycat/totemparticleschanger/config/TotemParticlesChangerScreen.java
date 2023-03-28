package ru.pycat.totemparticleschanger.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class TotemParticlesChangerScreen extends Screen {
    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#");

    private final Screen parent;
    private Checkbox enabled;
    private Checkbox staticColor;
    private Checkbox randomColor;

    public TotemParticlesChangerScreen(Screen parent) {
        super(Component.translatable("totemparticleschanger.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(enabled = new Checkbox(width / 2 - font.width(Component.translatable("totemparticleschanger.option.enabled")), 40, 24 + font.width(Component.translatable("totemparticleschanger.option.enabled")), 20, Component.translatable("totemparticleschanger.option.enabled"), TotemParticlesChangerConfig.enabled));
        addRenderableWidget(new TCSlider(width / 2 - 75, 64, 150, 20, Component.empty(), TotemParticlesChangerConfig.scale / 2D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.scale"), Component.literal(FORMAT.format(value * 2D))), value -> TotemParticlesChangerConfig.scale = (float) (value * 2D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 88, 150, 20, Component.empty(), TotemParticlesChangerConfig.velocityMultiplier / 2D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.velocityMultiplier"), Component.literal(FORMAT.format(value * 2D))), value -> TotemParticlesChangerConfig.velocityMultiplier = (float) (value * 2D)));
        addRenderableWidget(staticColor = new Checkbox(width / 2 - font.width(Component.translatable("totemparticleschanger.option.staticColor")) + 10, 112, 24 + font.width(Component.translatable("totemparticleschanger.option.staticColor")), 20, Component.translatable("totemparticleschanger.option.staticColor"), TotemParticlesChangerConfig.staticColor));
        addRenderableWidget(randomColor = new Checkbox(width / 2 - font.width(Component.translatable("totemparticleschanger.option.randomColor")) + 15, 136, 24 + font.width(Component.translatable("totemparticleschanger.option.randomColor")), 20, Component.translatable("totemparticleschanger.option.randomColor"), TotemParticlesChangerConfig.randomColor));
        addRenderableWidget(new TCSlider(width / 2 -  75, 160, 150, 20, Component.empty(), TotemParticlesChangerConfig.red / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.red"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.red = (int) (value * 255D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 184, 150, 20, Component.empty(), TotemParticlesChangerConfig.green / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.green"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.green = (int) (value * 255D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 208, 150, 20, Component.empty(), TotemParticlesChangerConfig.blue / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.blue"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.blue = (int) (value * 255D)));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> minecraft.setScreen(parent)).bounds(width / 2 - 75, height - 24, 150, 20).build());
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        drawCenteredString(stack, font, title, width / 2, 10, -1);
    }

    @Override
    public void tick() {
        TotemParticlesChangerConfig.enabled = enabled.selected();
        TotemParticlesChangerConfig.staticColor = staticColor.selected();
        TotemParticlesChangerConfig.randomColor = randomColor.selected();
    }

    @Override
    public void removed() {
        TotemParticlesChangerConfig.enabled = enabled.selected();
        TotemParticlesChangerConfig.staticColor = staticColor.selected();
        TotemParticlesChangerConfig.randomColor = randomColor.selected();
        TotemParticlesChangerConfig.saveConfig(FabricLoader.getInstance().getConfigDir());
    }

    public static class TCSlider extends AbstractSliderButton {
        private final DoubleFunction<Component> messageProvider;
        private final DoubleConsumer setter;

        public TCSlider(int x, int y, int width, int height, Component text, double value,
                        DoubleFunction<Component> messageProvider, DoubleConsumer setter) {
            super(x, y, width, height, text, value);
            this.messageProvider = messageProvider;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(messageProvider.apply(value));
        }

        @Override
        protected void applyValue() {
            setter.accept(value);
        }
    }
}
