package cn.fusionfish.sakuraplayer;

import cn.fusionfish.sakuraplayer.listeners.PlayerListener;
import cn.fusionfish.sakuraplayer.player.PlayerManager;
import cn.fusionfish.sakuraplayer.player.SakuraPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * 插件主类
 * @author JeremyHu
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private PlayerManager manager;

    public PlayerManager getManager() {
        return manager;
    }

    /**
     * 获取插件实例
     * @return 插件实例
     */
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        initFile();

        manager = new PlayerManager();
        manager.getPlayers().stream()
                .map(SakuraPlayer::getName)
                .map(s -> " - " + s)
                .forEach(Main::log);
    }

    /**
     * 向服务器后台输出信息
     * @param string 信息
     */
    public static void log(String string) {
        Main.getInstance().getLogger().info(string);
    }

    /**
     * 初始化文件
     */
    private void initFile() {
        if (!getDataFolder().exists()) {
            //插件文件夹不存在时
            log("正在创建插件文件夹...");
            getDataFolder().mkdir();
        }

        final File playersFolder = new File(getDataFolder(), "players");
        if (!playersFolder.exists()) {
            //玩家数据文件夹不存在
            log("正在创建玩家数据文件夹...");
            playersFolder.mkdir();
        }
    }

    @Override
    public void onDisable() {
        //保存所有数据
        manager.saveAll();
    }
}