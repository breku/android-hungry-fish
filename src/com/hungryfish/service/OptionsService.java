package com.hungryfish.service;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 23.03.14
 */
public class OptionsService extends BaseService {


    DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public boolean isFishLocked(FishType fishType){
        return databaseHelper.isFishLocked(fishType);
    }
}
