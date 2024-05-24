package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class JumpEffect extends Task
{
    private Player player;
    private int t = 0;

    public JumpEffect(Plugin _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 5, 0.05, 0.05, 0.05, new Particle.DustOptions(Color.WHITE, 1f));

        if(player.getVelocity().getY() < 0)
            cancel = true;
    }

    public Player getPlayer() { return player; }
    
}
