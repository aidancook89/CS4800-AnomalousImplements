package aidp;

public class EntityBuilder {
    
    public EntityBuilder(String type, String lootTable) {
        Entity testEntity = new Entity(type, lootTable);
        testEntity.setEntityAttributes(0, 0, 0, 0, 1, 1, 0, 20);
        testEntity.setGenericAttributes(0.5, 3, 3, 5, 1, 1);
        testEntity.updateEntityName("Mythical Skeleton", "gold", true, false, true, false, false);
        testEntity.addPotionEffects(8, 2, -1, 1);
        testEntity.buildTag();

        Structure.writeTo(App.f_loadmcfunction, "\n" + testEntity.getSpawnCommand(), true);
    }
}
