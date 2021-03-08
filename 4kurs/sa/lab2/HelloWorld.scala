object HelloWorld {
      case class Param(value: Int, name: String)
   def main(args: Array[String]) {

      
      val inputParams = Seq(30, 65, 45)
      
      val params = inputParams.zipWithIndex.map { case(param, index) => Param(param, s"z${index + 1}") }
      
      println("sequential:")
      normalize(sequentialComparisons(params)).foreach(println)
      
      
      println("pairwise:")
      normalize(pairwiseComparisons(params)).foreach(println)
      
   }
   
   def sequentialComparisons(input: Seq[Param]): Seq[Param] = {
        val paramsArr = input.sortBy(_.value)(Ordering[Int].reverse).toArray
        for (i <- 0 until (paramsArr.size - 2)) {
            val sumNextTwo = paramsArr(i + 1).value + paramsArr(i + 2).value
            paramsArr(i) match {
                case Param(v, name) if (v < sumNextTwo) => paramsArr(i) = Param(sumNextTwo + 1, name)
                case _ =>
            }
        }
        paramsArr
   }
   
   def pairwiseComparisons(input: Seq[Param]): Seq[Param] = {
       input.map { case Param(v, name) =>
            val sum = input.filter(_.value < v).size
            Param(sum, name)
       }
   }
   
   def normalize(input: Seq[Param]): Seq[(Double, String)] = {
       val sum = input.map(_.value).sum.toDouble
       input.map { case Param(v, name) => (v / sum, name) }
   }
}