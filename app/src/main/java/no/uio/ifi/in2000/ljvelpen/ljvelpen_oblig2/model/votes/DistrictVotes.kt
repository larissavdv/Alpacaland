package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.model.votes


data class DistrictVotes(
    val district: District,
    val alpacaPartyId: String,
    val numberOfVotesForParty: Int
)