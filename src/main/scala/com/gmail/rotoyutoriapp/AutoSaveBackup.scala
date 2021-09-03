package com.gmail.rotoyutoriapp

import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.plugin.java.JavaPlugin
import org.joda.time.{DateTime, DateTimeZone, Minutes}

import java.util

class AutoSaveBackup extends JavaPlugin {

  private val save = new Save(this)
  private val backup = new Backup(this)

  override def onEnable(): Unit = {
    super.onEnable()
    com.gmail.rotoyutoriapp.getConfig.config = getConfig
    saveDefaultConfig()
    save.start()
    backup.startBackup()
    if (com.gmail.rotoyutoriapp.getConfig.isRestart) {
      com.gmail.rotoyutoriapp.getConfig.getRestartTime.foreach(time => {
        val jst = new DateTime(DateTimeZone.forID("Asia/Tokyo"))
        val endTime = new DateTime(DateTimeZone.forID("Asia/Tokyo"))
        val end = new DateTime(endTime.withTime(time.toString.split(":")(0).toInt,time.toString.split(":")(1).toInt,0,0))
        var backupTime = ""
        if (Minutes.minutesBetween(jst,end).getMinutes <= 0) {
          backupTime = end.plusDays(1).toString("yyyy-MM-dd-HH-mm")
        } else {
          backupTime = end.toString("yyyy-MM-dd-HH-mm")
        }
        new Restart(this).restart(backupTime)
      })
    }
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
          if (!sender.hasPermission("asb.save")) {
            sender.sendMessage(ChatColor.RED + "実行権限がありません！")
            return true
          }
          save.saveWorld()
          return true
        case "backup" =>
          if (!sender.hasPermission("asb.backup")) {
            sender.sendMessage(ChatColor.RED + "実行権限がありません！")
            return true
          }
          new Backup(this).start()
          return true
        case "help" =>
          if (!sender.hasPermission("asb.help")) {
            sender.sendMessage(ChatColor.RED + "実行権限がありません！")
            return true
          }
          sender.sendMessage("+---------------------------------------+")
          sender.sendMessage("コマンド一覧")
          sender.sendMessage("/asb save - ワールドセーブを行います。")
          sender.sendMessage("/asb backup - ワールドバックアップを行います。")
          sender.sendMessage("/asb help - コマンド一覧を表示します。")
          sender.sendMessage("+---------------------------------------+")
          return true
      }
    }
    false
  }

  override def onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array[String]): util.List[String] = {
    super.onTabComplete(sender, command, alias, args)
    if (!command.getName.equalsIgnoreCase("asb")) return super.onTabComplete(sender,command,alias,args)
    if (args.length == 1) {
      return tabPermission(sender)
    }
    super.onTabComplete(sender,command,alias,args)
  }

  def tabPermission(sender: CommandSender): util.ArrayList[String] = {
    val permissionList = Map(
      "asb.save" -> "save",
      "asb.backup" -> "backup",
      "asb.help" -> "help"
    )
    val commandList = new util.ArrayList[String]()
    permissionList.foreach({case (p,cmd) =>
      if (sender.hasPermission(p)) commandList.add(cmd)
    })
    commandList
  }

}
