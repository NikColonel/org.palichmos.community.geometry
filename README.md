# What does it do

For each sales region, you can create a record in a new IP_Geometry table. Each record describes a sales region as a polygon. A polygon is an area on a map that is defined by a set of coordinates (longitude and latitude). Using the import and export tools, you can update the polygon. With the help of various services, for example https://geojson.io/ or https://yandex.ru/map-constructor/, you can create or edit a polygon or multipolygon.

With this plugin you can solve the problem “Does the buyer's address get into the delivery area?” (see MGeometry.isContains(lat, long)).

# Instructions for use

* Open any Geo JSON editor like https://geojson.io/ or https://yandex.ru/map-constructor/ and click New.
* Using the Polygon tool draw the desired area.
* Click Save and select GeoJSON, save the file.
* Open the Sales Region window and select the desired region.
* On the tab, select the Import Geo JSON process, specify the file, and click OK.
* Refresh the window and now a polygon has been created for this region.
* To edit the polygon, select the Export Geo JSON process and indicate on what date you want to receive the data.
* The MGeometry class has a method IsContains(lat, long) which will return True if the coordinate is in the polygon.

# Additionally

If the sales region is IsSummary, then a child sales region will be created when the polygon is imported.
If you want to have a multipolygon, then create a sales region as IsSummary and use the Import \ Export processes on it.
If you want to get a specific version of a polygon, then open an entry in the Geometry tab and select the Export Geo JSON process.

# How to Install

* Install the PostGIS Database Extension (https://postgis.net). Make sure that the following query works for you: SELECT public.postgis_Version();
* Install the plugin using Felix
* Done!
