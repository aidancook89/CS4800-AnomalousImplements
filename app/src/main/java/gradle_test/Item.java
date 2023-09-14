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
    public String itemTemplate = "" 
    + "{display:"
    + "{Name:'{\"text\":\"%s\",\"color\":\"white\",\"bold\":true,\"italic\":true}'," 
    + "Lore:['{\"text\":\"A staff that enables short range flight.\",\"italic\":true}']},"
    + "CustomModelData:1," 
    + "Enchantments:[{id:\"minecraft:vanishing_curse\",lvl:1s}]}";

    public String tag;

    public Item(String name) {
        tag = String.format(itemTemplate, name);
        System.out.println(tag);
    }
}


/*
{
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "item",
          "name": "minecraft:stick",
          "functions": [
            {
              "function": "set_nbt",
              "tag": "{display:{Name:'{\"text\":\"Staff of Flight\",\"color\":\"white\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"A staff that enables short range flight.\",\"italic\":true}']},CustomModelData:1,Enchantments:[{id:\"minecraft:vanishing_curse\",lvl:1s}]}"
            }
          ]
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:random_chance",
          "chance": 0.5
        }
      ]
    }
  ]
}
*/