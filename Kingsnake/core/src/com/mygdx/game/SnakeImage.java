package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SnakeImage extends Actor {

    private int x;
    private int y;
    private Sprite snakesprite;

    public SnakeImage(int x, int y, String textureregion) {

        this.x = x;
        this.y = y;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/gamesetup/gamesetup.pack"));
        Sprite snake = new Sprite(atlas.findRegion(textureregion));
        this.snakesprite = snake;

    }

    public void draw(Batch batch, float alpha) {

        batch.draw(this.snakesprite, this.x, this.y);
    }

    public void dispose() {
    }
}
