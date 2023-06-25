// Default Metric
let savedMetric = 'Status'

// The map object
const map = L.map('map').setView([51.90511, 4.37233], 9);
// L is leaflet global object
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 0,
    maxZoom: 20,
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
}).addTo(map);

let nodes = devices.map(d => deviceToNode(d));


/**
 * Function to reload the nodes.
 * Takes the devices array and turns them to nodes 
 */
function reloadNodes() {
    nodes = devices.map(d => deviceToNode(d));
}

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

// An array of all markers, default is Up-Status
let markersArray = nodes.map(node => nodeToMarker(node, savedMetric));
// First Time On Load
fillMap(markersArray);


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
            'Status' : device.status,
            'CPU': device.cpuUsage,
            'RAM': device.ram.usedPerc,
            'Storage' : device.storage.usedPercStorage
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
function getNodeMarker(node, metric) {
    const value = node['metrics'][metric];

    switch(metric) {
        case 'Status':
            switch(value) {
                case 'ONLINE':
                    return greenIcon;
                case 'WARNING':
                    return yellowIcon;
                default:
                    return redIcon;
            }
        case 'CPU':
            if(value > CPU_CRITICAL_THRESHOLD) return redIcon;
            if(value > CPU_WARNING_THRESHOLD) return yellowIcon;
            break;
        case 'RAM':
            if(value > RAM_CRITICAL_THRESHOLD) return redIcon;
            if(value > RAM_WARNING_THRESHOLD) return yellowIcon;
            break;
        case 'Storage':
            if(value > STORAGE_CRITICAL_THRESHOLD) return redIcon;
            if(value > STORAGE_WARNING_THRESHOLD) return yellowIcon;
            break;
    }

    return greenIcon;
}


// Takes a node from the array and returns a marker
// metric : Up-Status | CPU | Storage | ... 
function nodeToMarker(node, metric){
    var icon = getNodeMarker(node, metric);

    marker = L.marker(node['location'], {icon})

    hoverText = 'Node: ' + node.id + '<br>Location: ' + node.facility + '<br>' + metric + ": "
        + JSON.stringify(node.metrics[metric])
            .replaceAll("\"", "")
            .replaceAll("{", "")
            .replaceAll("}", "")
            .replaceAll(":", ": ");

    switch(metric) {
        case 'CPU':
        case 'RAM':
        case 'Storage':
            hoverText += '%'
            break;
    }

    marker.bindPopup(hoverText);
    marker.on('mouseover', function(e){this.openPopup();});
    marker.on('mouseout', function(e){this.closePopup();});

    marker.on('click', function(e){window.location.href = "/node/"+node.id;});

    return marker;
}


/**
 * Function to reload markers with the given metric.
 * @param metric the metric to reload with.
 */
function reloadMarkers(metric) {
    reloadNodes();
    markersArray = nodes.map(loc => nodeToMarker(loc, metric));
}

function fillMap(array){
    array.forEach(marker => map.addLayer(marker));
}

function emptyMap(){
    markersArray.forEach(marker=> map.removeLayer(marker));
}


// Callback function for the input radios on overview.html
// metric : upStatus, cpuStatus, ramStatus, storageStatus
function mapFilter(metric) {
    emptyMap();
    fillMap(nodes.map(loc=>nodeToMarker(loc, metric)));
    savedMetric = metric;
}
