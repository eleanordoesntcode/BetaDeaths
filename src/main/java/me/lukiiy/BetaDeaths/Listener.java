package me.lukiiy.BetaDeaths;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class Listener extends EntityListener {
    public void onEntityDamage(EntityDamageEvent ev) {
        if (!(ev.getEntity() instanceof Player)) return;
        if (!(ev instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
        Player p = (Player) e.getEntity();
        Entity damager = e.getDamager();
        Util.lastDamager.put(p, damager);
    }

    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        EntityDamageEvent.DamageCause c = p.getLastDamageCause().getCause();

        // todo
        String reason;
        String damager = BetaDeaths.get("unknownEntity");
        Entity lastDamager = Util.lastDamager.get(p);

        if (lastDamager != null) {
            if (lastDamager instanceof Projectile) lastDamager = ((Projectile) lastDamager).getShooter();
            if (lastDamager instanceof LivingEntity) {
                damager = (lastDamager instanceof Player) ? ((Player) lastDamager).getName() : Util.getEntityName(lastDamager);
                if (lastDamager instanceof Creeper || lastDamager instanceof Explosive) c = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
            }
        }

        // todo!!!
        switch (c) {
            case CONTACT:
                reason = BetaDeaths.get("contact");
                break;
            case ENTITY_ATTACK:
                reason = BetaDeaths.get("attack");
                break;
            case FALL:
                reason = BetaDeaths.get("fall");
                if (p.getFallDistance() < 5f) reason = BetaDeaths.get("hard_fall");
                break;
            case FIRE:
            case FIRE_TICK:
                if (p.getWorld().getBlockAt(p.getLocation()).getType() == Material.FIRE) reason = BetaDeaths.get("fire");
                else reason = BetaDeaths.get("burn");
                break;
            case LAVA:
                reason = BetaDeaths.get("lava");
                break;
            case VOID:
                reason = BetaDeaths.get("void");
                break;
            case SUICIDE:
                reason = BetaDeaths.get("suicide");
                break;
            case DROWNING:
                reason = BetaDeaths.get("drown");
                break;
            case LIGHTNING:
                reason = BetaDeaths.get("lightning");
                break;
            case PROJECTILE:
                reason = BetaDeaths.get("attack_projectile");
                break;
            case SUFFOCATION:
                reason = BetaDeaths.get("suffocation");
                break;
            case BLOCK_EXPLOSION:
                reason = BetaDeaths.get("explosion_block");
                break;
            case ENTITY_EXPLOSION:
                reason = BetaDeaths.get("explosion");
                break;
            default:
                reason = BetaDeaths.get("default_cause");
                break;
        }

        if (reason.isEmpty()) return;
        reason = reason.replace("%s", damager);
        Bukkit.getServer().broadcastMessage(p.getName() + " " + reason);
        Bukkit.getServer().getLogger().info(p.getName() + " " + reason);
    }
}
