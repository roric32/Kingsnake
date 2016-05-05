package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

public class Clock extends Actor{

    private TextureAtlas gameSetup = new TextureAtlas(Gdx.files.internal("images/gamesetup/gamesetup.pack"));
    private Sprite one = new Sprite(gameSetup.findRegion("one"));
    private Sprite two = new Sprite(gameSetup.findRegion("two"));
    private Sprite three = new Sprite(gameSetup.findRegion("three"));
    private Sprite four = new Sprite(gameSetup.findRegion("four"));
    private Sprite five = new Sprite(gameSetup.findRegion("five"));
    private Sprite six = new Sprite(gameSetup.findRegion("six"));
    private Sprite seven = new Sprite(gameSetup.findRegion("seven"));
    private Sprite eight = new Sprite(gameSetup.findRegion("eight"));
    private Sprite nine = new Sprite(gameSetup.findRegion("nine"));
    private Sprite ten = new Sprite(gameSetup.findRegion("ten"));
    private Sprite none = new Sprite(gameSetup.findRegion("none"));
    private Sprite currentSprite;
    private Sound countdownsound;
    private int x;
    private int y;
    private GameSetup gamesetup;

    public Clock(int locationx, int locationy, GameSetup gamesetup) {

        this.currentSprite = this.ten;
        this.gamesetup = gamesetup;
        this.x = locationx;
        this.y = locationy;
        this.countdownsound = Gdx.audio.newSound(Gdx.files.internal("sounds/Countdown.wav"));

        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                currentSprite = NextSprite();
            }

        }, 1, 1, 9);
    }

    @Override
    public void draw(Batch batch, float alpha) {

        if(this.currentSprite == none) {
            this.gamesetup.timetostart = true;
        }

        batch.draw(this.currentSprite, this.x, this.y);

    }

    public void dispose() {
        this.gameSetup.dispose();
    }

    private Sprite NextSprite() {

        Sprite newsprite;

        if(this.currentSprite == ten) {
            newsprite = nine;
        } else if(this.currentSprite == nine) {
            newsprite = eight;
        } else if(this.currentSprite == eight) {
            newsprite = seven;
        } else if(this.currentSprite == seven) {
            newsprite = six;
        } else if(this.currentSprite == six) {
            newsprite = five;
        } else if(this.currentSprite == five) {
            newsprite = four;
        } else if(this.currentSprite == four) {
            newsprite = three;
            countdownsound.play();
        } else if(this.currentSprite == three) {
            newsprite = two;
            countdownsound.play();
        } else if(this.currentSprite == two) {
            newsprite = one;
            countdownsound.play();
        } else if(this.currentSprite == one) {
            newsprite = none;
        } else {
            newsprite = none;
        }

        return newsprite;
    }

}
