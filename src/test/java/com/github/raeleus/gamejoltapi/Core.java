package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import com.github.raeleus.gamejoltapi.GameJoltUsers.UsersFetchData;
import com.github.raeleus.gamejoltapi.GameJoltUsers.UsersFetchRequest;
import lombok.var;

public class Core extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("game-jolt-api");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        new Lwjgl3Application(new Core(), configuration);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        var gj = new GameJoltApi();

        String gameID = "869827";
        String key = "78ac632c55945de5cb5f30b735246a8c";

        var request = UsersFetchRequest.builder()
            .gameID(gameID)
            .username("Raeleus")
            .build();

        gj.sendRequest(request, key, new GameJoltListener() {
            @Override
            public void response(JsonValue jsonValue, GameJoltData data) {
                var fetchResult = (UsersFetchData) data;
                System.out.println(fetchResult.users.get(0).avatarURL);
                gj.downloadImageUrlAsTexture(fetchResult.users.get(0).avatarURL, texture -> {
                    System.out.println("success");
                    image = texture;
                });
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("failed");
                t.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
