package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Kingsnake extends com.badlogic.gdx.Game{

	public TitleScreen titleScreen;
	public GameController gameController;
	public GameSetup gameSetup;
	public GameEnd gameEnd;

	public void create () {

		this.titleScreen = new TitleScreen(this);
		this.gameController = new GameController(4, this);
		this.gameSetup = new GameSetup(this);
		this.gameEnd = new GameEnd(this);

		setScreen(titleScreen);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		//gameController.render(delta);
		//this.titleScreen.render(delta);
		this.getScreen().render(delta);
	}

	@Override
	public void dispose () {
		this.getScreen().dispose();
	}

    @Override
    public void resize(int width, int height) {
        //gameController.viewport.update(width, height);
    }
}
