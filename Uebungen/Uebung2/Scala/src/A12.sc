
def tail(xs:List[Int]): List[Int] = xs match{
   case Nil => Nil
   case x::xs => xs
}


def prefix(xs:List[Int], ys:List[Int]) : Boolean = xs match {
   case Nil => if(ys != Nil) false else true
   case x::rs => {
      if(ys == Nil) return true
      if(x == ys.head) prefix(rs, ys.tail) else false}
}


print(prefix(1::2::3::4::Nil, 1::2::Nil)) //true
print(prefix(Nil, Nil)) //true
print(prefix(1::2::3::Nil, 4::5::Nil)) //false
print(prefix(Nil, 1::2::3::Nil)) //false
print(prefix(1::2::3::Nil, Nil)) //true
