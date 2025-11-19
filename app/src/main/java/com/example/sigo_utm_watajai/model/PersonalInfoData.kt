package com.example.sigo_utm_watajai.model

data class PersonalInfoData(
    var nombre: String = "",
    var apellidoPaterno: String = "",
    var apellidoMaterno: String = "",
    var curp: String = "",
    var nss: String = "",
    var fechaNacimiento: String = "",
    var estadoNacimiento: String = "Selecciona",
    var sexo: String = "Selecciona"
)