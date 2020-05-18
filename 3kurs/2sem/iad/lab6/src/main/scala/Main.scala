import java.io.{BufferedReader, FileReader}

import weka.associations.PredictiveApriori
import weka.core.Instances

object Main {
  def main(args: Array[String]): Unit = {
    val data = new Instances(new BufferedReader(new FileReader(args(0))))
    val model = new PredictiveApriori()
    model.setCar(true)
    model.buildAssociations(data)
    println(model)
  }
}
