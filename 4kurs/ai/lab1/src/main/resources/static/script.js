window.onload = function () {
    const windowHeight = window.innerHeight
    const windowWidth = window.innerWidth

    const canvas = document.getElementById('canvas')
    canvas.height = windowHeight
    canvas.width = windowWidth

    canvas.style.background = 'linear-gradient(#141e30, #243b55)'

    const context = canvas.getContext('2d')
    document.getElementById('submit').addEventListener('click', function () {

        const inputParams = {
            nIterations: parseInt(document.getElementById('nIterations').value, 10),
            nCities: parseInt(document.getElementById('nCities').value, 10),
            mutationsPercentage: parseInt(document.getElementById('mutationsPercentage').value, 10),
        }
        runGeneticAlgorithm(context, canvas, inputParams)
    })
}

function runGeneticAlgorithm(context, canvas, inputParams) {
    document.getElementById('start-screen').style.display = 'none'
    document.getElementById('canvas').style.display = 'inline'

    const cities = []

    for (let i = 0; i < document.getElementById('nCities').value; i++) {
        cities.push(randomCity(canvas.height, canvas.width, i))
    }

    cities.forEach(p => drawCity(p, context))

    const travellingSalesmanRequest = {
        inputParameters: inputParams,
        cities: cities
    }

    const gaRoutes = postRequest('http://localhost:3000/travelling_salesman', travellingSalesmanRequest)

    for (let i = 0; i < gaRoutes.length - 1; i++) {
        drawLines(gaRoutes[i], context, false)
    }

    drawLines(gaRoutes[gaRoutes.length - 1], context, true)

}

function postRequest(url, body) {
    const xmlHttp = new XMLHttpRequest()
    xmlHttp.open( 'POST', url, false)
    xmlHttp.setRequestHeader('Content-Type', 'application/json')
    xmlHttp.send(JSON.stringify(body))
    return JSON.parse(xmlHttp.responseText)
}

function randomCity(maxHeight, maxWidth, index) {
    return new City(getRandomInt(maxWidth), getRandomInt(maxHeight), index)
}

function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

class City {
    constructor(x, y, index) {
        this.x = x
        this.y = y
        this.index = index
    }
}

function drawLines(lines, context, isThick) {
    let color = '#' + Math.random().toString(16).substr(2,6)
    lines.forEach(l => drawLine(l, context, color, isThick))
}

function drawLine(line, context, color, isThick) {
    context.strokeStyle = color
    context.beginPath()
    context.moveTo(line.x1, line.y1)
    context.lineTo(line.x2, line.y2)
    if (isThick) {
        context.lineWidth = 5
    } else {
        context.lineWidth = 1
    }
    context.stroke()
}

function drawCity(city, context) {
    context.beginPath()
    context.arc(city.x, city.y, 6, 0, Math.PI * 2, true)
    context.fill()
}
