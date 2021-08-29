package com.gmail.rotoyutoriapp

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}

class Save(instance: AutoSaveBackup) {
  val autoSaveBackup: AutoSaveBackup = instance

  def saveWorld(): Unit = {
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドをセーブしています。")
    Bukkit.getWorlds.forEach(world => world.save())
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドのセーブが完了しました！")
  }

  private var timer:Option[BukkitRunnable] = None

  private def saveTimer(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        timer = Option(this)
        saveWorld()
      }
    }.runTaskTimer(autoSaveBackup,0,getConfig.getSaveInterval)
  }

  def start(): Unit = if (getConfig.isAutoSave) saveTimer()

  def stop(): Unit = {
    timer match {
      case Some(br) => br.cancel()
      case None =>
    }
  }

}
