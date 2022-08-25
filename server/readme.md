Photo Mosaic Server
-------------------

The server runs on port 8765 and accepts requests of the following form: /color/<width>/<height>/<hex>

<width> and <height> should be integers such as 32
<hex> should be a 6 digit hexadecimal string in RRGGBB format such as abc123

Installation Instructions for Mac OS X:
* Install homebrew from http://brew.sh/
* Install node: brew install npm

Installation Instructions for Ubuntu:
* Install node (note that this might remove a package named node, which you won't need unless you're into amateur packet radio): sudo apt-get install nodejs-legacy npm

Installation Instructions for Windows:
* Install node from https://nodejs.org/en/

Running:
* Download dependencies: npm install
* Start the server: npm start
* Check it's working by loading this URL in your favourite browser: http://localhost:8765/color/32/32/abc123  
* To access localhost from the android emulator use IP 10.0.2.2

