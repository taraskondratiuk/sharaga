import weka.classifiers.functions.GaussianProcesses
import weka.core.{Attribute, FastVector, Instance, Instances}

object Main {
  def main(args: Array[String]): Unit = {
    
    val bufferedSource = io.Source.fromFile(args(0))
    val lines = bufferedSource.getLines
    val header = lines.take(1).next.split(",").map(_.trim)
    val attrs = getAttributesVector(header)

    val dataset = new Instances("basketball", attrs, 200)
    for (line <- lines) {
      val cols = line.split(",").map(_.trim)
      addInstanceToDataset(dataset, cols, attrs)
    }
    bufferedSource.close
    
    dataset.setClassIndex(attrs.size - 1)
    val model = new GaussianProcesses
    
    model.buildClassifier(dataset)
  
    println("Building model")
    println(model)
    
    println("Testing model:")
    val instance = dataset.firstInstance()
    val res = model.classifyInstance(instance)
    println(s"points per minute\n\texpected: ${instance.classValue}\n\tactual: ${res}")
  }
  
  private def getAttributesVector(header: Array[String]): FastVector = {
    val attrs: FastVector = new FastVector()
    header.foreach(a => attrs.addElement(new Attribute(a)))
    attrs
  }
  
  private def addInstanceToDataset(dataset: Instances, columns: Array[String], attrs: FastVector) : Unit = {
    val i = new Instance(columns.length)
    attrs.toArray.map(_.asInstanceOf[Attribute])
      .zip(columns.map(_.toFloat))
      .foreach(v => i.setValue(v._1, v._2))
    dataset.add(i)
  }
}
