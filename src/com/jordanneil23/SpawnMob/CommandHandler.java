package com.jordanneil23.SpawnMob;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import com.jordanneil23.SpawnMob.Mob.MobException;
import com.jordanneil23.SpawnMob.TargetBlock;

import net.minecraft.server.EntitySlime;
import net.minecraft.server.EntityWolf;

/**
 *SpawnMob - Commands
 * @author jordanneil23
 */
public class CommandHandler{
    private SpawnMob plugin;
    private Mob kill;
    public void CommandListener(SpawnMob instance) {
        plugin = instance;
    }
    public void CommandListener(Mob instance) {
        kill = instance;
    }

    public boolean perform(CommandSender sender, Command command, String[] args) {
        int[] ignore = {8, 9};
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("spawnmob") || command.getName().toLowerCase().equalsIgnoreCase("sm") || command.getName().toLowerCase().equalsIgnoreCase("smob")) {
        	if (0 < args.length) {
            	if (!plugin.permissions){
            		if (!p.isOp()){
            			p.sendMessage("You can't do that.");
            			return false;
            		}
            	}
                if (args[0].equalsIgnoreCase("Kill")) {
                	Mob mob3 = Mob.fromName(args[1].equalsIgnoreCase("PigZombie") ? "PigZombie" : capitalCase(args[1]));
                    if (plugin.permissions){
                	if (!SpawnMob.Permissions.has(p, "spawnmob.kill")){
                        p.sendMessage("You are not authorized kill mobs.");
                        return false;
                	}
                    }
                    if (args[1].equalsIgnoreCase("All")){
                    	if (plugin.permissions){
                    	if (!(SpawnMob.Permissions.has(p, "spawnmob.kill.all"))){
            				p.sendMessage("You're not authorized to do that.");
            				return false;
                			}
                    	}
                        		p.sendMessage("Killed all mobs. (Not including wolves.)");
                        		kill.Kill(p.getWorld(), args[1]);
                                return true;
            		} else if (args[1].equalsIgnoreCase("Monsters")){
            			if (plugin.permissions){
            			if (!(SpawnMob.Permissions.has(p, "spawnmob.kill.monsters") || SpawnMob.Permissions.has(p, "spawnmob.kill.all"))){
                        p.sendMessage("You're not authorized to do that.");
        				return false;
            		}
            			}
            			p.sendMessage("Killed all monsters.");
            			kill.Kill(p.getWorld(), args[1]);
                        return true;
            		} else if (args[1].equalsIgnoreCase("Animals")){
            			if (plugin.permissions){
            			if (!(SpawnMob.Permissions.has(p, "spawnmob.kill.animals") || SpawnMob.Permissions.has(p, "spawnmob.kill.all"))){
            				p.sendMessage("You're not authorized to do that.");
            				return false;
            			}
            			}
            				p.sendMessage("Killed all animals. (Not including wolves.)");
            				kill.Kill(p.getWorld(), args[1]);
                            return true;
            		}
                    if (plugin.permissions){
                    if (!(SpawnMob.Permissions.has(p, "spawnmob.kill." + args[1]) || SpawnMob.Permissions.has(p, "spawnmob.kill.all"))){
        				p.sendMessage("You're not authorized to do that.");
        				return false;
            			} 
                    }
                    	if (mob3 == null && !(args[1].equalsIgnoreCase("Animals") || args[1].equalsIgnoreCase("Monsters") || args[1].equalsIgnoreCase("All"))) {
                            p.sendMessage("Invalid mob type.");
                            return false;
                        }
                    if (args[1].equalsIgnoreCase("Sheep") || args[1].equalsIgnoreCase("Squid")){
            			p.sendMessage("Killed all " + args[1] + ".");
            			kill.Kill(p.getWorld(), args[1]);
                        return true;
            		}
            		if (args[1].equalsIgnoreCase("Wolf") || args[1].equalsIgnoreCase("Wolves")){
            			p.sendMessage("Killed all wolves, including tamed ones.");
            			kill.Kill(p.getWorld(), args[1]);
                        return true;
            		}
                        p.sendMessage("Killed all " + args[1] + "s.");
                        kill.Kill(p.getWorld(), args[1]);
                        return true;
                } else if (args[0].equalsIgnoreCase("Undo")) {
                	if (plugin.permissions){
                	if (!SpawnMob.Permissions.has(p, "spawnmob.kill.all")) {
                    	p.sendMessage("You can't do that.");
                    	return false;
                    }
                	}
                        p.sendMessage("Undid SpawnMob");
                        kill.Kill(p.getWorld(), "all");
                        return true;
                }
                if (0 < args.length && args.length < 3) {
                    String[] split1 = args[0].split(":");
                    String[] split0 = new String[1];
                    LivingEntity spawned1 = null;
                    Mob mob2 = null;
                    Location loc = (new TargetBlock(p, 300, 0.2, ignore)).getTargetBlock().getLocation();
                    loc.setY(1 + loc.getY()); // TODO: Make mobs spawn on blocks, not in them. This is a quick and dirty partial solution.
                    if (args[0].equalsIgnoreCase("Wolf") && args[1].equalsIgnoreCase("Tamed")){
                    	if (plugin.permissions){
                    	if (!SpawnMob.Permissions.has(p, "spawnmob.wolf.tamed")){
                			p.sendMessage("You can't do that.");
                			return false;
                		}  
                    	}
                	 Wolf w = (Wolf) p.getWorld().spawnCreature(loc, CreatureType.WOLF);
   				     EntityWolf wolf = ((CraftWolf)  w).getHandle();
   				     wolf.a(p.getName()); //setOwner
   			         wolf.d(true); //owned
   				     w.setAngry(false);
   				     w.setHealth(10);
   				     p.sendMessage("You now have a tamed wolf!");
                	 return true;
                	}
                    if (split1.length == 1 && !split1[0].equalsIgnoreCase("Slime")) {
                        split0 = args[0].split(";");
                        split1[0] = split0[0];
                    }
                    if (split1.length == 2) {
                        args[0] = split1[0] + "";
                    }
                    Mob mob = Mob.fromName(split1[0].equalsIgnoreCase("PigZombie") ? "PigZombie" : capitalCase(split1[0]));
                    if (mob == null) {
                        p.sendMessage("Invalid mob type.");
                        return false;
                    }
                    if (plugin.permissions){
                    if(!(SpawnMob.Permissions.has(p, "spawnmob.spawnmob." + mob.getName().toLowerCase()) || SpawnMob.Permissions.has(p, "spawnmob." + mob.getName().toLowerCase()))){
                        p.sendMessage("You can't spawn this mob/mob type.");
                        return false;
                    }
                    }
                    LivingEntity spawned = null;
                    try {
                        spawned = mob.spawn(p, plugin, loc);
                    } catch (MobException e) {
                        p.sendMessage("Unable to spawn mob.");
                        return false;
                    }
                    if (split0.length == 2) {
                        mob2 = Mob.fromName(split0[1].equalsIgnoreCase("PigZombie") ? "PigZombie" : capitalCase(split0[1]));
                        if (mob2 == null) {
                            p.sendMessage("Invalid mob type.");
                            return false;
                        }
                        try {
                            spawned1 = mob2.spawn(p, plugin, loc);
                        } catch (MobException e) {
                            p.sendMessage("Unable to spawn mob.");
                            return false;
                        }
                        spawned1.setPassenger(spawned);
                    }
                    if (split1.length == 2 && mob.getName().toLowerCase() == "Slime") {
                        try {
                            ((EntitySlime) spawned).b(Integer.parseInt(split1[1]));
                        } catch (Exception e) {
                            p.sendMessage("Malformed size.");
                            return false;
                        }
                    }
                    if (args.length == 2) {
                        try {
                            for (int i = 1; i < Integer.parseInt(args[1]); i++) {
                                spawned = mob.spawn(p, plugin, loc);
                                if (split1.length > 1 && mob.getName().toLowerCase() == "Slime") {
                                    try {
                                        ((EntitySlime) spawned).b(Integer.parseInt(split1[1]));
                                    } catch (Exception e) {
                                        p.sendMessage("Malformed size.");
                                        return false;
                                    }
                                }
                                if (split0.length == 2) {
                                    if (mob2 == null) {
                                        p.sendMessage("Invalid mob type.");
                                        return false;
                                    }
                                    try {
                                        spawned1 = mob2.spawn(p, plugin, loc);
                                    } catch (MobException e) {
                                        p.sendMessage("Unable to spawn mob.");
                                        return false;
                                    }
                                    spawned1.setPassenger(spawned);
                                }
                            }
                            p.sendMessage(args[1] + " " + mob.getName().toLowerCase() + mob.s + (split0.length == 2 ? " riding " + mob2.getName().toLowerCase().toLowerCase() + mob2.s : "") + " spawned.");
                        } catch (MobException e1) {
                            p.sendMessage("Unable to spawn mobs.");
                            return false;
                        } catch (java.lang.NumberFormatException e2) {
                            p.sendMessage("Malformed integer.");
                            return false;
                        }
                    } else {
                        p.sendMessage(mob.getName().toLowerCase() + (split0.length == 2 ? " riding a " + mob2.getName().toLowerCase() : "") + " spawned.");
                    }
                    return true;
                } else {
                    p.sendMessage("/spawnmob <Mob Name> (Amount)");
                    p.sendMessage("/spawnmob kill <all-animals-monsters>");
                    p.sendMessage("/mspawn - Type for more info");
                    return false;
                }
            }else {
                p.sendMessage("/spawnmob <Mob Name> (Amount)");
                p.sendMessage("/spawnmob kill <all-animals-monsters>");
                p.sendMessage("/mspawn - Type for more info");
                return false;
            } 
        } else if (command.getName().toLowerCase().equalsIgnoreCase("mspawn")) {
            if (0 < args.length) {
                CreatureType mt = CreatureType.fromName(args[0].equalsIgnoreCase("PigZombie") ? "PigZombie" : capitalCase(args[0]));
                org.bukkit.block.Block blk = (new TargetBlock(p, 300, 0.2, ignore)).getTargetBlock();
                if (args[0].equalsIgnoreCase("Check") || args[0].equalsIgnoreCase("Info")) {
                	if (plugin.permissions){
                	if (!SpawnMob.Permissions.has(p, "spawnmob.mspawn.check")) {
                        p.sendMessage("You are not authorized to check a spawner.");
                        return false;
                    }
                	}
                    if (blk == null) {
                        p.sendMessage("You must be looking at a Mob Spawner.");
                        return false;
                    }
                    if (blk.getTypeId() != 52) {
                        p.sendMessage("You must be looking at a Mob Spawner.");
                        return false;
                    }
                    CreatureType mob1 = ((org.bukkit.block.CreatureSpawner) blk.getState()).getCreatureType();
                    int del = ((org.bukkit.block.CreatureSpawner) blk.getState()).getDelay();
                    p.sendMessage("This spawners mob type is " + mob1 + ".");
                    p.sendMessage("This spawners delay is set to " + del + ".");
                    return true;
                } else if (args[0].equalsIgnoreCase("Delay")) {
                    if (0 < args.length) {
                    	if (plugin.permissions){
                    	if (!SpawnMob.Permissions.has(p, "spawnmob.mspawn.delay")) {
                            p.sendMessage("You are not authorized to set a spawners delay.");
                            return false;
                        }
                    	}
                        if (blk == null) {
                            p.sendMessage("You must be looking at a Mob Spawner.");
                            return false;
                        }
                        if (blk.getTypeId() != 52) {
                            p.sendMessage("You must be looking at a Mob Spawner.");
                            return false;
                        }
                        if (checkIfNumber(args[1]) == false) {
                            p.sendMessage("You must enter a number.");
                            return false;
                        }
                        String Del1 = args[1];
                        int Del = Integer.parseInt(Del1);
                        ((org.bukkit.block.CreatureSpawner) blk.getState()).setDelay(Del);
                        p.sendMessage("This spawners delay is now set to " + Del + ".");
                        return true;

                    } else {
                        p.sendMessage("/mspawn delay <Number>");
                        p.sendMessage("Sets a mob spawners delay to spawn things.");
                        return false;
                    }
                }
                if (mt == null) {
                    p.sendMessage("Invalid mob type.");
                    return false;
                }
                if (plugin.permissions){
                if (!(SpawnMob.Permissions.has(p, "spawnmob.mspawn." + mt.getName().toLowerCase()) || SpawnMob.Permissions.has(p, "spawnmob.mspawn.allmobs"))) {
                    p.sendMessage("You are not authorized to do that.");
                    return false;
                }
                }
                    if (blk == null) {
                        p.sendMessage("You must be looking at a Mob Spawner.");
                        return false;
                    }
                if (blk.getTypeId() != 52) {
                    p.sendMessage("You must be looking at a Mob Spawner.");
                    return false;
                }
                ((org.bukkit.block.CreatureSpawner) blk.getState()).setCreatureType(mt);
                p.sendMessage("Mob spawner set as " + mt.getName().toLowerCase().toLowerCase() + ".");
            } else {
                p.sendMessage("/mspawn <Mob Name> - Set a mob spawner to spawn a mob");
                p.sendMessage("/mspawn check - See a spawners info.");
                p.sendMessage("/mspawn delay - Type for more info");
                return false;
            }

        }
		return false;
    }

 public boolean checkIfNumber(String in) {
        try {
            Long.parseLong(in);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
 
    private static String capitalCase(String s) {
        return s.toUpperCase().charAt(0) + s.toLowerCase().substring(1);
    }
}
