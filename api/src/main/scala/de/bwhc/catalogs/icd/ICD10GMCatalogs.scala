package de.bwhc.catalogs.icd



import scala.util.Try

import scala.concurrent.{
  ExecutionContext,
  Future
}

import java.util.ServiceLoader

import java.time.Year


trait ICD10GMCatalogsProvider
{
  def getInstance: ICD10GMCatalogs
}


trait ICD10GMCatalogs
{
  self =>

  def availableVersions: List[Year]

  def currentVersion: Year =
    self.availableVersions.max

  def codings(
    version: Year = self.currentVersion
  ): Iterable[ICD10GMCoding]


  def coding(
    code: ICD10GM.Code,
    version: Year = self.currentVersion
  ): Option[ICD10GMCoding] =
    self.codings(version).find(_.code == code)


  def matches(
    text: String,
    version: Year = self.currentVersion
  ): Iterable[ICD10GMCoding]

}


object ICD10GMCatalogs
{

  def getInstance: Try[ICD10GMCatalogs] =
    Try {
      ServiceLoader.load(classOf[ICD10GMCatalogsProvider])
        .iterator
        .next
        .getInstance
    }

}

