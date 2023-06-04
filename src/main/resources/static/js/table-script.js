var table = document.getElementById("device-table");
var isAsc = true;
var devicesPerPage = 10;
var currentPage = 1;

function sortCol(colNum) {

    if (currentPage !== 1) {
        changePage(1);
    }

    let col = document.getElementById("metric-"+colNum.toString()).innerText;

    if (devices.length === 0) return 0;

    if (isAsc) {
        if (isNaN(parseField(devices[0], col))) {
            devices = devices.sort((a, b) => parseField(a, col).localeCompare(parseField(b, col), undefined, {numeric: true}));

        } else {
            devices = devices.sort((a, b) => parseField(a, col) - parseField(b, col));
        }
    } else {
        if (isNaN(parseField(devices[0], col))) {
            devices = devices.sort((a, b) => parseField(b, col).localeCompare(parseField(a, col), undefined, { numeric: true }));
        } else {
            devices = devices.sort((a, b) => parseField(b, col) - parseField(a, col));
        }
    }
    isAsc = !isAsc

    createTable();
}

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
           window.location.href = "/node/"+device.indexSuffix;
        });
    }
}

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

function changePage(num) {
    currentPage = num;
    createTable();
    updatePaginate();
}

function prevPage() {
    if (currentPage > 1) {
        changePage(--currentPage);
    }
}

function nextPage() {
    if (currentPage < (devices.length / devicesPerPage)) {
        changePage(++currentPage);
    }
}

function lastPage() {
    currentPage = Math.ceil(devices.length / devicesPerPage)
    changePage(currentPage)
}

function setCol(colNum, metric) {
    let button = document.getElementById("metric-"+colNum.toString());
    button.innerText = metric;

    createTable();
}

function parseField(device, field) {
    let partition = field.split(".");

    switch (partition[0]) {
        case "Status": return device.online ? "Online" : "Offline";
        case "RAM": return partition.length === 3 ? parseRamField(device, partition[2]) : parseRamField(device, partition[1]);
        case "storage": return partition.length === 3 ? parseStorageField(device, partition[2]) : parseStorageField(device, partition[1]);
        case "CPU": return device.cpuUsage;
        case "upload": return parseBandwidthField(device,"u"+partition[1]);
        case "download": return parseBandwidthField(device, "d"+partition[1]);
        case "@timestamp": return device.timestamp;
        case "location": return parseLocationField(device, partition[1]);
        case "instrument": return partition[1] === "name" ? device.name : partition[1] === "type" ? device.type : "NaN";
        default: return "NaN";
    }
}

function parseRamField(device, field) {
    switch (field) {
        case "total": return device.ram.total;
        case "available": return device.ram.available;
        case "free": return device.ram.free;
        case "perc": return device.ram.usedPerc;
        case "bytes": return device.ram.usedBytes;
        default: return "NaN";
    }
}

function parseStorageField(device, field) {
    switch (field) {
        case "total": return device.storage.totalStorage;
        case "free": return device.storage.freeStorage;
        case "perc": return device.storage.usedPercStorage;
        case "bytes": return device.storage.usedBytesStorage;
        default: return "NaN";
    }
}

function parseBandwidthField(device, field) {
    switch (field) {
        case "usize": return device.bandwidth.uploadSize;
        case "uspeed": return device.bandwidth.uploadSpeed;
        case "dsize": return device.bandwidth.downloadSize;
        case "dspeed": return device.bandwidth.downloadSpeed;
        default: return "NaN";
    }
}

function parseLocationField(device, field) {
    switch (field) {
        case "coordinates": return device.location.coordinates;
        case "name": return device.location.name;
        case "elevation": return device.location.elevation;
        default: return  "NaN";
    }
}

function setStatusColor(device, cell) {
    if (device.online) {
        cell.style.color = "#4eb940";
    } else {
        cell.style.color = "#ad2626";
    }
}

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