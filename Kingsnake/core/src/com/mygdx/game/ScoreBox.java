package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

public class ScoreBox extends Actor{

    private TextureAtlas gameEndAtlas = new TextureAtlas(Gdx.files.internal("images/gameend/gameend.pack"));
    private Sprite sprite;
    public int x;
    public int y;

    public ScoreBox(int x, int y, String region) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(this.gameEndAtlas.findRegion(region));
    }

    @Override
    public void draw(Batch batch, float alpha) {

        batch.draw(this.sprite, this.x, this.y);

    }

    public void dispose() {
        this.gameEndAtlas.dispose();
    }

}
