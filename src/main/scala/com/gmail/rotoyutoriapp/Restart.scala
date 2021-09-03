package com.gmail.rotoyutoriapp

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

import java.text.SimpleDateFormat
import java.util.{Timer, TimerTask}

class Restart(autoSaveBackup: AutoSaveBackup) {

  def restart(time:String): Unit = {
    val restartTime:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm")
    val timer = new Timer(true)
    val task:TimerTask = new TimerTask {
      override def run(): Unit = {
        new BukkitRunnable {
          override def run(): Unit = {
            Bukkit.getServer.dispatchCommand(Bukkit.getConsoleSender,"restart")
          }
        }.runTask(autoSaveBackup)
      }
    }
    timer.schedule(task,restartTime.parse(time))
  }

}
