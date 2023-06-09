package ru.pycat.totemparticleschanger.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class TotemParticlesChangerScreen extends Screen {
    private static final DecimalFormat FORMAT1 = new DecimalFormat("#.#");
    private static final DecimalFormat FORMAT2 = new DecimalFormat("#.##");

    public final Screen parent;

    private static Checkbox enabled;
    private static ProfileButton profileButton;
    private static TCSlider scale;
    private static TCSlider velocityMultiplier;
    private static TCSlider lifetime;
    private static TCSlider gravity;
    private static Checkbox randomColor;
    private static TCSlider red;
    private static TCSlider green;
    private static TCSlider blue;
    private static Button done;

    private static boolean updateScreen = false;

    public TotemParticlesChangerScreen(Screen parent) {
        super(Component.translatable("totemparticleschanger.title"));
        this.parent = parent;
    }

    private void updateGuiElements() {
        enabled = new Checkbox(width / 2 - font.width(Component.translatable("totemparticleschanger.option.enabled")), 40, 24 + font.width(Component.translatable("totemparticleschanger.option.enabled")), 20, Component.translatable("totemparticleschanger.option.enabled"), TotemParticlesChangerConfig.enabled);
        profileButton = new ProfileButton(width / 2 - 75, 64, 150, 20);
        scale = new TCSlider(width / 2 - 75, 88, 150, 20, Component.empty(), TotemParticlesChangerConfig.scale / 2D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.scale"), Component.literal(FORMAT1.format(value * 2D))), value -> TotemParticlesChangerConfig.scale = (float) (value * 2D));
        velocityMultiplier = new TCSlider(width / 2 -  75, 112, 150, 20, Component.empty(), TotemParticlesChangerConfig.velocityMultiplier / 2D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.velocityMultiplier"), Component.literal(FORMAT1.format(value * 2D))), value -> TotemParticlesChangerConfig.velocityMultiplier = (float) (value * 2D));
        gravity = new TCSlider(width / 2 -  75, 136, 150, 20, Component.empty(), TotemParticlesChangerConfig.gravity / 2D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.gravity"), Component.literal(FORMAT2.format(value * 2D))), value -> TotemParticlesChangerConfig.gravity = (float) (value * 2D));
        lifetime = new TCSlider(width / 2 -  75, 160, 150, 20, Component.empty(), TotemParticlesChangerConfig.lifetime / 200D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.lifetime"), Component.literal(Integer.toString((int) (value * 200D)))), value -> TotemParticlesChangerConfig.lifetime = (int) (value * 200D));
        randomColor = new Checkbox(width  / 2 - font.width(Component.translatable("totemparticleschanger.option.randomColor")) + 15, 184, 24 + font.width(Component.translatable("totemparticleschanger.option.randomColor")), 20, Component.translatable("totemparticleschanger.option.randomColor"), TotemParticlesChangerConfig.randomColor);
        red = new TCSlider(width / 2 -  75, 208, 150, 20, Component.empty(), TotemParticlesChangerConfig.red / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.red"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.red = (int) (value * 255D));
        green = new TCSlider(width / 2 -  75, 232, 150, 20, Component.empty(), TotemParticlesChangerConfig.green / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.green"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.green = (int) (value * 255D));
        blue = new TCSlider(width / 2 -  75, 256, 150, 20, Component.empty(), TotemParticlesChangerConfig.blue / 255D, value -> CommonComponents.optionNameValue(Component.translatable("totemparticleschanger.option.blue"), Component.literal(Integer.toString((int) (value * 255D)))), value -> TotemParticlesChangerConfig.blue = (int) (value * 255D));
        done = Button.builder(CommonComponents.GUI_DONE, button -> minecraft.setScreen(parent)).bounds(width / 2 - 75, height - 24, 150, 20).build();
    }

    public static void updateScreen() {
        updateScreen = true;
    }

    @Override
    protected void init() {
        updateGuiElements();
        addRenderableWidget(enabled);
        addRenderableWidget(profileButton);
        addRenderableWidget(scale);
        addRenderableWidget(velocityMultiplier);
        addRenderableWidget(gravity);
        addRenderableWidget(lifetime);
        addRenderableWidget(randomColor);
        addRenderableWidget(red);
        addRenderableWidget(green);
        addRenderableWidget(blue);
        addRenderableWidget(done);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        drawCenteredString(stack, font, title, width / 2, 10, -1);

        if (!randomColor.selected()) {
            red.visible = true;
            green.visible = true;
            blue.visible = true;
            fill(stack, (width / 2 - 110), 232, (width / 2 - 90), 252, new Color(TotemParticlesChangerConfig.red, TotemParticlesChangerConfig.green, TotemParticlesChangerConfig.blue).getRGB());
        } else {
            red.visible = false;
            green.visible = false;
            blue.visible = false;
        }
    }

    @Override
    public void tick() {
        TotemParticlesChangerConfig.enabled = enabled.selected();
        TotemParticlesChangerConfig.randomColor = randomColor.selected();

        if (updateScreen) {
            removeWidget(enabled);
            removeWidget(profileButton);
            removeWidget(scale);
            removeWidget(velocityMultiplier);
            removeWidget(gravity);
            removeWidget(lifetime);
            removeWidget(randomColor);
            removeWidget(red);
            removeWidget(green);
            removeWidget(blue);
            removeWidget(done);
            init();
            updateScreen = false;
        }
    }

    @Override
    public void removed() {
        TotemParticlesChangerConfig.enabled = enabled.selected();
        TotemParticlesChangerConfig.randomColor = randomColor.selected();
        TotemParticlesChangerConfig.saveConfig(FabricLoader.getInstance().getConfigDir());
    }

    public static class ProfileButton extends AbstractButton {

        public ProfileButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal(Component.translatable("totemparticleschanger.option.profile").getString()+": "+TotemParticlesChangerConfig.profile));
        }

        @Override
        public void onPress() {

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int keyCode) {
            if (this.clicked(mouseX, mouseY)) {
                switch (keyCode) {
                    case GLFW.GLFW_MOUSE_BUTTON_1 -> {
                        TotemParticlesChangerConfig.setProfile(FabricLoader.getInstance().getConfigDir(), TotemParticlesChangerConfig.profile + 1);
                        TotemParticlesChangerScreen.updateScreen();
                        this.playDownSound(Minecraft.getInstance().getSoundManager());
                    }
                    case GLFW.GLFW_MOUSE_BUTTON_2 -> {
                        if (TotemParticlesChangerConfig.profile > 1) {
                            TotemParticlesChangerConfig.setProfile(FabricLoader.getInstance().getConfigDir(), TotemParticlesChangerConfig.profile - 1);
                            TotemParticlesChangerScreen.updateScreen();
                            this.playDownSound(Minecraft.getInstance().getSoundManager());
                        }
                    }
                }
            }

            return false;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            setMessage(Component.literal(Component.translatable("totemparticleschanger.option.profile").getString()+": "+TotemParticlesChangerConfig.profile));
        }
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
