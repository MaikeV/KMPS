def add(list: List[Int], i: Int) : Int = list match {
  case Nil => i
  case x::xs => add(xs, i + x)
}

def map(xs:List[Int], f :Int => Int) : List[Int] = xs match {
  case Nil => Nil
  case y::ys =>f(y) :: map(ys, f)
}

def addMap(list:List[Int], i: Int, f:Int => Int) : Int = list match {
  case Nil => i
  case x::xs => add(map(list, f), i)
}

add(1::2::3::Nil, 0)
addMap(1::2::3::4::5::6::7::8::9::10::Nil, 0, x=>x*x)