def fib(n : Int) : Int = n match {
  case 0 | 1 => n
  case _ => fib(n - 1) + fib(n - 2)
}

for (i <- 0 to 9) {
  println(fib(i))
}