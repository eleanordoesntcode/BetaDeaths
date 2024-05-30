package me.lukiiy.BetaDeaths;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetaDeaths extends JavaPlugin {
    public static Configuration config;
    public static Logger logger;

    @Override
    public void onEnable() {
        PluginManager pl = getServer().getPluginManager();
        logger = getServer().getLogger();

        pl.registerEvent(Event.Type.ENTITY_DAMAGE, new Listener(), Event.Priority.Lowest, this);
        pl.registerEvent(Event.Type.ENTITY_DEATH, new Listener(), Event.Priority.Normal, this);

        if (mcVersion() > 173) {
            Logger l = getServer().getLogger();
            l.warning("This plugin will be disabled due to death messages being added in b1.8.");
            pl.disablePlugin(this);
        }

        config();
    }

    @Override
    public void onDisable() {}

    public static String get(String path) {return config.getString(path);}

    private void config() { // todo
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

    private int mcVersion() { // workaround ;)
        String version = Bukkit.getServer().getVersion();
        Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d)\\.(\\d)\\)").matcher(version);
        if (matcher.find()) {
            String patch = matcher.group(3) != null ? matcher.group(3) : "0";
            return Integer.parseInt(matcher.group(1) + matcher.group(2) + patch);
        }
        return 0;
    }
}