var table = document.getElementById("device-table");
var isAsc = true;
var lastSorted = 0;
var devicesPerPage = 10;
var currentPage = 1;
const metricMapping = new Map([
    ["Status", "Status"],
    ["Total RAM", "RAM.total"],
    ["Available RAM", "RAM.available"],
    ["Used RAM (%)", "RAM.used.perc"],
    ["Used RAM (B)", "RAM.used.bytes"],
    ["Free RAM", "RAM.free"],
    ["Total Storage", "storage.total"],
    ["Used Storage (B)", "storage.used.bytes"],
    ["Free Storage", "storage.free"],
    ["Used Storage (%)", "storage.used.perc"],
    ["CPU", "CPU"],
    ["Upload Size", "upload.size"],
    ["Download Size", "download.size"],
    ["Upload Speed", "upload.speed"],
    ["Download Speed", "download.speed"],
    ["Latest Timestamp", "@timestamp"],
    ["Location Coordinates", "location.coordinates"],
    ["Location Elevation", "location.elevation"],
    ["Instrument Name", "instrument.name"],
    ["Location Name", "location.name"],
    ["Instrument Type", "instrument.type"]
]);
let interval = null;
let socket = new SockJS('/device-update');
let client = Stomp.over(socket);

// Runs the `connect()` function on load to create a connection with the websockets
connect();

/**
 * Function that connects to the websockets and sends messages to the '/app/devices'
 * message path every 60 second intervals.
 */
function connect() {
    client.connect({}, () => {
        interval = setInterval(() => {
            client.send('/app/devices', {});
        }, 60000);

        client.subscribe('/topic/devices', (d) => {
            devices = JSON.parse(d.body);
            // Once updated devices are received, the table is recreated
            // and the columns are sorted twice to restore the previous order.
            // createTable();
            createStatuses();
            let tempPage = currentPage;     // Sorting switches page to 1. To not have the
            sortCol(lastSorted);            // view reset to page 1 every minute, current
            sortCol(lastSorted);            // page is saved.
            changePage(tempPage);
        });
    });
}

/**
 * Function that disconnects from the websockets.
 */
function disconnect() {
    client.disconnect()
    interval = null;
}

/**
 * Function to sort the columns of the device table.
 * @param colNum the column number which to sort.
 */
function sortCol(colNum) {

    if (currentPage !== 1) {
        changePage(1);
    }

    let col = document.getElementById("metric-" + colNum.toString()).innerText;

    if (isAsc) {
        if (isNaN(parseField(devices[0], col))) {
            devices.sort((a, b) => parseField(a, col).localeCompare(parseField(b, col), undefined, {numeric: true}));

        } else {
            devices.sort((a, b) => parseField(a, col) - parseField(b, col));
        }
    } else {
        if (isNaN(parseField(devices[0], col))) {
            devices.sort((a, b) => parseField(b, col).localeCompare(parseField(a, col), undefined, {numeric: true}));
        } else {
            devices.sort((a, b) => parseField(b, col) - parseField(a, col));
        }
    }
    isAsc = !isAsc
    lastSorted = colNum;
    createTable();
}

/**
 * Function to populate the table with information about devices.
 */
function createTable() {
    let start = devicesPerPage * (currentPage - 1);
    let end = start + devicesPerPage;
    let selectedDevices = devices.slice(start, end);
    let tbody = table.getElementsByTagName("tbody")[0];

    while (tbody.rows.length > 0) {
        tbody.deleteRow(0);
    }

    for (let i = 0; i < selectedDevices.length; i++) {
        let row = tbody.insertRow(tbody.rows.length);
        let device = selectedDevices[i];
        row.insertCell(0).innerText = parseField(device, document.getElementById("metric-0").innerText);
        row.insertCell(1).innerText = parseField(device, document.getElementById("metric-1").innerText);
        row.insertCell(2).innerText = parseField(device, document.getElementById("metric-2").innerText);
        setStatusColorAll(device, row);

        row.addEventListener("click", () => {
            window.location.href = "/node/" + device.name;
        });
    }
}

/**
 * Function to update the view of the page control (number row under the table).
 */
function updatePaginate() {
    let listItems = document.getElementsByClassName("page-link");
    let currentItem = listItems.item(currentPage);
    currentItem.style.backgroundColor = "white";
    currentItem.style.color = "#0a58ca";

    for (let i = 1; i < listItems.length - 1; i++) {
        if (i === currentPage) {
            // continue
        } else {
            listItems.item(i).style.backgroundColor = "transparent";
            listItems.item(i).style.color = "white";
        }
    }

}

/**
 * Function to switch device table page number.
 * @param num the page number.
 */
function changePage(num) {
    currentPage = num;
    createTable();
    updatePaginate();
}

/**
 * Function to switch to the previous page.
 */
function prevPage() {
    if (currentPage > 1) {
        changePage(--currentPage);
    }
}

/**
 * Function to switch to the next page.
 */
function nextPage() {
    if (currentPage < (devices.length / devicesPerPage)) {
        changePage(++currentPage);
    }
}

/**
 * Function to switch to the last page.
 */
function lastPage() {
    currentPage = Math.ceil(devices.length / devicesPerPage)
    changePage(currentPage)
}

