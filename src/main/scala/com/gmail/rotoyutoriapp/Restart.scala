package com.gmail.rotoyutoriapp

import org.bukkit.{Bukkit, ChatColor}
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

  def restartTimer(time: String): Unit = {
    val beforeRestartTime:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm")
    val timer = new Timer(true)
    val task:TimerTask = new TimerTask {
      override def run(): Unit = {
        var count = 60
        new BukkitRunnable {
          override def run(): Unit = {
            if (count == 60 || count == 30 || count <= 10)
              Bukkit.broadcastMessage(ChatColor.AQUA + "定期再起動まであと" + count + "秒" )
            count -= 1
            if (count <= 0) this.cancel()
          }
        }.runTaskTimer(autoSaveBackup,0,20)
      }
    }
    timer.schedule(task,beforeRestartTime.parse(time))
  }

}
