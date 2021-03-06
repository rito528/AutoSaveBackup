package com.gmail.rotoyutoriapp

import org.bukkit.configuration.file.FileConfiguration

object getConfig {

  var config: FileConfiguration = null

  def isAutoSave: Boolean = config.getBoolean("autoSave")

  def getSaveInterval: Long = config.getLong("saveInterval") * 20 * 60

  def isAutoBackup: Boolean = config.getBoolean("autoBackup")

  def getBackupInterval: Long = config.getLong("backupInterval") * 20 * 60

  def getBackupWorld: Array[AnyRef] = config.getStringList("backupWorlds").toArray()

  def isAutoBackupDelete: Boolean = config.getBoolean("isAutoBackupDelete")

  def deleteTargetDay(): Int = config.getInt("deleteTargetDay")

  def isRestart: Boolean = config.getBoolean("restart")

  def getRestartTime: Array[AnyRef] = config.getStringList("restartTime").toArray()

}

