package ru.pycat.totemparticleschanger.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.text.DecimalFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class TotemParticlesChangerScreen extends Screen {
    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    private final Screen parent;
    private Checkbox enabled;
    private Checkbox staticColor;

    public TotemParticlesChangerScreen(Screen parent) {
        super(new TranslatableComponent("totemparticleschanger.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(enabled = new Checkbox(width / 2 - font.width(new TranslatableComponent("totemparticleschanger.option.enabled")), 40, 24 + font.width(new TranslatableComponent("totemparticleschanger.option.enabled")), 20, new TranslatableComponent("totemparticleschanger.option.enabled"), TotemParticlesChangerConfig.enabled));
        addRenderableWidget(new TCSlider(width / 2 - 75, 64, 150, 20, TextComponent.EMPTY, TotemParticlesChangerConfig.scale / 2D, value -> CommonComponents.optionNameValue(new TranslatableComponent("totemparticleschanger.option.scale"), new TextComponent(FORMAT.format(value * 2D))), value -> TotemParticlesChangerConfig.scale = (float) (value * 2D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 88, 150, 20, TextComponent.EMPTY, TotemParticlesChangerConfig.velocityMultiplier / 2D, value -> CommonComponents.optionNameValue(new TranslatableComponent("totemparticleschanger.option.velocityMultiplier"), new TextComponent(FORMAT.format(value * 2D))), value -> TotemParticlesChangerConfig.velocityMultiplier = (float) (value * 2D)));
        addRenderableWidget(staticColor = new Checkbox(width / 2 - font.width(new TranslatableComponent("totemparticleschanger.option.staticColor")), 112, 24 + font.width(new TranslatableComponent("totemparticleschanger.option.staticColor")), 20, new TranslatableComponent("totemparticleschanger.option.staticColor"), TotemParticlesChangerConfig.staticColor));
        addRenderableWidget(new TCSlider(width / 2 -  75, 136, 150, 20, TextComponent.EMPTY, TotemParticlesChangerConfig.red / 255D, value -> CommonComponents.optionNameValue(new TranslatableComponent("totemparticleschanger.option.red"), new TextComponent(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.red = (int) (value * 255D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 160, 150, 20, TextComponent.EMPTY, TotemParticlesChangerConfig.green / 255D, value -> CommonComponents.optionNameValue(new TranslatableComponent("totemparticleschanger.option.green"), new TextComponent(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.green = (int) (value * 255D)));
        addRenderableWidget(new TCSlider(width / 2 -  75, 184, 150, 20, TextComponent.EMPTY, TotemParticlesChangerConfig.blue / 255D, value -> CommonComponents.optionNameValue(new TranslatableComponent("totemparticleschanger.option.blue"), new TextComponent(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.blue = (int) (value * 255D)));
        addRenderableWidget(new Button(width / 2 - 75, height - 24, 150, 20, CommonComponents.GUI_DONE, button -> minecraft.setScreen(parent)));
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
    }

    @Override
    public void removed() {
        TotemParticlesChangerConfig.enabled = enabled.selected();
        TotemParticlesChangerConfig.staticColor = staticColor.selected();
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
