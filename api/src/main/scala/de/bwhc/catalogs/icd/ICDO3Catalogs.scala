package de.bwhc.catalogs.icd



import scala.util.Try

import java.util.ServiceLoader

import java.time.Year


trait ICDO3CatalogsProvider
{
  def getInstance: ICDO3Catalogs
}



trait ICDO3Catalogs
{
  self =>

//  def availableVersions: List[String]
  def availableVersions: List[(String,Year)]

  def currentVersion: String


  def topographyCodings(
    version: String = self.currentVersion
  ): Iterable[ICDO3TCoding]


  def topographyMatches(
    text: String,
    version: String = self.currentVersion
  ): Iterable[ICDO3TCoding]



  def morphologyCodings(
    version: String = self.currentVersion
  ): Iterable[ICDO3MCoding]


  def morphologyMatches(
    text: String,
    version: String = self.currentVersion
  ): Iterable[ICDO3MCoding]

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
