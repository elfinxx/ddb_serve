package services

import com.github.tototoshi.csv.CSVReader
import model.{Doll, Equipment, Skill}

import scala.io.{BufferedSource, Source}
import scala.util.Try

object DDBService {
  val dollSource: BufferedSource = Source.fromURL(getClass.getClassLoader.getResource("dolls.csv"))
  val reader: CSVReader = CSVReader.open(dollSource)
  val dolls: List[Doll] = reader.allWithHeaders().map { i =>
    val skill = Skill(
      i.getOrElse("skill_name", ""),
      i.getOrElse("skill_effect", ""),
      i.getOrElse("skill_description", ""),
      i.getOrElse("cooltime_pre", "") + " / " + i.getOrElse("cooltime_main", ""),
      i.getOrElse("skill_formation", "")
    )

    Doll(
      i.getOrElse("name", ""),
      i.getOrElse("star", "0").toInt,
      (i.getOrElse("name_alt", "") + i.getOrElse("extra", "")).split('|'),
      skill,
      i.getOrElse("image_url", "http://k.kakaocdn.net/dn/FZJ1b//btqhbJhe4yF//s3KEdDbB7LKJeUa5YTOaVK/original.jpg"),
      Try(i.getOrElse("no", "0").toInt).getOrElse(0),
      i.getOrElse("makingTime", ""),
      i.getOrElse("star", "0") + "성, 별명: " + i.getOrElse("name_alt", "") + "\n제조 시간: " + i.getOrElse("makingTime", "") + ", 관련 태그: " + i.getOrElse("extra", "") + "\n" + ""
    )
  }

  reader.close()
  dollSource.close()

  val eqSource: BufferedSource = Source.fromURL(getClass.getClassLoader.getResource("eq.csv"))
  println(eqSource)
  val eqReader: CSVReader = CSVReader.open(eqSource)
  println(eqReader)
  val eqs: List[Equipment] = eqReader.allWithHeaders().map { x =>
    println(x)
    Equipment(
      x.getOrElse("name", ""),
      x.getOrElse("type", ""),
      x.getOrElse("time", ""),
      x.getOrElse("star", "0").toInt
    )
  }


  def findDollsByName(query: String): Seq[Doll] = {
    println("Total dolls -> " + dolls.length)
    dolls.filter(d => d.name.toLowerCase.contains(query.toLowerCase) || d.tags.exists(t => t.contains(query.toLowerCase)))
  }

  def findDollByName(query: String): Doll = {
    dolls.find(d => d.name.toLowerCase.contains(query.toLowerCase) || d.tags.exists(t => t.contains(query.toLowerCase))).getOrElse(Doll.empty())
  }

  def findDollsByMakingTime(timeQuery: String): Seq[Doll] = {
    dolls.filter { x =>
      x.makingTime.replace(":", "") == timeQuery.replace(":", "")
    }
  }

  def findEquipmentsByName(query: String): Seq[Equipment] = {
    println("QUERY -> " + query)
    println("eqs -> " + eqs)
    eqs.filter( x => x.name.toLowerCase.contains(query.toLowerCase()))
  }
}
