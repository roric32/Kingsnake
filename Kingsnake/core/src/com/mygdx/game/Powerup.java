package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

interface Powerup {

    void Invoke(Player player);
    void Spawn();
    void render();
    void dispose();

    boolean getisCollected();
    int getNumber();
    void setIsCollected(boolean value);
    int getCurrentX();
    int getCurrentY();
}
