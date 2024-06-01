package me.lukiiy.BetaDeaths;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Util {
    // todo
    public static HashMap<Player, Entity> lastDamager = new HashMap<>();
    public static String getEntityName(Entity e) {
        return e.getClass().getSimpleName().replace("Craft", "");
    }
}