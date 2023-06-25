const table = document.getElementById("alerts-table");

/**
 * Takes an array of alerts and gives back a html table of it.
 */
function createTable(alertsArray) {
    alertList = (alertsArray == null) ? alerts : alertsArray;

    let tbody = table.getElementsByTagName('tbody')[0];

    while (tbody.rows.length > 0) {
        tbody.deleteRow(0);
    }


    for (let i = 0; i < alertList.length; i++) {
        let row = tbody.insertRow(tbody.rows.length);
        let alert = alertList[i];

        
        let timeStamp = new Date(alert['timeStamp'] + 'Z');
        row.insertCell(0).innerText = timeStamp.toLocaleTimeString() + " " + timeStamp.toLocaleDateString();
        row.insertCell(1).innerText = alert['deviceName'];
        row.insertCell(2).innerText = alert['metric'];

        let oldFlag = alert['oldFlag'];
        let newFlag = alert['newFlag'];

        row.insertCell(3).innerText = oldFlag;
        row.cells[3].style.color = row.cells[3].innerText;
        

        row.insertCell(4).innerText = newFlag;
        row.cells[4].style.color = row.cells[4].innerText;

        row.insertCell(5).innerText = 'Issue';

        row.children[1].addEventListener("click", () => {
            window.location.href = "/node/" + alert['deviceName'];
        });

        row.children[5].addEventListener("click", () => {
            window.location.href = "GITLAB"
        });

        
    }
}

createTable(alerts);