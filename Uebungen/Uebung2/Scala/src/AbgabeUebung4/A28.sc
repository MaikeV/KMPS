def prefix(xs:List[Int], ys:List[Int]) : Boolean = xs match {
  case Nil => if(ys != Nil) false else true
  case x::rs => {
    if(ys == Nil) return true
    if(x == ys.head) prefix(rs, ys.tail) else false}
}

def infix(xs:List[Int], ys:List[Int]) : Boolean = xs match {
  case Nil => if(ys != Nil) false else true
  case x::rs => if (prefix(xs, ys)) true else infix(rs, ys)
}

infix(1::2::3::4::Nil, 2::3::4::Nil)
infix(1::2::3::4::Nil, 5::6::7::Nil)
infix(1::2::3::4::Nil, 1::Nil)
infix(1::2::3::4::Nil, 4::Nil)
