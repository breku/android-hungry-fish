package com.hungryfish.model.scene;

import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.service.HighScoreService;
import com.hungryfish.util.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.List;

/**
 * User: Breku
 * Date: 06.10.13
 */
public class HighScoreScene extends BaseScene implements IOnSceneTouchListener {

    private HighScoreService highScoreService;

    public HighScoreScene(Object... objects) {
        super(objects);
    }

    @Override
    public void createScene(Object... objects) {
        init();
        setBackground(new Background(Color.WHITE));
        createHighScores();
        setOnSceneTouchListener(this);
    }

    private void createHighScores() {
        List<Integer> highScoreList = highScoreService.getHighScores();

        for (int i = 0; i < highScoreList.size(); i++) {
            Text text = new Text(400, 400 - i * 50, ResourcesManager.getInstance().getBlackFont(), highScoreList.get(i).toString(), vertexBufferObjectManager);
            attachChild(text);
        }

    }

    private void init() {
        highScoreService = new HighScoreService();
    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.RECORDS);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.RECORDS;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()) {
            SceneManager.getInstance().loadMenuSceneFrom(SceneType.RECORDS);
        }
        return false;
    }
}
