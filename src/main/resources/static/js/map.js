// map object
const map = L.map('map').setView([51.90511, 4.37233], 9);

// L is leaflet global object
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 0,
    maxZoom: 20,
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
}).addTo(map);

nodes = devices.map(d => deviceToNode(d));

// Dummy data for the nodes
// [
//     {
//     'id': 1,
//     "facility": "Almere",
//     "location": [52.23349,5.17502],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//     'id': 2,
//     "facility": "Architecture BK",
//     "location": [52.02128,4.22121],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 3,
//     "facility": "Bolnes",
//     "location": [51.53534,4.33662],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 4,
//     "facility": "CITG Storage Room 0.87",
//     "location": [51.59562,4.22317],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 5,
//     "facility": "CITG Storage Room 00.50",
//     "location": [51.59562,4.22317],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 6,
//     "facility": "Cabauw",
//     "location": [51.58129,4.55345],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 7,
//     "facility": "Capelle a/d Ijssel",
//     "location": [51.55388,4.3562],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 8,
//     "facility": "De Zweth",
//     "location": [51.57517,4.23404],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 9,
//     "facility": "Delfshaven",
//     "location": [51.541822,4.264625],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 10,
//     "facility": "EWI Roof",
//     "location": [51.59559,4.22245],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 1,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 11,
//     "facility": "Erasmus MC Roof",
//     "location": [51.54451,4.28132],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 2,
//         'ramStatus' : 0,
//         'storageStatus' : 1
//     }
//     },
//     {
//         'id': 12,
//     "facility": "Heijplaat",
//     "location": [51.53503,4.25221],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 13,
//     "facility": "Hoofddorp",
//     "location": [52.16541,4.41585],
//     "metrics":{
//         'upStatus' : 1,
//         'cpuStatus' : 0,
//         'ramStatus' : 1,
//         'storageStatus' : 2
//     }
//     },
//     {
//         'id': 14,
//     "facility": "Lansingerland",
//     "location": [52.03726,4.31436],
//     "metrics":{
//         'upStatus' : 1,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 1
//     }
//     },
//     {
//         'id': 15,
//     "facility": "Lutjewad",
//     "location": [53.24129,6.21923],
//     "metrics":{
//         'upStatus' : 2,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 16,
//     "facility": "Ommoord",
//     "location": [51.57303,4.32508],
//     "metrics":{
//         'upStatus' : 2,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 2
//     }
//     },
//     {
//         'id': 17,
//     "facility": "Oost",
//     "location": [51.55310,4.32516],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 1,
//         'storageStatus' : 2
//     }
//     },
//     {
//         'id': 18,
//     "facility": "Ridderkerk",
//     "location": [51.52403,4.35643],
//     "metrics":{
//         'upStatus' : 2,
//         'cpuStatus' : 1,
//         'ramStatus' : 2,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 19,
//     "facility": "Slufter",
//     "location": [51.56059,3.59594],
//     "metrics":{
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     },
//     {
//         'id': 20,
//     "facility": "Spaanse Polder",
//     "location": [51.55568,4.24584],
//     "metrics": {
//         'upStatus' : 0,
//         'cpuStatus' : 0,
//         'ramStatus' : 0,
//         'storageStatus' : 0
//     }
//     }
// ]

var greenIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});
var redIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});
var yellowIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-yellow.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});


// Takes a node from the array and returns a marker
// metric : upStatus | cpuStatus | storageStatus | ... 
function nodeToMarker(node, metric){
    var icon = getIcon(node, metric);

    marker = L.marker(node['location'], {icon})

    hoverText = '#' + node.id + ' ' + node.facility + ': ' + metric + ": <br>"
        + JSON.stringify(node.metrics[metric])
            .replaceAll("\"", "")
            .replaceAll("{", "")
            .replaceAll("}", "")
            .replaceAll(":", ": ");
    marker.bindPopup(hoverText);
    marker.on('mouseover', function(e){this.openPopup();});
    marker.on('mouseout', function(e){this.closePopup();});

    marker.on('click', function(e){window.location.href = "/node/"+node.id;});

    return marker;
}


// An array of all markers, default is upStatus
markersArray = nodes.map(loc => nodeToMarker(loc, 'Up Status'));


function fillMap(array){
    array.forEach(marker => map.addLayer(marker));
}

function emptyMap(){
    markersArray.forEach(marker=> map.removeLayer(marker));
}

// First Time On Load
fillMap(markersArray);


// Callback function for the input radios on overview.html
// metric : upStatus, cpuStatus, ramStatus, storageStatus
function mapFilter(metric) {
    emptyMap();
    fillMap(nodes.map(loc=>nodeToMarker(loc, metric)));
}

/**
 * Transforms device data to node format to be displayed in the map.
 *
 * @param device the device to be parsed.
 * @returns a map in the required structure of a node.
 */
function deviceToNode(device) {
    return {
        'id': device.name,
        'facility': device.location.name,
        'location': [device.location.longitude, device.location.latitude],
        'metrics': {
            'Up Status' : device.online ? 0 : 2,
            'CPU/RAM' : {
                'CPU (%)': device.cpuUsage,
                'RAM (%)': device.ram.usedPerc
            },
            'Storage (%)' : device.storage.usedPercStorage
        }
    }
}

/**
 * Determines the color of the icon to be displayed in the map by taking into account the given metric value.
 *
 * @param node the node with metrics and metadata.
 * @param metric the metric that determines icon type.
 * @returns {L.Icon} an icon to be displayed in the map.
 */
function getIcon(node, metric) {
    // TODO: implement the outlier detection to determine which colors the nodes should be.
    return greenIcon;
}