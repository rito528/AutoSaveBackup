package com.gmail.rotoyutoriapp

import org.bukkit.configuration.file.FileConfiguration

object getConfig {

  var config: FileConfiguration = null

  def isAutoSave: Boolean = config.getBoolean("autoSave")

  def getSaveInterval: Long = config.getLong("saveInterval") * 20 * 60

}

