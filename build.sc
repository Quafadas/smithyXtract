import $ivy.`com.disneystreaming.smithy4s::smithy4s-mill-codegen-plugin::0.18.7`
import smithy4s.codegen.mill._

import $ivy.`io.github.quafadas::millSite::0.0.16`
import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0`

import de.tobiasroeser.mill.vcs.version._

import mill._, mill.scalalib._, mill.scalajslib._, mill.scalanativelib._
import io.github.quafadas.millSite.SiteModule
import io.github.quafadas.millSite.QuickChange
import mill._, scalalib._, publish._
import mill.scalajslib.api._
import mill.scalanativelib._

import mill.api.Result




object textract extends Smithy4sModule {
  def scalaVersion = "3.3.1"
   override def ivyDeps = Agg(
    ivy"com.disneystreaming.smithy4s::smithy4s-aws-http4s:${smithy4sVersion()}",
    ivy"org.http4s::http4s-ember-client:0.23.25",
    ivy"com.lihaoyi::os-lib:0.9.3",
    ivy"is.cir::ciris:3.5.0",
    ivy"software.amazon.awssdk:textract:2.23.15",
    ivy"software.amazon.awssdk:bom:2.23.15"
  )

  override def smithy4sAwsSpecs: T[Seq[String]] = T(Seq(AWS.textract))

  def forkArgs = Seq("""-Djavax.net.ssl.trustStore=C:\gitCustomCerts\cacerts""")
}
