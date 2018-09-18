package fr.paragoumba.mlfg.game;

import fr.paragoumba.mlfg.engine.GameEngine;
import fr.paragoumba.mlfg.engine.IGameLogic;

import java.awt.*;

public class MarioAndLuigi {

    public static void main(String[] args) {

        try {

            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

            IGameLogic gameLogic = new Game();
            GameEngine gameEngine = new GameEngine("Mario and Luigi : Fan Game", dimension.width, dimension.height, true, gameLogic);
            gameEngine.start();

        } catch (Exception e){

            e.printStackTrace();
            System.exit(-1);

        }
    }
}
