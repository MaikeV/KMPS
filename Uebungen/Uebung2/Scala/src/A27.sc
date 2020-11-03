abstract class BinTree
case object NullTree extends BinTree
case class BTree(elem: Int, left:BinTree, right:BinTree) extends BinTree

def filter(xb: BinTree, f : Int => Boolean) : List[Int] = xb match {
  case NullTree => Nil
  case BTree(y, left, right) => {
    if (f(y)) {
      y::filter(left, f):::filter(right, f)
    } else {
      filter(left, f):::filter(right, f)
    }
  }
}

filter(BTree(5, BTree(3, NullTree, NullTree), BTree(7, NullTree, NullTree)), x=> x % 2 == 0)
filter(BTree(5, BTree(3, NullTree, NullTree), BTree(8, NullTree, NullTree)), x=> x % 2 == 0)