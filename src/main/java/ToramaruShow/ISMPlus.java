package ToramaruShow;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class ISMPlus extends JavaPlugin {
    private static Plugin plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Long[] ticks = new Long[3];//0-2 reelstoptick
        String[] soundset = new String[7];//0 mcid/1 allplayer(t/f)/2 volume/3 pitch/4-6 soundname
        boolean onesound = false;
        for (int i = 0; ticks.length > i; i++) {
            ticks[i] = Long.valueOf(args[i]);
        }
        try {
            for (int i = 0; soundset.length > i; i++) {
                soundset[i] = args[i + 3];
            }
            if (Objects.equals(args[9], null)) {
                onesound = true;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            onesound = true;
        }
        Player p = Bukkit.getPlayer(soundset[0]);
        Server s = p.getServer();
        boolean ap = Boolean.parseBoolean(soundset[1]);
        int vo = Integer.parseInt(soundset[2]), pi = Integer.parseInt(soundset[3]);
        if (onesound) {
            for (int i = 0; 3 > i; i++) {
                new SoundScheduler(s, p, ap, vo, pi, soundset[4]).runTaskTimer(plugin, ticks[i], 0);
            }
        } else {
            for (int i = 0; 3 > i; i++) {
                new SoundScheduler(s, p, ap, vo, pi, soundset[i + 4]).runTaskTimer(plugin, ticks[i], 0);
            }
        }
        return true;
    }

    @Override
    public void onEnable() {
        plugin = this;
    }
}

class SoundScheduler extends BukkitRunnable {
    private final Player p;
    private final Server s;
    private final boolean ap;
    private final String SoundSet;
    private final int vo, pi;
    private final int[] loc = new int[3];

    public SoundScheduler(Server ser, Player mcid, boolean ap, int vo, int pi, String sn) {
        this.s = ser;
        this.p = mcid;
        this.ap = ap;
        this.SoundSet = sn;
        this.vo = vo;
        this.pi = pi;
        loc[0] = p.getLocation().getBlockX();
        loc[1] = p.getLocation().getBlockY();
        loc[2] = p.getLocation().getBlockZ();
    }

    @Override
    public void run() {
        if (ap) {
            this.s.dispatchCommand(this.s.getConsoleSender(), "playsound " + SoundSet + " master @a " + loc[0] + " " + loc[1] + " " + loc[2] + " " + vo + " " + pi);
        } else {
            this.s.dispatchCommand(this.s.getConsoleSender(), "playsound " + SoundSet + " master " + p.getName() + " " + loc[0] + " " + loc[1] + " " + loc[2] + " " + vo + " " + pi);
        }
        cancel();
    }
}

