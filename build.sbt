name := "ddb_serve"
 
version := "1.0" 
      
lazy val `ddb_serve` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Sonatype OSS Repository" at "https://oss.sonatype.org/content/groups/public/"

resolvers += "kakao" at "http://repo.daumkakao.io/content/groups/dk-aa-group/"

//resolvers += "Maven Central" at "http://central.maven.org/maven2/"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
libraryDependencies += "aa.mango" %% "mango-core" % "0.4.7"
//libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.9.0"
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.0"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.4"
//libraryDependencies += "org.seleniumhq.selenium" %% "selenium-java" % "3.5.3"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.0.0"
// compile 'org.seleniumhq.selenium:selenium-java:3.5.3'



unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      