abstract class BinTree
   case object NullTree extends BinTree
   case class BTree(elem: Int, left:BinTree, right:BinTree) extends BinTree

def treeLength(bt:BinTree) : Int = bt match {
   case NullTree => 0
   case BTree(_, left, right) => {
      1+treeLength(left) + treeLength(right)
   }
}

treeLength(BTree(5, BTree(2, NullTree, NullTree), BTree(9, BTree(7, NullTree, NullTree), NullTree)))