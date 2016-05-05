package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;

public class Mouse {

    public boolean isDead = false;
    private float dt;
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
    private Sprite leftsprite;
    private Sprite rightsprite;
    private Sprite upsprite;
    private Sprite downsprite;
    private Sprite blanksprite;
    private Sound munch;
    private Sound squeak;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Grid grid;
    private Overlord overlord;

    public Mouse(int number, SpriteBatch batch, Grid grid, int startx, int starty, Overlord overlord) {

        System.out.println("New mouse created!");

        //The index of this mouse. For the overlord to keep track of.
        this.number = number;

        //Set up coordinates.
        this.batch = batch;
        this.grid = grid;
        this.overlord = overlord;
        this.startx = startx;
        this.starty = starty;
        this.currentx = startx;
        this.currenty = starty;

        //Set up sprites.
        this.atlas = new TextureAtlas(Gdx.files.internal("images/mouse/mouse.pack"));
        this.leftsprite = new Sprite(atlas.findRegion("left"));
        this.rightsprite = new Sprite(atlas.findRegion("right"));
        this.upsprite = new Sprite(atlas.findRegion("up"));
        this.downsprite = new Sprite(atlas.findRegion("down"));
        this.grid.gridlist[this.startx][this.starty].sprite = this.downsprite;
        TextureAtlas maptextures = new TextureAtlas(Gdx.files.internal("images/obstacles.pack"));
        this.blanksprite = new Sprite(maptextures.findRegion("blank"));

        //Set up sounds.
        this.squeak = Gdx.audio.newSound(Gdx.files.internal("sounds/squeak.wav"));
        this.munch = Gdx.audio.newSound(Gdx.files.internal("sounds/munch.wav"));

        Spawn();

    }

    private void Move() {

        this.previousx = this.currentx;
        this.previousy = this.currenty;
        this.currentx = this.nextx;
        this.currenty = this.nexty;
        this.grid.gridlist[this.previousx][this.previousy].sprite = this.blanksprite;
        this.grid.gridlist[this.currentx][this.currenty].sprite = this.downsprite;

    }

    private void Spawn() {

        this.squeak.play();

    }

    public void render() {

        if(!this.isDead) {
            float dt = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
            this.dt = this.dt + dt;

            if (this.dt > 0.5) {
                this.dt = 0;

                RandomXS128 random = new RandomXS128();

                this.nextposition = random.nextInt(4);
                //System.out.println("This.nextposition: " + this.nextposition);

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

    private void KillMe() {
        this.isDead = true;
    }

    public void dispose() {
        this.atlas.dispose();
        this.squeak.dispose();
        this.munch.dispose();
    }
}
