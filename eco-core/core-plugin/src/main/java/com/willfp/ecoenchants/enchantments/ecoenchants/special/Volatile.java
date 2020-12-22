package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.eco.util.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Volatile extends EcoEnchant {
    public Volatile() {
        super(
                "volatile", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity uncastAttacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastAttacker instanceof Player)) return;

        Player attacker = (Player) uncastAttacker;

        if (ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown(attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(attacker, event.getEntity().getLocation())) return;
        if (breakblocks && !AntigriefManager.canBreakBlock(attacker, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation()))) {
            return;
        }

        Location explosionLoc = victim.getEyeLocation();

        victim.getWorld().createExplosion(explosionLoc, power, fire, breakblocks);
    }
}