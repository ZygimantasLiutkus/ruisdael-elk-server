var table = document.getElementById("device-table");
var isAsc = true;
var devicesPerPage = 10;
var currentPage = 1;
var isSorting = false;

function sortCol(col) {

    if (currentPage !== 1) {
        changePage(1);
    }
    let deviceList = devices;

    if (isSorting) {
        deviceList = [];
        for (let i = 1; i < table.rows.length; i++) {
            let row = table.rows[i];
            deviceList.push({
                "name": row.cells[0].innerText,
                "location": row.cells[1].innerText,
                "online": row.cells[2].innerText === "Online"
            });
        }
    }

    if (isAsc) {
        if (col === "online") {
            deviceList.sort((a, b) => a["online"] - b["online"]);
        } else {
            deviceList.sort((a, b) => a[col].localeCompare(b[col], undefined, {numeric: true}));
        }
    } else {
        if (col === "online") {
            deviceList.sort((a, b) => b["online"] - a["online"]);
        } else {
            deviceList.sort((a, b) => b[col].localeCompare(a[col], undefined, { numeric: true }));
        }
    }
    isAsc = !isAsc

    for (let i = 0; i < deviceList.length; i++) {
        let device = deviceList[i];
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

function createTable(devs) {
    let deviceList = (devs != null) ? devs : devices;
    let start = devicesPerPage * (currentPage - 1);
    let end = start + devicesPerPage;
    let selectedDevices = deviceList.slice(start, end);

    while (table.rows.length > 1) {
        table.deleteRow(1);
    }

    for (let i = 0; i < selectedDevices.length; i++) {
        let row = table.insertRow(table.rows.length);
        let device = selectedDevices[i];
        row.insertCell(0).innerHTML = `<td><a class="node-link" href="/node/`+ device.name +`">`+ device.name +`</a></td>`;
        row.insertCell(1).innerHTML = `<td>` + device.location.humanreadableCoordinates + `</td>`;
        let cell3 = row.insertCell(2);
        cell3.innerText = device.online ? "Online" : "Offline";
        if (device.online) {
            cell3.style.color = "#4eb940";
        } else {
            cell3.style.color = "#ad2626";
        }
    }
}

// Requires further integration with the pagination, for now if we want to view all the found
// elements on page 2, it resets the table and displays all the devices.
function search() {
    let radioButtons = document.getElementsByClassName("form-check-input");
    let tag = document.getElementById("search-tag").value.trim().toLowerCase();
    let found = [];
    let filter = ""
    let checked = false;
    isSorting = true;

    if (radioButtons.item(2).checked) {
        tag = "true";
        filter = "online";
    } else if (radioButtons.item(3).checked) {
        tag = "false";
        filter = "online";
    } else if (tag === "") {
        isSorting = false;
        createTable();
        return;
    } else {
        for (let i = 0; i < radioButtons.length - 2; i++) {
            let radioButton = radioButtons.item(i);
            if (radioButton.checked) {
                filter = radioButton.value;
                checked = true;
                break;
            }
        }
        if (!checked) {
            isSorting = false;
            return;
        }
    }

    for (let i = 0; i < devices.length; i++) {
        let deviceTag = devices[i][filter].toString().toLowerCase();
        if (deviceTag.startsWith(tag)) {
            found.push(devices[i]);
        }
    }

    createTable(found)
}

function updatePaginate() {
    let listItems = document.getElementsByClassName("page-link");
    let currentItem = listItems.item(currentPage);
    currentItem.style.backgroundColor = "white";
    currentItem.style.color = "#0a58ca";

    for (let i = 1; i < listItems.length - 1; i++) {
        if (i !== currentPage) {
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
    if (currentPage < Math.ceil(devices.length / devicesPerPage)) {
        changePage(++currentPage);
    }
}

function lastPage() {
    currentPage = Math.ceil(devices.length / devicesPerPage);
    changePage(currentPage)
}