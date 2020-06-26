package info.biacco42.study.soft3d

case class Pixel private (a: Int, r: Int, g: Int, b: Int) {
  private def this(intRepresentation: Int) = {
    this(
      intRepresentation >> 24 & 0xff,
      intRepresentation >> 16 & 0xff,
      intRepresentation >> 8 & 0xff,
      intRepresentation & 0xff
    )
  }

  def intRepresentation: Int = {
    a << 24 | r << 16 | g << 8 | b
  }
}

object Pixel {
  def apply(a: Int, r: Int, g: Int, b: Int): Pixel = new Pixel(a, r, g, b)
  def apply(intRepresentation: Int): Pixel = new Pixel(intRepresentation)
}
