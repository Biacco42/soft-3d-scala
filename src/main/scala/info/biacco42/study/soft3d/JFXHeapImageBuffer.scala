package info.biacco42.study.soft3d

import java.nio.IntBuffer

class JFXHeapImageBuffer(override val width: Int, override val height: Int) extends ImageBuffer {
  private val _buffer = (0 until width * height).map{ _: Int => 0xFF << 24 }.toArray
  val buffer: IntBuffer = IntBuffer.wrap(_buffer)

  override def apply(index: Int): Pixel = Pixel(_buffer(index))
  override def update(index: Int, pixel: Pixel): Unit = {
    _buffer(index) = pixel.intRepresentation
  }
}
