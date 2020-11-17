def filter[A](list:List[A], condition: A => Boolean) : List[A] = list match {
  case Nil => Nil
  case x::xs =>
    if(condition(x)) {
      x::filter(xs, condition)
    } else {
      filter(xs, condition)
    }
}

def forall[A](list: List[A], condition: A => Boolean): Boolean = {
  filter[A](list, condition) == list
}

def exists[A](list: List[A], elem:A):Boolean = {
  filter[A](list, x => x == elem) != Nil
}

exists[Int](1::2::3::Nil, 2)
exists[Int](1::2::3::Nil, 4)

forall[Int](1::2::3::Nil, x => x >= 1)
forall[Int](1::2::3::Nil, x => x >= 2)