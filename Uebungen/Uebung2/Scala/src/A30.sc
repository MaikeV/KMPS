import scala.annotation.tailrec

@tailrec
def zip(xs:List[Int], ys:List[Int], zs:List[Int], f : (Int, Int) => Int) : List[Int] = xs match {
  case Nil => zs
  case x::xs => ys match {
    case Nil => zs
    case y::ys => zip(xs, ys, zs:+f(x, y), f)
  }
}

zip(1::2::3::Nil, 4::5::6::Nil, Nil, (x, y)=> x + y)