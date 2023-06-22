import { setUp, reset, handleButton } from "./pagination.js";
import { loadTextBox, loadStatuses, loadSlider, setLeftValue, setRightValue, hideAll } from "./filters.js";

const table = document.getElementById("device-table");
const docTitle = document.title;
let isAsc = true;
let currentPage = 1;
let lastSorted = 0;
let filter = "status";
const devicesPerPage = 10;
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

window.addEventListener("load", init);

/**
 * Function that connects to the websockets and sends messages to the '/app/devices'
 * message path with an interval set by the ApplicationConfig.
 */
function connect() {
    client.connect({}, () => {
        interval = setInterval(() => {
            client.send('/app/devices', {});
        }, websocketDelay);

        client.subscribe('/topic/devices', (d) => {
            devices = JSON.parse(d.body);
            // Once updated devices are received, the table is recreated
            // and the columns are sorted twice to restore the previous order.
            // createTable();
            createStatuses();
            // currentPage = Math.min(currentPage, Math.ceil(devices.lenght / 10.0));
            let tempPage = currentPage;     // Sorting switches page to 1. To not have the
            sortCol(lastSorted);            // view reset to page 1 every minute, current
            sortCol(lastSorted);            // page is saved.
            reset(Math.ceil(devices.length / 10.0));
            updatePaginate(tempPage);
            if (docTitle === "Ruisdael Monitoring | Overview") {
                // Reload status boxes
                createStatuses();
                // Reload map
                reloadMarkers(savedMetric);
                mapFilter(savedMetric);
            }
        });

        client.subscribe('/topic/enable', (d) => {
            client.send('/app/devices', {});
            createTable();
        });
        client.subscribe('/topic/disable', (d) => {
            client.send('/app/devices', {});
            createTable();
        });
        client.subscribe('/topic/delete', (d) => {
            client.send('/app/devices', {});
            createTable();
        });
    });
}

/**
 * Function that disconnects from the websockets.
 */
function disconnect() {
    client.disconnect();
    interval = null;
}

// The initialize function sets up all the event listeners for the elements in the HTML page, and calls the
// createTable() function, as well as the setUp() function which initializes the Pagination.
function init() {

    // Runs the `connect()` function on load to create a connection with the websockets
    connect();

    if (document.title === "Ruisdael Monitoring | Device List") {
        document.getElementById("btn-reset-table").addEventListener("click", () => {
            currentPage = 1;
            resetTable();
            createTable();
        });

        document.getElementById("btn-search").addEventListener("click", search);
        document.getElementById("dropdown-name").addEventListener("click", () => {
            filter = "name";
            loadTextBox("Instrument Name");
            });
        document.getElementById("dropdown-location").addEventListener("click", () => {
            filter = "location";
            loadTextBox("Location");
        });
        document.getElementById("dropdown-status").addEventListener("click", () => {
            filter = "status";
            loadStatuses("Statuses");
        });
        document.getElementById("dropdown-storage").addEventListener("click", () => {
            filter = "storage";
            loadSlider("Storage Used (%)");
        });
        document.getElementById("dropdown-ram").addEventListener("click", () => {
            filter = "RAM";
            loadSlider("RAM Used (%)");
        });

        let inputLeft = document.getElementById("input-left");
        let inputRight = document.getElementById("input-right");
        let thumbLeft = document.querySelector(".slider > .thumb.left");
        let thumbRight = document.querySelector(".slider > .thumb.right");

        inputLeft.addEventListener("input", setLeftValue);
        inputRight.addEventListener("input", setRightValue);

        inputLeft.addEventListener("mousedown", function() {
            thumbLeft.classList.add("active");
        });
        inputLeft.addEventListener("mouseup", function() {
            thumbLeft.classList.remove("active");
        });

        inputRight.addEventListener("mouseover", function() {
            thumbRight.classList.add("hover");
        });

        inputRight.addEventListener("mouseout", function() {
            thumbRight.classList.remove("hover");
        });

        inputRight.addEventListener("mousedown", function() {
            thumbRight.classList.add("active");
        });

        inputRight.addEventListener("mouseup", function() {
            thumbRight.classList.remove("active");
        });

    }

    document.getElementById("sort-arrow-col-0").addEventListener("click", () => sortCol(0));
    document.getElementById("sort-arrow-col-1").addEventListener("click", () => sortCol(1));
    document.getElementById("sort-arrow-col-2").addEventListener("click", () => sortCol(2));

    for (let element of document.getElementsByClassName("dropdown-item zero")) {
        element.addEventListener("click", () => setCol(0, element.innerText));
    }
    for (let element of document.getElementsByClassName("dropdown-item one")) {
        element.addEventListener("click", () => setCol(1, element.innerText));
    }
    for (let element of document.getElementsByClassName("dropdown-item two")) {
        element.addEventListener("click", () => setCol(2, element.innerText));
    }
    createTable();
    // Use to compute the number of overall pages
    // const pageNum = Math.round(Math.ceil(devices.length / 10.0));
    setUp(Math.ceil(devices.length / 10.0));
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
        if (device.status !== "DISABLED" || docTitle === "Ruisdael Monitoring | Device List") {
            row.insertCell(0).innerText = parseField(device, document.getElementById("metric-0").innerText);
            row.insertCell(1).innerText = parseField(device, document.getElementById("metric-1").innerText);
            row.insertCell(2).innerText = parseField(device, document.getElementById("metric-2").innerText);
            setStatusColorAll(device, row);

            row.cells[0].addEventListener("click", () => {
                window.location.href = "/node/" + device.name;
            });
            row.cells[1].addEventListener("click", () => {
                window.location.href = "/node/" + device.name;
            });
            row.cells[2].addEventListener("click", () => {
                window.location.href = "/node/" + device.name;
            });
        }
        if (docTitle === "Ruisdael Monitoring | Device List") {
            let n = i.toString();
            row.insertCell(3).innerHTML = `<button id="enable-button`+n+`" class="btn-secondary">Enable</button>
                    <button id="disable-button`+n+`" class="btn-secondary">Disable</button>
                    <button id="delete-button`+n+`" class="btn-secondary">Delete</button>`;
            row.cells[3].classList.add("none");

            if (device.status === "DISABLED") {
                let tempBtn = document.getElementById("disable-button"+i.toString());
                tempBtn.disabled = true;
            } else {
                let tempBtn = document.getElementById("enable-button"+i.toString());
                tempBtn.disabled = true;
            }

            document.getElementById("enable-button"+i.toString()).addEventListener("click", () => {
                client.send('/app/enable/collector_'+device.name, {});
            });
            document.getElementById("disable-button"+i.toString()).addEventListener("click", () => {
                client.send('/app/disable/collector_'+device.name, {});
            });
            document.getElementById("delete-button"+i.toString()).addEventListener("click", () => {
                client.send('/app/delete/collector_'+device.name, {});
            });
        }
    }
}

