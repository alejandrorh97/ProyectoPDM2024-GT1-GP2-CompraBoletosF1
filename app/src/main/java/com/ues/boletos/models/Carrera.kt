package com.ues.boletos.models

data class CarreraItem(
    val idCarrera: Int,
    val nombreCarrera: String,
    val nombreCircuito: String,
    val ubicacionCircuito: String,
    val longitudCircuito: String,
    val curvasCircuito: String,
    val fechaCarrera: String,
    val vueltasCarrera: String
)
