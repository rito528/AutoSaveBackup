package com.gmail.rotoyutoriapp

import java.io.{BufferedInputStream, File, FileInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util.zip.{ZipEntry, ZipOutputStream}
import scala.annotation.tailrec

object fileCompression {

  def zipFolder(targetDir: String, outputZip: String): Unit = {
    val baseFile = new File(targetDir)
    val zos = new ZipOutputStream(new FileOutputStream(new File(outputZip)), StandardCharsets.UTF_8)
    val fileList: Seq[File] = baseFile.listFiles()
    zipFileList(zos, fileList, baseFile)
    zos.closeEntry()
    zos.close()
  }

  private def zipFileList(zos: ZipOutputStream, fileList: Seq[File], baseFile: File): Unit = {
    for (file <- fileList) {
      if (file.isDirectory) {
        zipFileList(zos, file.listFiles(), baseFile)
      } else {
        entryZos(file, zos, baseFile)
      }
    }
  }

  private def entryZos(targetFile: File, zos: ZipOutputStream, baseFile: File): Unit = {
    val entry = new ZipEntry(targetFile.getAbsolutePath.replace(baseFile.getParentFile.getAbsolutePath, "").drop(1))
    zos.putNextEntry(entry)
    val in = new BufferedInputStream(new FileInputStream(targetFile))
    writeZos(zos, in, targetFile)
    in.close()
  }

  @tailrec
  private def writeZos(zos: ZipOutputStream, in: BufferedInputStream, targetFile: File): Unit = {
    val readBytes = in.read()
    if (readBytes != -1) {
      zos.write(readBytes)
      writeZos(zos, in, targetFile)
    }
  }

  def deepCopy(paths: String): Unit = {
    if (Paths.get(paths).toFile.isDirectory) {
      Paths.get("plugins/AutoSaveBackup/Backups/tmp/" + paths).toFile.mkdirs()
      Files.list(Paths.get(paths)).forEach(file => {
        deepCopy(file.toFile.getPath)
      })
    } else if (Paths.get(paths).toFile.isFile){
      Files.copy(Paths.get(paths), Paths.get("plugins/AutoSaveBackup/Backups/tmp/" + paths))
    }
  }

  def deleteFiles(paths: String): Unit = {
    if (Paths.get(paths).toFile.isDirectory) {
      Files.list(Paths.get(paths)).forEach(file => {
        deleteFiles(file.toFile.getPath)
      })
      Paths.get(paths).toFile.delete()
    } else if (Paths.get(paths).toFile.isFile) {
      Files.delete(Paths.get(paths))
    }
  }

}
