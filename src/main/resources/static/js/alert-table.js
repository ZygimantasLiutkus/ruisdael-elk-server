const table = document.getElementById("alerts-table");

function colorToStatus(color) {
    switch(color) {
        case 'RED':
            return 'Failure';
        case 'YELLOW':
            return 'Warning';
        case 'GREEN':
            return 'OK';
    }
}

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

        row.insertCell(3).innerText = colorToStatus(oldFlag);
        row.cells[3].style.color = oldFlag;
        

        row.insertCell(4).innerText = colorToStatus(newFlag);
        row.cells[4].style.color = newFlag;

        row.insertCell(5).innerText = 'Issue';

        row.children[1].addEventListener("click", () => {
            window.location.href = "/node/" + alert['deviceName'];
        });

        row.children[5].addEventListener("click", () => {
            window.location.href = gitlabURL + "/-/issues/new?issue[title]=" + encodeURI("Monitoring Alert: " + alert['deviceName'] + " - " + alert['metric']);
        });

        
    }
}

createTable(alerts);