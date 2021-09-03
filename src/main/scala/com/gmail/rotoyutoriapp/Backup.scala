package com.gmail.rotoyutoriapp

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}
import org.joda.time.{DateTime, DateTimeZone}

import java.io.File

class Backup(instance: AutoSaveBackup) extends Thread {
  private val autoSaveBackup: AutoSaveBackup = instance

  private val backupDirectory = "plugins/AutoSaveBackup/Backups/"

  override def run(): Unit = {
    super.run()
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドをバックアップしています...")
    Bukkit.broadcastMessage(ChatColor.AQUA + "ラグにご注意ください。")
    val worldBackupDirectory = new File(s"${backupDirectory}worldBackups")
    val tmp = new File(s"${backupDirectory}tmp")
    if (!worldBackupDirectory.exists()) worldBackupDirectory.mkdirs()
    if (!tmp.exists()) tmp.mkdirs()
    val jst = new DateTime(DateTimeZone.forID("Asia/Tokyo"))
    Bukkit.getWorlds.forEach(w => {
      val worldFile = new File(w.getName)
      val tmpWorldFile = new File(backupDirectory + "tmp/" + w.getName)
      if (!tmpWorldFile.exists()) tmpWorldFile.mkdirs()
      worldFile.listFiles().foreach(f => {
        fileCompression.deepCopy(f.getPath)
      })
      fileCompression.zipFolder(backupDirectory + "tmp",backupDirectory + s"worldBackups/${jst.toString("yyyy-MM-dd-HH-mm")}.zip")
    })
    fileCompression.deleteFiles(backupDirectory + "tmp/")
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドのバックアップが完了しました！")
  }

  private var timer:Option[BukkitRunnable] = None

  private def backupTimer(): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        timer = Option(this)
        new Backup(autoSaveBackup).start()
      }
    }.runTaskTimer(autoSaveBackup,getConfig.getBackupInterval,getConfig.getBackupInterval)
  }

  def startBackup(): Unit = if (getConfig.isAutoBackup) backupTimer()

  def stopBackup(): Unit = {
    timer match {
      case Some(br) => br.cancel()
      case None =>
    }
  }

}
