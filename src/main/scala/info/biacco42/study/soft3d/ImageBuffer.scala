package info.biacco42.study.soft3d

trait ImageBuffer[PT <: PixelType] {
  def width: Int
  def height: Int
  def apply(index: Int): PT
  def update(index: Int, pixel: PT): Unit
}
