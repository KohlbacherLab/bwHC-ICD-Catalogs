package de.bwhc.catalogs.icd


import play.api.libs.json._

import java.time.Year

trait Formats
{

  implicit val formatYear: Format[Year] =
    Format(
      Reads(_.validate[Int].map(Year.of)),
      Writes(y => JsString(y.toString))
    )

}
object Formats extends Formats
