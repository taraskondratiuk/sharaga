import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class Lab1Test extends AnyFlatSpec {
  val numbers = Seq(1, -1, 0, Integer.MAX_VALUE, Integer.MIN_VALUE, 334132354, 45642, -8324523, 84654, -4645014)
  
  "reverseBytes" should "work as Integer.reverseBytes" in {
    numbers.foreach { num =>
      Lab1.reverseBytes(num) shouldBe Integer.reverseBytes(num)
    }
  }
  
  "reverseBytes" should "work backwards" in {
    numbers.foreach { num =>
      Lab1.reverseBytes(Lab1.reverseBytes(num)) shouldBe num
    }
  }
}
