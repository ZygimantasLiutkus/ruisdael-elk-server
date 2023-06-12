/* The code below is an adaptation of the original which can be found at
 * https://codesandbox.io/embed/dynamic-pagination-y5brt?file=/
 * The original code was changed and modified such that it satisfies our purposes.
*/
import { updatePaginate } from "./table-script.js";

const pg = document.getElementById("pagination");
const btnFirstPage = document.getElementById("btn-first-page");
const btnPrevious = document.getElementById("btn-prev-page");
const btnNext = document.getElementById("btn-next-page");
const btnLastPage = document.getElementById("btn-last-page");

const valuePage = {
    curPage: 1,
    numLinksTwoSide: 1,
    totalPages: 1
};

export function setUp(totalPages) {
    valuePage.totalPages = totalPages;
    pagination();

    pg.addEventListener("click", (e) => {
        const ele = e.target;
        valuePage.totalPages = totalPages;

        if (ele.dataset.page) {
            valuePage.curPage = parseInt(e.target.dataset.page, 10);
            pagination(valuePage);
            handleButtonLeft();
            handleButtonRight();
        }
    });

    document.getElementById("page-container").onclick = function (e) {
        handleButton(e.target);
    };
}

function pagination() {
    const { totalPages, curPage, numLinksTwoSide: delta } = valuePage;

    const range = delta + 4; // use for handle visible number of links left side

    let render = "";
    let renderTwoSide = "";
    let dot = `<li class="pg-item"><a class="pg-link">...</a></li>`;
    let countTruncate = 0; // use for ellipsis - truncate left side or right side

    // use for truncate two side
    const numberTruncateLeft = curPage - delta;
    const numberTruncateRight = curPage + delta;

    let active = "";
    for (let pos = 1; pos <= totalPages; pos++) {
        active = pos === curPage ? "active" : "";

        // truncate
        if (totalPages >= 2 * range - 1) {
            if (numberTruncateLeft > 3 && numberTruncateRight < totalPages - 3 + 1) {
                // truncate 2 side
                if (pos >= numberTruncateLeft && pos <= numberTruncateRight) {
                    renderTwoSide += renderPage(pos, active);
                }
            } else {
                // truncate left side or right side
                if (
                    (curPage < range && pos <= range) ||
                    (curPage > totalPages - range && pos >= totalPages - range + 1) ||
                    pos === totalPages || pos === 1
                ) {
                    render += renderPage(pos, active);
                } else {
                    countTruncate++;
                    if (countTruncate === 1) render += dot;
                }
            }
        } else {
            // not truncate
            render += renderPage(pos, active);
        }
    }

    if (renderTwoSide) {
        renderTwoSide =
            renderPage(1) + dot + renderTwoSide + dot + renderPage(totalPages);
        pg.innerHTML = renderTwoSide;
    } else {
        pg.innerHTML = render;
    }

    updatePaginate(valuePage.curPage);
}

function renderPage(index, active = "") {
    return ` <li class="pg-item ${active}" data-page="${index}">
        <a class="pg-link" href="#">${index}</a>
    </li>`;
}

function handleButton(element) {
    if (element.id === "btn-first-page") {
        valuePage.curPage = 1;
    } else if (element.id ===  "btn-last-page") {
        valuePage.curPage = valuePage.totalPages;
    } else if (element.id === "btn-prev-page") {
        valuePage.curPage--;
    } else if (element.id === "btn-next-page") {
        valuePage.curPage++;
    }
    handleButtonLeft();
    handleButtonRight();
    pagination();
}
function handleButtonLeft() {
    if (valuePage.curPage === 1) {
        btnPrevious.disabled = true;
        btnFirstPage.disabled = true;
    } else {
        btnPrevious.disabled = false;
        btnFirstPage.disabled = false;
    }
}
function handleButtonRight() {
    if (valuePage.curPage === valuePage.totalPages) {
        btnNext.disabled = true;
        btnLastPage.disabled = true;
    } else {
        btnNext.disabled = false;
        btnLastPage.disabled = false;
    }
}