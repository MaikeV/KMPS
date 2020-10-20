abstract class MyList
case object Null extends MyList
case class Ls(head:Int, tail:MyList) extends MyList

def append(xs:List[Int], ys:List[Int]) : List[Int] = xs match {
   case Nil => ys
   case x:: rs=> x :: append(rs, ys)
}

def myAppend(xs:MyList, ys:MyList) : MyList = xs match {
   case Null => ys
   case Ls(head, tail)=> Ls(head, myAppend(tail, ys))
}

print(append(1::2::3::Nil,4::5::6::Nil))
print(myAppend(Ls(1, Ls(2, Ls(3, Null))), Ls(4, Ls(5, Null))))