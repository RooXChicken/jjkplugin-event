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

public class Red extends Task
{
    private Player player;
    private Location start;
    private int t = 0;

    private double size = 0.2;
    private int count = 10;

    private double[] cacheX;
    private double[] cacheZ;

    private Color[] colors;

    public Red(Plugin _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        start = player.getLocation().clone().add(0, 1, 0);
        tickThreshold = 1;

        cacheX = new double[count];
        cacheZ = new double[count];

        for(int i = 0; i < count; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }

        colors = new Color[] {Color.RED, Color.ORANGE, Color.RED};
        player.getWorld().playSound(start, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void run()
    {
        start.add(start.getDirection().multiply(1.5));

        for(Object e : JJKPlugin.getNearbyEntities(start, (int)Math.ceil(size)))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                double distance = 3-JJKPlugin.ClampD(start.distance(entity.getLocation()), 0, 3);
                Bukkit.getLogger().info("" + distance);
                Vector move = start.clone().subtract(entity.getLocation()).toVector().multiply(distance*-2);
                move.setY(Math.abs(move.getY())/4);
                entity.setVelocity(move); //(start.clone().getDirection().multiply(-distance/3).add(new Vector(0, 1, 0)));
                if(entity instanceof LivingEntity)
                    ((LivingEntity)entity).damage(28);
                player.getWorld().playSound(start, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1.2f);
            }
        }

        double offset = 0.05;
        for(int i = 0; i < count; i++)
        {
            double sphereOffset = Math.sin(Math.toRadians((i * (90.0/count) ) * 2 - 90.0));
            double sphereOffsetXZ = Math.sin(Math.toRadians((i * (90.0/count) ) * 2));
            //double yDirOffset = Math.sin(Math.toRadians((i*(180.0/count))) * (90.0/count));
            for(int k = 0; k < count; k++)
            {
                Location particlePos = start.clone();
                double xOffset = 0;
                double zOffset = 0;

                if(cacheX[k] == -1)
                {
                    double rad = Math.toRadians(k*(360.0/count));
                    xOffset = Math.sin(rad);
                    zOffset = Math.cos(rad);
                    
                    cacheX[k] = xOffset;
                    cacheZ[k] = zOffset;
                }
                else
                {
                    xOffset = cacheX[k];
                    zOffset = cacheZ[k];
                }

                player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(colors[(int)(Math.random()*3)], 1f));
                if(particlePos.getBlock().getType().isSolid())
                    cancel = true;
            }
        }

        t++;

        if(t > 20)
            cancel = true;
    }
    
}
