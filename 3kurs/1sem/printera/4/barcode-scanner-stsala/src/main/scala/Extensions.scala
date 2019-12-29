import scala.language.postfixOps

object Extensions {
    
    implicit class SeqExtensions[T](seq: Seq[T]) {
        
        @scala.annotation.tailrec
        final def chopSequence(
                                  pieces: Int,
                                  len: Int = -1, done: Int = 0, waiting: Seq[Seq[T]] = Nil
                              ): Seq[Seq[T]] = {
            if (seq isEmpty) waiting.reverse
            
            else {
                val n = if (len < 0) seq.length else len
                val ls = seq.splitAt(n * (done + 1) / pieces - n * done / pieces)
                ls._2.chopSequence(pieces, n, done + 1, ls._1 +: waiting)
            }
        }
    }
    
    implicit class StringExtensions(string: String) {
        final def getMostCommonChar: String = {
            string.toSeq.groupBy(identity).view.mapValues(_.size).maxBy(_._2)._1.toString
        }
    
        @scala.annotation.tailrec
        final def chopString(
                                  pieces: Int,
                                  len: Int = -1, done: Int = 0, waiting: Seq[String] = Nil
                              ): Seq[String] = {
            if (string isEmpty) waiting.reverse
        
            else {
                val n = if (len < 0) string.length else len
                val ls = string.splitAt(n * (done + 1) / pieces - n * done / pieces)
                ls._2.chopString(pieces, n, done + 1, ls._1 +: waiting)
            }
        }
    }
}
