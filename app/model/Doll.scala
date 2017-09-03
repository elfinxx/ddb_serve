package model

case class Doll(
  name: String,
  star: Int,
  tags: Seq[String],
  skill: Skill,
  photo: String,
  no: Int,
  makingTime: String
){
  override
  def toString: String = {
    s"$name : $star / $makingTime"
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
