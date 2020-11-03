import scala.annotation.tailrec

@tailrec
def range(list:List[Int], a:Int, b:Int) : List[Int] = {
  if (a <= b) {
    range(list:+a, a + 1, b)
  } else {
    list
  }
}

range(Nil, 1, 5)