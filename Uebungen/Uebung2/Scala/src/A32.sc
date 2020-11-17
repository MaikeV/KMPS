abstract class TrafficLight
case object Red extends TrafficLight
case object RedYellow extends TrafficLight
case object Green extends TrafficLight
case object Yellow extends TrafficLight

def switch(light: TrafficLight) : TrafficLight = light match {
  case Red => print("Red"); Red
  case RedYellow => print("RedYellow"); RedYellow
  case Green => print("Green"); Green
  case Yellow => print("Yellow"); Yellow
}

switch(Red)
switch(RedYellow)
switch(Yellow)
switch(Green)