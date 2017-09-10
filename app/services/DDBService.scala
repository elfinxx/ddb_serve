package services

import com.github.tototoshi.csv.CSVReader
import model.{Doll, Skill}

import scala.io.{BufferedSource, Source}
import scala.util.Try

object DDBService {
  val source: BufferedSource = Source.fromURL(getClass.getClassLoader.getResource("dolls.csv"))
  val reader: CSVReader = CSVReader.open(source)
  val dolls: List[Doll] = reader.allWithHeaders().map{ i =>
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
      i.getOrElse("makingTime", "")

    )
  }

  def findDollsByName(query: String): Seq[Doll] = {
    dolls.filter(d => d.name.toLowerCase.contains(query.toLowerCase) || d.tags.exists(t => t.contains(query.toLowerCase)))
  }

  def findDollsByMakingTime(timeQuery: String): Seq[Doll] = {
    dolls.filter{ x =>
      x.makingTime.replace(":", "") == timeQuery.replace(":", "")
    }
  }
}
