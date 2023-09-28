package aidp;

import java.util.ArrayList;

import java.nio.file.Path;

public class SwordBuilder {

    private Sword sword; 

    private String requestJson = "{"
    + "name: <string>," 
    + "color: <hexcode>," 
    + "lore: <string>"
    + "enchantments: [<unbreaking, knockback, sharpness, fire_aspect, looting>],"
    + "player_effects: [<speed, slowness, jump_boost, levitation>]"
    + "player_particles: [<cloud,flame,barrier,bubble,dust,enchant>]"
    + "entity_effects: [<speed, slowness, jump_boost, levitation>]"
    + "entity_particles: [<cloud,flame,barrier,bubble,dust,enchant>]"
    + "} ";

    private String restrictions = ""
    + "enchantments: pick <= 2,"
    + "player_effects: pick <= 2," 
    + "player_particles: pick <= 2," 
    + "entity_effects: pick <= 1,"
    + "entity_particles: pick <= 2";

    public SwordBuilder(int id, int rarity, String theme) {
         
        Request request = RequestHandler.makeRequest(
            "Provide me with a JSON in the following format: " + requestJson + restrictions,
            String.format("Interesting sword with theme: %s", theme), 
            0.9
        );        
        System.out.println(request.getContentString());
        System.out.println(request.getAsArrayList("player_effects"));

        sword = new Sword("wooden_sword", id, rarity);
        sword.setName(request.getAsString("name"), request.getAsString("color"));
        sword.setLore(request.getAsString("lore"));
        sword.addEnchantment("unbreaking", 1);
        addPlayerPotionEffect(sword, request.getAsArrayList("player_effects"));

        sword.setEntityParticle("cloud");
        sword.setPlayerParticle("flame");
        sword.buildTag();

        // ADD ITEM TO DEAL DAMAGE
        Structure.writeToLine(App.f_deal_damagemcfunction, sword.getDealDamageString(), 2);

        // CREATE DAMAGE FUNCTION
        Path attackFunction = Structure.newDir(App.d_swords, sword.getAttackFunctionName() + ".mcfunction", true);
        Structure.writeTo(attackFunction, sword.getAttackFunctionString(), true);

        // GIVE ITEM ON LOAD
        Structure.writeTo(App.f_loadmcfunction, "\n" + sword.getGiveCommand(), true);

        // ADD POTION EFFECTS TO item_tick
        Structure.writeTo(App.f_item_tickmcfunction, sword.getPlayerPotionEffectString(), true);
    }

    public void addPlayerPotionEffect(Sword sword, ArrayList<String> list) {
        for (String item : list) {
            sword.addPlayerPotionEffect(item, 0, false);
        }
    }

    public Sword getSword() {
        return sword;
    }
}

/*

{id:"minecraft:binding_curse",lvl:1s} 2
{id:"minecraft:sharpness",lvl:1s} -2 -4 -6 -8 -10
{id:"minecraft:smite",lvl:1s}
{id:"minecraft:bane_of_arthropods",lvl:1s}
{id:"minecraft:knockback",lvl:1s}
{id:"minecraft:fire_aspect",lvl:1s}
{id:"minecraft:looting",lvl:1s} -3 -6 -9
{id:"minecraft:sweeping",lvl:1s} -
{id:"minecraft:unbreaking",lvl:1s} -3 -6 -9
{id:"minecraft:mending",lvl:1s} -15
{id:"minecraft:vanishing_curse",lvl:1s} 5
    
{AttributeName:"generic.max_health",Name:"generic.max_health",Amount:1,Operation:2,UUID:[I;-2048207306,2079277823,-1829575274,1514256703],Slot:"mainhand"}
{AttributeName:"generic.follow_range",Name:"generic.follow_range",Amount:1,Operation:2,UUID:[I;-1071343243,-632797570,-1634786299,-992118208],Slot:"offhand"}
{AttributeName:"generic.knockback_resistance",Name:"generic.knockback_resistance",Amount:1,Operation:2,UUID:[I;-576070441,-180925074,-1983950958,2016677081],Slot:"feet"}
{AttributeName:"generic.movement_speed",Name:"generic.movement_speed",Amount:1,Operation:2,UUID:[I;-1616763457,652756468,-1255044040,495331673],Slot:"legs"}
{AttributeName:"generic.attack_damage",Name:"generic.attack_damage",Amount:1,Operation:2,UUID:[I;1039631330,-1392164380,-1685550125,2052723954],Slot:"chest"}
{AttributeName:"generic.armor",Name:"generic.armor",Amount:1,Operation:2,UUID:[I;-966410269,367413497,-1322336267,-879490572],Slot:"head"}
{AttributeName:"generic.armor_toughness",Name:"generic.armor_toughness",Amount:1,Operation:2,UUID:[I;1853727647,-1374141926,-1240270752,1854574506]}
{AttributeName:"generic.attack_speed",Name:"generic.attack_speed",Amount:1,Operation:2,UUID:[I;333910313,-860730335,-1454187290,-1632758482]}" 
{AttributeName:"generic.luck",Name:"generic.luck",Amount:1,Operation:2,UUID:[I;555339872,202067683,-1188013754,-448789942]}
 

*/