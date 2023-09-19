package gradle_test;

public class ItemBuilder {


    public ItemBuilder(String type, String theme, int rarity) {
        Item testItem = new Item("stone_sword", 0);
        testItem.updateName("Test Sword", "white", true, true, false, false, false);
        testItem.updateLore("Test Lore", "dark_red", false, true, false, false, false);
        testItem.addEnchantment("knockback", 1);
        testItem.addEnchantment("unbreaking", 1);
        testItem.addPotionEffect("speed", 0, true);
        testItem.addPotionEffect("jump_boost", 0, true);
        testItem.buildTag();
        //System.out.println(testItem.getPotionEffectString());

        // GIVE ITEM ON LOAD
        Structure.writeTo(App.f_loadmcfunction, "\n" + testItem.getGiveCommand());

        // ADD POTION EFFECTS TO item_tick
        Structure.writeTo(App.f_item_tickmcfunction, testItem.getPotionEffectString());
    }
}