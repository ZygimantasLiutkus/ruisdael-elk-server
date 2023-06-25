let socket = new SockJS('/alert-update');
let client = Stomp.over(socket);

window.addEventListener("load", init);

/**
 * Function that connects to the websockets and sends messages to the '/app/alerts'
 * message path with an interval set by the ApplicationConfig.
 */
function connect() {
    client.connect({}, () => {
        interval = setInterval(() => {
            client.send('/app/alerts', {});
        }, websocketDelay);

        client.subscribe('/topic/alerts', (d) => {
            alerts = JSON.parse(d.body);
            createTable(alerts);
        });
    });
}

/**
 * Function that disconnects from the websockets.
 */
function disconnect() {
    client.disconnect()
    interval = null;
}

// The initialize function sets up all the event listeners for the elements in the HTML page, and calls the
// createTable() function, as well as the setUp() function which initializes the Pagination.
function init() {
    // Runs the `connect()` function on load to create a connection with the websockets
    connect();
    createTable(alerts);
}


