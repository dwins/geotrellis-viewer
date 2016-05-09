# GeoTrellis Viewer

*A simple ZXY tile server for GeoTrellis tilesets*

Viewer is a tool for GeoTrellis administrators and developers to inspect ingested datasets in GeoTrellis.
It is also a fairly simple GeoTrellis application and can hopefully serve as a starting point for developers building custom services with GeoTrellis.

## Why a viewer just for GeoTrellis?

In distributed computing, a general performance guideline is to keep the data where the processing is going to happen - if you have 100 nodes but only one of them has the numbers that you want to crunch, then a lot of the benefit of those extra nodes is wasted on getting the data to them for processing.
So, a first step in deploying GeoTrellis is to *ingest* data - transforming it from a contiguous format like a collection of big geotiffs to something more amenable to distributed processing, often a network accessible store with fast access to small tiles.
GeoTrellis can do this work for you, but once your raster is in a GeoTrellis-specific format you need a GeoTrellis-specific viewer to inspect it.

## How do I use it?

Before you can use Viewer you need to ingest some data with GeoTrellis, so please consult the GeoTrellis documentation about the [ingest process](https://github.com/geotrellis/geotrellis/blob/master/docs/spark-etl/spark-etl-intro.md) if you need to.
With data ingested, you can deploy Viewer to have a simple tile service, amenable to viewing with a browser-based library such as [Leaflet](http://leafletjs.com/) or [OpenLayers](http://openlayers.org/). 

Viewer needs to be configured with a few details such as the Amazon S3 bucket where data tiles are stored.
Please see [src/main/resources/application.conf]() for details.

### Endpoint reference

All dynamic endpoints are under the `gt` prefix to avoid collisions with static resources.

* ``gt/breaks/{layer}`` gives class breaks for the named layer.
  The *required* parameter `numBreaks` specifies a maximum number of classes to return.

* ``gt/tms/{layer}/{z}/{x}/{y}`` gives a PNG tile rendered from the layer.
  The *optional* parameter `breaks` gives a list of class breaks for rendering.
  The *optional* parameter `colorRamp` names a color ramp to colorize the classes.
  Names for color ramps are defined in ColorRampMap.scala.
  Every tile also has a few sub-resources that give views into the data behind the rendered tile.

  * `grid` gives a textual representation of the values of all the cells in the tile
  * `type` gives the name of the class representing the tile (useful to verify cell type)
  * `breaks` gives class breaks considering only the specified tile rather than the entire dataset
  * `histo` gives a histogram computed over the specified tile
  * `stats` gives a geotrellis Statistics report with mean, median, mode, min, max, standard deviation.

## Misc

For reference, here is the command (slightly obfuscated) that I've been using to rebuild and upload to the EC2 VM that I have been testing on:

```
$ ./sbt assembly && rsync -e 'ssh -i ~/geotrellis.pem' target/scala-2.10/geotrellis-ingest-test-assembly-0.1.0.jar user@<machine>.amazonaws.com: -P
```

And to run on the VM:

```
$ java -cp /usr/lib/spark/lib/spark-assembly.jar:geotrellis-ingest-test-assembly-0.1.0.jar geotrellis.ingest.test.Server
```
