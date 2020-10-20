def attach(x:Int, ys:List[Int]) : List[Int] = ys match {
   case Nil => x::Nil
   case r:: rs=> r :: attach(x, rs)
}

print(attach(1, 3::2::1::Nil)) //List(3, 2, 1, 1)
print(attach(2, Nil)) //List(2)
print(attach(3, 4::5::Nil)) //List(4, 5, 3)