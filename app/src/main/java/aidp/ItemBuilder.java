package aidp;

import java.nio.file.Path;

public class ItemBuilder {
    public ItemBuilder(String type, String theme, int rarity) {
        Item testItem = new Item("wooden_sword", 0);
        testItem.setName("Hello", "white", true, true, false, false, false);
        testItem.setLore("Test Lore", "dark_red", false, true, false, false, false);
        testItem.addEnchantment("knockback", 1);
        testItem.addEnchantment("unbreaking", 1);
        testItem.addPlayerPotionEffect("speed", 0, true);
        testItem.addPlayerPotionEffect("jump_boost", 0, true);
        testItem.addEntityPotionEffect("levitation", 5, true);
        testItem.setEntityParticle("cloud");
        testItem.setPlayerParticle("flame");
        testItem.buildTag();

        // ADD ITEM TO DEAL DAMAGE
        Structure.writeToLine(App.f_deal_damagemcfunction, testItem.getDealDamageString(), 2);

        // CREATE DAMAGE FUNCTION
        Path attackFunction = Structure.newDir(App.d_swords, testItem.getAttackFunctionName() + ".mcfunction", true);
        Structure.writeTo(attackFunction, testItem.getAttackFunctionString(), true);

        // GIVE ITEM ON LOAD
        Structure.writeTo(App.f_loadmcfunction, "\n" + testItem.getGiveCommand(), true);

        // ADD POTION EFFECTS TO item_tick
        Structure.writeTo(App.f_item_tickmcfunction, testItem.getPlayerPotionEffectString(), true);
    }
}