package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;

public class Gem implements Powerup{

    public boolean isCollected;
    private float dt;
    private double speed = 1;
    public int number;
    public int currentx;
    public int currenty;
    private int nextx;
    private int nexty;
    private int startx;
    private int starty;
    private int previousx;
    private int previousy;
    private int nextposition;
    private Sprite crownsprite;
    private Sprite blanksprite;
    private Sound appearsound;
    private Sound collectsound;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Grid grid;
    private Overlord overlord;

    public boolean getisCollected() {
        return this.isCollected;
    }

    public void setIsCollected(boolean value) {
        this.isCollected = value;
    }

    public int getCurrentX() {
        return this.currentx;
    }

    public int getCurrentY() {
        return this.currenty;
    }

    public int getNumber() {
        return this.number;
    }

    public Gem(int number, SpriteBatch batch, Grid grid, int startx, int starty, Overlord overlord) {

        System.out.println("New Gem created!");

        //The index of this mouse. For the overlord to keep track of.
        this.number = number;

        this.isCollected = false;

        //Set up coordinates.
        this.batch = batch;
        this.grid = grid;
        this.overlord = overlord;
        this.startx = startx;
        this.starty = starty;
        this.currentx = startx;
        this.currenty = starty;

        //Set up sprites.
        this.atlas = new TextureAtlas(Gdx.files.internal("images/powerups/powerups.pack"));
        this.crownsprite = new Sprite(this.atlas.findRegion("gem"));
        TextureAtlas maptextures = new TextureAtlas(Gdx.files.internal("images/obstacles.pack"));
        this.blanksprite = new Sprite(maptextures.findRegion("blank"));

        //Set up sounds.
        this.appearsound = Gdx.audio.newSound(Gdx.files.internal("sounds/appearsound.wav"));
        this.collectsound = Gdx.audio.newSound(Gdx.files.internal("sounds/gem.wav"));

        Spawn();

    }

    public void Move() {
        this.previousx = this.currentx;
        this.previousy = this.currenty;
        this.currentx = this.nextx;
        this.currenty = this.nexty;
        this.grid.gridlist[this.previousx][this.previousy].sprite = this.blanksprite;
        this.grid.gridlist[this.currentx][this.currenty].sprite = this.crownsprite;
    }

    public void Spawn() {
        this.appearsound.play();
        this.grid.gridlist[this.currentx][this.currenty].sprite = this.crownsprite;
    }

    public void Invoke(Player player) {
        this.collectsound.play();
        player.score += 5;
        CollectMe();
    }

    public void render() {

        if(!this.isCollected) {
            float dt = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
            this.dt = this.dt + dt;

            if (this.dt > this.speed) {
                this.dt = 0;

                RandomXS128 random = new RandomXS128();

                this.nextposition = random.nextInt(4);

                if (nextposition == 0) {
                    //Move right.
                    this.nextx = this.currentx + 1;
                    this.nexty = this.currenty;
                } else if (nextposition == 1) {
                    //Move left.
                    this.nextx = this.currentx - 1;
                    this.nexty = this.currenty;
                } else if (nextposition == 2) {
                    //Move up.
                    this.nextx = this.currentx;
                    this.nexty = this.currenty + 1;
                } else if (nextposition == 3) {
                    //Move down.
                    this.nextx = this.currentx;
                    this.nexty = this.currenty - 1;
                }

                if(this.overlord.SquareIsPassable(this.nextx, this.nexty)) {
                    Move();
                }

            }
        }
    }

    private void CollectMe() {
        this.isCollected = true;
    }

    public void dispose() {

        this.collectsound.dispose();
        this.appearsound.dispose();
        this.atlas.dispose();
    }

}
