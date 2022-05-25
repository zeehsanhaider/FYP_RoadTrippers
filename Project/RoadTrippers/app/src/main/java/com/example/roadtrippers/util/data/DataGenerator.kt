package com.example.roadtrippers.util.data

import com.example.roadtrippers.model.Vehicle
import java.util.*

object DataGenerator {

    fun getVehicles(): ArrayList<Vehicle> {

        val vehicles = ArrayList<Vehicle>()

        vehicles.add(
            Vehicle(
                name = "Honda City",
                mileage = 18,
                dateCreated = Date().time,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_honda_city.jpg?alt=media&token=4feb2e63-d95b-4a1a-9f0a-b4b31515f1f3",
                seats = 4,
                expenseFactor = 1.5
            )
        )

        vehicles.add(
            Vehicle(
                name = "Suzuki Minibus",
                mileage = 12,
                seats = 10,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_minibus.jpeg?alt=media&token=527614ae-ae42-4cc1-b5fd-d356ecd511d7",
                dateCreated = Date().time,
                expenseFactor = 1.0
            )
        )

        vehicles.add(
            Vehicle(
                name = "Pajero",
                mileage = 8,
                seats = 6,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_pajero.JPG?alt=media&token=9c0c4111-9be1-49fc-9003-09cd37edde46",
                dateCreated = Date().time,
                expenseFactor = 2.0
            )
        )

        vehicles.add(
            Vehicle(
                name = "Suzuki Wagon R",
                mileage = 22,
                seats = 4,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_wagon_r.jpg?alt=media&token=8d449924-0409-45d4-8d76-a86e2ed0bd02",
                dateCreated = Date().time,
                expenseFactor = 1.2
            )
        )

        return vehicles
    }

}