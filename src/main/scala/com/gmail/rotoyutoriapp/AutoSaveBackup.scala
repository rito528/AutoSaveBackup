package com.gmail.rotoyutoriapp

import org.bukkit.plugin.java.JavaPlugin

class AutoSaveBackup extends JavaPlugin {

  override def onEnable(): Unit = {
    super.onEnable()
    com.gmail.rotoyutoriapp.getConfig.config = getConfig
    saveDefaultConfig()
    new Save(this).start()
    getLogger.info("AutoSaveBackup enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    new Save(this).stop()
    getLogger.info("AutoSaveBackup disabled.")
  }

}
