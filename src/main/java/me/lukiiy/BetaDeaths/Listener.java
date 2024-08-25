package me.lukiiy.BetaDeaths;

import me.lukiiy.discordBridge.DCBridge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class Listener extends EntityListener {
    public void onEntityDamage(EntityDamageEvent ev) {
        if (!(ev instanceof EntityDamageByEntityEvent) || !(isEntityCacheable(ev.getEntity()))) return;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;

        BetaDeaths.setEntityLastDamager(event.getEntity(), event.getDamager());
    }

    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        
        if (!isEntityCacheable(entity)) return;
        if (entity instanceof Tameable && !((Tameable) entity).isTamed()) return;

        String entityName = entity instanceof Player ? ((Player) entity).getName() : GenericUtils.getEntityName(entity);

        EntityDamageEvent.DamageCause cause = entity.getLastDamageCause().getCause();

        Entity lastDamager = BetaDeaths.getEntityLastDamager(entity);
        String damagerName = BetaDeaths.getDeathMsg("unknownEntity");


        if (lastDamager != null) {
            if (lastDamager instanceof Projectile) lastDamager = ((Projectile) lastDamager).getShooter();
            if (lastDamager instanceof LivingEntity) {
                damagerName = (lastDamager instanceof Player) ? ((Player) lastDamager).getDisplayName() : GenericUtils.getEntityName(lastDamager);
                if (lastDamager instanceof Creeper && cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) cause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
            }
        }

        String reason;
        switch (cause) {
            case CONTACT:
                reason = BetaDeaths.getDeathMsg("contact");
                break;
            case ENTITY_ATTACK:
                reason = BetaDeaths.getDeathMsg("attack");
                break;
            case FALL:
                reason = BetaDeaths.getDeathMsg("fall");
                if (entity.getFallDistance() < 5f) reason = BetaDeaths.getDeathMsg("hard_fall");
                break;
            case FIRE:
            case FIRE_TICK:
                if (entity.getWorld().getBlockAt(entity.getLocation()).getType() == Material.FIRE) reason = BetaDeaths.getDeathMsg("fire");
                else reason = BetaDeaths.getDeathMsg("burn");
                break;
            case LAVA:
                reason = BetaDeaths.getDeathMsg("lava");
                break;
            case VOID:
                reason = BetaDeaths.getDeathMsg("void");
                break;
            case SUICIDE:
                reason = BetaDeaths.getDeathMsg("suicide");
                break;
            case DROWNING:
                reason = BetaDeaths.getDeathMsg("drown");
                break;
            case LIGHTNING:
                reason = BetaDeaths.getDeathMsg("lightning");
                break;
            case PROJECTILE:
                reason = BetaDeaths.getDeathMsg("attack_projectile");
                break;
            case SUFFOCATION:
                reason = BetaDeaths.getDeathMsg("suffocation");
                break;
            case BLOCK_EXPLOSION:
                reason = BetaDeaths.getDeathMsg("explosion_block");
                break;
            case ENTITY_EXPLOSION:
                reason = BetaDeaths.getDeathMsg("explosion");
                break;
            default:
                reason = BetaDeaths.getDeathMsg("default_cause");
                break;
        }

        if (reason.isEmpty()) return;
        String msg = reason
                .replace("(victim)", entityName)
                .replace("(damager)", damagerName)
                .replace('&', '§');
        Bukkit.getServer().broadcastMessage(msg);
        BetaDeaths.log(msg);
        if (BetaDeaths.getInstance().dcBridgeHook) DCBridge.sendDCMsg(msg);
    }

    private boolean isEntityCacheable(Entity entity) {return entity instanceof Player || entity instanceof Tameable;}
}
