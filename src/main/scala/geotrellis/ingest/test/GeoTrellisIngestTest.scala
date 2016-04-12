package geotrellis.ingest.test

import geotrellis.raster.{Tile, MultibandTile}
import geotrellis.spark._
import geotrellis.spark.etl.Etl
import geotrellis.spark.io.index.ZCurveKeyIndexMethod
import geotrellis.spark.util.SparkUtils
import geotrellis.spark.ingest._
import geotrellis.vector.ProjectedExtent
import org.apache.spark.SparkConf

object GeoTrellisIngestTest extends App {
  implicit val sc = SparkUtils.createSparkContext("GeoTrellis ETL", new SparkConf(true))

  /* parse command line arguments */
  val etl = Etl(args)
  /* load source tiles using input module specified */
  val sourceTiles = etl.load[ProjectedExtent, Tile]
  /* perform the reprojection and mosaicing step to fit tiles to LayoutScheme specified */
  val (zoom, tiled) = etl.tile(sourceTiles)
  /* save and optionally pyramid the mosaiced layer */
  etl.save(LayerId(etl.conf.layerName(), zoom), tiled, ZCurveKeyIndexMethod)

  sc.stop()
}
