package com.hungryfish.util;

import org.andengine.util.Constants;
import org.andengine.util.level.constants.LevelConstants;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class ConstantsUtil implements Constants, LevelConstants {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public static final float LOADING_SCENE_TIME = 0.1f;
    public static final float SPLASH_SCREEN_TIME = 0.2f;


    public static final String MY_AD_UNIT_ID = "ca-app-pub-6574392848136998/7431396465";

    public static final int POOL_SIZE = 10;


    /**
     * Number of enemy fishes on the screen
     */
    public static final int NUMBER_OF_ENEMIES = 40;

    public static final int TAG_SPRITE_PLAYER = 1;

    public static final int LEFT_BORDER = 0;
    public static final int RIGHT_BORDER = 1600;
    public static final int TOP_BORDER = 920;
    public static final int BOTTOM_BORDER = 0;


    /* The categories. */
    public static final short CATEGORY_BIT_PLAYER = 1;
    public static final short CATEGORY_BIT_ENEMY = 2;
    public static final short CATEGORY_BIT_WALL = 4;


    /* And what should collide with what. */
    public static final short MASK_BITS_PLAYER = CATEGORY_BIT_ENEMY + CATEGORY_BIT_WALL;
    public static final short MASK_BITS_ENEMY = CATEGORY_BIT_PLAYER;
    public static final short MASK_BITS_WALL = CATEGORY_BIT_PLAYER;


    /* 99 seconds for play */
    public static final int GAME_TIME = 99;


    /* Number of fishes you have to eat before unlocking the the next */
    public static final int UNLOCK_LEVEL_1 = 10;
    public static final int UNLOCK_LEVEL_2 = 30;
    public static final int UNLOCK_LEVEL_3 = 60;
    public static final int UNLOCK_LEVEL_4 = 100;
    public static final int UNLOCK_LEVEL_5 = 200;


}
