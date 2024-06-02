package com.ues.boletos.comprar

import android.os.Parcelable

data class DetalleCompra(
    val carrera: Int,
    val titulo: String,
    val fecha: String,
    val lugar: String,
    val entradas: List<DetalleCompraEntrada>
) : Parcelable
{
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(DetalleCompraEntrada.CREATOR)!!
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeInt(carrera)
        parcel.writeString(titulo)
        parcel.writeString(fecha)
        parcel.writeString(lugar)
        parcel.writeTypedList(entradas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : android.os.Parcelable.Creator<DetalleCompra> {
        override fun createFromParcel(parcel: android.os.Parcel): DetalleCompra {
            return DetalleCompra(parcel)
        }

        override fun newArray(size: Int): Array<DetalleCompra?> {
            return arrayOfNulls(size)
        }
    }
}