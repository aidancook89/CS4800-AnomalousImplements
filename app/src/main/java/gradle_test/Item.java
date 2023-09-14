package gradle_test;

import gradle_test.Structure;
import gradle_test.App;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Item {
    public String tag;
    public String type;

    public int customModelData = 1;

    public String name = "default";
    public String nameColor = "white"; // Can be hex code
    public boolean nameBold = false;
    public boolean nameItalic = false;
    public boolean nameUnderlined = false;
    public boolean nameStrikethrough = false;
    public boolean nameObfuscated = false;

    public String lore = "default";
    public String loreColor = "white";
    public boolean loreBold = false;
    public boolean loreItalic = false;
    public boolean loreUnderlined = false;
    public boolean loreStrikethrough = false;
    public boolean loreObfuscated = false;

    public String[] enchantmentsArray;
    public String enchantments = "";

    public String attributeModifiersArray;
    public String attributeModifiers = "";


    public Item(String type) {
		this.type = type;
    }

	public void updateName(String name, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.name = name;
		nameColor = color;
		nameBold = bold;
		nameItalic = italic;
		nameUnderlined = underlined;
		nameStrikethrough = strikethrough;
		nameObfuscated = obfuscated;
	}

	public void updateLore(String lore, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.lore = lore;
		loreColor = color;
		loreBold = bold;
		loreItalic = italic;
		loreUnderlined = underlined;
		loreStrikethrough = strikethrough;
		loreObfuscated = obfuscated;
	}

	public void buildTag() {
		String nameAppend = String.format(
            "{Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":%b,\"italic\":%b,\"underlined\":%b,\"strikethrough\":%b,\"obfuscated\":%b}',", 
            name, nameColor, nameBold, nameItalic, nameUnderlined, nameStrikethrough, nameObfuscated    
        );
        String loreAppend = String.format(
            "Lore:['{\"text\":\"%s\",\"color\":\"%s\",\"bold\":%b,\"italic\":%b,\"underlined\":%b,\"strikethrough\":%b,\"obfuscated\":%b}']},",
            lore, loreColor, loreBold, loreItalic, loreUnderlined, loreStrikethrough, nameObfuscated 
        );
        String customModelDataAppend = String.format("CustomModelData:%d,", customModelData);
        String enchantmensAppend = String.format("Enchantments:[%s],", enchantments);
        String attributeModifiersAppend = String.format("AttributeModifiers:[]} 1", attributeModifiers);

        tag = "{display:" + nameAppend + loreAppend + customModelDataAppend + enchantmensAppend + attributeModifiersAppend;
	}

	public String getGiveCommand() {
		buildTag();
		return "give @a " + type + tag; 
	} 

    /* 
    {id:"minecraft:protection",lvl:1s}
    {id:"minecraft:fire_protection",lvl:1s}
    {id:"minecraft:feather_falling",lvl:1s}
    {id:"minecraft:blast_protection",lvl:1s}
    {id:"minecraft:projectile_protection",lvl:1s}
    {id:"minecraft:respiration",lvl:1s}
    {id:"minecraft:aqua_affinity",lvl:1s}
    {id:"minecraft:thorns",lvl:1s}
    {id:"minecraft:depth_strider",lvl:1s}
    {id:"minecraft:frost_walker",lvl:1s}
    {id:"minecraft:binding_curse",lvl:1s}
    {id:"minecraft:sharpness",lvl:1s}
    {id:"minecraft:smite",lvl:1s}
    {id:"minecraft:bane_of_arthropods",lvl:1s}
    {id:"minecraft:knockback",lvl:1s}
    {id:"minecraft:fire_aspect",lvl:1s}
    {id:"minecraft:looting",lvl:1s}
    {id:"minecraft:sweeping",lvl:1s}
    {id:"minecraft:efficiency",lvl:1s}
    {id:"minecraft:silk_touch",lvl:1s}
    {id:"minecraft:unbreaking",lvl:1s}
    {id:"minecraft:fortune",lvl:1s}
    {id:"minecraft:power",lvl:1s}
    {id:"minecraft:punch",lvl:1s}
    {id:"minecraft:flame",lvl:1s}
    {id:"minecraft:infinity",lvl:1s}
    {id:"minecraft:luck_of_the_sea",lvl:1s}
    {id:"minecraft:lure",lvl:1s}
    {id:"minecraft:loyalty",lvl:1s}
    {id:"minecraft:impaling",lvl:1s}
    {id:"minecraft:riptide",lvl:1s}
    {id:"minecraft:channeling",lvl:1s}
    {id:"minecraft:mending",lvl:1s}
    {id:"minecraft:vanishing_curse",lvl:1s}
    {id:"minecraft:multishot",lvl:1s}
    {id:"minecraft:piercing",lvl:1s}
    {id:"minecraft:quick_charge",lvl:1s}
    {id:"minecraft:soul_speed",lvl:1s}
    {id:"minecraft:swift_sneak",lvl:1s}
      
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
}

