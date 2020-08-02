package info.biacco42.study.soft3d

trait VertexOutput {
  def vertex: Int
  def varying: Seq[Float]
  def varying_=(vs: Seq[Float]): Unit
}
