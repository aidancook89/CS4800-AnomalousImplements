package gradle_test;

public class ItemBuilder {


    public ItemBuilder(String type, String theme, int rarity) {

        Item testItem = new Item("stone_sword", 0);
        testItem.updateName("Sword of Flight", "dark_blue", true, true, false, false, false);
        testItem.updateLore("Although stone, this sword is a powerful additional to any players toolkit.", "white", false, true, false, false, false);
        testItem.addEnchantment("sharpness", 1);
        testItem.addEnchantment("knockback", 1);
        testItem.addPotionEffect("speed", 1, true);
        testItem.addPotionEffect("jump_boost", 1, true);
        testItem.buildTag();
        //System.out.println(testItem.getPotionEffectString());

        // GIVE ITEM ON LOAD
        Structure.writeTo(App.f_loadmcfunction, "\n" + testItem.getGiveCommand());

        // ADD POTION EFFECTS TO item_tick
        Structure.writeTo(App.f_item_tickmcfunction, testItem.getPotionEffectString());
    }
}