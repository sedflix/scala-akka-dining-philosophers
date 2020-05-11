package sorting

object SortIntegerArray {

  /**
   * Quick Sort!
   *
   * @param array array that needs to be sorted
   * @return sorted array
   */
  def quickSort(array: Array[Int]): Array[Int] = {
    if (array.length <= 1) {
      array
    } else {
      val pivot = array(array.length / 2)

      quickSort(array.filter(pivot > _)).
        appendedAll(array.filter(pivot == _)).
        appendedAll(quickSort(array.filter(pivot < _)))
    }
  }

  def main(args: Array[String]): Unit = {
    var array = Array((for (i <- 0 to 10) yield scala.util.Random.nextInt()):
      _* /*Sequence is passed as multiple parameters to Array(xs : T*)*/)

    /** Function test the array if it is in order */
    def isSorted[T](arr: Array[T]) = array.sliding(2).forall(pair => pair(0) <= pair(1))

    println("Before: " + array.mkString(", "))

    assert(!isSorted(array), "Not random")
    //  scala.util.Sorting.quickSort(array)
    array = quickSort(array)
    assert(isSorted(array), "Not sorted")

    println("After: " + array.mkString(", "))
  }

}
