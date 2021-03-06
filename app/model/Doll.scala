package model

case class Doll(
  name: String,
  star: Int,
  tags: Seq[String],
  skill: Skill,
  photo: String,
  no: Int,
  makingTime: String,
  summary: String = ""
){
  override
  def toString: String = {
    s"$name : $star / $makingTime"
  }
}

object Doll{
  def empty() = {
    Doll("미상", 0, Seq(), Skill("", "", "", "", ""), "", 0, "")
  }
}

case class Skill(
  name: String,
  effect: String,
  description: String,
  coolTime: String,
  formation: String
)

case class Stat(
  hp: Int,
  attack: Int,
  accuracy: Int,
  evasion: Int,
  attackSpeed: Int,
  attackValue: Int,
  defenceValue: Int
)
