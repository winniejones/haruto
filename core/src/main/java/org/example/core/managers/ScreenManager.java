package org.example.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.example.core.screens.MainGameScreen;

public class ScreenManager implements Renderable {
    private Screen currentScreen;

    public ScreenManager() {
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        if (this.currentScreen != null)
            this.currentScreen.dispose();

        this.currentScreen = currentScreen;
    }

    public void loadWorldScreen() {
        setCurrentScreen(new MainGameScreen());
    }

    @Override
    public void render() {
        this.currentScreen.render(Gdx.graphics.getDeltaTime());
    }
}
