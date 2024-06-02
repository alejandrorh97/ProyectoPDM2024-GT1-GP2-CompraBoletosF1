package com.ues.boletos.comprar

import android.os.Parcelable


data class DetalleCompraEntrada(
    val id : Int,
    val cantidadAComprar: Int,
    val precio: Double
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(cantidadAComprar)
        parcel.writeDouble(precio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : android.os.Parcelable.Creator<DetalleCompraEntrada> {
        override fun createFromParcel(parcel: android.os.Parcel): DetalleCompraEntrada {
            return DetalleCompraEntrada(parcel)
        }

        override fun newArray(size: Int): Array<DetalleCompraEntrada?> {
            return arrayOfNulls(size)
        }
    }
}
