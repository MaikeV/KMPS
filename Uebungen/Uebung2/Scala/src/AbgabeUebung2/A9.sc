abstract class BinTree {
   case object Nil extends BinTree
   case class Tree(elem: Int, left:BinTree, right:BinTree) extends BinTree
}
