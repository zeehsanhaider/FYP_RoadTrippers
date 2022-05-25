package com.example.letsgoadmin.util.db

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DbReferences {

    private const val USERS_DB_TITLE = "USERS"
    private const val TRIPS_DB_TITLE = "TRIPS"
    private const val VEHICLES_DB_TITLE = "VEHICLES"
    private const val PLACES_DB_TITLE = "PLACES"
    private const val DEFAULT_TRIP_TICKETS_DB_TITLE = "DEFAULT-TRIP-TICKETS"
    private const val CUSTOM_TRIP_TICKETS_DB_TITLE = "CUSTOM-TRIP-TICKETS"

    lateinit var usersDbRef: DatabaseReference
    lateinit var tripsDbRef: DatabaseReference
    lateinit var vehiclesDbRef: DatabaseReference
    lateinit var placesDbRef: DatabaseReference
    lateinit var defaultTripTicketsDbRef: DatabaseReference
    lateinit var customTripTicketsDbRef: DatabaseReference

    fun initDatabaseReferences() {
        usersDbRef = FirebaseDatabase.getInstance().getReference(USERS_DB_TITLE)
        tripsDbRef = FirebaseDatabase.getInstance().getReference(TRIPS_DB_TITLE)
        vehiclesDbRef = FirebaseDatabase.getInstance().getReference(VEHICLES_DB_TITLE)
        placesDbRef = FirebaseDatabase.getInstance().getReference(PLACES_DB_TITLE)
        defaultTripTicketsDbRef = FirebaseDatabase.getInstance().getReference(DEFAULT_TRIP_TICKETS_DB_TITLE)
        customTripTicketsDbRef = FirebaseDatabase.getInstance().getReference(CUSTOM_TRIP_TICKETS_DB_TITLE)
    }

}