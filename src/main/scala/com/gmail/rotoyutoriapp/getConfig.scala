package com.gmail.rotoyutoriapp

import org.bukkit.configuration.file.FileConfiguration

object getConfig {

  var config: FileConfiguration = null

  def isAutoSave: Boolean = config.getBoolean("autoSave")

  def getSaveInterval: Long = config.getLong("saveInterval") * 20 * 60

  def isAutoBackup: Boolean = config.getBoolean("autoBackup")

  def getBackupInterval: Long = config.getLong("backupInterval") * 20 * 60

  def getBackupWorld: Array[AnyRef] = config.getStringList("backupWorlds").toArray()

}