/**
 * Function to set a column metric.
 * @param colNum the column whose metric will be set.
 * @param metric the metric to be displayed in the table.
 */
function setCol(colNum, metric) {
    let button = document.getElementById("metric-" + colNum.toString());
    button.innerText = metric;

    createTable();
}

/**
 * Function to parse a device for its data depending on the field.
 * @param device the device to be parsed.
 * @param field the field describing info to be retrieved from the device.
 * @returns value of the given field of the given device.
 */
function parseField(device, field) {
    const mapped = metricMapping.get(field);
    const partition = mapped.split(".");

    switch (partition[0]) {
        case "Status":
            return device.status;
        case "RAM":
            return partition.length === 3 ? parseRamField(device, partition[2]) : parseRamField(device, partition[1]);
        case "storage":
            return partition.length === 3 ? parseStorageField(device, partition[2]) : parseStorageField(device, partition[1]);
        case "CPU":
            return device.cpuUsage;
        case "upload":
            return parseBandwidthField(device, "u" + partition[1]);
        case "download":
            return parseBandwidthField(device, "d" + partition[1]);
        case "@timestamp":
            return device.timestamp;
        case "location":
            return parseLocationField(device, partition[1]);
        case "instrument":
            return partition[1] === "name" ? device.instrument.name : partition[1] === "type" ? device.instrument.type : "NaN";
        default:
            return "NaN";
    }
}

/**
 * Function to parse a device for info about its RAM.
 * @param device the device to be parsed.
 * @param field the field of ram to be selected.
 * @returns value of ram-related metric.
 */
function parseRamField(device, field) {
    switch (field) {
        case "total":
            return device.ram.total;
        case "available":
            return device.ram.available;
        case "free":
            return device.ram.free;
        case "perc":
            return device.ram.usedPerc;
        case "bytes":
            return device.ram.usedBytes;
        default:
            return "NaN";
    }
}

/**
 * Function to parse a device for info about its storage.
 * @param device the device to be parsed.
 * @param field the field of storage to be selected.
 * @returns value of storage-related metric.
 */
function parseStorageField(device, field) {
    switch (field) {
        case "total":
            return device.storage.totalStorage;
        case "free":
            return device.storage.freeStorage;
        case "perc":
            return device.storage.usedPercStorage;
        case "bytes":
            return device.storage.usedBytesStorage;
        default:
            return "NaN";
    }
}

/**
 * Function to parse a device for info about its bandwidth.
 * @param device the device to be parsed.
 * @param field the field of bandwidth to be selected.
 * @returns value of bandwidth-related metric.
 */
function parseBandwidthField(device, field) {
    switch (field) {
        case "usize":
            return device.bandwidth.uploadSize;
        case "uspeed":
            return device.bandwidth.uploadSpeed;
        case "dsize":
            return device.bandwidth.downloadSize;
        case "dspeed":
            return device.bandwidth.downloadSpeed;
        default:
            return "NaN";
    }
}

/**
 * Function to parse a device for info about its location.
 * @param device the device to be parsed.
 * @param field the field of location to be selected.
 * @returns value of location-related metric.
 */
function parseLocationField(device, field) {
    switch (field) {
        case "coordinates":
            return device.location.humanreadableCoordinates;
        case "name":
            return device.location.name;
        case "elevation":
            return device.location.elevation;
        default:
            return "NaN";
    }
}

/**
 * Function to set the color of a cell of a device if it displays the 'Status' metric.
 * @param device the device whose status is checked.
 * @param cell the cell to be colored.
 */
function setStatusColor(device, cell) {
    if (device.status === "ONLINE") {
        cell.innerText = "Online";
        cell.style.color = "#4eb940";
    } else if (device.status === "WARNING") {
        cell.innerText = "Warning";
        cell.style.color = "#b9a940ff";
    } else {
        cell.innerText = "Offline";
        cell.style.color = "#ad2626";
    }
}

/**
 * Function to check and color column content if they contain the 'Status' metric
 * @param device the device whose info will be checked.
 * @param row the row whose columns will be checked.
 */
function setStatusColorAll(device, row) {
    if (document.getElementById("metric-0").innerText === "Status") {
        setStatusColor(device, row.cells[0]);
    } else {
        row.cells[0].style.color = "#ffffff";
    }

    if (document.getElementById("metric-1").innerText === "Status") {
        setStatusColor(device, row.cells[1]);
    } else {
        row.cells[1].style.color = "#ffffff";
    }

    if (document.getElementById("metric-2").innerText === "Status") {
        setStatusColor(device, row.cells[2]);
    } else {
        row.cells[2].style.color = "#ffffff";
    }
}

/**
 * Function to create/update the status squares and their colors.
 */
function createStatuses() {
    let container = document.getElementsByClassName("status-container")[0];
    let innerHTML = "";
    for (let i = 0; i < devices.length; i++) {
        let style = devices[i].status === "ONLINE" ? "#4eb940" :
            devices[i].status === "WARNING" ? "#b9a940" :
                "#ad2626";
        innerHTML+=`<div class="status-div-container"><div class="status-div" title="`+devices[i].name+`" style="background: `+style+`">
                    <a class="status-div" href="/node/`+devices[i].name+`"></a>
                    </div></div>`;
    }
    container.innerHTML = innerHTML;
}