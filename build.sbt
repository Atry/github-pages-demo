import java.nio.file.StandardCopyOption

import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport.fastOptJS

resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")


enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

name := "Simple Tutorials"
scalaVersion := "2.12.7" // or any other Scala version >= 2.10.2

webpackBundlingMode := BundlingMode.LibraryAndApplication()

libraryDependencies ++= Seq(
  ScalablyTyped.P.`plotly_dot_js`,
  ScalablyTyped.M.`mathjs`,
  //  ScalablyTyped.J.`jquery`,
  //  ScalablyTyped.S.`semantic-ui`,
)

npmDependencies in Compile ++= Seq(
  "plotly.js" -> "1.47.2",
  "mathjs" -> "5.0",
  //  "jquery" -> "1.0.4",
  //  "semantic-ui" -> "2.2",
)

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "latest.release"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val copyTask = taskKey[Unit]("copyJS")

copyTask := {
  val bundle = (Compile / fastOptJS / webpack).value.head

  val destinationPath = file(s"docs/assets/javascripts/${bundle.data.name}").toPath
  java.nio.file.Files.copy(bundle.data.toPath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
  val destinationPathMap = file(s"docs/assets/javascripts/${bundle.data.name}.map").toPath
  val sourcePathMap = file(s"${bundle.data.toPath}.map").toPath
  java.nio.file.Files.copy(sourcePathMap, destinationPathMap, StandardCopyOption.REPLACE_EXISTING)
}
