import scala.annotation.tailrec
import scala.io.Source

case class Track(title: String, length: String, rating: Int, features: List[String], writers: List[String])
case class Album(title: String, date: String, artist: String, tracks: List[Track])

@tailrec
def createTokenList(chars:List[Char], tokenList:List[String], s:String) : List[String] = chars match {
  case Nil => tokenList
  case ('\n' | '\t' | '\r')::xs => createTokenList(xs, tokenList, s)
  case ('<' | '>')::xs => s match {
    case "" => createTokenList(xs, tokenList, "")
    case _ => createTokenList(xs, tokenList:+s, "")
  }
  case x::xs => createTokenList(xs, tokenList, s+x)
}

@tailrec
def parseTrack(list: List[String], track: Track, features: List[String], writers:List[String]) : Track = list match {
  case Nil => track
  case "title"::x::xs => parseTrack(xs, track.copy(title = x), features, writers)
  case "length"::x::xs => parseTrack(xs, track.copy(length = x), features, writers)
  case "rating"::x::xs => parseTrack(xs, track.copy(rating = x.toInt), features, writers)
  case "feature"::x::xs => parseTrack(xs, track.copy(features = track.features:+x), track.features:+x, writers)
  case "writing"::x::xs => parseTrack(xs, track.copy(writers = track.writers:+x), features, track.writers:+x)
  case "/track"::xs => track
  case _::xs => parseTrack(xs, track, features, writers)
}

@tailrec
def parseAlbum(list:List[String], album: Album, inTrack: Boolean) : Album = list match {
  case Nil => album
  case "title"::x::xs => inTrack match {
    case true => parseAlbum(xs, album, inTrack)
    case false => parseAlbum(xs, album.copy(title = x), inTrack)
  }
  case "date"::x::xs => parseAlbum(xs, album.copy(date = x), inTrack)
  case "artist"::x::xs => parseAlbum(xs, album.copy(artist = x), inTrack)
  case "track"::xs => parseAlbum(xs, album.copy(tracks = album.tracks:+parseTrack(xs, Track("", "", 0, Nil, Nil), Nil, Nil)), inTrack = true)
  case "/track"::xs => parseAlbum(xs, album, inTrack = false)
  case "/album"::xs => album
  case _::xs => parseAlbum(xs, album, inTrack)
}

@tailrec
def parseFile(tokenList:List[String], albumList: List[Album]) : List[Album] = tokenList match {
  case Nil => albumList
  case "album"::xs => parseFile(xs, albumList:+parseAlbum(xs, Album("", "", "", Nil), inTrack = false))
  case _::xs => parseFile(xs, albumList)
}

@tailrec
def printTrack(trackList:List[Track], s:String): String = trackList match {
  case Nil => s + ""
  case Track(title, length, rating, features, writers)::xs => printTrack(xs, s + s"\n\tTrack:\n\t\tTitle: $title," +
    s"\n\t\tLength: $length,\n\t\tRating: $rating,\n\t\tFeatures: $features,\n\t\tWriters: $writers")
}

@tailrec
def printAlbum(albumList:List[Album], s:String): String = albumList match {
  case Nil => s + ""
  case Album(title, date, artist, tracks)::xs => printAlbum(xs, s + s"\nAlbum:\n\tTitle: $title,\n\tDate: $date," +
    s"\n\tArtist: $artist,\n\tTracks: [" + printTrack(tracks, "") + "]")
}

def map[A](list:List[A], f: A => A) : List[A]  = list match {
  case Nil => Nil
  case x::xs => f(x)::map(xs, f)
}

def polyMap[A, B](list:List[A], f: A => B) : List[B] = list match {
  case Nil => Nil
  case x::xs => f(x)::polyMap(xs, f)
}

def filter[A](list:List[A], condition: A => Boolean) : List[A] = list match {
  case Nil => Nil
  case x::xs =>
    if(condition(x)) {
      x::filter(xs, condition)
    } else {
      filter(xs, condition)
    }
}

def partition[A](list:List[A], condition: A => Boolean) : List[List[A]] = list match {
  case Nil => Nil
  case x::xs =>
    @tailrec
    def findNextCondition(list:List[A], ls:List[A]) : List[A] = list match {
      case Nil => ls
      case x::xs => if(condition(x)) xs else findNextCondition(xs, ls:+x)
    }

    @tailrec
    def buildInnerList(list:List[A], ys:List[A]) : List[A] = list match {
      case Nil => ys
      case a::as => if(condition(a)) ys else buildInnerList(as, ys:+a)
    }

    @tailrec
    def buildOuterList(list:List[A], ys:List[List[A]]) : List[List[A]] = list match {
      case Nil =>  ys
      case a::as => buildOuterList(findNextCondition(as, Nil), ys :+ buildInnerList(a :: as, Nil))
    }

    buildOuterList(x::xs, Nil)
}

def main() : Unit = {
  val list = Source.fromFile("E:\\Studium\\KMPS\\KMPS\\Praktika\\Praktikum2\\src\\alben.xml").toList
  val tokenList = createTokenList(list, Nil, "")

  println(tokenList)

  val albumList = parseFile(tokenList, Nil)
  println(albumList)
  print(printAlbum(albumList, ""))

  val thriller = Album("Thriller","30.11.1982","Michael Jackson",List(Track("Wanna Be Startin Somethin","6:03",3,List(),List("Michael Jackson")), Track("Baby Be Mine","4:20",4,List(),List("Rod Temperton")), Track("The Girl Is Mine","3:42",3,List("Paul McCartney"),List("Michael Jackson")), Track("Thriller","5:57",5,List(),List("Rod Temperton")), Track("Billie Jean","4:54",5,List(),List("Michael Jackson")), Track("Human Nature","4:06",3,List(),List("Steve Porcaro", "John Bettis")), Track("P.Y.T. (Pretty Young Thing)","3:59",4,List(),List("James Ingram", "Quincy Jones")), Track("The Lady in My Life","5:00",3,List(),List("Rod Temperton"))))

  map[Int](1::2::3::Nil, x => x + x)

  println()
  println()

  println(map[Album](albumList, Album => Album.copy(title = Album.title.toUpperCase)))
  println(map[Album](albumList, Album => Album.copy(tracks = map[Track](Album.tracks, Track => Track.copy(Track.title.toUpperCase)))))
  println(polyMap[Album, List[String]](albumList, Album => polyMap[Track, String](Album.tracks, Track => Track.length)))
  println(filter[Track](thriller.tracks, Track => Track.rating >= 4))
  println(polyMap[Track, String](filter[Track](thriller.tracks, Track => filter[String](Track.writers, w => w == "Rod Temperton") != Nil), Track => Track.title))
  println(partition[Track](thriller.tracks, Track => Track.title == "Thriller"))
  println(
    polyMap[List[Char], String](
      filter[List[Char]](
        partition[Char](
          list, a=> (a == '<' || a == '>')
        ), b => (b != '\n' || b != '\r' || b != '\t')
      ), (c: List[Char]) => c.mkString
    )
  )
}

main()


map[Int](1::2::3::Nil, x => x + x)
filter[Int](1::2::3::4::5::6::6::Nil, x => x % 2 == 0)
partition[Int](1::2::3::2::1::3::3::4::Nil, x => x == 3)
