def insert(x:Int, xs:List[Int]) : List[Int] = xs match {
   case Nil => x :: Nil
   case y :: ys=> if (x<=y) x::xs else y :: insert(x,ys)
}

def insertionSort(xs: List[Int]): List[Int] = xs match {
   case Nil => Nil
   case y :: ys => {
       insert(y, insertionSort(ys))
   }
}

print(insertionSort(5::7::3::2::6::1::4::Nil))