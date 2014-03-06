package com.hungryfish.pool;

import com.hungryfish.model.shape.Fish;
import com.hungryfish.model.shape.FishBodyData;
import com.hungryfish.util.FishType;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.pool.GenericPool;

import java.util.Random;

/**
 * User: Breku
 * Date: 24.01.14
 */
public class FishPool extends GenericPool<Fish> {

    private FishType fishType = FishType.BLACK;
    private Random random = new Random();
    private int counter = 1;
    private PhysicsWorld physicsWorld;

    public FishPool(PhysicsWorld physicsWorld) {
        super();
        this.physicsWorld = physicsWorld;
    }

    /**
     * Called when a Bullet is required but there isn't one in the pool
     */
    @Override
    protected Fish onAllocatePoolItem() {
        Fish fish;
        fishType = fishType.next();
        counter++;

        FishBodyData fishBodyData = new FishBodyData("enemyFish " + fishType.name() + " "+ counter,counter);
        if (random.nextBoolean()) {
            fish = new Fish(random.nextInt(50) + 1500, random.nextInt(910) + 25, fishType, physicsWorld, true, fishBodyData,true,counter);
            fish.setCurrentTileIndex(1);
        } else {
            fish = new Fish(random.nextInt(50) + 25, random.nextInt(910) + 25, fishType, physicsWorld, true, fishBodyData,false,counter);
            fish.setCurrentTileIndex(0);
        }
        return fish;
    }


    /**
     * Called when a Bullet is sent to the pool
     */
    @Override
    protected void onHandleRecycleItem(Fish pItem) {
        super.onHandleRecycleItem(pItem);
    }


    /**
     * Called just before a Bullet is returned to the caller, this is where you write your initialize code
     * i.e. set location, rotation, etc.
     */
    @Override
    protected void onHandleObtainItem(Fish pItem) {
        pItem.getCurrentBody().setActive(true);
    }
}
