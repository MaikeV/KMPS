def fak(num:Int) : Int = {
  if (num == 0) 1
  else num * fak(num - 1)
}

fak(5)