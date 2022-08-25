// Simulates a mosaic server.
//
// /color/<width>/<height>/<hex>  generates a tile for the color <hex>
//
var http = require('http');
var url = require('url');
var PNG = require('pngjs').PNG;

http.createServer(function (req, res) { 
  var pathname = url.parse(req.url).pathname;
  var m;
  if (m = pathname.match(/^\/color\/([0-9]+)\/([0-9]+)\/([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})/)) {
    var png = new PNG({
        width: parseInt(m[1]),
        height: parseInt(m[2]),
        filterType: -1
    });
    var red   = parseInt(m[3], 16);
    var green = parseInt(m[4], 16);
    var blue  = parseInt(m[5], 16);
    res.writeHead(200, {'Content-Type' : 'image/png'});
    for (var y = 0; y < png.height; y++) {
      for (var x = 0; x < png.width; x++) {
        var dx = 2.0 * x / png.width - 1.0, dy = 2.0 * y / png.height - 1.0
        var idx = (png.width * y + x) << 2;
        png.data[idx+0] = red;
        png.data[idx+1] = green;
        png.data[idx+2] = blue;
        if (dx * dx + dy * dy < 1.0) {
          png.data[idx+3] = 255;
        }
        else {
          png.data[idx+3] = 0;
        }
      }
    }
    png.pack().pipe(res);
    return;
  }
  res.writeHead(404, {'Content-Type': 'text/plain'});
  res.write('404 Not Found\n');
  res.end();
}).listen(8765, '0.0.0.0');

console.log('mosaic server running on port 8765');

