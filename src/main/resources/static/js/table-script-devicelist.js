var table = document.getElementById("device-table");
var isAsc = true;
var devicesPerPage = 10;
var currentPage = 1;
var isSorting = false;

function setCellColors(device, row) {
    // In order to change the colors
    if (device.status === "ONLINE") {
        row.cells[2].innerText = "Online";
        row.cells[2].style.color = "#4eb940";
    } else if (device.status === "WARNING") {
        row.cells[2].innerText = "Warning";
        row.cells[2].style.color = "#B9A940FF";
    } else {
        row.cells[2].innerText = "Offline";
        row.cells[2].style.color = "#ad2626";
    }
}

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
                "status": row.cells[2].innerText
            });
        }
    }

    if (isAsc) {
        if (col === "location") {
            deviceList.sort((a, b) => a[col].name.localeCompare(b[col].name, undefined, { numeric: true }));
        } else {
            deviceList.sort((a, b) => a[col].localeCompare(b[col], undefined, { numeric: true }));
        }
    } else {
        if (col === "location") {
            deviceList.sort((a, b) => b[col].name.localeCompare(a[col].name, undefined, { numeric: true }));
        } else {
            deviceList.sort((a, b) => b[col].localeCompare(a[col], undefined, { numeric: true }));
        }
    }
    isAsc = !isAsc

    for (let i = 0; i < deviceList.length; i++) {
        let device = deviceList[i];
        let row = table.rows[i + 1];

        if (row === undefined) {
            break;
        }

        row.cells[0].innerText = device.name;
        row.cells[1].innerText = device.location.name;

        // In order to change the colors
        setCellColors(device, row);
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
        row.insertCell(1).innerHTML = `<td>` + device.location.name + `</td>`;
        row.insertCell(2).innerHTML = ``;
        setCellColors(device, row);
    }
}

// Requires further integration with the pagination, for now if we want to view all the found
// elements on page 2, it resets the table and displays all the devices.
function search() {
    let radioButtons = document.getElementsByClassName("form-check-input");
    let tag = document.getElementById("search-tag").value.trim().toLowerCase();
    let found = [];
    let filter = "status";
    let checked = false;
    isSorting = true;

    if (radioButtons.item(2).checked) {
        tag = "online";
    } else if (radioButtons.item(3).checked) {
        tag = "warning";
    } else if (radioButtons.item(4).checked) {
        tag = "offline";
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
        let deviceTag = "";
        if (filter === "location") {
            deviceTag = devices[i][filter]["name"].toString().toLowerCase();
        } else {
            deviceTag = devices[i][filter].toString().toLowerCase();
        }
        if (deviceTag.startsWith(tag.trim().toLowerCase())) {
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