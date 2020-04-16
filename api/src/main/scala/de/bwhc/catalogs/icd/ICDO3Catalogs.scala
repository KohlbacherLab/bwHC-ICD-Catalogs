package de.bwhc.catalogs.icd



import scala.util.Try
import scala.concurrent.Future
import java.util.ServiceLoader


trait ICDO3CatalogsProvider
{
  def getInstance: ICDO3Catalogs
}



trait ICDO3Catalogs
{

/*
  def topographyCodings(
    version: ICDO3.Version
  ): Iterable[ICDO3TCoding]

  def topographyCodings: Iterable[ICDO3TCoding] =
    topographyCodings(ICDO3.Version.current)

  def topographyMatches(
    version: ICDO3.Version,
    text: String
  ): Iterable[ICDO3TCoding]

  def topographyMatches(
    text: String
  ): Iterable[ICDO3TCoding] =
    topographyMatches(
      ICDO3.Version.current,
      text
    )


  def morphologyCodings(
    version: ICDO3.Version
  ): Iterable[ICDO3MCoding]

  def morphologyCodings: Iterable[ICDO3MCoding] =
    morphologyCodings(ICDO3.Version.current)

  def morphologyMatches(
    version: ICDO3.Version,
    text: String
  ): Iterable[ICDO3MCoding]

  def morphologyMatches(
    text: String
  ): Iterable[ICDO3MCoding] =
    morphologyMatches(
      ICDO3.Version.current,
      text
    )
*/

  def topographyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3TCoding]]

  def topographyCodings: Future[Iterable[ICDO3TCoding]] =
    topographyCodings(ICDO3.Version.current)

  def topographyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3TCoding]]

  def topographyMatches(
    text: String
  ): Future[Iterable[ICDO3TCoding]] =
    topographyMatches(
      ICDO3.Version.current,
      text
    )


  def morphologyCodings(
    version: ICDO3.Version
  ): Future[Iterable[ICDO3MCoding]]

  def morphologyCodings: Future[Iterable[ICDO3MCoding]] =
    morphologyCodings(ICDO3.Version.current)

  def morphologyMatches(
    version: ICDO3.Version,
    text: String
  ): Future[Iterable[ICDO3MCoding]]

  def morphologyMatches(
    text: String
  ): Future[Iterable[ICDO3MCoding]] =
    morphologyMatches(
      ICDO3.Version.current,
      text
    )

}


object ICDO3Catalogs
{

  def getInstance: Try[ICDO3Catalogs] = Try {
    ServiceLoader.load(classOf[ICDO3CatalogsProvider])
      .iterator
      .next
      .getInstance
  }

}
