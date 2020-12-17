const inputCanvas = document.getElementById('input-canvas')
const outputCanvas = document.getElementById('output-canvas')
const savedCanvases = [
    document.getElementById('saved-1'),
    document.getElementById('saved-2'),
    document.getElementById('saved-3')
]
const saveBtn = document.getElementById('button-save')
const clearBtn = document.getElementById('button-clear')
const recognizeBtn = document.getElementById('button-recognize')

document.body.style.margin = 0
inputCanvas.style.position = 'fixed'
outputCanvas.style.position = 'fixed'

const ctxInput = inputCanvas.getContext('2d')
const ctxOutput = outputCanvas.getContext('2d')
const savedCtxts = [
    savedCanvases[0].getContext('2d'),
    savedCanvases[1].getContext('2d'),
    savedCanvases[2].getContext('2d')
]

let pos = { x: 0, y: 0 }

document.addEventListener('mousemove', draw)
document.addEventListener('mousedown', setPosition)
document.addEventListener('mouseenter', setPosition)
clearBtn.addEventListener('click', clear)
saveBtn.addEventListener('click', save)
recognizeBtn.addEventListener('click', recognize)

function setPosition(e) {
    pos.x = (e.clientX - 400) / 20
    pos.y = (e.clientY - 150) / 20
}

function draw(e) {
    if (e.buttons !== 1) return

    ctxInput.beginPath()
    ctxInput.lineWidth = 1
    ctxInput.lineCap = 'square'
    ctxInput.strokeStyle = '#c0392b'

    ctxInput.moveTo(pos.x, pos.y)
    setPosition(e)
    ctxInput.lineTo(pos.x, pos.y)

    ctxInput.stroke()
}

function clear() {
    ctxInput.clearRect(0, 0, inputCanvas.width, inputCanvas.height)
}

function getInputPixelsArr() {
    const imgd = ctxInput.getImageData(0, 0, inputCanvas.width, inputCanvas.height)
    const pixelsArr = []
    imgd.data.filter(function(value, index) {
        return index % 4 === 0
    }).forEach(function (num) {
        if (num > 0) {
            pixelsArr.push(1)
        } else {
            pixelsArr.push(-1)
        }
    })
    return pixelsArr
}

let indexSaved = 0
function save() {
    const imgd = ctxInput.getImageData(0, 0, inputCanvas.width, inputCanvas.height)

    savedCtxts[indexSaved].clearRect(0, 0, savedCanvases[indexSaved].width, savedCanvases[indexSaved].height)
    savedCtxts[indexSaved].putImageData(imgd, 0, 0)
    indexSaved++
    if (indexSaved === 3) {
        indexSaved = 0
    }
    const pixelsArr = getInputPixelsArr()
    const reqBody = { pixels: pixelsArr }
    postRequest('http://localhost:3000/add_picture', reqBody)
}

function recognize() {
    ctxOutput.clearRect(0, 0, outputCanvas.width, outputCanvas.height)
    const pixelsArr = getInputPixelsArr()
    const reqBody = { pixels: pixelsArr }
    const resPicPixels = []
    postRequest('http://localhost:3000/recognize_picture', reqBody).pixels.forEach(function (num) {
        if (num === -1) {
            resPicPixels.push(0)
            resPicPixels.push(0)
            resPicPixels.push(0)
            resPicPixels.push(0)
        } else {
            resPicPixels.push(0)
            resPicPixels.push(0)
            resPicPixels.push(255)
            resPicPixels.push(255)
        }
    })
    ctxOutput.putImageData(new ImageData(Uint8ClampedArray.from(resPicPixels), outputCanvas.height), 0, 0)
}

function postRequest(url, body) {
    const xmlHttp = new XMLHttpRequest()
    xmlHttp.open( 'POST', url, false)
    xmlHttp.setRequestHeader('Content-Type', 'application/json')
    xmlHttp.send(JSON.stringify(body))
    return JSON.parse(xmlHttp.responseText)
}