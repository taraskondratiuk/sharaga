const blueStyle = "background-color: #4B4BFF"
const greenStyle = "background-color: #4BFF69"
const blackStyle = "background-color: #000000"
const greyStyle = "background-color: #565656"

const orderedSeats = new Set();

document.querySelectorAll(".seat").forEach(item => {
    item.addEventListener("click", evt => {
            bookPlace(
                item,
                item.parentElement.classList.item(1),
                item.classList.item(1)
            )
        }
    )
})

document.getElementById("confirm-button").addEventListener("click", evt => {
    const boughtSeats = (Array.from(orderedSeats)).map(v => {
        return  {"row": v.charAt(0), "place": parseInt(v.slice(1), 10), "status": "paid"}
    })
    orderedSeats.clear()
    boughtSeats.forEach(v => bookingSocket.send(JSON.stringify(v)))
    alert("You bought seats " + boughtSeats.map(v => v.place + v.row).join(", "))
})

const bookingSocket = new WebSocket("ws://localhost:9000/booking");

bookingSocket.onmessage = function (event) {
    const order = JSON.parse(event.data)

    const el = document
        .querySelector("." + order.row)
        .getElementsByClassName(order.place)
        .item(0)

    if (!orderedSeats.has(order.row + order.place)) {
        if (order.status === "booked") {
            el.setAttribute("style", greyStyle)
        } else if (order.status === "unbooked") {
            el.setAttribute("style", blueStyle)
        } else {
            el.setAttribute("style", blackStyle)
        }
    }
}

function bookPlace(seatItem, row, place) {
    const style = seatItem.getAttribute("style")
    if (style === null || style === blueStyle) {
        seatItem.setAttribute("style", greenStyle)
        const order = {"row": row, "place": parseInt(place, 10), "status": "booked"}
        orderedSeats.add(row + place)
        bookingSocket.send(JSON.stringify(order))
    } else if (style === greenStyle) {
        seatItem.setAttribute("style", blueStyle)
        const order = {"row": row, "place": parseInt(place, 10), "status": "unbooked"}
        orderedSeats.delete(row + place)
        bookingSocket.send(JSON.stringify(order))
    }
}

bookingSocket.onopen = () => bookingSocket.send("start")
