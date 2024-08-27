package me.lukiiy.BetaDeaths;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BetaDeaths extends JavaPlugin {
    @Getter private static BetaDeaths instance;

    public Configuration config;
    private static Logger log;

    public boolean dcBridgeHook;
    private Map<Entity, Entity> lastDamager;

    @Override
    public void onEnable() {
        instance = this;
        log = getServer().getLogger();
        setupConfig();

        lastDamager = new HashMap<>();
        PluginManager pl = getServer().getPluginManager();

        pl.registerEvent(Event.Type.ENTITY_DAMAGE, new Listener(), Event.Priority.Lowest, this);
        pl.registerEvent(Event.Type.ENTITY_DEATH, new Listener(), Event.Priority.Normal, this);

        getCommand("betadeaths").setExecutor(new ReloadCMD());

        if (GenericUtils.getMCVersion(getServer().getVersion()) > 173) {
            log.warning("This plugin will be disabled due to death messages being added in b1.8.");
            pl.disablePlugin(this);
            return;
        }

        dcBridgeHook = BetaDeaths.confBool("hooks.dcBridge") && pl.isPluginEnabled("DiscordBridge");
    }

    @Override
    public void onDisable() {}

    public static void log(String info) {log.info(info);}

    // Config
    public void setupConfig() {
        config = getConfiguration();
        config.load();

        String m = "msgs.";
        config.getString(m + "contact", "(victim) was pricked to death");
        config.getString(m + "attack", "(victim) was slain by (damager)");
        config.getString(m + "attack_projectile", "(victim) was shot by (damager)");
        config.getString(m + "fall", "(victim) fell from a high place");
        config.getString(m + "hard_fall", "(victim) hit the ground too hard");
        config.getString(m + "fire", "(victim) went up in flames");
        config.getString(m + "burn", "(victim) burned to death");
        config.getString(m + "lava", "(victim) tried to swim in lava");
        config.getString(m + "void", "(victim) fell out of the world");
        config.getString(m + "suicide", "(victim) was killed");
        config.getString(m + "drown", "(victim) drowned");
        config.getString(m + "lightning", "(victim) was struck by lightning");
        config.getString(m + "suffocation", "(victim) suffocated in a wall");
        config.getString(m + "explosion", "(victim) was blown up by (damager)");
        config.getString(m + "explosion_block", "(victim) blew up");
        config.getString(m + "void", "(victim) fell out of the world");
        config.getString(m + "default_cause", "(victim) died");
        config.getString(m + "unknownEntity", "(victim) unknown");

        config.getBoolean("broadcastTamedMobsDeaths", true);
        config.getBoolean("hooks.dcBridge", true);

        config.save();
    }

    public static String getDeathMsg(String path) {return instance.config.getString("msgs." + path);}
    public static boolean confBool(String path) {return instance.config.getBoolean(path, false);}
    
    // Entity Damage By Entity Cache
    public static Entity getEntityLastDamager(Entity entity) {
        return instance.lastDamager.get(entity);
    }

    public static void setEntityLastDamager(Entity entity, Entity damager) {
        instance.lastDamager.put(entity, damager);
    }
}