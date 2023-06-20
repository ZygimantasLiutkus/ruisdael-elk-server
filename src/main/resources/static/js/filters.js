/**
 * The code for the loadSlider(), setLeftValue(), and setRightValue()
 * was recreated using code found at https://codepen.io/MinzCode/pen/MWKgyqb .
 * The original code has been changed to suit the implementation.
 */
const inputLeft = document.getElementById("input-left");
const inputRight = document.getElementById("input-right");
const thumbLeft = document.querySelector(".slider > .thumb.left");
const thumbRight = document.querySelector(".slider > .thumb.right");
const range = document.querySelector(".slider > .range");


/**
 * This function displays the status radio buttons.
 */
export function loadStatuses() {
    hideAll();
    document.getElementById("status-container").style.display = "block";
    document.getElementById("current-filter").value = "Current Filter: Statuses";
}

/**
 * This function is displays the search box.
 */
export function loadTextBox(name) {
    hideAll();
    document.getElementById("search-input").style.display = "flex";
    document.getElementById("current-filter").value = "Current Filter: " + name;
}

export function setLeftValue() {
    let _this = inputLeft,
        min = parseInt(_this.min),
        max = parseInt(_this.max);

    _this.value = Math.min(parseInt(_this.value), parseInt(inputRight.value) - 1);

    let percent = ((_this.value - min) / (max - min)) * 100;

    thumbLeft.style.left = percent + "%";
    range.style.left = percent + "%";
}

export function setRightValue() {
    let _this = inputRight,
        min = parseInt(_this.min),
        max = parseInt(_this.max);

    _this.value = Math.max(parseInt(_this.value), parseInt(inputLeft.value) + 1);
    let percent = ((_this.value - min) / (max - min)) * 100;
    thumbRight.style.right = (100 - percent) + "%";
    range.style.right = (100 - percent) + "%";
}

export function loadSlider(name) {
    hideAll();
    document.getElementById("range-slider").style.display = "block";
    document.getElementById("current-filter").value = "Current Filter: " + name;
    document.getElementById("filter-container").style.marginTop = "10%";
    setLeftValue();
    setRightValue();
}

/**
 * This function is used to hide all the previously revealed filters.
 */
export function hideAll() {
    document.getElementById("filter-container").style.marginTop = "5%";
    document.getElementById("status-container").style.display = "none";
    document.getElementById("range-slider").style.display = "none";
    document.getElementById("search-input").style.display = "none";
}