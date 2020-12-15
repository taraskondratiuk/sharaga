import scala.annotation.tailrec

import scala.util.Random

case class InputParameters(nIterations: Int, nCities: Int, mutationsPercentage: Int)

case class City(x: Int, y: Int, index: Int) {
  def distance(c2: City): Double = {
    Math.sqrt(Math.pow(x - c2.x, 2) + Math.pow(y - c2.y, 2))
  }
}

object GeneticAlgorithm {

  def getChildren(p1: Array[Int], p2: Array[Int], mutationChance: Int): Seq[Array[Int]] = {
    def maybeMutate(child: Array[Int]): Array[Int] = {
      if (Random.nextInt(100) < mutationChance) {
        val (randEl1, randEl2) = { val rnd = Random.shuffle(child); (rnd.head, rnd.tail.head)}
        val idx1 = child.indexOf(randEl1)
        val idx2 = child.indexOf(randEl2)
        child(idx1) = randEl2
        child(idx2) = randEl1
      }
      child
    }
    val cycles = buildAllCycles(p1, p2)
    
    cycles match {
      case c if c.length == 1 => Seq(maybeMutate(p1), maybeMutate(p2))
      case c                  =>
        val child1 = Array.fill(p2.length)(0)
        val child2 = Array.fill(p2.length)(0)
        val (cycles1, cycles2) = c.splitAt(c.length / 2)
        cycles1.flatten.foreach { idx => child1(idx) = p1(idx); child2(idx) = p2(idx) }
        cycles2.flatten.foreach { idx => child1(idx) = p1(idx); child2(idx) = p2(idx) }
        Seq(maybeMutate(child1), maybeMutate(child2))
    }
  }
  
  def generateCities(nCities: Int): Seq[City] = for { idx <- 0 until nCities } yield City(Random.nextInt(100), Random.nextInt(100), idx)
  
  def geneticAlgorithm(params: InputParameters, cities: Seq[City]): Seq[Seq[Line]] = {
    val startTime = System.currentTimeMillis()
    println(s"genetic algorithm started, input params: $params")
    val initialPopulation = for (_ <- 0 until 30) yield Random.shuffle((0 until params.nCities).toList)
    val distances = generateDistancesMap(cities)
    
    val res = getPopulation(initialPopulation.toList.map(_.toArray), distances, params.mutationsPercentage, params.nIterations)
    val endTime = System.currentTimeMillis()
    println(s"genetic algorithm finished with result ${res.last.toSeq.mkString("(", " -> ", ")")} in ${(endTime - startTime) / 1000}s, it has length ${countRouteDistance(res.head, distances)}")
    citiesRoutesToCoordinates(res, cities)
  }
  
  case class Line(x1: Int, y1: Int, x2: Int, y2: Int)
  object Line {
    def apply(city1: City, city2: City): Line = new Line(city1.x, city1.y, city2.x, city2.y)
  }
  
  private def citiesRoutesToCoordinates(routes: Seq[Array[Int]], cities: Seq[City]): Seq[Seq[Line]] = {
    routes.map { route =>
      (route :+ route.head).sliding(2, 1).map { r =>
        val (city1, city2) = (cities(r(0)), cities(r(1)))
        Line(city1, city2)
      }.toSeq
    }
  }
  
  @tailrec
  private def getPopulation(initialPopulation: List[Array[Int]], distanceMap: Map[(Int, Int), Double], mutationChance: Int, numIterationsLeft: Int, bestEveryIteration: List[Array[Int]] = List.empty): List[Array[Int]] = {
    val children =
      Random.shuffle(initialPopulation).grouped(2).flatMap { case (p1 :: p2 :: _) => getChildren(p1, p2, mutationChance); case _ => Seq.empty } ++
      Random.shuffle(initialPopulation).grouped(2).flatMap { case (p1 :: p2 :: _) => getChildren(p1, p2, mutationChance); case _ => Seq.empty }
    val resPopulation = children.toList
      .sortWith { case (child1, child2) => countRouteDistance(child1, distanceMap) < countRouteDistance(child2, distanceMap) }
      .take(initialPopulation.length)
    if (numIterationsLeft > 0) getPopulation(resPopulation, distanceMap, mutationChance, numIterationsLeft - 1, bestEveryIteration :+ resPopulation.head)
    else bestEveryIteration :+ resPopulation.head
  }
  
  private def countRouteDistance(route: Array[Int], distanceMap: Map[(Int, Int), Double]): Double = {
    route.sliding(2).map {
      vect => distanceMap(vect(0), vect(1))
    }.sum +
      distanceMap((route.last, route.head))
  }
  
  private def generateDistancesMap(cities: Seq[City]): Map[(Int, Int), Double] = {
    cities.toList.combinations(2).flatMap { case (c1 :: c2 :: _) => Seq(((c1.index, c2.index), c1.distance(c2)), ((c2.index, c1.index), c1.distance(c2))) }.toMap
  }
  
  private def buildAllCycles(p1: Array[Int], p2: Array[Int]): Array[Seq[Int]] = {
    @tailrec
    def buildCycle(startIndex: Int, p1: Array[Int], p2: Array[Int], cycle: Seq[Int] = Seq.empty): Seq[Int] = {
      val p1El = p1(startIndex)
      val p2Index = p2.indexOf(p1El)
      if (cycle.headOption.contains(p2Index)) cycle else buildCycle(p2Index, p1, p2, cycle :+ p2Index)
    }
    
    @tailrec
    def buildCyclesForAllIndices(p1: Array[Int], p2: Array[Int], notUsedIndices: Set[Int] = p1.indices.toSet, cycles: Array[Seq[Int]] = Array.empty): Array[Seq[Int]] = {
      if (notUsedIndices.isEmpty) cycles
      else {
        val idx = notUsedIndices.head
        val nextCycle = buildCycle(idx, p1, p2)
        buildCyclesForAllIndices(p1, p2, notUsedIndices.removedAll(nextCycle), cycles :+ nextCycle)
      }
    }
    buildCyclesForAllIndices(p1, p2)
  }
}
