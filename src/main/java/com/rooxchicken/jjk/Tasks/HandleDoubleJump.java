package com.rooxchicken.jjk.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.ProjectionSorcery;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HandleDoubleJump extends Task implements Listener
{
    private ArrayList<Player> jumps;

    public HandleDoubleJump(Plugin _plugin)
    {
        super(_plugin);
        tickThreshold = 1;

        jumps = new ArrayList<Player>();
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.isOnGround())
            {
                player.setAllowFlight(true);
                if(jumps.contains(player))
                    jumps.remove(player);
            }
            
            if(!player.isOnGround() && player.getAllowFlight() && player.getVelocity().getY() < -0.2)
            {
                player.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void activateDoubleJump(PlayerToggleFlightEvent event)
    {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        player.setFlying(false);
        player.setAllowFlight(false);

        if(!JJKPlugin.useCursedEnergy(player, 60))
            return;

        Vector direction = player.getLocation().getDirection().multiply(2.2);
        player.setVelocity(player.getVelocity().add(direction));

        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 100, 0.5f, 0.2f, 0.5f, new Particle.DustOptions(Color.WHITE, 1f));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);

        jumps.add(player);
        JJKPlugin.tasks.add(new JumpEffect(null, player));
    }

    @EventHandler
    public void cancelFallIfBoots(EntityDamageEvent event)
    {
        if(event.getCause() != DamageCause.FALL || event.getEntityType() != EntityType.PLAYER)
            return;

            for(Task t : JJKPlugin.tasks)
            {
                if(t instanceof InfinityBarrier)
                    if(((InfinityBarrier)t).player == event.getEntity())
                    {
                        return;
                    }
            }

        event.setCancelled(jumps.contains(event.getEntity()));
    }
}
