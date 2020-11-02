import scala.:+
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
def parseAlbum(list:List[String], album: Album) : Album = list match {
  case Nil => album
  case "title"::x::xs => parseAlbum(xs, album.copy(title = x))
  case "date"::x::xs => parseAlbum(xs, album.copy(date = x))
  case "artist"::x::xs => parseAlbum(xs, album.copy(artist = x))
  case "track"::xs => parseAlbum(xs, album.copy(tracks = album.tracks:+parseTrack(xs, Track("", "", 0, Nil, Nil), Nil, Nil)))
  case "/album"::xs => album
  case _::xs => parseAlbum(xs, album)
}

def parseFile(tokenList:List[String], albumList: List[Album]) : List[Album] = tokenList match {
  case Nil => albumList
  case "album"::xs => parseFile(xs, albumList:+parseAlbum(xs, Album("", "", "", Nil)))
  case _::xs => parseFile(xs, albumList)
}

@tailrec
def printTrack(trackList:List[Track], s:String): String = trackList match {
  case Nil => s + ""
  case Track(title, length, rating, features, writers)::xs =>
    printTrack(xs, s +
      s"\n\tTrack:\n\t\tTitle: $title,\n\t\tLength: $length,\n\t\tRating: $rating," +
      s"\n\t\tFeatures: $features,\n\t\tWriters: $writers")
}

@tailrec
def printAlbum(albumList:List[Album], s:String): String = albumList match {
  case Nil => s + ""
  case Album(title, date, artist, tracks)::xs =>
    printAlbum(xs, s +
      s"\nAlbum:\n\tTitle: $title,\n\tDate: $date,\n\tArtist: $artist,\n\tTracks: [" +
      printTrack(tracks, "") + "]")
}

def main() : Unit = {
  val list = Source.fromFile("E:\\Studium\\KMPS\\KMPS\\Praktika\\Praktikum2\\src\\alben.xml").toList
  val tokenList = createTokenList(list, Nil, "")

  println(tokenList)

  val albumList = parseFile(tokenList, Nil)
  println(albumList)
  print(printAlbum(albumList, ""))
}

main()