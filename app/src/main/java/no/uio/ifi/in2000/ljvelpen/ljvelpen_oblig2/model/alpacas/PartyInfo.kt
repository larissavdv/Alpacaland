package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.alpacas

import kotlinx.serialization.Serializable

@Serializable           //Serializable er en funksjon som gjør det mulig å oversette JSON format til kotlin objekter
data class PartyInfo(
    val id: String,
    val name: String,
    val leader: String,
    val img: String,
    val color: String,
    val description: String
)

