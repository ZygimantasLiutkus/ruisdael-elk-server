var table = document.getElementById("device-table");
var isAsc = true;
var devicesPerPage = 10;
var currentPage = 1;

function sortCol(col) {

    if (currentPage !== 1) {
        changePage(1);
    }

    if (isAsc) {
        if (col === "online") {
            devices = devices.sort((a, b) => a["online"] - b["online"]);
        } else {
            devices = devices.sort((a, b) => a[col].localeCompare(b[col], undefined, {numeric: true}));
        }
    } else {
        if (col === "online") {
            devices = devices.sort((a, b) => b["online"] - a["online"]);
        } else {
            devices = devices.sort((a, b) => b[col].localeCompare(a[col], undefined, { numeric: true }));
        }
    }
    isAsc = !isAsc

    for (let i = 0; i < devices.length; i++) {
        let device = devices[i];
        let row = table.rows[i + 1];

        row.cells[0].innerText = device.name;
        row.cells[1].innerText = device.location.coordinates;
        row.cells[2].innerText = device.online ? "Online" : "Offline";

        // In order to change the colors
        setStatusColor(device, row.cells[2]);
    }
}

function createTable() {
    let start = devicesPerPage * (currentPage - 1);
    let end = start + devicesPerPage;
    let selectedDevices = devices.slice(start, end);

    while (table.rows.length > 1) {
        table.deleteRow(1);
    }

    for (let i = 0; i < selectedDevices.length; i++) {
        let row = table.insertRow(table.rows.length);
        let device = selectedDevices[i];
        row.insertCell(0).innerHTML = `<td><a class="node-link" href="/node/`+ device.name +`">`+ device.name +`</a></td>`;
        row.insertCell(1).innerHTML = `<td>` + device.location.coordinates + `</td>`;
        let cell3 = row.insertCell(2);
        cell3.innerText = device.online ? "Online" : "Offline";
        setStatusColor(device, cell3);
    }
}

function updatePaginate() {
    let listItems = document.getElementsByClassName("page-link");
    let currentItem = listItems.item(currentPage);
    currentItem.style.backgroundColor = "white";
    currentItem.style.color = "#0a58ca";

    for (let i = 1; i < listItems.length - 1; i++) {
        if (i === currentPage) {
            continue;
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
    button.innerHTML = metric;
    for (let i = 0; i < devices.length; i++) {
        let device = devices[i];
        let row = table.rows[i+1];

        row.cells[0].innerHTML = parseField(device, document.getElementById("metric-0").innerHTML);
        row.cells[1].innerHTML = parseField(device, document.getElementById("metric-1").innerHTML);
        row.cells[2].innerHTML = parseField(device, document.getElementById("metric-2").innerHTML);

        if (document.getElementById("metric-0").innerHTML === "Status") {
            setStatusColor(device, row.cells[0]);
        } else {
            row.cells[0].style.color = "#ffffff";
        }

        if (document.getElementById("metric-1").innerHTML === "Status") {
            setStatusColor(device, row.cells[1]);
        } else {
            row.cells[1].style.color = "#ffffff";
        }

        if (document.getElementById("metric-2").innerHTML === "Status") {
            setStatusColor(device, row.cells[2]);
        } else {
            row.cells[2].style.color = "#ffffff";
        }
    }
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