abstract class BinTree
case object NullTree extends BinTree
case class BTree(elem: Int, left:BinTree, right:BinTree) extends BinTree

def map(xb:BinTree, f :Int => Int) : BinTree = xb match {
  case NullTree => NullTree
  case BTree(y, left, right) => BTree(f(y), map(left, f), map(right, f))
}

map(BTree(5, BTree(3, NullTree, NullTree), BTree(7, NullTree, NullTree)), x=> x+x)
