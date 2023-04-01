package me.renard.batonquifaisvoler;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("baton").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("baton")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack baton = new ItemStack(Material.STICK);
                baton.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
                player.getInventory().addItem(baton);
                player.sendMessage("§aVous avez reçu le bâton qui fait voler !");
            } else {
                sender.sendMessage("§cCette commande ne peut être exécutée que par un joueur !");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            ItemStack itemInHand = damager.getInventory().getItemInMainHand();

            if (itemInHand.getType() == Material.STICK && itemInHand.containsEnchantment(Enchantment.KNOCKBACK)) {
                Entity damagedEntity = event.getEntity();
                damagedEntity.setVelocity(damagedEntity.getVelocity().add(damagedEntity.getLocation().getDirection().multiply(1.5)));
                // Vérifie si l'entité touchée est une instance de LivingEntity
                if (damagedEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) damagedEntity;
                    // Ajoute un effet de lévitation à l'entité touchée
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 2));
                }
            }
        }
    }
}
