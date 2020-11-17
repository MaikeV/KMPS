abstract class BinTree[+A] {
  case object Nil extends BinTree[Nothing]
  case class Tree(elem: A, left:BinTree[A], right:BinTree[A]) extends BinTree[A]
}
