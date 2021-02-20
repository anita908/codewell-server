import cors from "cors"
import dotenv from "dotenv"
import express from "express"
import helmet from "helmet"
import morgan from "morgan"
import "core-js/stable"
import "regenerator-runtime/runtime"

const server = express()

dotenv.config()

server.use(cors())
server.use(helmet())
server.use(morgan("short"))
server.use(express.json())
server.use(express.urlencoded({ extended: true }))

server.get("/", async (req, res) => {
    res.status(200).send("This is the codewell backend server")
})

server.listen(process.env.SERVER_PORT, () => console.log(`Listening on port ${process.env.SERVER_PORT}`))