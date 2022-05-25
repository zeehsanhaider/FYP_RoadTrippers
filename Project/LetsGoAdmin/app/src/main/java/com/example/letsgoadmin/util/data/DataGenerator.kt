package com.example.letsgoadmin.util.data

import com.example.letsgoadmin.model.Vehicle
import com.example.letsgoadmin.util.constant.Constants

object DataGenerator {

    private fun getVehiclesData(): ArrayList<Vehicle> {
        val vehicles = ArrayList<Vehicle>()
        vehicles.add(
            Vehicle(
                id = "-N2J5cTctIoL7J53kPGu",
                name = "Honda City",
                mileage = 18,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_honda_city.jpg?alt=media&token=4feb2e63-d95b-4a1a-9f0a-b4b31515f1f3",
                expenseFactor = 1.8,
                seats = 4
            )
        )
        vehicles.add(
            Vehicle(
                id = "-N2J7Hb0ffTZKcA5UIk9",
                name = "Wagon R",
                mileage = 22,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_wagon_r.jpg?alt=media&token=8d449924-0409-45d4-8d76-a86e2ed0bd02",
                expenseFactor = 1.0,
                seats = 4
            )
        )
        vehicles.add(
            Vehicle(
                id = "-N2J6FtCEH6I6OLcZI_J",
                name = "Suzuki Minibus",
                mileage = 15,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_minibus.jpeg?alt=media&token=527614ae-ae42-4cc1-b5fd-d356ecd511d7",
                expenseFactor = 1.3,
                seats = 10
            )
        )
        vehicles.add(
            Vehicle(
                id = "-N2J6jcDS-WX16ZLavAy",
                name = "Pajero",
                mileage = 8,
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_pajero.JPG?alt=media&token=9c0c4111-9be1-49fc-9003-09cd37edde46",
                expenseFactor = 2.0,
                seats = 6
            )
        )
        return vehicles
    }

    fun getVehicleNameById(vehicleId: String): String {
        for (vehicle in getVehiclesData()) {
            if (vehicle.id == vehicleId) {
                return vehicle.name
            }
        }
        return Constants.NULL_STRING
    }

}