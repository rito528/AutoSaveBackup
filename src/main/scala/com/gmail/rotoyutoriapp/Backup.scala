package com.gmail.rotoyutoriapp

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor}
import org.joda.time.{DateTime, DateTimeZone, Days}

import java.io.File
import java.nio.file.Paths

class Backup(instance: AutoSaveBackup) extends Thread {
  private val autoSaveBackup: AutoSaveBackup = instance

  private val backupDirectory = "plugins/AutoSaveBackup/Backups/"

  override def run(): Unit = {
    super.run()
    //ワールドのバックアップを行う
    Bukkit.broadcastMessage(ChatColor.AQUA + "ワールドをバックアップしています...")
    Bukkit.broadcastMessage(ChatColor.AQUA + "ラグにご注意ください。")
    val worldBackupDirectory = new File(s"${backupDirectory}worldBackups")
    val tmp = new File(s"${backupDirectory}tmp")
    if (!worldBackupDirectory.exists()) worldBackupDirectory.mkdirs()
    if (!tmp.exists()) tmp.mkdirs()
    val jst = new DateTime(DateTimeZone.forID("Asia/Tokyo"))
    Bukkit.getWorlds.forEach(w => {
      if (getConfig.getBackupWorld.contains("*") || getConfig.getBackupWorld.contains(w.getName)) {
        val worldFile = new File(w.getName)
        val tmpWorldFile = new File(backupDirectory + "tmp/" + w.getName)
        if (!tmpWorldFile.exists()) tmpWorldFile.mkdirs()
        worldFile.listFiles().foreach(f => {
          fileCompression.deepCopy(f.getPath)
        })
        fileCompression.zipFolder(backupDirectory + "tmp", backupDirectory + s"worldBackups/${jst.toString("yyyy-MM-dd-HH-mm")}.zip")
      }
    })
    fileCompression.deleteFiles(backupDirectory + "tmp/")
    //古いバックアップを削除する
    if (getConfig.isAutoBackupDelete) {
      Paths.get(backupDirectory + "worldBackups/").toFile.listFiles().foreach(f => {
        val fileName = f.getName.replace(".zip","").split("-")
        val fileTime = new DateTime(fileName(0).toInt,fileName(1).toInt,fileName(2).toInt,fileName(3).toInt,fileName(4).toInt)
        if (Days.daysBetween(fileTime,jst).getDays >= getConfig.deleteTargetDay()) {
          f.delete()
        }
      })
    }
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
