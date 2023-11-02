package aidp;

import java.util.ArrayList;


public class EntityFactory {

    public static ArrayList<Entity> list = new ArrayList<Entity>();

    public static void create(int count) {

        Entity newEnt = null;
        for (int i = 0; i < count; i++) {
            newEnt = EntityBuilder.newEntity();
            list.add(newEnt);
        }
    }

}
