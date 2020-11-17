def foldr[A](f:(A,A) => A, start: A, xs: List[A]) : A = xs match {
  case x::Nil => f(start,x)
  case y::ys => f(foldr(f,start,ys),y)
}

foldr[Int]((x, y) => x - y, 5, 1::2::3::Nil)