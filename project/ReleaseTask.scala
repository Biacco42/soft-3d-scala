/**
 * This file is part of scala-jfx-crossplatform.
 *
 * Copyright (c) 2020 Biacco42
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

import java.io.File

import sbt.io.IO

import scala.sys.process._
import java.nio.file.{Path, Paths}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReleaseTask {
  def buildReleasePackage(name: String, version: String, scalaVersion: String,
                          targetPlatform: String, targetPath: Path, libPath: Path, releasePath: Path,
                          jmodsPath: Path, javaHome: Path, currentPlatform: String): Unit = {
    val jarPath = getJarPath(name, version, scalaVersion, targetPath)
    println(s"Release jar $jarPath for $targetPlatform")

    cleanBeforeBuild(targetPath)
    val depList = getDependencyList(jarPath, jmodsPath, javaHome)
    val deps = depList.mkString(", ")
    println("Dependent JRE modules: " + deps)
    buildReleaseJRE(jarPath, targetPath, jmodsPath, depList, javaHome)
    buildPackage(name, version, targetPlatform, jarPath, targetPath, libPath, releasePath, currentPlatform)
  }

  def getJarPath(name: String, version: String, scalaVersion: String, targetPath: Path): Path = {
    val regex = """^(\d\.\d\d).+""".r
    val regex(scalaMajorMinor) = scalaVersion
    val scalaDirectory = s"scala-$scalaMajorMinor"
    val jarName = s"$name-assembly-$version.jar"
    Paths.get(targetPath.toString, scalaDirectory, jarName)
  }

  def cleanBeforeBuild(targetPath: Path): Unit = {
    val jrePath = targetPath.resolve("jre")
    IO.delete(jrePath.toFile)
  }

  def getDependencyList(jarPath: Path, jmodsPath: Path, javaHome: Path): List[String] = {
    val jdeps = Paths.get(javaHome.toString, "bin", "jdeps")
    val depsLines = Process(
      Seq(jdeps.toString, "--module-path", jmodsPath.toString, "-s", jarPath.toString)
    ).!!.split("\n")
    depsLines.flatMap { depsLine =>
      val regex = """^((jdk|java)\..*)""".r
      val depPackage = depsLine.split(" -> ").last.stripLineEnd.trim
      depPackage match {
        case regex(jreDep, _) => Some(jreDep)
        case _ => None
      }
    }.toList
  }

  def buildReleaseJRE(jarPath: Path, targetPath: Path, jmodsPath: Path, depList: List[String], javaHome: Path): Unit = {
    val jrePath = targetPath.resolve("jre")
    val jlink = Paths.get(javaHome.toString, "bin", "jlink")
    val deps = depList.mkString(",")
    Process(
      Seq(jlink.toString, "--module-path", jmodsPath.toString, "--add-modules", deps, "--compress=2", "--output", jrePath.toString).!!
    )
  }

  def buildPackage(name: String, version: String, targetPlatform: String,
                   jarPath: Path, targetPath: Path, libPath: Path, releasePath: Path,
                   currentPlatform: String): Unit = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")
    val dateString = LocalDateTime.now().format(formatter)
    val packagePath = Paths.get(releasePath.toString, s"$name-$version-$targetPlatform-$dateString")
    IO.createDirectory(packagePath.toFile)

    val jrePath = targetPath.resolve("jre")
    IO.copyDirectory(jrePath.toFile, packagePath.resolve("jre").toFile)

    val jarName = jarPath.getFileName
    IO.copyFile(jarPath.toFile, packagePath.resolve(jarName).toFile)

    val packageLibPath = packagePath.resolve("lib")
    IO.createDirectory(packageLibPath.toFile)
    IO.listFiles(libPath.toFile).foreach { file =>
      val fileName = file.getName
      if (fileName.endsWith("so") || fileName.endsWith("dylib") || fileName.endsWith("dll")) {
        println("Copy shared lib: " + fileName)
        IO.copyFile(file, packageLibPath.resolve(fileName).toFile)
      }
    }

    if (currentPlatform == "win") {
      val ps = File.separator
      val batch =
        s"""
           |@echo off
           |cd /d %~dp0
           |
           |.${ps}jre${ps}bin${ps}java -jar -Djava.library.path=lib .${ps}$jarName
           |""".stripMargin
      val batchFile = packagePath.resolve(s"$name.bat").toFile
      IO.write(batchFile, batch, IO.utf8)
    } else {
      val shellScript =
        """#!/bin/sh
          |
          |SCRIPT_DIR=$(cd $(dirname $0); pwd)
          |cd $SCRIPT_DIR
          |nohup ./jre/bin/java -jar -Djava.library.path=lib ./"""".stripMargin + jarName + "\" &\n"

      val shellScriptFile = packagePath.resolve(name).toFile
      IO.write(shellScriptFile, shellScript, IO.utf8)
      IO.setPermissions(shellScriptFile, "rwxr-xr-x")
    }
  }
}
