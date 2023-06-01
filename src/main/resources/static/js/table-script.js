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
        row.cells[1].innerText = device.location;
        row.cells[2].innerText = device.online ? "Online" : "Offline";

        // In order to change the colors
        if (device.online) {
            row.cells[2].style.color = "#4eb940";
        } else {
            row.cells[2].style.color = "#ad2626";
        }
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
        row.insertCell(1).innerHTML = `<td>` + device.location + `</td>`;
        let cell3 = row.insertCell(2);
        cell3.innerText = device.online ? "Online" : "Offline";
        if (device.online) {
            cell3.style.color = "#4eb940";
        } else {
            cell3.style.color = "#ad2626";
        }
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