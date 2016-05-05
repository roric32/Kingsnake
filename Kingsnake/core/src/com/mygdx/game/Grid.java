package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;

public class Grid {

    private Camera camera;
    private float squarewidth;
    private float squareheight;
    private Sprite square;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private int squareCountWidth;
    private int squareCountHeight;
    public GridSquare[][] gridlist;


    public Grid(Camera camera, SpriteBatch batch) {

        this.squarewidth = 64;
        this.squareheight = 48;
        this.atlas = new TextureAtlas(Gdx.files.internal("images/obstacles.pack"));
        this.square = new Sprite(atlas.findRegion("blank"));
        this.camera = camera;
        this.batch = batch;
        this.squareCountWidth = 16;
        this.squareCountHeight = 16;
        this.gridlist = new GridSquare[16][16];
        float squareWidth = 64;
        float squareHeight = 48;

        for(int x = 0; x < squareCountWidth; x++) {
            for(int y = 0; y < squareCountHeight; y++) {
                GridSquare gridsquare = new GridSquare(x * 64, y * 48, 64, 48, this.square);
                this.gridlist[x][y] = gridsquare;
            }
        }

    }

    public void render() {

        for(GridSquare[] row : this.gridlist) {
            for(GridSquare cell : row) {
                cell.draw(this.batch);
            }
        }

    }


    public void dispose() {
        this.atlas.dispose();
    }

    public Sprite getSprite(int x, int y) {
        return this.gridlist[x][y].sprite;
    }

}
