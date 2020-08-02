package info.biacco42.study.soft3d

sealed trait PixelType

final case class ARGB(a: Int, r: Int, g: Int, b: Int) extends PixelType {
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

object ARGB {
  def apply(intRepresentation: Int): ARGB = new ARGB(intRepresentation)
}

final case class Depth(index: Int) extends PixelType {}
final case class Raw(value: Int) extends PixelType {}
