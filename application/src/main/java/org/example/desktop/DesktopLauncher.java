package org.example.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.example.core.HarutoGame;
import org.example.core.utils.Dimensions;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] args){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimensions.SCREEN_WIDTH = screenSize.width;
		Dimensions.SCREEN_HEIGHT = screenSize.height;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Dimensions.SCREEN_WIDTH;
		config.height = Dimensions.SCREEN_HEIGHT;
		config.fullscreen = false;
		config.title = "Haruto";
		Application app = new LwjglApplication(new HarutoGame(), config);
		Gdx.app = app;
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
