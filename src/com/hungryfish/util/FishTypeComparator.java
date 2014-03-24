package com.hungryfish.util;

import java.util.Comparator;

/**
 * User: Breku
 * Date: 24.03.14
 */
public class FishTypeComparator implements Comparator<FishType> {
    @Override
    public int compare(FishType fishType, FishType fishType1) {
        return fishType.getFishLevel().compareTo(fishType1.getFishLevel());
    }
}
