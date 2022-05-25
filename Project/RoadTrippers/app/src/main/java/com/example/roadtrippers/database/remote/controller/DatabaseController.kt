package com.example.roadtrippers.database.remote.controller

import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.callback.FirebaseCallback
import com.example.roadtrippers.model.*
import com.example.roadtrippers.util.constant.Constants
import com.example.roadtrippers.util.db.DbReferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DatabaseController {

    private val usersDbRef = DbReferences.usersDbRef
    private val tripsDbRef = DbReferences.tripsDbRef
    private val tripTicketsDbRef = DbReferences.tripTicketsDbRef
    private val defaultTripTicketsDbRef = DbReferences.defaultTripTicketsDbRef
    private val customTripTicketsDbRef = DbReferences.customTripTicketsDbRef
    private val placesDbRef = DbReferences.placesDbRef
    private val vehiclesDbRef = DbReferences.vehiclesDbRef

    fun getUniqueUserId(): String? {
        return usersDbRef.push().key
    }

    fun getDefaultTripTicketId(): String? {
        return defaultTripTicketsDbRef.push().key
    }

    fun updateToken(
        token: String,
        userId: String,
        listener: (status: Boolean) -> Unit
    ) {
        usersDbRef
            .child(userId)
            .child("token")
            .setValue(token)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    listener(true)
                else
                    listener(false)
            }
            .addOnCanceledListener {
                listener(false)
            }
            .addOnFailureListener {
                listener(false)
            }
    }

    fun addUser(user: User, callback: FirebaseCallback) {
        if (user.id == Constants.NO_ID_ASSIGNED_USER) {
            return
        }

        usersDbRef // 1
            .child(user.id) // 3
            .setValue(user) // 1
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess("")
                } else {
                    callback.onNoSuccess("")
                }
            }
            .addOnCanceledListener {
                callback.onCancel("")
            }
            .addOnFailureListener {
                callback.onFailure("")
            }
    }

    fun getAllUsers(dataChangeListener: DataChangeListener) {
        val allUsers = ArrayList<User>()
        usersDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allUsers.clear()
                for (dataSnapshot in snapshot.children) {
                    // Here I am converting json data into User class objects
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (!user.isDeleted && !user.isAdmin) {
                            allUsers.add(user)
                        }
                    }
                }
                dataChangeListener.onDataChange(allUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun loginUser(
        email: String,
        password: String,
        isAdmin: Boolean,
        dataChangeListener: DataChangeListener
    ) {
        var userFound: Boolean = false
        usersDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // conversion from json to class object
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (isAdmin) {
                            // For retailer
                            if (user.email == email && user.password == password && user.isAdmin) {
                                userFound = true
                            }
                        } else {
                            // For customer
                            if (user.email == email && user.password == password) {
                                userFound = true
                            }
                        }
                    }
                }
                dataChangeListener.onDataChange(
                    listOf(userFound)
                )
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun doesEmailAlreadyExists(
        email: String,
        dataChangeListener: DataChangeListener
    ) {
        var emailFound = false
        usersDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // conversion from json to class object
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.email == email) {
                            emailFound = true
                        }
                    }
                }
                dataChangeListener.onDataChange(
                    listOf(emailFound)
                )
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    /**
     * We should also set the isLogin field to true when user logs in
     * And also set it to false when user signs out.
     **/
    fun signOutUser(userId: String, firebaseCallback: FirebaseCallback) {
        usersDbRef
            .child(userId)
            .child("login")
            .setValue(false)
            .addOnCompleteListener {
                firebaseCallback.onSuccess("Sign out successful.")
            }
            .addOnCanceledListener {
                firebaseCallback.onCancel("Sign out cancelled.")
            }
            .addOnFailureListener {
                firebaseCallback.onFailure("Sign out failure.")
            }
    }

    fun setUserLoggedIn(userId: String, firebaseCallback: FirebaseCallback) {
        usersDbRef
            .child(userId)
            .child("login")
            .setValue(true)
            .addOnCompleteListener {
                firebaseCallback.onSuccess("Login status successful.")
            }
            .addOnCanceledListener {
                firebaseCallback.onCancel("Login status cancelled.")
            }
            .addOnFailureListener {
                firebaseCallback.onFailure("Login status failure.")
            }
    }

    fun getUserByEmail(email: String, dataChangeListener: DataChangeListener) {
        val userByEmail = ArrayList<User>()
        usersDbRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userByEmail.clear()
                    for (dataSnapshot in snapshot.children) {
                        // Here I am converting json data into User class objects
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (!user.isDeleted && user.email == email) {
                                userByEmail.add(user)
                            }
                        }
                    }
                    dataChangeListener.onDataChange(userByEmail)
                }

                override fun onCancelled(error: DatabaseError) {
                    dataChangeListener.onCancel("Operation failed! Check your internet connection.")
                }
            }
        )
    }

    fun getUserById(id: String, dataChangeListener: DataChangeListener) {
        val userById = ArrayList<User>()
        usersDbRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userById.clear()
                    for (dataSnapshot in snapshot.children) {
                        // Here I am converting json data into User class objects
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (!user.isDeleted && user.id == id) {
                                userById.add(user)
                            }
                        }
                    }
                    dataChangeListener.onDataChange(userById)
                }

                override fun onCancelled(error: DatabaseError) {
                    dataChangeListener.onCancel("Operation failed! Check your internet connection.")
                }
            }
        )
    }

    fun setNewUserPassword(
        userId: String,
        newPassword: String,
        firebaseCallback: FirebaseCallback
    ) {
        usersDbRef
            .child(userId)
            .child("password")
            .setValue(newPassword)
            .addOnCompleteListener {
                firebaseCallback.onSuccess("Password changed successfully.")
            }
            .addOnCanceledListener {
                firebaseCallback.onCancel("Password change cancelled.")
            }
            .addOnFailureListener {
                firebaseCallback.onFailure("Password change failure.")
            }
    }

    fun getAllTrips(dataChangeListener: DataChangeListener) {
        val trips = ArrayList<Trip>()
        tripsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trips.clear()
                for (dataSnapshot in snapshot.children) {
                    val trip = dataSnapshot.getValue(Trip::class.java)
                    if (trip != null) {
                        if (!trip.isDeleted) {
                            trips.add(trip)
                        }
                    }
                }
                dataChangeListener.onDataChange(trips)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getTripById(tripId: String, dataChangeListener: DataChangeListener) {
        val trips = ArrayList<Trip>()
        tripsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trips.clear()
                for (dataSnapshot in snapshot.children) {
                    val trip = dataSnapshot.getValue(Trip::class.java)
                    if (trip != null) {
                        if (!trip.isDeleted && trip.id == tripId) {
                            trips.add(trip)
                        }
                    }
                }
                dataChangeListener.onDataChange(trips)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getUniqueTripTicketId(): String? {
        return tripTicketsDbRef.push().key
    }

    fun addTripTicket(tripTicket: TripTicket, callback: FirebaseCallback) {
        tripTicketsDbRef
            .child(tripTicket.id)
            .setValue(tripTicket)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess("Ticket posted successfully.")
                } else {
                    callback.onNoSuccess("Failure posting ticket.")
                }
            }
            .addOnCanceledListener {
                callback.onCancel("Failure posting ticket.")
            }
            .addOnFailureListener {
                callback.onFailure("Failure posting ticket. Please check your internet connection.")
            }
    }

    fun addDefaultTripTicket(defaultTripTicket: DefaultTripTicket, callback: FirebaseCallback) {
        defaultTripTicketsDbRef
            .child(defaultTripTicket.id)
            .setValue(defaultTripTicket)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess("Trip booking successful.")
                } else {
                    callback.onNoSuccess("Failure booking the trip.")
                }
            }
            .addOnCanceledListener {
                callback.onCancel("Failure booking the trip.")
            }
            .addOnFailureListener {
                callback.onFailure("Failure booking the trip. Please check your internet connection.")
            }
    }

    fun getAllDefaultTripTickets(dataChangeListener: DataChangeListener) {
        val defaultTripTickets = ArrayList<DefaultTripTicket>()

        defaultTripTicketsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                defaultTripTickets.clear()
                for (dataSnapshot in snapshot.children) {
                    val ticket = dataSnapshot.getValue(DefaultTripTicket::class.java)
                    if (ticket != null) {
                        defaultTripTickets.add(ticket)
                    }
                }
                dataChangeListener.onDataChange(defaultTripTickets)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getAllPlaces(dataChangeListener: DataChangeListener) {
        val allPlaces = ArrayList<Place>()
        placesDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allPlaces.clear()
                for (dataSnapshot in snapshot.children) {
                    // Here I am converting json data into User class objects
                    val place = dataSnapshot.getValue(Place::class.java)
                    if (place != null) {
                        if (!place.isDeleted) {
                            allPlaces.add(place)
                        }
                    }
                }
                dataChangeListener.onDataChange(allPlaces)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getPlaceById(placeId: String, dataChangeListener: DataChangeListener) {
        val places = ArrayList<Place>()
        placesDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                places.clear()
                for (dataSnapshot in snapshot.children) {
                    val trip = dataSnapshot.getValue(Place::class.java)
                    if (trip != null) {
                        if (!trip.isDeleted && trip.id == placeId) {
                            places.add(trip)
                        }
                    }
                }
                dataChangeListener.onDataChange(places)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getUniqueCustomTripTicketId(): String? {
        return customTripTicketsDbRef.push().key
    }

    fun addCustomTripTicket(
        customTripTicket: CustomTripTicket,
        callback: FirebaseCallback
    ) {
        customTripTicketsDbRef
            .child(customTripTicket.id)
            .setValue(customTripTicket)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess("Ticket posted successfully.")
                } else {
                    callback.onNoSuccess("Failure posting ticket.")
                }
            }
            .addOnCanceledListener {
                callback.onCancel("Failure posting ticket.")
            }
            .addOnFailureListener {
                callback.onFailure("Failure posting ticket. Please check your internet connection.")
            }
    }

    fun getAllCustomTripTickets(dataChangeListener: DataChangeListener) {
        val customTripTickets = ArrayList<CustomTripTicket>()
        customTripTicketsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                customTripTickets.clear()
                for (dataSnapshot in snapshot.children) {
                    val ticket = dataSnapshot.getValue(CustomTripTicket::class.java)
                    if (ticket != null) {
                        customTripTickets.add(ticket)
                    }
                }
                dataChangeListener.onDataChange(customTripTickets)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun getUniqueVehicleId(): String? {
        return vehiclesDbRef.push().key
    }

    fun addVehicle(vehicle: Vehicle, callback: FirebaseCallback) {
        vehiclesDbRef
            .child(vehicle.id)
            .setValue(vehicle)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback.onSuccess("Posted")
                } else {
                    callback.onNoSuccess("Failure")
                }
            }
            .addOnCanceledListener {
                callback.onCancel("Cancelled")
            }
            .addOnFailureListener {
                callback.onFailure("Failure")
            }
    }

    fun getAllVehicles(dataChangeListener: DataChangeListener) {
        val vehicles = ArrayList<Vehicle>()
        vehiclesDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vehicles.clear()
                for (dataSnapshot in snapshot.children) {
                    val v = dataSnapshot.getValue(Vehicle::class.java)
                    if (v != null) {
                        vehicles.add(v)
                    }
                }
                dataChangeListener.onDataChange(vehicles)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun isTripAlreadyBooked(
        tripId: String,
        userId: String,
        dataChangeListener: DataChangeListener
    ) {
        val isTripBooked = ArrayList<Boolean>()
        defaultTripTicketsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isTripBooked.clear()
                for (dataSnapshot in snapshot.children) {
                    val ticket = dataSnapshot.getValue(DefaultTripTicket::class.java)
                    if (ticket != null) {
                        if (ticket.tripId == tripId && ticket.userId == userId) {
                            isTripBooked.add(true)
                        }
                    }
                }
                if (isTripBooked.isEmpty()) {
                    isTripBooked.add(false)
                }
                dataChangeListener.onDataChange(isTripBooked)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    fun isPlaceAlreadyBooked(
        placeId: String,
        userId: String,
        dataChangeListener: DataChangeListener
    ) {
        val isPlaceBooked = ArrayList<Boolean>()
        customTripTicketsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isPlaceBooked.clear()
                for (dataSnapshot in snapshot.children) {
                    val ticket = dataSnapshot.getValue(CustomTripTicket::class.java)
                    if (ticket != null) {
                        if (ticket.placeId == placeId && ticket.userId == userId) {
                            isPlaceBooked.add(true)
                        }
                    }
                }
                if (isPlaceBooked.isEmpty()) {
                    isPlaceBooked.add(false)
                }
                dataChangeListener.onDataChange(isPlaceBooked)
            }

            override fun onCancelled(error: DatabaseError) {
                dataChangeListener.onCancel(error.message)
            }
        })
    }

    companion object {
        private const val TAG = "DatabaseController"
    }

}