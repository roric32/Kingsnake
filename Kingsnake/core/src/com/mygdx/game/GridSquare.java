package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GridSquare {

    private ShapeRenderer renderer;
    private int x;
    private int y;
    private int width;
    private int height;
    public Sprite sprite;
    public Animation animation = null;
    public boolean isObstacle = false;
    private float elapsedTime = 0;

    public GridSquare(int x, int y, int width, int height, Sprite sprite) {
        renderer = new ShapeRenderer();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void drawOutline() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(this.x, this.y, this.width, this.height);
        renderer.end();
    }

    public void draw(SpriteBatch batch) {
        if(this.animation == null) {
            batch.draw(this.sprite, this.x, this.y, this.width, this.height);
        } else {
            this.elapsedTime += Gdx.graphics.getDeltaTime();
            batch.draw(this.animation.getKeyFrame(this.elapsedTime, true), this.x, this.y, this.width, this.height);
        }
    }

    public void dispose() {
        renderer.dispose();
    }

}
