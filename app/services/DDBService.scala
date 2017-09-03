package services

import com.github.tototoshi.csv.CSVReader
import model.{Doll, Skill}

import scala.io.{BufferedSource, Source}
import scala.util.Try

object DDBService {
  val source: BufferedSource = Source.fromURL(getClass.getClassLoader.getResource("HG.csv"))
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
      i.getOrElse("name_alt", "").split('|'),
      skill,
      "",
      Try(i.getOrElse("no", "0").toInt).getOrElse(0),
      i.getOrElse("makingTime", "")
    )
  }

  def findDollsByName(query: String): Seq[Doll] = {
    dolls.filter(d => d.name.contains(query) || d.tags.exists(t => t.contains(query)))
  }

  def findDollsByMakingTime(timeQuery: String): Seq[Doll] = {
    dolls.filter{ x =>
      x.makingTime.replace(":", "") == timeQuery.replace(":", "")
    }
  }
}
