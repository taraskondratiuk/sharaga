import Constants._
import Extensions.StringExtensions

import scala.math.pow

object CodeResolver {
    
    
    def printCodeFromFile(path: String): Unit = {
        val choppedPictureFullCode = ImageReader.getFullBinaryCode(path).chopString(EAN13_BINARY_LENGTH)
        
        val fullEan13Code = getMostFrequentColors(choppedPictureFullCode)
        
        val withoutDelimitersEan13Code = removeDelimiters(fullEan13Code)
        
        val numericCode = convertBinaryEan13ToDecimal(withoutDelimitersEan13Code)

        checkFinalCode(numericCode)
        println(numericCode)
    }
    
    
    private def checkFinalCode(numericCode: String): Unit = {

        var sum = 0
        for (i <- 0 until numericCode.length - 1) {
            sum += numericCode.charAt(i).asDigit * pow(3, (i % 2)).toInt
        }
        val checkingVal = 10 - sum % 10
        
        if (checkingVal != numericCode.takeRight(1).toInt)  throw new Exception("Wrong input code! Checking digit doesn't match")
        
    }
    
    private def convertBinaryEan13ToDecimal(withoutDelimitersEan13Code: String): String = {
        val ean13FullListed = withoutDelimitersEan13Code.chopString(EAN13_NUM_DIGITS)
        getFirstDigit(ean13FullListed) + get12Digits(ean13FullListed)
    }
    
    private def getFirstDigit(ean13FullListed: Seq[String]): String = {
        var res = ""
        ean13FullListed.slice(0, EAN13_NUM_DIGITS / 2).foreach(string => {
            res += string.toList.count(char => char.toInt % 2 == 0) % 2
        })
        Ean13Helper.getFirstNumber(res)
    }
    
    private def get12Digits(ean13FullListed: Seq[String]): String = {
        
        var leftNumbers = ""
        var rightNumbers = ""
        
        val leftNumbersListed = ean13FullListed.slice(0, EAN13_NUM_DIGITS / 2)
        val rightNumbersListed = ean13FullListed.slice(EAN13_NUM_DIGITS / 2, EAN13_NUM_DIGITS)
        
        leftNumbersListed.foreach(v => leftNumbers += Ean13Helper.getLeftNumber(v))
        rightNumbersListed.foreach(v => rightNumbers += Ean13Helper.getRightNumber(v))
        
        leftNumbers + rightNumbers
    }
    
    private def getMostFrequentColors(seq: Seq[String]): String = {
        seq.map(string => string.getMostCommonChar).reduce(_ + _)
    }
    
    private def removeDelimiters(str: String): String = {
        str.substring(3, 45) + str.substring(50, 92)
    }
}
