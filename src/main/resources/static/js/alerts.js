let interval = null;

window.addEventListener("load", init);
window.addEventListener("unload", interval = null);

// The initialize function sets up all the event listeners for the elements in the HTML page, and calls the
// createTable() function, as well as the setUp() function which initializes the Pagination.
function init() {
    // Runs the `connect()` function on load to create a connection with the websockets
    interval = setInterval(() => {
        fetch("/alert-update")
            .then((response) => response.json())
            .then((json) => {
                alerts = json;
                createTable(alerts);
            });
    }, 2000);
    createTable(alerts);
}


