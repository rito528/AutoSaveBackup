package com.gmail.rotoyutoriapp

import org.bukkit.command.{Command, CommandSender}
import org.bukkit.plugin.java.JavaPlugin

class AutoSaveBackup extends JavaPlugin {

  private val save = new Save(this)

  override def onEnable(): Unit = {
    super.onEnable()
    com.gmail.rotoyutoriapp.getConfig.config = getConfig
    saveDefaultConfig()
    save.start()
    getLogger.info("AutoSaveBackup enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    save.stop()
    getLogger.info("AutoSaveBackup disabled.")
  }

  @Override
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    super.onCommand(sender, command, label, args)
    if (label.equalsIgnoreCase("asb")) {
      args(0) match {
        case "save" =>
          save.saveWorld()
          return true
        case "help" =>
          sender.sendMessage("+-----------------------------------+")
          sender.sendMessage("コマンド一覧")
          sender.sendMessage("/asb save - ワールドセーブを行います。")
          sender.sendMessage("+-----------------------------------+")
          return true
      }
    }
    false
  }

}