/** Function is used to query the Elasticsearch database and retrieve the list of all devices, when the
 * reset table button is clicked.
 */
function resetTable() {
    client.send('/app/devices', {});
}

/* Function to update the view of the page control (number row under the table).
*/
function sortCol(colNum) {
    let col = document.getElementById("metric-" + colNum.toString()).innerText;

    if (devices.length === 0) {
        return;
    }

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
    // Changes the arrows from up to down, and vice versa
    if (isAsc) {
        document.getElementById("sort-arrow-col-" + colNum.toString()).innerHTML = `<i class="bi bi-arrow-up"></i>`;
    } else {
        document.getElementById("sort-arrow-col-" + colNum.toString()).innerHTML = `<i class="bi bi-arrow-down"></i>`;
    }
    isAsc = !isAsc
    lastSorted = colNum;
    createTable();
}

export function updatePaginate(curPage) {
    currentPage = curPage;
    createTable();
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
    } else if (device.status === "OFFLINE") {
        cell.innerText = "Offline";
        cell.style.color = "#ad2626";
    } else if (device.status === "DISABLED"){
        cell.innerText = "Disabled";
        cell.style.color = "#a0a0a0"
    } else {
        cell.innerText = $.camelCase(device.status);
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
 * This function is used to query the list of devices based on their tags. A tag can be one of the following:
 * Instrument Name, Location, Status (Online, Warning, Offline).
 */
function search() {
    let radioButtons = document.getElementsByClassName("form-check-input");
    let tag = document.getElementById("search-tag").value.trim().toLowerCase();
    let found = [];
    filter = filter.toLowerCase();

    if (filter === "storage" || filter === "ram") {
        searchNum();
    } else {
        if (radioButtons !== null) {
            for (let i = 0; i < radioButtons.length; i++) {
                if (radioButtons.item(i).checked) {
                    tag = radioButtons.item(i).id.toString().split("-")[0];
                    break;
                }
            }
        }
        for (let i = 0; i < devices.length; i++) {
            let deviceTag = "";
            if (filter === "location") {
                deviceTag = devices[i][filter]["name"].toString().toLowerCase();
            } else if (filter === "name") {
                deviceTag = devices[i]["instrument"][filter].toString().toLowerCase();
            } else if (filter === "status") {
                deviceTag = devices[i]["status"].toString().toLowerCase();
            }
            if (deviceTag.startsWith(tag.trim().toLowerCase())) {
                found.push(devices[i]);
            }
        }
        devices = found;
    }
    setUp(Math.round(Math.ceil(devices.length / 10.0)));
    updatePaginate(1);
    if (found.length > 0) {
        handleButton(document.getElementById("btn-first-page"));
    }
}

function searchNum() {
    let min = parseInt(document.getElementsByClassName("thumb left")[0].style.left.toString().replace("%", ""));
    let max = 100 - parseInt(document.getElementsByClassName("thumb right")[0].style.right.toString().replace("%", ""));
    let found = [];

    for (let i = 0; i < devices.length; i++) {
        let device = devices[i];
        if (filter == "storage") {
            let perc = device[filter]["usedPercStorage"];
            if (min <= perc && perc <= max) {
                found.push(device);
            }
        } else {
            let perc = device[filter]["usedPerc"];
            if (min <= perc && perc <= max) {
                found.push(device);
            }
        }
    }
    devices = found;
}

/**
 * Function to create/update the status squares and their colors.
 */
function createStatuses() {

    if (document.title === "Ruisdael Monitoring | Device List") {
        return;
    }

    let container = document.getElementsByClassName("status-container")[0];
    let innerHTML = "";
    for (let i = 0; i < devices.length; i++) {
        let style = devices[i].status === "ONLINE" ? "#4eb940" :
            devices[i].status === "WARNING" ? "#b9a940" :
                "#ad2626";
        if (devices[i].status !== "DISABLED") {
            innerHTML += `<div class="status-div-container"><div class="status-div" title="` + devices[i].name + `" style="background: ` + style + `">
                <a class="status-div" href="/node/` + devices[i].name + `"></a>
                </div></div>`;
        }
    }
    container.innerHTML = innerHTML;
}