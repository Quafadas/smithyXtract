import $ivy.`com.disneystreaming.smithy4s::smithy4s-mill-codegen-plugin::0.18.12`
import smithy4s.codegen.mill._

import mill._, mill.scalalib._
import mill.api._

object textract extends ScalaModule with Smithy4sModule {

  def scalaVersion = "3.3.1"

  override def ivyDeps = Agg(
    ivy"com.disneystreaming.smithy4s::smithy4s-aws-http4s:${smithy4sVersion()}",
    ivy"org.http4s::http4s-ember-client:0.23.25",
    ivy"com.lihaoyi::os-lib:0.9.3",
    ivy"is.cir::ciris:3.5.0"
  )

  override def smithy4sAwsSpecs: T[Seq[String]] = T(Seq(AWS.textract))

    // def forkArgs = Seq("""-Djavax.net.ssl.trustStore=C:\gitCustomCerts\cacerts""")
}