package info.biacco42.study.soft3d

final class Soft3DRenderer[VT, VO <: VertexOutput](val vertex: Array[VT],
                                                   val vShader: VT => VO,
                                                   val pShader: VO => ARGB,
                                                   val renderTarget: ImageBuffer[ARGB],
                                                   val depthTarget: ImageBuffer[Depth]) {

  // render コマンドを発行する。完了コールバックを渡すことができるようにする。
}

object Soft3DRenderer {
  // sequential な実行パイプラインと並列計算の仕組みを提供する
  // Future に対していい感じの ExecutionContext を自分で作って渡せば Future でよさそう
}