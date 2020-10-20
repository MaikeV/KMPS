abstract class MyList
case object Null extends MyList
case class Ls(head:Int, tail:MyList) extends MyList

def transList(xs:List[Int]) : MyList = xs match {
   case Nil => Null
   case x::xs => Ls(x, transList(xs))
}

transList(1::2::3::Nil)