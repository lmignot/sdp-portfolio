package sml

import sml.instructions._

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
class Translator(fileName: String) {
  private final val ADD = "add"
  private final val SUB = "sub"
  private final val MUL = "mul"
  private final val DIV = "div"
  private final val OUT = "out"
  private final val LIN = "lin"
  private final val BNZ = "bnz"

  /**
    * translate the small program in the file into lab (the labels) and prog (the program)
    */
  def readAndTranslate(m: Machine): Machine = {
    val labels = m.labels
    var program = m.prog
    import scala.io.Source
    val lines = Source.fromFile(fileName).getLines
    for (line <- lines) {
      val fields = line.split(" ")
      if (fields.nonEmpty) {
        labels.add(fields(0))
        val clsOption = InstructionFactory.get(fields(1))
        if (clsOption.isEmpty) {
          println(s"Unknown instruction ${fields(1)}")
        } else {
          val cls = clsOption.get
          val cons = cls.getMethods.find(_.getName == "apply").get
          val parameters =
            fields
              .filter(f => fields.indexOf(f) != 1)
              .map {
                case e if e.matches("[0-9]+") => e.toInt.asInstanceOf[Object]
                case e => e
              }
          try {
            program = program :+ cons.invoke(cls, parameters:_*).asInstanceOf[Instruction]
          } catch {
            case ex: IllegalArgumentException => println(s"Parameters did not match Instruction: ${cls.getSimpleName}")
          }
        }
      }
    }
    new Machine(labels, program)
  }
}

object Translator {
  def apply(file: String) = new Translator(file)
}
