package info.biacco42.study.soft3d

import java.nio.IntBuffer

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.{ImageView, PixelBuffer, PixelFormat, WritableImage}
import javafx.scene.layout.StackPane
import javafx.stage.Stage

object Main extends App {
  Application.launch(classOf[AppMain], args: _*)
}

class AppMain extends Application {
  val renderBuffer: JFXHeapImageBuffer = new JFXHeapImageBuffer(640, 480)
  val renderer: Soft3DRenderer = Soft3DRenderer()

  override def start(stage: Stage): Unit = {
    val pixelFormat = PixelFormat.getIntArgbPreInstance
    val pixelBuffer = new PixelBuffer[IntBuffer](renderBuffer.width, renderBuffer.height, renderBuffer.buffer, pixelFormat)
    val renderImage = new WritableImage(pixelBuffer)
    val renderImageView = new ImageView(renderImage)
    val scene = new Scene(new StackPane(renderImageView), 640, 480)
    stage.setScene(scene)
    stage.show
  }
}
