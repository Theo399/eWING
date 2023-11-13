package com.example.ewing20.map

class GoogleMapDTO {
    var routes = ArrayList<Routes>()
}

class Routes {
    var legs = ArrayList<Legs>()
}

class Legs {
    var distance = Distance()
    var duration = Duration()
    var end_address = ""
    var end_location = Location()
    var start_address = ""
    var start_location = Location()
    var steps = ArrayList<Steps>()
}

class Steps {
    var distance = Distance()
    var duration = Duration()
    var end_location = Location()
    var maneuver = ""
    var polyline = PolyLine()
    var start_location = Location()
    var travel_mode = ""
}

class Duration {
    var text = ""
    var value = 0
}

class Distance {
    var text = ""
    var value = 0
}

class PolyLine {
    var points = ""
}

class Location {
    var lat = ""
    var lng = ""
}

