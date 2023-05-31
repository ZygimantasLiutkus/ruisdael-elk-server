var table = document.getElementById("device-table");
var isAsc = true;
var devices = [];

function sortCol(col) {
    if (isAsc) {
        devices = devices.sort((a, b) => a[col].localeCompare(b[col], undefined, { numeric: true }))
    } else {
        devices = devices.sort((a, b) => b[col].localeCompare(a[col], undefined, { numeric: true }))
    }
    isAsc = !isAsc

    for (let i = 0; i < devices.length; i++) {
        let device = devices[i];
        let row = table.rows[i + 1];

        row.cells[0].innerText = device.name;
        row.cells[1].innerText = device.location;
        row.cells[2].innerText = device.status;

        // In order to change the colors
        if (device.status === "Online") {
            row.cells[2].style.color = "#4eb940";
        } else {
            row.cells[2].style.color = "#ad2626";
        }
    }
}