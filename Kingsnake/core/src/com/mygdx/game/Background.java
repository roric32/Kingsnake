package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {

    private int width;
    private int height;
    private Texture texture;
    private SpriteBatch batch;

    public Background(int width, int height, FileHandle texture, SpriteBatch batch, boolean repeat) {

        this.width = width;
        this.height = height;
        this.texture = new Texture(texture);
        this.batch = batch;
        if(repeat) {
            this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }

    }

    public void render() {
        this.batch.draw(this.texture, 0, 0, width, height, 0, 0, width, height, false, false);
    }

    public void dispose() {
        this.texture.dispose();
    }

}
