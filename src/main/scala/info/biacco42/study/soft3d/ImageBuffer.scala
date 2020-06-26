package info.biacco42.study.soft3d

trait ImageBuffer {
  def width: Int
  def height: Int
  def apply(index: Int): Pixel
  def update(index: Int, pixel: Pixel): Unit
}
