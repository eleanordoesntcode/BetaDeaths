package me.lukiiy.BetaDeaths;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class BetaDeaths extends JavaPlugin {
    public static Configuration config;

    @Override
    public void onEnable() {
        PluginManager pl = getServer().getPluginManager();
        pl.registerEvent(Event.Type.ENTITY_DAMAGE, new Listener(), Event.Priority.Lowest, this);
        pl.registerEvent(Event.Type.ENTITY_DEATH, new Listener(), Event.Priority.Normal, this);
        config();
    }

    @Override
    public void onDisable() {}

    public static String get(String path) {return config.getString(path);}

    public void config() {
        config = getConfiguration();
        config.load();
        config.getString("contact", "was pricked to death");
        config.getString("attack", "was slain by %s");
        config.getString("attack_projectile", "was shot by %s");
        config.getString("fall", "fell from a high place");
        config.getString("hard_fall", "hit the ground too hard");
        config.getString("fire", "went up in flames");
        config.getString("burn", "burned to death");
        config.getString("lava", "tried to swim in lava");
        config.getString("void", "fell out of the world");
        config.getString("suicide", "was killed");
        config.getString("drown", "drowned");
        config.getString("lightning", "was struck by lightning");
        config.getString("suffocation", "suffocated in a wall");
        config.getString("explosion", "was blown up by %s");
        config.getString("explosion_block", "blew up");
        config.getString("void", "fell out of the world");
        config.getString("default_cause", "died");
        config.getString("unknownEntity", "unknown");
        config.save();
    }
}