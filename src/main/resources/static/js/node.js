window.addEventListener("load", init);


/* This is an individual node*/

// The initialize function sets up all the event listeners for the elements in the HTML page, and calls the
// createTable() function, as well as the setUp() function which initializes the Pagination.
function init() {
    // Runs the `connect()` function on load to create a connection with the websockets
    createTable(alerts);
}
