package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Obstacle {

    private Sprite sprite;
    private Grid grid;
    private int x;
    private int y;

    public Obstacle(Grid grid, Sprite sprite, int x, int y) {

        this.sprite = sprite;
        this.grid = grid;
        this.x = x;
        this.y = y;

    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public int[] getLocation() {
        return new int[] {this.x, this.y};
    }

    public void render() {
        this.grid.gridlist[this.x][this.y].sprite = this.sprite;
        this.grid.gridlist[this.x][this.y].isObstacle = true;
    }

}
