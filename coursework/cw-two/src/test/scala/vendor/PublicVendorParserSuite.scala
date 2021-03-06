package vendor

import factory.VirtualMachineFactory
import org.scalatest.FunSuite

class PublicVendorParserSuite extends FunSuite {
  val vp = VirtualMachineFactory.vendorParser

  test("[0] vendor parser throws if parsing unrecognised instruction") {
    intercept[vendor.InvalidInstructionFormatException] {
      val insts = vp.parseString("ssss\niconst 2")
    }
  }

  test("[0b] vendor parser throws if parsing unrecognised instruction with arguments") {
    intercept[vendor.InvalidInstructionFormatException] {
      val insts = vp.parseString("ssss 2\n")
    }
  }

  test("[0c] vendor parser throws if parsing iconst with no argument") {
    intercept[vendor.InvalidInstructionFormatException] {
      val insts = vp.parseString("iconst")
    }
  }

  test("[1] vendor parser throw if parsing bad program") {
    intercept[vendor.InvalidInstructionFormatException] {
      val insts = vp.parseString("programs/p04-bad-program.vm")
    }
  }

  test("[2] vendor parser throw if parsing invalid arguments") {
    intercept[vendor.InvalidInstructionFormatException] {
      val insts = vp.parseString("iconst x\niconst aa\niadd\nprint")
    }
  }

  test("[3] vendor parser should parse a program string correctly") {
    val insts = vp.parseString("iconst 4\niconst 5\niadd\nprint")
    assert(insts.length == 4)
  }

  test("[4] vendor parser should parse a program file correctly") {
    val insts = vp.parse("programs/p03.vm")
    assert(insts.length == 20)

    val all = Vector("iconst", "iconst", "iswap", "iadd", "iconst", "iadd",
      "iconst", "isub", "iconst", "imul", "iconst", "idiv",
      "iconst", "irem", "ineg", "idec", "iinc", "idup", "print", "print")
    for (i <- insts.indices) {
      assert(insts(i).name == all(i))
    }
  }
}
