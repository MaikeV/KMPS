import scala.annotation.tailrec

def isPalindrom(list:List[Char]) : Boolean = {
  def reverse(ls:List[Char]): List[Char] = ls match {
    case Nil => Nil
    case x::xs => reverse(xs):+x
  }

  val rev = reverse(list)
  list == reverse(list)

//  @tailrec
//  def checkForPalindrom(list:List[Char], rev:List[Char]) : Boolean = list match {
//    case Nil => true
//    case x::xs => rev match {
//      case Nil => true
//      case r::rs => {
//        if(r == x) {
//          checkForPalindrom(xs, rs)
//        } else {
//          false
//        }
//      }
//    }
//  }
//
//  checkForPalindrom(list, rev)
}

isPalindrom('a'::'b'::'b'::'a'::Nil)
isPalindrom('a'::'b'::'c'::'a'::Nil)
isPalindrom('a'::'b'::'c'::'b'::'a'::Nil)