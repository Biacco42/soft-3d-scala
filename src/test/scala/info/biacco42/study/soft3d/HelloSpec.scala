package info.biacco42.study.soft3d

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFlatSpec with Matchers {
  "Hello" should "say hello!" in {
    val hello = new Hello()

    assert(hello.say == "hello!")
  }
}
