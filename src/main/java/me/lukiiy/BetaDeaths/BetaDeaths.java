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
    public static BetaDeaths inst;
    public static Configuration config;
    public static Logger logger;

    @Override
    public void onEnable() {
        inst = this;
        PluginManager pl = getServer().getPluginManager();
        logger = getServer().getLogger();

        pl.registerEvent(Event.Type.ENTITY_DAMAGE, new Listener(), Event.Priority.Lowest, this);
        pl.registerEvent(Event.Type.ENTITY_DEATH, new Listener(), Event.Priority.Normal, this);
        getCommand("betadeaths").setExecutor(new ReloadCMD());

        if (mcVersion() > 173) {
            logger.warning("This plugin will be disabled due to death messages being added in b1.8.");
            pl.disablePlugin(this);
        }

        config();
    }

    @Override
    public void onDisable() {}

    public static String get(String path) {return config.getString(path);}

    public void config() { // todo
        config = getConfiguration();
        config.load();
        config.getString("contact", "(p) was pricked to death");
        config.getString("attack", "(p) was slain by (e)");
        config.getString("attack_projectile", "(p) was shot by (e)");
        config.getString("fall", "(p) fell from a high place");
        config.getString("hard_fall", "(p) hit the ground too hard");
        config.getString("fire", "(p) went up in flames");
        config.getString("burn", "(p) burned to death");
        config.getString("lava", "(p) tried to swim in lava");
        config.getString("void", "(p) fell out of the world");
        config.getString("suicide", "(p) was killed");
        config.getString("drown", "(p) drowned");
        config.getString("lightning", "(p) was struck by lightning");
        config.getString("suffocation", "(p) suffocated in a wall");
        config.getString("explosion", "(p) was blown up by (e)");
        config.getString("explosion_block", "(p) blew up");
        config.getString("void", "(p) fell out of the world");
        config.getString("default_cause", "(p) died");
        config.getString("unknownEntity", "(p) unknown");
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