package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor {

    private Texture background;

    public BackgroundActor(Texture texture) {
        this.background = texture;
    }

    public void draw(Batch batch, float alpha) {
        batch.draw(this.background, 0, 0);
    }

    public void dispose() {
        this.background.dispose();
    }
}
