/**
 * This file is part of scala-jfx-crossplatform.
 *
 * Copyright (c) 2020 Biacco42
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package info.biacco42.study.soft3d

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

object Main extends App {
  Application.launch(classOf[Hello], args: _*)
}

class Hello extends Application {
  override def start(stage: Stage): Unit = {
    val javaVersion = System.getProperty("java.version")
    val javafxVersion = System.getProperty("javafx.version")
    val l = new Label(s"Hello, JavaFX $javafxVersion running on Java $javaVersion")
    val scene = new Scene(new StackPane(l), 640, 480)
    stage.setScene(scene)
    stage.show
  }

  def say: String = {
    return "hello!";
  } 
}
