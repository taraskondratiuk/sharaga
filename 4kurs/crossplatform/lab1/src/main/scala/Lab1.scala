object Lab1 {
  
  def reverseBytes(num: Int): Int = {
    (num << 24) | ((num << 8) & 0x00FF0000) | ((num >>> 8) & 0x0000FF00) | (num >>> 24)
  }
}
