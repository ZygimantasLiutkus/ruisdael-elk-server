

const map = L.map('map').setView([51.90511, 4.37233], 9);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    minZoom: 0,
    maxZoom: 20,
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
}).addTo(map);


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


locations = [
    {
    "facility": "Almere",
    "location": [52.23349,5.17502]
    },
    {
    "facility": "Architecture BK",
    "location": [52.02128,4.22121]
    },
    {
    "facility": "Bolnes",
    "location": [51.53534,4.33662]
    },
    {
    "facility": "CITG Storage Room 0.87",
    "location": [51.59562,4.22317]
    },
    {
    "facility": "CITG Storage Room 00.50",
    "location": [51.59562,4.22317]
    },
    {
    "facility": "Cabauw",
    "location": [51.58129,4.55345]
    },
    {
    "facility": "Capelle a/d Ijssel",
    "location": [51.55388,4.3562]
    },
    {
    "facility": "De Zweth",
    "location": [51.57517,4.23404]
    },
    {
    "facility": "Delfshaven",
    "location": [51.541822,4.264625]
    },
    {
    "facility": "EWI Roof",
    "location": [51.59559,4.22245]
    },
    {
    "facility": "Erasmus MC Roof",
    "location": [51.54451,4.28132]
    },
    {
    "facility": "Heijplaat",
    "location": [51.53503,4.25221]
    },
    {
    "facility": "Hoofddorp",
    "location": [52.16541,4.41585]
    },
    {
    "facility": "Lansingerland",
    "location": [52.03726,4.31436]
    },
    {
    "facility": "Lutjewad",
    "location": [53.24129,6.21923]
    },
    {
    "facility": "Ommoord",
    "location": [51.57303,4.32508]
    },
    {
    "facility": "Oost",
    "location": [51.55310,4.32516]
    },
    {
    "facility": "Ridderkerk",
    "location": [51.52403,4.35643]
    },
    {
    "facility": "Slufter",
    "location": [51.56059,3.59594]
    },
    {
    "facility": "Spaanse Polder",
    "location": [51.55568,4.24584]
    }
]

locations.forEach(
    function(loc){
        coin = Math.floor(Math.random() * 2) == 0
        return L.marker(loc['location'], {icon: coin ? greenIcon : redIcon}).addTo(map);
    }
);